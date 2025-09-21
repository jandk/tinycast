package be.twofold.tinycast.nodes;

import be.twofold.tinycast.CastNode;
import be.twofold.tinycast.CastNodeID;
import be.twofold.tinycast.CastProperty;
import be.twofold.tinycast.CastPropertyID;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public final class CurveModeOverride extends CastNode {
    CurveModeOverride(AtomicLong hasher) {
        super(CastNodeID.ROOT, hasher);
    }

    CurveModeOverride(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
        super(CastNodeID.ROOT, hash, properties, children);
        // TODO: Validation
    }

    public String getNodeName() {
        return getProperty("nn", String.class::cast).orElseThrow();
    }

    public CurveModeOverride setNodeName(String nodeName) {
        createProperty(CastPropertyID.STRING, "nn", nodeName);
        return this;
    }

    public Mode getMode() {
        return getProperty("m", Mode::from).orElseThrow();
    }

    public CurveModeOverride setMode(Mode mode) {
        createProperty(CastPropertyID.STRING, "m", mode.toString().toLowerCase());
        return this;
    }

    public Optional<Boolean> getOverrideTranslationCurves() {
        return getProperty("ot", this::parseBoolean);
    }

    public CurveModeOverride setOverrideTranslationCurves(Boolean overrideTranslationCurves) {
        createProperty(CastPropertyID.BYTE, "ot", overrideTranslationCurves ? 1 : 0);
        return this;
    }

    public Optional<Boolean> getOverrideRotationCurves() {
        return getProperty("or", this::parseBoolean);
    }

    public CurveModeOverride setOverrideRotationCurves(Boolean overrideRotationCurves) {
        createProperty(CastPropertyID.BYTE, "or", overrideRotationCurves ? 1 : 0);
        return this;
    }

    public Optional<Boolean> getOverrideScaleCurves() {
        return getProperty("os", this::parseBoolean);
    }

    public CurveModeOverride setOverrideScaleCurves(Boolean overrideScaleCurves) {
        createProperty(CastPropertyID.BYTE, "os", overrideScaleCurves ? 1 : 0);
        return this;
    }
}
