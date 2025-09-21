package be.twofold.tinycast.nodes;

import be.twofold.tinycast.CastNode;
import be.twofold.tinycast.CastNodeID;
import be.twofold.tinycast.CastProperty;
import be.twofold.tinycast.CastPropertyID;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public final class Hair extends CastNode {
    Hair(AtomicLong hasher) {
        super(CastNodeID.ROOT, hasher);
    }

    Hair(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
        super(CastNodeID.ROOT, hash, properties, children);
        // TODO: Validation
    }

    public Optional<String> getName() {
        return getProperty("n", String.class::cast);
    }

    public Hair setName(String name) {
        createProperty(CastPropertyID.STRING, "n", name);
        return this;
    }

    public Buffer getSegmentsBuffer() {
        return getProperty("se", Buffer.class::cast).orElseThrow();
    }

    public Hair setSegmentsBuffer(Buffer segmentsBuffer) {
        createIntBufferProperty("se", segmentsBuffer);
        return this;
    }

    public FloatBuffer getParticleBuffer() {
        return getProperty("pt", FloatBuffer.class::cast).orElseThrow();
    }

    public Hair setParticleBuffer(FloatBuffer particleBuffer) {
        createProperty(CastPropertyID.VECTOR3, "pt", particleBuffer);
        return this;
    }

    public Optional<Long> getMaterial() {
        return getProperty("m", Long.class::cast);
    }

    public Hair setMaterial(Long material) {
        createProperty(CastPropertyID.LONG, "m", material);
        return this;
    }
}
