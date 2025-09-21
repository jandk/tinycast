package be.twofold.tinycast.generator.model;

import java.util.*;

public final class RawType {
    private final String name;
    private final String children;
    private final String properties;

    public RawType(String name, String children, String properties) {
        this.name = Objects.requireNonNull(name);
        this.children = Objects.requireNonNull(children);
        this.properties = Objects.requireNonNull(properties);
    }

    public String name() {
        return name;
    }

    public String children() {
        return children;
    }

    public String properties() {
        return properties;
    }
}
