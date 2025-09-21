package be.twofold.tinycast.nodes;

import be.twofold.tinycast.CastNode;
import be.twofold.tinycast.CastNodeID;
import be.twofold.tinycast.CastProperty;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public final class Root extends CastNode {
    Root(AtomicLong hasher) {
        super(CastNodeID.ROOT, hasher);
    }

    Root(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
        super(CastNodeID.ROOT, hash, properties, children);
        // TODO: Validation
    }

    public List<Model> getModels() {
        return getChildrenOfType(Model.class);
    }

    public Model createModel() {
        return createChild(new Model(hasher));
    }

    public List<Animation> getAnimations() {
        return getChildrenOfType(Animation.class);
    }

    public Animation createAnimation() {
        return createChild(new Animation(hasher));
    }

    public List<Instance> getInstances() {
        return getChildrenOfType(Instance.class);
    }

    public Instance createInstance() {
        return createChild(new Instance(hasher));
    }

    public List<Metadata> getMetadatas() {
        return getChildrenOfType(Metadata.class);
    }

    public Metadata createMetadata() {
        return createChild(new Metadata(hasher));
    }
}
