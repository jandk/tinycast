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

public final class Model extends CastNode {
    Model(AtomicLong hasher) {
        super(CastNodeID.ROOT, hasher);
    }

    Model(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
        super(CastNodeID.ROOT, hash, properties, children);
        // TODO: Validation
    }

    public Optional<Skeleton> getSkeletons() {
        return getChildOfType(Skeleton.class);
    }

    public Skeleton createSkeleton() {
        return createChild(new Skeleton(hasher));
    }

    public List<Mesh> getMeshes() {
        return getChildrenOfType(Mesh.class);
    }

    public Mesh createMesh() {
        return createChild(new Mesh(hasher));
    }

    public List<Hair> getHairs() {
        return getChildrenOfType(Hair.class);
    }

    public Hair createHair() {
        return createChild(new Hair(hasher));
    }

    public List<BlendShape> getBlendShapes() {
        return getChildrenOfType(BlendShape.class);
    }

    public BlendShape createBlendShape() {
        return createChild(new BlendShape(hasher));
    }

    public List<Material> getMaterials() {
        return getChildrenOfType(Material.class);
    }

    public Material createMaterial() {
        return createChild(new Material(hasher));
    }

    public Optional<String> getName() {
        return getProperty("n", String.class::cast);
    }

    public Model setName(String name) {
        createProperty(CastPropertyID.STRING, "n", name);
        return this;
    }

    public Optional<Vec3> getPosition() {
        return getProperty("p", Vec3.class::cast);
    }

    public Model setPosition(Vec3 position) {
        createProperty(CastPropertyID.VECTOR3, "p", position);
        return this;
    }

    public Optional<Vec4> getRotation() {
        return getProperty("r", Vec4.class::cast);
    }

    public Model setRotation(Vec4 rotation) {
        createProperty(CastPropertyID.VECTOR4, "r", rotation);
        return this;
    }

    public Optional<Vec3> getScale() {
        return getProperty("s", Vec3.class::cast);
    }

    public Model setScale(Vec3 scale) {
        createProperty(CastPropertyID.VECTOR3, "s", scale);
        return this;
    }
}
