package be.twofold.tinycast.nodes;

import be.twofold.tinycast.CastNode;
import be.twofold.tinycast.CastNodeID;
import be.twofold.tinycast.CastProperty;
import be.twofold.tinycast.CastPropertyID;
import java.nio.Buffer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public final class NotificationTrack extends CastNode {
    NotificationTrack(AtomicLong hasher) {
        super(CastNodeID.ROOT, hasher);
    }

    NotificationTrack(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
        super(CastNodeID.ROOT, hash, properties, children);
        // TODO: Validation
    }

    public String getName() {
        return getProperty("n", String.class::cast).orElseThrow();
    }

    public NotificationTrack setName(String name) {
        createProperty(CastPropertyID.STRING, "n", name);
        return this;
    }

    public Buffer getKeyFrameBuffer() {
        return getProperty("kb", Buffer.class::cast).orElseThrow();
    }

    public NotificationTrack setKeyFrameBuffer(Buffer keyFrameBuffer) {
        createIntBufferProperty("kb", keyFrameBuffer);
        return this;
    }
}
