package be.twofold.tinycast.generator.model;

import be.twofold.tinycast.CastPropertyID;

import java.util.*;

public final class PropertyDef {
    private final String name;
    private final String key;
    private final Set<CastPropertyID> types;
    private final List<String> values;
    private final boolean isArray;
    private final boolean required;

    public PropertyDef(String name, String key, Set<CastPropertyID> types, List<String> values, boolean isArray, boolean required) {
        this.name = Objects.requireNonNull(name);
        this.key = Objects.requireNonNull(key);
        this.types = EnumSet.copyOf(types);
        this.values = List.copyOf(values);
        this.isArray = isArray;
        this.required = required;

        if (types.isEmpty()) {
            throw new IllegalArgumentException("types is empty");
        }
    }


    public String name() {
        return name;
    }

    public String key() {
        return key;
    }

    public Set<CastPropertyID> types() {
        return types;
    }

    public List<String> values() {
        return values;
    }

    public boolean isArray() {
        return isArray;
    }

    public boolean required() {
        return required;
    }


    public boolean isIndexed() {
        return key.endsWith("%d");
    }

    public boolean isSingular() {
        return types.size() == 1;
    }

    public boolean isBoolean() {
        return !values.isEmpty() && values.equals(List.of("True", "False"));
    }

    public boolean isEnum() {
        return !values.isEmpty() && !values.equals(List.of("True", "False"));
    }


}
