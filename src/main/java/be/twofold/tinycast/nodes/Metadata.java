package be.twofold.tinycast.nodes;

import be.twofold.tinycast.CastNode;
import be.twofold.tinycast.CastNodeID;
import be.twofold.tinycast.CastProperty;
import be.twofold.tinycast.CastPropertyID;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public final class Metadata extends CastNode {
    Metadata(AtomicLong hasher) {
        super(CastNodeID.ROOT, hasher);
    }

    Metadata(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
        super(CastNodeID.ROOT, hash, properties, children);
        // TODO: Validation
    }

    public Optional<String> getAuthor() {
        return getProperty("a", String.class::cast);
    }

    public Metadata setAuthor(String author) {
        createProperty(CastPropertyID.STRING, "a", author);
        return this;
    }

    public Optional<String> getSoftware() {
        return getProperty("s", String.class::cast);
    }

    public Metadata setSoftware(String software) {
        createProperty(CastPropertyID.STRING, "s", software);
        return this;
    }

    public Optional<UpAxis> getUpAxis() {
        return getProperty("up", UpAxis::from);
    }

    public Metadata setUpAxis(UpAxis upAxis) {
        createProperty(CastPropertyID.STRING, "up", upAxis.toString().toLowerCase());
        return this;
    }

    public Optional<String> getSceneRoot() {
        return getProperty("sr", String.class::cast);
    }

    public Metadata setSceneRoot(String sceneRoot) {
        createProperty(CastPropertyID.STRING, "sr", sceneRoot);
        return this;
    }
}
