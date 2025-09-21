package be.twofold.tinycast.nodes;

import be.twofold.tinycast.CastNode;
import be.twofold.tinycast.CastNodeID;
import be.twofold.tinycast.CastProperty;
import be.twofold.tinycast.CastPropertyID;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public final class Animation extends CastNode {
    Animation(AtomicLong hasher) {
        super(CastNodeID.ROOT, hasher);
    }

    Animation(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
        super(CastNodeID.ROOT, hash, properties, children);
        // TODO: Validation
    }

    public Optional<Skeleton> getSkeletons() {
        return getChildOfType(Skeleton.class);
    }

    public Skeleton createSkeleton() {
        return createChild(new Skeleton(hasher));
    }

    public List<Curve> getCurves() {
        return getChildrenOfType(Curve.class);
    }

    public Curve createCurve() {
        return createChild(new Curve(hasher));
    }

    public List<CurveModeOverride> getCurveModeOverrides() {
        return getChildrenOfType(CurveModeOverride.class);
    }

    public CurveModeOverride createCurveModeOverride() {
        return createChild(new CurveModeOverride(hasher));
    }

    public List<NotificationTrack> getNotificationTracks() {
        return getChildrenOfType(NotificationTrack.class);
    }

    public NotificationTrack createNotificationTrack() {
        return createChild(new NotificationTrack(hasher));
    }

    public Optional<String> getName() {
        return getProperty("n", String.class::cast);
    }

    public Animation setName(String name) {
        createProperty(CastPropertyID.STRING, "n", name);
        return this;
    }

    public float getFramerate() {
        return getProperty("fr", Float.class::cast).orElseThrow();
    }

    public Animation setFramerate(Float framerate) {
        createProperty(CastPropertyID.FLOAT, "fr", framerate);
        return this;
    }

    public Optional<Boolean> getLooping() {
        return getProperty("lo", this::parseBoolean);
    }

    public Animation setLooping(Boolean looping) {
        createProperty(CastPropertyID.BYTE, "lo", looping ? 1 : 0);
        return this;
    }
}
