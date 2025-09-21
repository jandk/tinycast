package be.twofold.tinycast.nodes;

import be.twofold.tinycast.CastNode;
import be.twofold.tinycast.CastNodeID;
import be.twofold.tinycast.CastProperty;
import be.twofold.tinycast.CastPropertyID;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public final class IkHandle extends CastNode {
    IkHandle(AtomicLong hasher) {
        super(CastNodeID.ROOT, hasher);
    }

    IkHandle(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
        super(CastNodeID.ROOT, hash, properties, children);
        // TODO: Validation
    }

    public Optional<String> getName() {
        return getProperty("n", String.class::cast);
    }

    public IkHandle setName(String name) {
        createProperty(CastPropertyID.STRING, "n", name);
        return this;
    }

    public long getStartBoneHash() {
        return getProperty("sb", Long.class::cast).orElseThrow();
    }

    public IkHandle setStartBoneHash(Long startBoneHash) {
        createProperty(CastPropertyID.LONG, "sb", startBoneHash);
        return this;
    }

    public long getEndBoneHash() {
        return getProperty("eb", Long.class::cast).orElseThrow();
    }

    public IkHandle setEndBoneHash(Long endBoneHash) {
        createProperty(CastPropertyID.LONG, "eb", endBoneHash);
        return this;
    }

    public Optional<Long> getTargetBoneHash() {
        return getProperty("tb", Long.class::cast);
    }

    public IkHandle setTargetBoneHash(Long targetBoneHash) {
        createProperty(CastPropertyID.LONG, "tb", targetBoneHash);
        return this;
    }

    public Optional<Long> getPoleVectorBoneHash() {
        return getProperty("pv", Long.class::cast);
    }

    public IkHandle setPoleVectorBoneHash(Long poleVectorBoneHash) {
        createProperty(CastPropertyID.LONG, "pv", poleVectorBoneHash);
        return this;
    }

    public Optional<Long> getPoleBoneHash() {
        return getProperty("pb", Long.class::cast);
    }

    public IkHandle setPoleBoneHash(Long poleBoneHash) {
        createProperty(CastPropertyID.LONG, "pb", poleBoneHash);
        return this;
    }

    public Optional<Boolean> getUseTargetRotation() {
        return getProperty("tr", this::parseBoolean);
    }

    public IkHandle setUseTargetRotation(Boolean useTargetRotation) {
        createProperty(CastPropertyID.BYTE, "tr", useTargetRotation ? 1 : 0);
        return this;
    }
}
