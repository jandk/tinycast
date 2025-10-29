package be.twofold.tinycast.generator.model;

import be.twofold.tinycast.CastPropertyID;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class PropertyDef {
    private final String key;
    private final String name;
    private final boolean array;
    private final boolean required;
    private final Set<CastPropertyID> types;
    private final List<String> values;

    public PropertyDef(String key, String name, boolean array, boolean required, Set<CastPropertyID> types, List<String> values) {
        this.key = Objects.requireNonNull(key);
        this.name = Objects.requireNonNull(name);
        this.array = array;
        this.required = required;
        this.types = EnumSet.copyOf(types);
        this.values = List.copyOf(values);

        if (types.isEmpty()) {
            throw new IllegalArgumentException("types is empty");
        }
    }


    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public boolean isArray() {
        return array;
    }

    public boolean isRequired() {
        return required;
    }

    public Set<CastPropertyID> getTypes() {
        return types;
    }

    public List<String> getValues() {
        return values;
    }

    public PropertyType getType() {
        if (values.isEmpty()) {
            return PropertyType.SIMPLE;
        } else if (values.equals(List.of("True", "False"))) {
            return PropertyType.BOOL;
        } else {
            return PropertyType.ENUM;
        }
    }

    public boolean isIndexed() {
        return key.endsWith("%d");
    }

    @Override
    public String toString() {
        return "PropertyDef(" +
            "key=" + key + ", " +
            "name=" + name + ", " +
            "array=" + array + ", " +
            "required=" + required + ", " +
            "types=" + types + ", " +
            "values=" + values +
            ")";
    }

    public String variableName() {
        return wordsToCamelCase(getName(), false);
    }

    public String upperCamelCase() {
        return wordsToCamelCase(getName(), true);
    }

    private String wordsToCamelCase(String s, boolean upper) {
        String result = String.join("", s.split("\\s+"));
        char first = upper
            ? Character.toUpperCase(result.charAt(0))
            : Character.toLowerCase(result.charAt(0));
        return first + result.substring(1);
    }

}
