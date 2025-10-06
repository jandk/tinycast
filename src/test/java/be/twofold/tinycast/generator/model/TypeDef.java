package be.twofold.tinycast.generator.model;

import be.twofold.tinycast.CastNodeID;

import java.util.List;
import java.util.Objects;

public final class TypeDef {
    private final CastNodeID type;
    private final List<CastNodeID> children;
    private final List<PropertyDef> properties;

    public TypeDef(CastNodeID type, List<CastNodeID> children, List<PropertyDef> properties) {
        this.type = Objects.requireNonNull(type);
        this.children = List.copyOf(children);
        this.properties = List.copyOf(properties);
    }

    public CastNodeID type() {
        return type;
    }

    public List<CastNodeID> children() {
        return children;
    }

    public List<PropertyDef> properties() {
        return properties;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("TypeDef(")
            .append("type=").append(type).append(", ")
            .append("children=").append(children).append(", ")
            .append("properties=[");

        for (PropertyDef property : properties) {
            builder.append("\n\t").append(property);
        }

        return builder.append("\n])").toString();
    }
}
