package be.twofold.tinycast.nodes;

import be.twofold.tinycast.CastNode;
import be.twofold.tinycast.CastNodeID;
import be.twofold.tinycast.CastProperty;
import be.twofold.tinycast.CastPropertyID;
import be.twofold.tinycast.Vec3;
import be.twofold.tinycast.Vec4;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public final class Bone extends CastNode {
    Bone(AtomicLong hasher) {
        super(CastNodeID.ROOT, hasher);
    }

    Bone(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
        super(CastNodeID.ROOT, hash, properties, children);
        // TODO: Validation
    }

    public String getName() {
        return getProperty("n", String.class::cast).orElseThrow();
    }

    public Bone setName(String name) {
        createProperty(CastPropertyID.STRING, "n", name);
        return this;
    }

    public Optional<Integer> getParentIndex() {
        return getProperty("p", Integer.class::cast);
    }

    public Bone setParentIndex(Integer parentIndex) {
        createProperty(CastPropertyID.INT, "p", parentIndex);
        return this;
    }

    public Optional<Boolean> getSegmentScaleCompensate() {
        return getProperty("ssc", this::parseBoolean);
    }

    public Bone setSegmentScaleCompensate(Boolean segmentScaleCompensate) {
        createProperty(CastPropertyID.BYTE, "ssc", segmentScaleCompensate ? 1 : 0);
        return this;
    }

    public Optional<Vec3> getLocalPosition() {
        return getProperty("lp", Vec3.class::cast);
    }

    public Bone setLocalPosition(Vec3 localPosition) {
        createProperty(CastPropertyID.VECTOR3, "lp", localPosition);
        return this;
    }

    public Optional<Vec4> getLocalRotation() {
        return getProperty("lr", Vec4.class::cast);
    }

    public Bone setLocalRotation(Vec4 localRotation) {
        createProperty(CastPropertyID.VECTOR4, "lr", localRotation);
        return this;
    }

    public Optional<Vec3> getWorldPosition() {
        return getProperty("wp", Vec3.class::cast);
    }

    public Bone setWorldPosition(Vec3 worldPosition) {
        createProperty(CastPropertyID.VECTOR3, "wp", worldPosition);
        return this;
    }

    public Optional<Vec4> getWorldRotation() {
        return getProperty("wr", Vec4.class::cast);
    }

    public Bone setWorldRotation(Vec4 worldRotation) {
        createProperty(CastPropertyID.VECTOR4, "wr", worldRotation);
        return this;
    }

    public Optional<Vec3> getScale() {
        return getProperty("s", Vec3.class::cast);
    }

    public Bone setScale(Vec3 scale) {
        createProperty(CastPropertyID.VECTOR3, "s", scale);
        return this;
    }
}
