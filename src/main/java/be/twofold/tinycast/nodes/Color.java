package be.twofold.tinycast.nodes;

import be.twofold.tinycast.CastNode;
import be.twofold.tinycast.CastNodeID;
import be.twofold.tinycast.CastProperty;
import be.twofold.tinycast.CastPropertyID;
import be.twofold.tinycast.Vec4;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public final class Color extends CastNode {
    Color(AtomicLong hasher) {
        super(CastNodeID.ROOT, hasher);
    }

    Color(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
        super(CastNodeID.ROOT, hash, properties, children);
        // TODO: Validation
    }

    public Optional<String> getName() {
        return getProperty("n", String.class::cast);
    }

    public Color setName(String name) {
        createProperty(CastPropertyID.STRING, "n", name);
        return this;
    }

    public Optional<ColorSpace> getColorSpace() {
        return getProperty("cs", ColorSpace::from);
    }

    public Color setColorSpace(ColorSpace colorSpace) {
        createProperty(CastPropertyID.STRING, "cs", colorSpace.toString().toLowerCase());
        return this;
    }

    public Vec4 getRgbaColor() {
        return getProperty("rgba", Vec4.class::cast).orElseThrow();
    }

    public Color setRgbaColor(Vec4 rgbaColor) {
        createProperty(CastPropertyID.VECTOR4, "rgba", rgbaColor);
        return this;
    }
}
