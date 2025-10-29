package be.twofold.tinycast.generator;

import be.twofold.tinycast.CastNodeID;
import be.twofold.tinycast.CastPropertyID;
import be.twofold.tinycast.generator.model.PropertyDef;
import be.twofold.tinycast.generator.model.TypeDef;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class TypeParser {
    private final Map<List<String>, String> enums = new HashMap<>();

    public Map<List<String>, String> getEnums() {
        return Collections.unmodifiableMap(enums);
    }

    public List<TypeDef> parse(InputStream in) throws IOException {
        try (Reader reader = new InputStreamReader(in)) {
            JsonArray root = JsonParser
                .parseReader(reader)
                .getAsJsonArray();

            return stream(root)
                .map(e -> parseType(e.getAsJsonObject()))
                .collect(Collectors.toList());
        }
    }

    private TypeDef parseType(JsonObject object) {
        CastNodeID type = parseNodeID(object.getAsJsonPrimitive("type").getAsString());

        List<CastNodeID> children = stream(object, "children")
            .map(element -> parseNodeID(element.getAsString()))
            .collect(Collectors.toList());

        List<PropertyDef> properties = stream(object, "properties")
            .map(element -> parseProperty(element.getAsJsonObject()))
            .collect(Collectors.toList());

        return new TypeDef(type, children, properties);
    }

    private PropertyDef parseProperty(JsonObject object) {
        String key = object.getAsJsonPrimitive("key").getAsString();
        String name = object.getAsJsonPrimitive("name").getAsString();
        boolean array = object.getAsJsonPrimitive("array").getAsBoolean();
        boolean required = object.getAsJsonPrimitive("required").getAsBoolean();
        Set<CastPropertyID> types = stream(object, "types")
            .map(e -> parsePropertyID(e.getAsString()))
            .collect(Collectors.toSet());
        List<String> values = stream(object, "values")
            .map(JsonElement::getAsString)
            .collect(Collectors.toList());

        if (!values.isEmpty() && !values.equals(List.of("True", "False"))) {
            enums.put(values, name);
        }

        return new PropertyDef(key, name, array, required, types, values);
    }

    private CastNodeID parseNodeID(String id) {
        switch (id) {
            case "Root":
                return CastNodeID.ROOT;
            case "Model":
                return CastNodeID.MODEL;
            case "Mesh":
                return CastNodeID.MESH;
            case "Hair":
                return CastNodeID.HAIR;
            case "BlendShape":
                return CastNodeID.BLEND_SHAPE;
            case "Skeleton":
                return CastNodeID.SKELETON;
            case "Bone":
                return CastNodeID.BONE;
            case "IKHandle":
                return CastNodeID.IK_HANDLE;
            case "Constraint":
                return CastNodeID.CONSTRAINT;
            case "Animation":
                return CastNodeID.ANIMATION;
            case "Curve":
                return CastNodeID.CURVE;
            case "CurveModeOverride":
                return CastNodeID.CURVE_MODE_OVERRIDE;
            case "NotificationTrack":
                return CastNodeID.NOTIFICATION_TRACK;
            case "Material":
                return CastNodeID.MATERIAL;
            case "File":
                return CastNodeID.FILE;
            case "Color":
                return CastNodeID.COLOR;
            case "Instance":
                return CastNodeID.INSTANCE;
            case "Metadata":
                return CastNodeID.METADATA;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private CastPropertyID parsePropertyID(String id) {
        switch (id) {
            case "b":
                return CastPropertyID.BYTE;
            case "h":
                return CastPropertyID.SHORT;
            case "i":
                return CastPropertyID.INTEGER_32;
            case "l":
                return CastPropertyID.INTEGER_64;
            case "f":
                return CastPropertyID.FLOAT;
            case "d":
                return CastPropertyID.DOUBLE;
            case "s":
                return CastPropertyID.STRING;
            case "v2":
                return CastPropertyID.VECTOR_2;
            case "v3":
                return CastPropertyID.VECTOR_3;
            case "v4":
                return CastPropertyID.VECTOR_4;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private Stream<JsonElement> stream(JsonObject object, String property) {
        JsonElement element = object.get(property);
        if (element == null || !element.isJsonArray()) {
            return Stream.empty();
        }
        return stream(element.getAsJsonArray());
    }

    private Stream<JsonElement> stream(JsonArray array) {
        return IntStream.range(0, array.size()).mapToObj(array::get);
    }
}
