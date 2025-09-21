package be.twofold.tinycast.generator;

import be.twofold.tinycast.*;
import be.twofold.tinycast.generator.model.*;
import com.squareup.javapoet.*;

import javax.lang.model.element.*;
import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;

final class TypeClassWriter {
    private static final String PACKAGE_NAME = "be.twofold.tinycast";
    private static final ClassName OUTER_CLASS = ClassName.get(PACKAGE_NAME, "CastNodes");
    private static final ClassName SUPER_CLASS = ClassName.get(CastNode.class);

    private final Map<List<String>, ClassName> enumLookup = new HashMap<>();

    void generate(List<TypeDef> types) throws IOException {
        TypeSpec.Builder builder = TypeSpec.classBuilder(OUTER_CLASS)
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
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
            .superclass(SUPER_CLASS);

        List<PropertyDef> indexedProperties = type.properties().stream()
            .filter(prop -> prop.key().contains("%d"))
            .collect(Collectors.toList());

        for (PropertyDef property : indexedProperties) {
            builder.addField(FieldSpec.builder(int.class, indexName(property))
                .addModifiers(Modifier.PRIVATE)
                .build());
        }

        // Constructors
        builder.addMethod(MethodSpec.constructorBuilder()
            .addParameter(AtomicLong.class, "hasher")
            .addStatement("super($T.$L, hasher)", CastNodeID.class, CastNodeID.ROOT)
            .build());

        builder.addMethod(MethodSpec.constructorBuilder()
            .addParameter(long.class, "hash")
            .addParameter(ParameterizedTypeName.get(Map.class, String.class, CastProperty.class), "properties")
            .addParameter(ParameterizedTypeName.get(List.class, CastNode.class), "children")
            .addStatement("super($T.$L, hash, properties, children)", CastNodeID.class, CastNodeID.ROOT)
            .addComment("TODO: Validation")
            .build());

        for (CastNodeID child : type.children()) {
            builder.addMethods(generateNodeMethods(child));
        }

        for (PropertyDef property : type.properties()) {
            String name = wordsToCamelCase(property.name(), true);
            TypeName typeName = propertyType(property);

            builder.addMethod(generateGetProperty(property, name, typeName));
            builder.addMethod(generateSetProperty(property, name, typeName, className));
        }

        return builder.build();
    }

    private MethodSpec generateGetProperty(PropertyDef property, String name, TypeName typeName) {
        TypeName returnType = makeOptional(property, typeName);
        String required = property.required() ? ".orElseThrow()" : "";
        String getKey = property.isIndexed()
            ? "String.format(" + property.key() + ", index)"
            : property.key();

        MethodSpec.Builder builder = MethodSpec.methodBuilder("get" + name)
            .addModifiers(Modifier.PUBLIC)
            .returns(returnType);
        if (property.isIndexed()) {
            builder.addParameter(int.class, "index");
        }

        if (property.isEnum()) {
            builder.addStatement("return getProperty($S, $T::from)" + required, getKey, typeName);
        } else if (property.isBoolean()) {
            builder.addStatement("return getProperty($S, this::parseBoolean)" + required, getKey);
        } else {
            builder.addStatement("return getProperty($S, $T.class::cast)" + required, getKey, typeName);
        }
        return builder.build();
    }

    private MethodSpec generateSetProperty(PropertyDef property, String name, TypeName typeName, ClassName className) {
        String setterName = Character.toLowerCase(name.charAt(0)) + name.substring(1);

        MethodSpec.Builder builder = MethodSpec.methodBuilder((property.isIndexed() ? "add" : "set") + name)
            .addModifiers(Modifier.PUBLIC)
            .returns(className)
            .addParameter(typeName, setterName);

        String key = '"' + (property.isIndexed() ? property.key().replace("%d", "") : property.key()) + '"';
        String setKey = property.isIndexed() ? key + " + " + indexName(property) + "++" : key;
        if (property.isSingular()) {
            String setMapper = getSetMapper(property);
            builder.addStatement("createProperty($T.$L, $L, " + setMapper + ")", CastPropertyID.class, property.types().iterator().next(), setKey, setterName);
        } else if (property.types().equals(EnumSet.of(CastPropertyID.BYTE, CastPropertyID.SHORT, CastPropertyID.INT))) {
            if (property.isArray()) {
                builder.addStatement("createIntBufferProperty($L, $L)", setKey, setterName);
            } else {
                builder.addStatement("createIntProperty($L, $L)", setKey, setterName);
            }
        } else {
            boolean first = true;
            for (CastPropertyID propertyID : property.types()) {
                Class<?> instanceType = property.isArray() ? arrayType(propertyID) : singularType(propertyID);
                if (first) {
                    builder.beginControlFlow("if ($L instanceof $T)", setterName, instanceType);
                    first = false;
                } else {
                    builder.nextControlFlow("else if ($L instanceof $T)", setterName, instanceType);
                }
                builder.addStatement("createProperty($T.$L, $L, $L)", CastPropertyID.class, propertyID, setKey, setterName);
            }
            builder
                .nextControlFlow("else")
                .addStatement("throw new $T(\"Invalid type for property $L\")", IllegalArgumentException.class, setterName)
                .endControlFlow();
        }

        return builder
            .addStatement("return this")
            .build();
    }

