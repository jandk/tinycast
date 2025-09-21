package be.twofold.tinycast.nodes;

import be.twofold.tinycast.CastNode;
import be.twofold.tinycast.CastNodeID;
import be.twofold.tinycast.CastProperty;
import be.twofold.tinycast.CastPropertyID;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public final class Material extends CastNode {
    private int extraIndex;

    Material(AtomicLong hasher) {
        super(CastNodeID.ROOT, hasher);
    }

    Material(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
        super(CastNodeID.ROOT, hash, properties, children);
        // TODO: Validation
    }

    public List<File> getFiles() {
        return getChildrenOfType(File.class);
    }

    public File createFile() {
        return createChild(new File(hasher));
    }

    public List<Color> getColors() {
        return getChildrenOfType(Color.class);
    }

    public Color createColor() {
        return createChild(new Color(hasher));
    }

    public String getName() {
        return getProperty("n", String.class::cast).orElseThrow();
    }

    public Material setName(String name) {
        createProperty(CastPropertyID.STRING, "n", name);
        return this;
    }

    public Type getType() {
        return getProperty("t", Type::from).orElseThrow();
    }

    public Material setType(Type type) {
        createProperty(CastPropertyID.STRING, "t", type.toString().toLowerCase());
        return this;
    }

    public Optional<Long> getAlbedoHash() {
        return getProperty("albedo", Long.class::cast);
    }

    public Material setAlbedoHash(Long albedoHash) {
        createProperty(CastPropertyID.LONG, "albedo", albedoHash);
        return this;
    }

    public Optional<Long> getDiffuseHash() {
        return getProperty("diffuse", Long.class::cast);
    }

    public Material setDiffuseHash(Long diffuseHash) {
        createProperty(CastPropertyID.LONG, "diffuse", diffuseHash);
        return this;
    }

    public Optional<Long> getNormalHash() {
        return getProperty("normal", Long.class::cast);
    }

    public Material setNormalHash(Long normalHash) {
        createProperty(CastPropertyID.LONG, "normal", normalHash);
        return this;
    }

    public Optional<Long> getSpecularHash() {
        return getProperty("specular", Long.class::cast);
    }

    public Material setSpecularHash(Long specularHash) {
        createProperty(CastPropertyID.LONG, "specular", specularHash);
        return this;
    }

    public Optional<Long> getGlossHash() {
        return getProperty("gloss", Long.class::cast);
    }

    public Material setGlossHash(Long glossHash) {
        createProperty(CastPropertyID.LONG, "gloss", glossHash);
        return this;
    }

    public Optional<Long> getRoughnessHash() {
        return getProperty("roughness", Long.class::cast);
    }

    public Material setRoughnessHash(Long roughnessHash) {
        createProperty(CastPropertyID.LONG, "roughness", roughnessHash);
        return this;
    }

    public Optional<Long> getEmissiveHash() {
        return getProperty("emissive", Long.class::cast);
    }

    public Material setEmissiveHash(Long emissiveHash) {
        createProperty(CastPropertyID.LONG, "emissive", emissiveHash);
        return this;
    }

    public Optional<Long> getEmissiveMaskHash() {
        return getProperty("emask", Long.class::cast);
    }

    public Material setEmissiveMaskHash(Long emissiveMaskHash) {
        createProperty(CastPropertyID.LONG, "emask", emissiveMaskHash);
        return this;
    }

    public Optional<Long> getAmbientOcclusionHash() {
        return getProperty("ao", Long.class::cast);
    }

    public Material setAmbientOcclusionHash(Long ambientOcclusionHash) {
        createProperty(CastPropertyID.LONG, "ao", ambientOcclusionHash);
        return this;
    }

    public Optional<Long> getCavityHash() {
        return getProperty("cavity", Long.class::cast);
    }

    public Material setCavityHash(Long cavityHash) {
        createProperty(CastPropertyID.LONG, "cavity", cavityHash);
        return this;
    }

    public Optional<Long> getAnisotropyHash() {
        return getProperty("aniso", Long.class::cast);
    }

    public Material setAnisotropyHash(Long anisotropyHash) {
        createProperty(CastPropertyID.LONG, "aniso", anisotropyHash);
        return this;
    }

    public Optional<Long> getExtra(int index) {
        return getProperty("String.format(extra%d, index)", Long.class::cast);
    }

    public Material addExtra(Long extra) {
        createProperty(CastPropertyID.LONG, "extra" + extraIndex++, extra);
        return this;
    }
}
