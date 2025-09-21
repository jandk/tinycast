package be.twofold.tinycast.nodes;

import be.twofold.tinycast.CastNode;
import be.twofold.tinycast.CastNodeID;
import be.twofold.tinycast.CastProperty;
import be.twofold.tinycast.CastPropertyID;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public final class Mesh extends CastNode {
    private int vertexColorBufferIndex;

    private int vertexUVBufferIndex;

    Mesh(AtomicLong hasher) {
        super(CastNodeID.ROOT, hasher);
    }

    Mesh(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
        super(CastNodeID.ROOT, hash, properties, children);
        // TODO: Validation
    }

    public Optional<String> getName() {
        return getProperty("n", String.class::cast);
    }

    public Mesh setName(String name) {
        createProperty(CastPropertyID.STRING, "n", name);
        return this;
    }

    public FloatBuffer getVertexPositionBuffer() {
        return getProperty("vp", FloatBuffer.class::cast).orElseThrow();
    }

    public Mesh setVertexPositionBuffer(FloatBuffer vertexPositionBuffer) {
        createProperty(CastPropertyID.VECTOR3, "vp", vertexPositionBuffer);
        return this;
    }

    public Optional<FloatBuffer> getVertexNormalBuffer() {
        return getProperty("vn", FloatBuffer.class::cast);
    }

    public Mesh setVertexNormalBuffer(FloatBuffer vertexNormalBuffer) {
        createProperty(CastPropertyID.VECTOR3, "vn", vertexNormalBuffer);
        return this;
    }

    public Optional<FloatBuffer> getVertexTangentBuffer() {
        return getProperty("vt", FloatBuffer.class::cast);
    }

    public Mesh setVertexTangentBuffer(FloatBuffer vertexTangentBuffer) {
        createProperty(CastPropertyID.VECTOR3, "vt", vertexTangentBuffer);
        return this;
    }

    public Optional<Buffer> getVertexColorBuffer(int index) {
        return getProperty("String.format(c%d, index)", Buffer.class::cast);
    }

    public Mesh addVertexColorBuffer(Buffer vertexColorBuffer) {
        if (vertexColorBuffer instanceof IntBuffer) {
            createProperty(CastPropertyID.INT, "c" + vertexColorBufferIndex++, vertexColorBuffer);
        } else if (vertexColorBuffer instanceof FloatBuffer) {
            createProperty(CastPropertyID.VECTOR4, "c" + vertexColorBufferIndex++, vertexColorBuffer);
        } else {
            throw new IllegalArgumentException("Invalid type for property vertexColorBuffer");
        }
        return this;
    }

    public Optional<FloatBuffer> getVertexUVBuffer(int index) {
        return getProperty("String.format(u%d, index)", FloatBuffer.class::cast);
    }

    public Mesh addVertexUVBuffer(FloatBuffer vertexUVBuffer) {
        createProperty(CastPropertyID.VECTOR2, "u" + vertexUVBufferIndex++, vertexUVBuffer);
        return this;
    }

    public Optional<Buffer> getVertexWeightBoneBuffer() {
        return getProperty("wb", Buffer.class::cast);
    }

    public Mesh setVertexWeightBoneBuffer(Buffer vertexWeightBoneBuffer) {
        createIntBufferProperty("wb", vertexWeightBoneBuffer);
        return this;
    }

    public Optional<FloatBuffer> getVertexWeightValueBuffer() {
        return getProperty("wv", FloatBuffer.class::cast);
    }

    public Mesh setVertexWeightValueBuffer(FloatBuffer vertexWeightValueBuffer) {
        createProperty(CastPropertyID.FLOAT, "wv", vertexWeightValueBuffer);
        return this;
    }

    public Buffer getFaceBuffer() {
        return getProperty("f", Buffer.class::cast).orElseThrow();
    }

    public Mesh setFaceBuffer(Buffer faceBuffer) {
        createIntBufferProperty("f", faceBuffer);
        return this;
    }

    public Optional<Integer> getColorLayerCount() {
        return getProperty("cl", Integer.class::cast);
    }

    public Mesh setColorLayerCount(Integer colorLayerCount) {
        createIntProperty("cl", colorLayerCount);
        return this;
    }

    public Optional<Integer> getUVLayerCount() {
        return getProperty("ul", Integer.class::cast);
    }

    public Mesh setUVLayerCount(Integer uVLayerCount) {
        createIntProperty("ul", uVLayerCount);
        return this;
    }

    public Optional<Integer> getMaximumWeightInfluence() {
        return getProperty("mi", Integer.class::cast);
    }

    public Mesh setMaximumWeightInfluence(Integer maximumWeightInfluence) {
        createIntProperty("mi", maximumWeightInfluence);
        return this;
    }

    public Optional<SkinningMethod> getSkinningMethod() {
        return getProperty("sm", SkinningMethod::from);
    }

    public Mesh setSkinningMethod(SkinningMethod skinningMethod) {
        createProperty(CastPropertyID.STRING, "sm", skinningMethod.toString().toLowerCase());
        return this;
    }

    public Optional<Long> getMaterial() {
        return getProperty("m", Long.class::cast);
    }

    public Mesh setMaterial(Long material) {
        createProperty(CastPropertyID.LONG, "m", material);
        return this;
    }
}
