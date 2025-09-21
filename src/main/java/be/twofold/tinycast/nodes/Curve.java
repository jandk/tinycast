package be.twofold.tinycast.nodes;

import be.twofold.tinycast.CastNode;
import be.twofold.tinycast.CastNodeID;
import be.twofold.tinycast.CastProperty;
import be.twofold.tinycast.CastPropertyID;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public final class Curve extends CastNode {
    Curve(AtomicLong hasher) {
        super(CastNodeID.ROOT, hasher);
    }

    Curve(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
        super(CastNodeID.ROOT, hash, properties, children);
        // TODO: Validation
    }

    public String getNodeName() {
        return getProperty("nn", String.class::cast).orElseThrow();
    }

    public Curve setNodeName(String nodeName) {
        createProperty(CastPropertyID.STRING, "nn", nodeName);
        return this;
    }

    public KeyPropertyName getKeyPropertyName() {
        return getProperty("kp", KeyPropertyName::from).orElseThrow();
    }

    public Curve setKeyPropertyName(KeyPropertyName keyPropertyName) {
        createProperty(CastPropertyID.STRING, "kp", keyPropertyName.toString().toLowerCase());
        return this;
    }

    public Buffer getKeyFrameBuffer() {
        return getProperty("kb", Buffer.class::cast).orElseThrow();
    }

    public Curve setKeyFrameBuffer(Buffer keyFrameBuffer) {
        createIntBufferProperty("kb", keyFrameBuffer);
        return this;
    }

    public Buffer getKeyValueBuffer() {
        return getProperty("kv", Buffer.class::cast).orElseThrow();
    }

    public Curve setKeyValueBuffer(Buffer keyValueBuffer) {
        if (keyValueBuffer instanceof ByteBuffer) {
            createProperty(CastPropertyID.BYTE, "kv", keyValueBuffer);
        } else if (keyValueBuffer instanceof ShortBuffer) {
            createProperty(CastPropertyID.SHORT, "kv", keyValueBuffer);
        } else if (keyValueBuffer instanceof IntBuffer) {
            createProperty(CastPropertyID.INT, "kv", keyValueBuffer);
        } else if (keyValueBuffer instanceof FloatBuffer) {
            createProperty(CastPropertyID.FLOAT, "kv", keyValueBuffer);
        } else if (keyValueBuffer instanceof FloatBuffer) {
            createProperty(CastPropertyID.VECTOR4, "kv", keyValueBuffer);
        } else {
            throw new IllegalArgumentException("Invalid type for property keyValueBuffer");
        }
        return this;
    }

    public Mode getMode() {
        return getProperty("m", Mode::from).orElseThrow();
    }

    public Curve setMode(Mode mode) {
        createProperty(CastPropertyID.STRING, "m", mode.toString().toLowerCase());
        return this;
    }

    public Optional<Float> getAdditiveBlendWeight() {
        return getProperty("ab", Float.class::cast);
    }

    public Curve setAdditiveBlendWeight(Float additiveBlendWeight) {
        createProperty(CastPropertyID.FLOAT, "ab", additiveBlendWeight);
        return this;
    }
}
