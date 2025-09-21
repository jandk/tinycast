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

public final class Instance extends CastNode {
    Instance(AtomicLong hasher) {
        super(CastNodeID.ROOT, hasher);
    }

    Instance(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
        super(CastNodeID.ROOT, hash, properties, children);
        // TODO: Validation
    }

    public List<File> getFiles() {
        return getChildrenOfType(File.class);
    }

    public File createFile() {
        return createChild(new File(hasher));
    }

    public Optional<String> getName() {
        return getProperty("n", String.class::cast);
    }

    public Instance setName(String name) {
        createProperty(CastPropertyID.STRING, "n", name);
        return this;
    }

    public long getReferenceFile() {
        return getProperty("rf", Long.class::cast).orElseThrow();
    }

    public Instance setReferenceFile(Long referenceFile) {
        createProperty(CastPropertyID.LONG, "rf", referenceFile);
        return this;
    }

    public Vec3 getPosition() {
        return getProperty("p", Vec3.class::cast).orElseThrow();
    }

    public Instance setPosition(Vec3 position) {
        createProperty(CastPropertyID.VECTOR3, "p", position);
        return this;
    }

    public Vec4 getRotation() {
        return getProperty("r", Vec4.class::cast).orElseThrow();
    }

    public Instance setRotation(Vec4 rotation) {
        createProperty(CastPropertyID.VECTOR4, "r", rotation);
        return this;
    }

    public Vec3 getScale() {
        return getProperty("s", Vec3.class::cast).orElseThrow();
    }

    public Instance setScale(Vec3 scale) {
        createProperty(CastPropertyID.VECTOR3, "s", scale);
        return this;
    }
}
