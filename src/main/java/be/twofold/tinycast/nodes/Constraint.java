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

public final class Constraint extends CastNode {
    Constraint(AtomicLong hasher) {
        super(CastNodeID.ROOT, hasher);
    }

    Constraint(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
        super(CastNodeID.ROOT, hash, properties, children);
        // TODO: Validation
    }

    public Optional<String> getName() {
        return getProperty("n", String.class::cast);
    }

    public Constraint setName(String name) {
        createProperty(CastPropertyID.STRING, "n", name);
        return this;
    }

    public ConstraintType getConstraintType() {
        return getProperty("ct", ConstraintType::from).orElseThrow();
    }

    public Constraint setConstraintType(ConstraintType constraintType) {
        createProperty(CastPropertyID.STRING, "ct", constraintType.toString().toLowerCase());
        return this;
    }

    public long getConstraintBoneHash() {
        return getProperty("cb", Long.class::cast).orElseThrow();
    }

    public Constraint setConstraintBoneHash(Long constraintBoneHash) {
        createProperty(CastPropertyID.LONG, "cb", constraintBoneHash);
        return this;
    }

    public long getTargetBoneHash() {
        return getProperty("tb", Long.class::cast).orElseThrow();
    }

    public Constraint setTargetBoneHash(Long targetBoneHash) {
        createProperty(CastPropertyID.LONG, "tb", targetBoneHash);
        return this;
    }

    public Optional<Boolean> getMaintainOffset() {
        return getProperty("mo", this::parseBoolean);
    }

    public Constraint setMaintainOffset(Boolean maintainOffset) {
        createProperty(CastPropertyID.BYTE, "mo", maintainOffset ? 1 : 0);
        return this;
    }

    public Optional<Object> getCustomOffset() {
        return getProperty("co", Object.class::cast);
    }

    public Constraint setCustomOffset(Object customOffset) {
        if (customOffset instanceof Vec3) {
            createProperty(CastPropertyID.VECTOR3, "co", customOffset);
        } else if (customOffset instanceof Vec4) {
            createProperty(CastPropertyID.VECTOR4, "co", customOffset);
        } else {
            throw new IllegalArgumentException("Invalid type for property customOffset");
        }
        return this;
    }

    public Optional<Float> getWeight() {
        return getProperty("wt", Float.class::cast);
    }

    public Constraint setWeight(Float weight) {
        createProperty(CastPropertyID.FLOAT, "wt", weight);
        return this;
    }

    public Optional<Boolean> getSkipX() {
        return getProperty("sx", this::parseBoolean);
    }

    public Constraint setSkipX(Boolean skipX) {
        createProperty(CastPropertyID.BYTE, "sx", skipX ? 1 : 0);
        return this;
    }

    public Optional<Boolean> getSkipY() {
        return getProperty("sy", this::parseBoolean);
    }

    public Constraint setSkipY(Boolean skipY) {
        createProperty(CastPropertyID.BYTE, "sy", skipY ? 1 : 0);
        return this;
    }

    public Optional<Boolean> getSkipZ() {
        return getProperty("sz", this::parseBoolean);
    }

    public Constraint setSkipZ(Boolean skipZ) {
        createProperty(CastPropertyID.BYTE, "sz", skipZ ? 1 : 0);
        return this;
    }
}
