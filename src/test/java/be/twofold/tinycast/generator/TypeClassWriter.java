package be.twofold.tinycast.generator;

import be.twofold.tinycast.CastNode;
import be.twofold.tinycast.CastNodeID;
import be.twofold.tinycast.CastProperty;
import be.twofold.tinycast.CastPropertyID;
import be.twofold.tinycast.Vec2;
import be.twofold.tinycast.Vec3;
import be.twofold.tinycast.Vec4;
import be.twofold.tinycast.generator.model.PropertyDef;
import be.twofold.tinycast.generator.model.TypeDef;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

final class TypeClassWriter {
    private static final String PACKAGE_NAME = "be.twofold.tinycast";
    private static final ClassName OUTER_CLASS = ClassName.get(PACKAGE_NAME, "CastNodes");
    private static final ClassName SUPER_CLASS = ClassName.get(CastNode.class);
    private static final EnumSet<CastPropertyID> INTEGER_TYPES = EnumSet.of(
        CastPropertyID.BYTE,
        CastPropertyID.SHORT,
        CastPropertyID.INTEGER_32
    );

    private final Map<List<String>, ClassName> enumLookup = new HashMap<>();

    void generate(List<TypeDef> types) throws IOException {
        TypeSpec.Builder builder = TypeSpec.classBuilder(OUTER_CLASS)
            .addJavadoc("Namespace class containing the different Cast node types")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .build());

        builder.addMethod(generateCreate());

        buildEnumLookup(types);
        for (Map.Entry<List<String>, ClassName> entry : enumLookup.entrySet()) {
            builder.addType(generateEnum(entry.getValue(), entry.getKey()));
        }

        for (TypeDef type : types) {
            builder.addType(generateClass(type));
        }

