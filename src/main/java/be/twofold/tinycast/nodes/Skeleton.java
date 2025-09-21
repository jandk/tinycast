package be.twofold.tinycast.nodes;

import be.twofold.tinycast.CastNode;
import be.twofold.tinycast.CastNodeID;
import be.twofold.tinycast.CastProperty;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public final class Skeleton extends CastNode {
    Skeleton(AtomicLong hasher) {
        super(CastNodeID.ROOT, hasher);
    }

    Skeleton(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
        super(CastNodeID.ROOT, hash, properties, children);
        // TODO: Validation
    }

    public List<Bone> getBones() {
        return getChildrenOfType(Bone.class);
    }

    public Bone createBone() {
        return createChild(new Bone(hasher));
    }

    public List<IkHandle> getIkHandles() {
        return getChildrenOfType(IkHandle.class);
    }

    public IkHandle createIkHandle() {
        return createChild(new IkHandle(hasher));
    }

    public List<Constraint> getConstraints() {
        return getChildrenOfType(Constraint.class);
    }

    public Constraint createConstraint() {
        return createChild(new Constraint(hasher));
    }
}
