package be.twofold.tinycast.nodes;

import be.twofold.tinycast.CastNode;
import be.twofold.tinycast.CastNodeID;
import be.twofold.tinycast.CastProperty;
import be.twofold.tinycast.CastPropertyID;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public final class File extends CastNode {
    File(AtomicLong hasher) {
        super(CastNodeID.ROOT, hasher);
    }

    File(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
        super(CastNodeID.ROOT, hash, properties, children);
        // TODO: Validation
    }

    public String getPath() {
        return getProperty("p", String.class::cast).orElseThrow();
    }

    public File setPath(String path) {
        createProperty(CastPropertyID.STRING, "p", path);
        return this;
    }
}