    private List<MethodSpec> generateNodeMethods(CastNodeID child) {
        String childClassName = className(child);
        TypeName childType = OUTER_CLASS.nestedClass(childClassName);
        TypeName returnType = child == CastNodeID.SKELETON
            ? ParameterizedTypeName.get(ClassName.get(Optional.class), OUTER_CLASS.nestedClass("Skeleton"))
            : ParameterizedTypeName.get(ClassName.get(List.class), childType);
        String methodName = child == CastNodeID.SKELETON ? "getChildOfType" : "getChildrenOfType";

        MethodSpec getter = MethodSpec.methodBuilder("get" + multiple(childClassName))
            .addModifiers(Modifier.PUBLIC)
            .returns(returnType)
            .addStatement("return $L($T.class)", methodName, childType)
            .build();

        MethodSpec creator = MethodSpec.methodBuilder("create" + childClassName)
            .addModifiers(Modifier.PUBLIC)
            .returns(childType)
            .addStatement("return createChild(new $T(hasher))", childType)
            .build();

        return List.of(getter, creator);
    }

    private String getSetMapper(PropertyDef property) {
        if (property.isEnum()) {
            return "$N.toString().toLowerCase()";
        }
        if (property.isBoolean()) {
            return "$N ? 1 : 0";
        }
        return "$N";
    }

    private TypeName propertyType(PropertyDef property) {
        if (property.isSingular()) {
            CastPropertyID propertyID = property.types().iterator().next();
            if (property.isArray()) {
                return ClassName.get(arrayType(propertyID));
            } else if (property.isBoolean()) {
                return ClassName.get(Boolean.class);
            } else if (property.isEnum()) {
                return enumLookup.get(property.values());
            } else {
                return ClassName.get(singularType(propertyID));
            }
        }

        // Multiple types and it's an array, that's just some form of buffer
        if (property.isArray()) {
            return ClassName.get(Buffer.class);
        }

        // Stupid special cases
        EnumSet<CastPropertyID> types = EnumSet.copyOf(property.types());
        if (types.equals(EnumSet.of(CastPropertyID.VECTOR3, CastPropertyID.VECTOR4))) {
            return ClassName.get(Object.class);
        } else if (types.equals(EnumSet.of(CastPropertyID.BYTE, CastPropertyID.SHORT, CastPropertyID.INT))) {
            return ClassName.get(Integer.class);
        } else {
            throw new IllegalArgumentException(types.toString());
        }
    }

    private Class<?> singularType(CastPropertyID propertyID) {
        switch (propertyID) {
            case BYTE:
                return Byte.class;
            case SHORT:
                return Short.class;
            case INT:
                return Integer.class;
            case LONG:
                return Long.class;
            case FLOAT:
                return Float.class;
            case DOUBLE:
                return Double.class;
            case STRING:
                return String.class;
            case VECTOR2:
                return Vec2.class;
            case VECTOR3:
                return Vec3.class;
            case VECTOR4:
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
            case INT:
                return IntBuffer.class;
            case LONG:
                return LongBuffer.class;
            case FLOAT:
            case VECTOR2:
            case VECTOR3:
            case VECTOR4:
                return FloatBuffer.class;
            case DOUBLE:
                return DoubleBuffer.class;
            case STRING:
                throw new IllegalArgumentException();
            default:
                throw new UnsupportedOperationException();
        }
    }

    private TypeName makeOptional(PropertyDef property, TypeName type) {
        if (!property.required()) {
            return ParameterizedTypeName.get(ClassName.get(Optional.class), type);
        }
        if (type.isBoxedPrimitive()) {
            return type.unbox();
        }
        return type;
    }

    // region Enums

    private void buildEnumLookup(List<TypeDef> types) {
        for (TypeDef type : types) {
            for (PropertyDef property : type.properties()) {
                List<String> values = property.values();
                if (values.isEmpty() || values.equals(List.of("True", "False"))) {
                    continue;
                }
                String sanitized = wordsToCamelCase(property.name(), true);
                ClassName name = OUTER_CLASS.nestedClass(sanitized);
                if (enumLookup.containsKey(values) && !enumLookup.get(values).equals(name)) {
                    throw new IllegalArgumentException("not unique");
                }
                enumLookup.put(values, name);
            }
        }
    }

    private TypeSpec generateEnum(ClassName name, List<String> values) {
        MethodSpec fromMethod = MethodSpec.methodBuilder("from")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(name)
            .addParameter(Object.class, "o")
            .addStatement("return valueOf(o.toString().toUpperCase())")
            .build();

        TypeSpec.Builder builder = TypeSpec.enumBuilder(name)
            .addModifiers(Modifier.PUBLIC);

        for (String value : values) {
            builder.addEnumConstant(value.toUpperCase());
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

    private String className(CastNodeID child) {
        String result = Arrays.stream(child.name().split("_"))
            .map(s -> changeFirst(s.toLowerCase(), true))
            .collect(Collectors.joining());

        return changeFirst(result, true);
    }

    private String indexName(PropertyDef property) {
        return wordsToCamelCase(property.name(), false) + "Index";
    }

    private String wordsToCamelCase(String s, boolean upper) {
        String result = String.join("", s.split("\\s+"));
        return changeFirst(result, upper);
    }

    private String changeFirst(String s, boolean upper) {
        char first = upper
            ? Character.toUpperCase(s.charAt(0))
            : Character.toLowerCase(s.charAt(0));
        return first + s.substring(1);
    }
}
