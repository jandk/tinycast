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

public final class BlendShape extends CastNode {
    BlendShape(AtomicLong hasher) {
        super(CastNodeID.ROOT, hasher);
    }

    BlendShape(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
        super(CastNodeID.ROOT, hash, properties, children);
        // TODO: Validation
    }

    public String getName() {
        return getProperty("n", String.class::cast).orElseThrow();
    }

    public BlendShape setName(String name) {
        createProperty(CastPropertyID.STRING, "n", name);
        return this;
    }

    public long getBaseShape() {
        return getProperty("b", Long.class::cast).orElseThrow();
    }

    public BlendShape setBaseShape(Long baseShape) {
        createProperty(CastPropertyID.LONG, "b", baseShape);
        return this;
    }

    public Buffer getTargetShapeVertexIndices() {
        return getProperty("vi", Buffer.class::cast).orElseThrow();
    }

    public BlendShape setTargetShapeVertexIndices(Buffer targetShapeVertexIndices) {
        createIntBufferProperty("vi", targetShapeVertexIndices);
        return this;
    }

    public FloatBuffer getTargetShapeVertexPositions() {
        return getProperty("vp", FloatBuffer.class::cast).orElseThrow();
    }

    public BlendShape setTargetShapeVertexPositions(FloatBuffer targetShapeVertexPositions) {
        createProperty(CastPropertyID.VECTOR3, "vp", targetShapeVertexPositions);
        return this;
    }

    public Optional<FloatBuffer> getTargetWeightScale() {
        return getProperty("ts", FloatBuffer.class::cast);
    }

    public BlendShape setTargetWeightScale(FloatBuffer targetWeightScale) {
        createProperty(CastPropertyID.FLOAT, "ts", targetWeightScale);
        return this;
    }
}