        JavaFile
            .builder(PACKAGE_NAME, builder.build())
            .skipJavaLangImports(true)
            .indent("    ")
            .build()
            .writeTo(Path.of("src/main/java"));
    }

    private MethodSpec generateCreate() {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("create")
            .addModifiers(Modifier.STATIC)
            .returns(CastNode.class)
            .addParameter(CastNodeID.class, "identifier")
            .addParameter(long.class, "nodeHash")
            .addParameter(ParameterizedTypeName.get(Map.class, String.class, CastProperty.class), "properties")
            .addParameter(ParameterizedTypeName.get(List.class, CastNode.class), "children")
            .beginControlFlow("switch (identifier)");

        for (CastNodeID id : CastNodeID.values()) {
            methodBuilder
                .beginControlFlow("case $L:", id)
                .addStatement("return new $T(nodeHash, properties, children)", OUTER_CLASS.nestedClass(className(id)))
                .endControlFlow();
        }

        return methodBuilder
            .endControlFlow()
            .addStatement("throw new $T()", UnsupportedOperationException.class)
            .build();
    }

    private TypeSpec generateClass(TypeDef type) {
        ClassName className = OUTER_CLASS.nestedClass(className(type.type()));
        TypeSpec.Builder builder = TypeSpec.classBuilder(className)
            .addJavadoc("Implementation of the \"" + className(type.type()) + "\" node")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
            .superclass(SUPER_CLASS);

        List<PropertyDef> indexedProperties = type.properties().stream()
            .filter(prop -> prop.getKey().contains("%d"))
            .collect(Collectors.toList());

        for (PropertyDef property : indexedProperties) {
            builder.addField(FieldSpec.builder(int.class, indexName(property))
                .addModifiers(Modifier.PRIVATE)
                .build());
        }

        // Constructors
        builder.addMethod(MethodSpec.constructorBuilder()
            .addParameter(AtomicLong.class, "hasher")
            .addStatement("super($T.$L, hasher)", CastNodeID.class, type.type())
            .build());

        builder.addMethod(MethodSpec.constructorBuilder()
            .addParameter(long.class, "hash")
            .addParameter(ParameterizedTypeName.get(Map.class, String.class, CastProperty.class), "properties")
            .addParameter(ParameterizedTypeName.get(List.class, CastNode.class), "children")
            .addStatement("super($T.$L, hash, properties, children)", CastNodeID.class, type.type())
            .addComment("TODO: Validation")
            .build());

        for (CastNodeID child : type.children()) {
            builder.addMethods(generateNodeMethod(child));
        }

        for (PropertyDef property : type.properties()) {
            List<Set<CastPropertyID>> types = splitTypes(property.getTypes());
            builder.addMethod(generatePropertyGetter(property, property.getTypes()));
            for (Set<CastPropertyID> subTypes : types) {
                String suffix = types.size() == 1 ? "" : suffix(subTypes);
                builder.addMethod(generatePropertySetter(property, subTypes, suffix, className));
            }
        }

        return builder.build();
    }

    private List<Set<CastPropertyID>> splitTypes(Set<CastPropertyID> types) {
        Set<CastPropertyID> copy = EnumSet.copyOf(types);
        List<Set<CastPropertyID>> result = new ArrayList<>();
        if (copy.containsAll(INTEGER_TYPES)) {
            result.add(INTEGER_TYPES);
            copy.removeAll(INTEGER_TYPES);
        }
        for (CastPropertyID castPropertyID : copy) {
            result.add(EnumSet.of(castPropertyID));
        }
        return result;
    }

    private List<MethodSpec> generateNodeMethod(CastNodeID child) {
        boolean single = child == CastNodeID.SKELETON;
        String childClassName = className(child);
        TypeName childType = OUTER_CLASS.nestedClass(childClassName);
        TypeName returnType = single
            ? ParameterizedTypeName.get(ClassName.get(Optional.class), OUTER_CLASS.nestedClass("Skeleton"))
            : ParameterizedTypeName.get(ClassName.get(List.class), childType);
        String methodName = single ? "getChildOfType" : "getChildrenOfType";

        MethodSpec getter = MethodSpec.methodBuilder("get" + (single ? childClassName : multiple(childClassName)))
            .addJavadoc("Returns the child" + (single ? "" : "ren") + " of type {@link " + childClassName + "}.\n" +
                "\n" +
                "@return The " + (single ? "single" : "list of") + " " + multiple(childClassName) + "")
            .addModifiers(Modifier.PUBLIC)
            .returns(returnType)
            .addStatement("return $L($T.class)", methodName, childType)
            .build();

        MethodSpec creator = MethodSpec.methodBuilder("create" + childClassName)
            .addJavadoc("Create and add a new instance of type {@link " + childClassName + "}.\n" +
                "\n" +
                "@return The new instance")
            .addModifiers(Modifier.PUBLIC)
            .returns(childType)
            .addStatement("return createChild(new $T(hasher))", childType)
            .build();

        return List.of(getter, creator);
    }

    private MethodSpec generatePropertyGetter(PropertyDef property, Set<CastPropertyID> types) {
        String propertyName = property.upperCamelCase();
        TypeName returnType = returnType(property, types);

        MethodSpec.Builder builder = MethodSpec.methodBuilder("get" + propertyName)
            .addJavadoc("Returns the value of the {@code \"" + property.getKey() + "\"} property (" + property.getName() + ").\n" +
                "\n" +
                (property.isIndexed() ? "@param index The index of the value to get\n" : "") +
                "@return The value of the {@code \"" + property.getKey() + "\"} property")
            .addModifiers(Modifier.PUBLIC)
            .returns(returnType);
        if (property.isIndexed()) {
            builder.addParameter(int.class, "index");
        }
        return builder
            .addStatement(generateGetterCode(property, types))
            .build();
    }

    private CodeBlock generateGetterCode(PropertyDef property, Set<CastPropertyID> types) {
        String required = property.isRequired() ? ".orElseThrow()" : "";
        String name = property.isIndexed()
            ? "String.format(" + property.getKey() + ", index)"
            : property.getKey();
        TypeName type = propertyType(property, types);

        switch (property.getType()) {
            case SIMPLE:
                if (!property.isArray() && types.equals(INTEGER_TYPES)) {
                    return CodeBlock.of("return getIntProperty($S)" + required, name);
                }
                return CodeBlock.of("return getProperty($S, $T.class::cast)" + required, name, type);
            case ENUM:
                return CodeBlock.of("return getProperty($S, $T::from)" + required, name, type);
            case BOOL:
                return CodeBlock.of("return getProperty($S, this::parseBoolean)" + required, name);
            default:
                throw new UnsupportedOperationException();
        }
    }

    private MethodSpec generatePropertySetter(PropertyDef property, Set<CastPropertyID> types, String suffix, ClassName className) {
        return MethodSpec.methodBuilder((property.isIndexed() ? "add" : "set") + property.upperCamelCase() + suffix)
            .addJavadoc("Sets the value of the {@code \"" + property.getKey() + "\"} property (" + property.getName() + ").\n" +
                "\n" +
                "@param " + property.variableName() + " The new value.\n" +
                "@return The {@code this} instance for chaining")
            .addModifiers(Modifier.PUBLIC)
            .returns(className)
            .addParameter(parameterType(property, types), property.variableName())
            .addStatement(generateSetterCode(property, types))
            .addStatement("return this")
            .build();
    }

    private CodeBlock generateSetterCode(PropertyDef property, Set<CastPropertyID> types) {
        String setKey = property.isIndexed()
            ? '"' + property.getKey().replace("%d", "") + '"' + " + " + indexName(property) + "++"
            : '"' + property.getKey() + '"';

        if (types.equals(INTEGER_TYPES)) {
            if (property.isArray()) {
                return CodeBlock.of("createIntBufferProperty($L, $L)", setKey, property.variableName());
            } else {
                return CodeBlock.of("createIntProperty($L, $L)", setKey, property.variableName());
            }
        } else {
            String setMapper = getSetMapper(property);
            return CodeBlock.of("createProperty($T.$L, $L, " + setMapper + ")", CastPropertyID.class, types.iterator().next(), setKey, property.variableName());
        }
    }

    private String getSetMapper(PropertyDef property) {
        switch (property.getType()) {
            case SIMPLE:
                return "$N";
            case ENUM:
                return "$N.toString().toLowerCase()";
            case BOOL:
                return "$N ? 1 : 0";
            default:
                throw new UnsupportedOperationException();
        }
    }

    private String suffix(Set<CastPropertyID> set) {
        if (set.equals(INTEGER_TYPES)) {
            return "Int";
        }
        switch (set.iterator().next()) {
            case INTEGER_32:
                return "I32";
            case FLOAT:
                return "F32";
            case VECTOR_3:
                return "V3";
            case VECTOR_4:
                return "V4";
            default:
                throw new UnsupportedOperationException(set.iterator().next().toString());
        }
    }

    // region Type Resolution

    private TypeName propertyType(PropertyDef property, Set<CastPropertyID> subTypes) {
        switch (property.getType()) {
            case SIMPLE:
                if (subTypes.size() == 1) {
                    CastPropertyID propertyID = subTypes.iterator().next();
                    return ClassName.get(property.isArray() ? arrayType(propertyID) : singularType(propertyID));
                }

                // Multiple types and it's an array, that's just some form of buffer
                if (property.isArray()) {
                    return ClassName.get(Buffer.class);
                }

                // Stupid special cases
                EnumSet<CastPropertyID> types = EnumSet.copyOf(property.getTypes());
                if (types.equals(EnumSet.of(CastPropertyID.VECTOR_3, CastPropertyID.VECTOR_4))) {
                    return ClassName.get(Object.class);
                } else if (types.equals(INTEGER_TYPES)) {
                    return ClassName.get(Integer.class);
                } else {
                    throw new IllegalArgumentException(types.toString());
                }

            case ENUM:
                return enumLookup.get(property.getValues());

            case BOOL:
                return ClassName.get(Boolean.class);

            default:
                throw new UnsupportedOperationException();
        }
    }

    private Class<?> singularType(CastPropertyID propertyID) {
        switch (propertyID) {
            case BYTE:
                return Byte.class;
            case SHORT:
                return Short.class;
            case INTEGER_32:
                return Integer.class;
            case INTEGER_64:
                return Long.class;
            case FLOAT:
                return Float.class;
            case DOUBLE:
                return Double.class;
            case STRING:
                return String.class;
            case VECTOR_2:
                return Vec2.class;
            case VECTOR_3:
                return Vec3.class;
            case VECTOR_4:
                return Vec4.class;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private Class<?> arrayType(CastPropertyID propertyID) {
        switch (propertyID) {
            case BYTE:
                return ByteBuffer.class;
            case SHORT:
                return ShortBuffer.class;
            case INTEGER_32:
                return IntBuffer.class;
            case INTEGER_64:
                return LongBuffer.class;
            case FLOAT:
            case VECTOR_2:
            case VECTOR_3:
            case VECTOR_4:
                return FloatBuffer.class;
            case DOUBLE:
                return DoubleBuffer.class;
            case STRING:
                throw new IllegalArgumentException();
            default:
                throw new UnsupportedOperationException();
        }
    }

    private TypeName returnType(PropertyDef property, Set<CastPropertyID> types) {
        TypeName type = propertyType(property, types);
        if (!property.isRequired()) {
            return ParameterizedTypeName.get(ClassName.get(Optional.class), type);
        } else if (type.isBoxedPrimitive()) {
            return type.unbox();
        } else {
            return type;
        }
    }

    private TypeName parameterType(PropertyDef property, Set<CastPropertyID> types) {
        TypeName type = propertyType(property, types);
        if (type.isBoxedPrimitive()) {
            return type.unbox();
        } else {
            return type;
        }
    }

    // endregion

    // region Enums

    private void buildEnumLookup(List<TypeDef> types) {
        for (TypeDef type : types) {
            for (PropertyDef property : type.properties()) {
                List<String> values = property.getValues();
                if (values.isEmpty() || values.equals(List.of("True", "False"))) {
                    continue;
                }
                String sanitized = property.upperCamelCase();
                ClassName name = OUTER_CLASS.nestedClass(sanitized);
                if (enumLookup.containsKey(values) && !enumLookup.get(values).equals(name)) {
                    throw new IllegalArgumentException("not unique");
                }
                enumLookup.put(values, name);
            }
        }
    }

    private static TypeSpec generateEnum(ClassName name, List<String> values) {
        String nameWithSpaces = name.simpleName().replaceAll("([a-z])([A-Z])", "$1 $2");

        MethodSpec fromMethod = MethodSpec.methodBuilder("from")
            .addJavadoc("Converts an Object (usually a String) to the correct value\n" +
                "\n" +
                "@param o The Object to convert\n" +
                "@return The corresponding enum value")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(name)
            .addParameter(Object.class, "o")
            .addStatement("return valueOf(o.toString().toUpperCase())")
            .build();

        TypeSpec.Builder builder = TypeSpec.enumBuilder(name)
            .addJavadoc("Enumeration with possible values for the \"" + nameWithSpaces + "\" property")
            .addModifiers(Modifier.PUBLIC);

        for (String value : values) {
            builder.addEnumConstant(value.toUpperCase(), TypeSpec.anonymousClassBuilder("")
                .addJavadoc("Value representing the string {@code $S}", value)
                .build());
        }

        return builder
            .addMethod(fromMethod)
            .build();
    }

    // endregion

    private String multiple(String name) {
        if (name.endsWith("sh")) {
            return name + "es";
        }
        return name + 's';
    }

    private String className(CastNodeID id) {
        return Arrays.stream(id.name().split("_"))
            .map(s -> Character.toUpperCase(s.charAt(0)) + s.substring(1).toLowerCase())
            .collect(Collectors.joining());
    }

    private String indexName(PropertyDef property) {
        return property.variableName() + "Index";
    }
}
