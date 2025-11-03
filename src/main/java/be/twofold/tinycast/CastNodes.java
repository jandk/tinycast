package be.twofold.tinycast;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Namespace class containing the different Cast node types
 */
public final class CastNodes {
    private CastNodes() {
    }

    static CastNode create(CastNodeID identifier, long nodeHash,
            Map<String, CastProperty> properties, List<CastNode> children) {
        switch (identifier) {
            case ROOT: {
                return new Root(nodeHash, properties, children);
            }
            case MODEL: {
                return new Model(nodeHash, properties, children);
            }
            case MESH: {
                return new Mesh(nodeHash, properties, children);
            }
            case HAIR: {
                return new Hair(nodeHash, properties, children);
            }
            case BLEND_SHAPE: {
                return new BlendShape(nodeHash, properties, children);
            }
            case SKELETON: {
                return new Skeleton(nodeHash, properties, children);
            }
            case BONE: {
                return new Bone(nodeHash, properties, children);
            }
            case IK_HANDLE: {
                return new IkHandle(nodeHash, properties, children);
            }
            case CONSTRAINT: {
                return new Constraint(nodeHash, properties, children);
            }
            case ANIMATION: {
                return new Animation(nodeHash, properties, children);
            }
            case CURVE: {
                return new Curve(nodeHash, properties, children);
            }
            case CURVE_MODE_OVERRIDE: {
                return new CurveModeOverride(nodeHash, properties, children);
            }
            case NOTIFICATION_TRACK: {
                return new NotificationTrack(nodeHash, properties, children);
            }
            case MATERIAL: {
                return new Material(nodeHash, properties, children);
            }
            case FILE: {
                return new File(nodeHash, properties, children);
            }
            case COLOR: {
                return new Color(nodeHash, properties, children);
            }
            case INSTANCE: {
                return new Instance(nodeHash, properties, children);
            }
            case METADATA: {
                return new Metadata(nodeHash, properties, children);
            }
        }
        throw new UnsupportedOperationException();
    }

    /**
     * Enumeration with possible values for the "Constraint Type" property
     */
    public enum ConstraintType {
        /**
         * Value representing the string {@code "sc"}
         */
        SC,

        /**
         * Value representing the string {@code "or"}
         */
        OR,

        /**
         * Value representing the string {@code "pt"}
         */
        PT;

        /**
         * Converts an Object (usually a String) to the correct value
         *
         * @param o The Object to convert
         * @return The corresponding enum value
         */
        public static ConstraintType from(Object o) {
            return valueOf(o.toString().toUpperCase());
        }
    }

    /**
     * Enumeration with possible values for the "Color Space" property
     */
    public enum ColorSpace {
        /**
         * Value representing the string {@code "linear"}
         */
        LINEAR,

        /**
         * Value representing the string {@code "srgb"}
         */
        SRGB;

        /**
         * Converts an Object (usually a String) to the correct value
         *
         * @param o The Object to convert
         * @return The corresponding enum value
         */
        public static ColorSpace from(Object o) {
            return valueOf(o.toString().toUpperCase());
        }
    }

    /**
     * Enumeration with possible values for the "Up Axis" property
     */
    public enum UpAxis {
        /**
         * Value representing the string {@code "x"}
         */
        X,

        /**
         * Value representing the string {@code "y"}
         */
        Y,

        /**
         * Value representing the string {@code "z"}
         */
        Z;

        /**
         * Converts an Object (usually a String) to the correct value
         *
         * @param o The Object to convert
         * @return The corresponding enum value
         */
        public static UpAxis from(Object o) {
            return valueOf(o.toString().toUpperCase());
        }
    }

    /**
     * Enumeration with possible values for the "Key Property Name" property
     */
    public enum KeyPropertyName {
        /**
         * Value representing the string {@code "bs"}
         */
        BS,

        /**
         * Value representing the string {@code "tx"}
         */
        TX,

        /**
         * Value representing the string {@code "ty"}
         */
        TY,

        /**
         * Value representing the string {@code "sx"}
         */
        SX,

        /**
         * Value representing the string {@code "tz"}
         */
        TZ,

        /**
         * Value representing the string {@code "sy"}
         */
        SY,

        /**
         * Value representing the string {@code "sz"}
         */
        SZ,

        /**
         * Value representing the string {@code "vb"}
         */
        VB,

        /**
         * Value representing the string {@code "rq"}
         */
        RQ;

        /**
         * Converts an Object (usually a String) to the correct value
         *
         * @param o The Object to convert
         * @return The corresponding enum value
         */
        public static KeyPropertyName from(Object o) {
            return valueOf(o.toString().toUpperCase());
        }
    }

    /**
     * Enumeration with possible values for the "Mode" property
     */
    public enum Mode {
        /**
         * Value representing the string {@code "absolute"}
         */
        ABSOLUTE,

        /**
         * Value representing the string {@code "additive"}
         */
        ADDITIVE,

        /**
         * Value representing the string {@code "relative"}
         */
        RELATIVE;

        /**
         * Converts an Object (usually a String) to the correct value
         *
         * @param o The Object to convert
         * @return The corresponding enum value
         */
        public static Mode from(Object o) {
            return valueOf(o.toString().toUpperCase());
        }
    }

    /**
     * Enumeration with possible values for the "Type" property
     */
    public enum Type {
        /**
         * Value representing the string {@code "pbr"}
         */
        PBR;

        /**
         * Converts an Object (usually a String) to the correct value
         *
         * @param o The Object to convert
         * @return The corresponding enum value
         */
        public static Type from(Object o) {
            return valueOf(o.toString().toUpperCase());
        }
    }

    /**
     * Enumeration with possible values for the "Skinning Method" property
     */
    public enum SkinningMethod {
        /**
         * Value representing the string {@code "linear"}
         */
        LINEAR,

        /**
         * Value representing the string {@code "quaternion"}
         */
        QUATERNION;

        /**
         * Converts an Object (usually a String) to the correct value
         *
         * @param o The Object to convert
         * @return The corresponding enum value
         */
        public static SkinningMethod from(Object o) {
            return valueOf(o.toString().toUpperCase());
        }
    }

    /**
     * Implementation of the "Root" node
     */
    public static final class Root extends CastNode {
        Root(AtomicLong hasher) {
            super(CastNodeID.ROOT, hasher);
        }

        Root(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
            super(CastNodeID.ROOT, hash, properties, children);
            // TODO: Validation
        }

        /**
         * Returns the children of type {@link Model}.
         *
         * @return The list of Models
         */
        public List<Model> getModels() {
            return getChildrenOfType(Model.class);
        }

        /**
         * Create and add a new instance of type {@link Model}.
         *
         * @return The new instance
         */
        public Model createModel() {
            return createChild(new Model(hasher));
        }

        /**
         * Returns the children of type {@link Animation}.
         *
         * @return The list of Animations
         */
        public List<Animation> getAnimations() {
            return getChildrenOfType(Animation.class);
        }

        /**
         * Create and add a new instance of type {@link Animation}.
         *
         * @return The new instance
         */
        public Animation createAnimation() {
            return createChild(new Animation(hasher));
        }

        /**
         * Returns the children of type {@link Instance}.
         *
         * @return The list of Instances
         */
        public List<Instance> getInstances() {
            return getChildrenOfType(Instance.class);
        }

        /**
         * Create and add a new instance of type {@link Instance}.
         *
         * @return The new instance
         */
        public Instance createInstance() {
            return createChild(new Instance(hasher));
        }

        /**
         * Returns the children of type {@link Metadata}.
         *
         * @return The list of Metadatas
         */
        public List<Metadata> getMetadatas() {
            return getChildrenOfType(Metadata.class);
        }

        /**
         * Create and add a new instance of type {@link Metadata}.
         *
         * @return The new instance
         */
        public Metadata createMetadata() {
            return createChild(new Metadata(hasher));
        }
    }

    /**
     * Implementation of the "Model" node
     */
    public static final class Model extends CastNode {
        Model(AtomicLong hasher) {
            super(CastNodeID.MODEL, hasher);
        }

        Model(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
            super(CastNodeID.MODEL, hash, properties, children);
            // TODO: Validation
        }

        /**
         * Returns the child of type {@link Skeleton}.
         *
         * @return The single Skeletons
         */
        public Optional<Skeleton> getSkeleton() {
            return getChildOfType(Skeleton.class);
        }

        /**
         * Create and add a new instance of type {@link Skeleton}.
         *
         * @return The new instance
         */
        public Skeleton createSkeleton() {
            return createChild(new Skeleton(hasher));
        }

        /**
         * Returns the children of type {@link Mesh}.
         *
         * @return The list of Meshes
         */
        public List<Mesh> getMeshes() {
            return getChildrenOfType(Mesh.class);
        }

        /**
         * Create and add a new instance of type {@link Mesh}.
         *
         * @return The new instance
         */
        public Mesh createMesh() {
            return createChild(new Mesh(hasher));
        }

        /**
         * Returns the children of type {@link Hair}.
         *
         * @return The list of Hairs
         */
        public List<Hair> getHairs() {
            return getChildrenOfType(Hair.class);
        }

        /**
         * Create and add a new instance of type {@link Hair}.
         *
         * @return The new instance
         */
        public Hair createHair() {
            return createChild(new Hair(hasher));
        }

        /**
         * Returns the children of type {@link BlendShape}.
         *
         * @return The list of BlendShapes
         */
        public List<BlendShape> getBlendShapes() {
            return getChildrenOfType(BlendShape.class);
        }

        /**
         * Create and add a new instance of type {@link BlendShape}.
         *
         * @return The new instance
         */
        public BlendShape createBlendShape() {
            return createChild(new BlendShape(hasher));
        }

        /**
         * Returns the children of type {@link Material}.
         *
         * @return The list of Materials
         */
        public List<Material> getMaterials() {
            return getChildrenOfType(Material.class);
        }

        /**
         * Create and add a new instance of type {@link Material}.
         *
         * @return The new instance
         */
        public Material createMaterial() {
            return createChild(new Material(hasher));
        }

        /**
         * Returns the value of the {@code "n"} property (Name).
         *
         * @return The value of the {@code "n"} property
         */
        public Optional<String> getName() {
            return getProperty("n", String.class::cast);
        }

        /**
         * Sets the value of the {@code "n"} property (Name).
         *
         * @param name The new value.
         * @return The {@code this} instance for chaining
         */
        public Model setName(String name) {
            createProperty(CastPropertyID.STRING, "n", name);
            return this;
        }

        /**
         * Returns the value of the {@code "p"} property (Position).
         *
         * @return The value of the {@code "p"} property
         */
        public Optional<Vec3> getPosition() {
            return getProperty("p", Vec3.class::cast);
        }

        /**
         * Sets the value of the {@code "p"} property (Position).
         *
         * @param position The new value.
         * @return The {@code this} instance for chaining
         */
        public Model setPosition(Vec3 position) {
            createProperty(CastPropertyID.VECTOR_3, "p", position);
            return this;
        }

        /**
         * Returns the value of the {@code "r"} property (Rotation).
         *
         * @return The value of the {@code "r"} property
         */
        public Optional<Vec4> getRotation() {
            return getProperty("r", Vec4.class::cast);
        }

        /**
         * Sets the value of the {@code "r"} property (Rotation).
         *
         * @param rotation The new value.
         * @return The {@code this} instance for chaining
         */
        public Model setRotation(Vec4 rotation) {
            createProperty(CastPropertyID.VECTOR_4, "r", rotation);
            return this;
        }

        /**
         * Returns the value of the {@code "s"} property (Scale).
         *
         * @return The value of the {@code "s"} property
         */
        public Optional<Vec3> getScale() {
            return getProperty("s", Vec3.class::cast);
        }

        /**
         * Sets the value of the {@code "s"} property (Scale).
         *
         * @param scale The new value.
         * @return The {@code this} instance for chaining
         */
        public Model setScale(Vec3 scale) {
            createProperty(CastPropertyID.VECTOR_3, "s", scale);
            return this;
        }
    }

    /**
     * Implementation of the "Mesh" node
     */
    public static final class Mesh extends CastNode {
        private int vertexColorBufferIndex;

        private int vertexUVBufferIndex;

        Mesh(AtomicLong hasher) {
            super(CastNodeID.MESH, hasher);
        }

        Mesh(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
            super(CastNodeID.MESH, hash, properties, children);
            // TODO: Validation
        }

        /**
         * Returns the value of the {@code "n"} property (Name).
         *
         * @return The value of the {@code "n"} property
         */
        public Optional<String> getName() {
            return getProperty("n", String.class::cast);
        }

        /**
         * Sets the value of the {@code "n"} property (Name).
         *
         * @param name The new value.
         * @return The {@code this} instance for chaining
         */
        public Mesh setName(String name) {
            createProperty(CastPropertyID.STRING, "n", name);
            return this;
        }

        /**
         * Returns the value of the {@code "vp"} property (Vertex Position Buffer).
         *
         * @return The value of the {@code "vp"} property
         */
        public FloatBuffer getVertexPositionBuffer() {
            return getProperty("vp", FloatBuffer.class::cast).orElseThrow();
        }

        /**
         * Sets the value of the {@code "vp"} property (Vertex Position Buffer).
         *
         * @param vertexPositionBuffer The new value.
         * @return The {@code this} instance for chaining
         */
        public Mesh setVertexPositionBuffer(FloatBuffer vertexPositionBuffer) {
            createProperty(CastPropertyID.VECTOR_3, "vp", vertexPositionBuffer);
            return this;
        }

        /**
         * Returns the value of the {@code "vn"} property (Vertex Normal Buffer).
         *
         * @return The value of the {@code "vn"} property
         */
        public Optional<FloatBuffer> getVertexNormalBuffer() {
            return getProperty("vn", FloatBuffer.class::cast);
        }

        /**
         * Sets the value of the {@code "vn"} property (Vertex Normal Buffer).
         *
         * @param vertexNormalBuffer The new value.
         * @return The {@code this} instance for chaining
         */
        public Mesh setVertexNormalBuffer(FloatBuffer vertexNormalBuffer) {
            createProperty(CastPropertyID.VECTOR_3, "vn", vertexNormalBuffer);
            return this;
        }

        /**
         * Returns the value of the {@code "vt"} property (Vertex Tangent Buffer).
         *
         * @return The value of the {@code "vt"} property
         */
        public Optional<FloatBuffer> getVertexTangentBuffer() {
            return getProperty("vt", FloatBuffer.class::cast);
        }

        /**
         * Sets the value of the {@code "vt"} property (Vertex Tangent Buffer).
         *
         * @param vertexTangentBuffer The new value.
         * @return The {@code this} instance for chaining
         */
        public Mesh setVertexTangentBuffer(FloatBuffer vertexTangentBuffer) {
            createProperty(CastPropertyID.VECTOR_3, "vt", vertexTangentBuffer);
            return this;
        }

        /**
         * Returns the value of the {@code "c%d"} property (Vertex Color Buffer).
         *
         * @param index The index of the value to get
         * @return The value of the {@code "c%d"} property
         */
        public Optional<Buffer> getVertexColorBuffer(int index) {
            return getProperty("String.format(c%d, index)", Buffer.class::cast);
        }

        /**
         * Sets the value of the {@code "c%d"} property (Vertex Color Buffer).
         *
         * @param vertexColorBuffer The new value.
         * @return The {@code this} instance for chaining
         */
        public Mesh addVertexColorBufferI32(IntBuffer vertexColorBuffer) {
            createProperty(CastPropertyID.INTEGER_32, "c" + vertexColorBufferIndex++, vertexColorBuffer);
            return this;
        }

        /**
         * Sets the value of the {@code "c%d"} property (Vertex Color Buffer).
         *
         * @param vertexColorBuffer The new value.
         * @return The {@code this} instance for chaining
         */
        public Mesh addVertexColorBufferV4(FloatBuffer vertexColorBuffer) {
            createProperty(CastPropertyID.VECTOR_4, "c" + vertexColorBufferIndex++, vertexColorBuffer);
            return this;
        }

        /**
         * Returns the value of the {@code "u%d"} property (Vertex UV Buffer).
         *
         * @param index The index of the value to get
         * @return The value of the {@code "u%d"} property
         */
        public Optional<FloatBuffer> getVertexUVBuffer(int index) {
            return getProperty("String.format(u%d, index)", FloatBuffer.class::cast);
        }

        /**
         * Sets the value of the {@code "u%d"} property (Vertex UV Buffer).
         *
         * @param vertexUVBuffer The new value.
         * @return The {@code this} instance for chaining
         */
        public Mesh addVertexUVBuffer(FloatBuffer vertexUVBuffer) {
            createProperty(CastPropertyID.VECTOR_2, "u" + vertexUVBufferIndex++, vertexUVBuffer);
            return this;
        }

        /**
         * Returns the value of the {@code "wb"} property (Vertex Weight Bone Buffer).
         *
         * @return The value of the {@code "wb"} property
         */
        public Optional<Buffer> getVertexWeightBoneBuffer() {
            return getProperty("wb", Buffer.class::cast);
        }

        /**
         * Sets the value of the {@code "wb"} property (Vertex Weight Bone Buffer).
         *
         * @param vertexWeightBoneBuffer The new value.
         * @return The {@code this} instance for chaining
         */
        public Mesh setVertexWeightBoneBuffer(Buffer vertexWeightBoneBuffer) {
            createIntBufferProperty("wb", vertexWeightBoneBuffer);
            return this;
        }

        /**
         * Returns the value of the {@code "wv"} property (Vertex Weight Value Buffer).
         *
         * @return The value of the {@code "wv"} property
         */
        public Optional<FloatBuffer> getVertexWeightValueBuffer() {
            return getProperty("wv", FloatBuffer.class::cast);
        }

        /**
         * Sets the value of the {@code "wv"} property (Vertex Weight Value Buffer).
         *
         * @param vertexWeightValueBuffer The new value.
         * @return The {@code this} instance for chaining
         */
        public Mesh setVertexWeightValueBuffer(FloatBuffer vertexWeightValueBuffer) {
            createProperty(CastPropertyID.FLOAT, "wv", vertexWeightValueBuffer);
            return this;
        }

        /**
         * Returns the value of the {@code "f"} property (Face Buffer).
         *
         * @return The value of the {@code "f"} property
         */
        public Buffer getFaceBuffer() {
            return getProperty("f", Buffer.class::cast).orElseThrow();
        }

        /**
         * Sets the value of the {@code "f"} property (Face Buffer).
         *
         * @param faceBuffer The new value.
         * @return The {@code this} instance for chaining
         */
        public Mesh setFaceBuffer(Buffer faceBuffer) {
            createIntBufferProperty("f", faceBuffer);
            return this;
        }

        /**
         * Returns the value of the {@code "cl"} property (Color Layer Count).
         *
         * @return The value of the {@code "cl"} property
         */
        public Optional<Integer> getColorLayerCount() {
            return getIntProperty("cl");
        }

        /**
         * Sets the value of the {@code "cl"} property (Color Layer Count).
         *
         * @param colorLayerCount The new value.
         * @return The {@code this} instance for chaining
         */
        public Mesh setColorLayerCount(int colorLayerCount) {
            createIntProperty("cl", colorLayerCount);
            return this;
        }

        /**
         * Returns the value of the {@code "ul"} property (UV Layer Count).
         *
         * @return The value of the {@code "ul"} property
         */
        public Optional<Integer> getUVLayerCount() {
            return getIntProperty("ul");
        }

        /**
         * Sets the value of the {@code "ul"} property (UV Layer Count).
         *
         * @param uVLayerCount The new value.
         * @return The {@code this} instance for chaining
         */
        public Mesh setUVLayerCount(int uVLayerCount) {
            createIntProperty("ul", uVLayerCount);
            return this;
        }

        /**
         * Returns the value of the {@code "mi"} property (Maximum Weight Influence).
         *
         * @return The value of the {@code "mi"} property
         */
        public Optional<Integer> getMaximumWeightInfluence() {
            return getIntProperty("mi");
        }

        /**
         * Sets the value of the {@code "mi"} property (Maximum Weight Influence).
         *
         * @param maximumWeightInfluence The new value.
         * @return The {@code this} instance for chaining
         */
        public Mesh setMaximumWeightInfluence(int maximumWeightInfluence) {
            createIntProperty("mi", maximumWeightInfluence);
            return this;
        }

        /**
         * Returns the value of the {@code "sm"} property (Skinning Method).
         *
         * @return The value of the {@code "sm"} property
         */
        public Optional<SkinningMethod> getSkinningMethod() {
            return getProperty("sm", SkinningMethod::from);
        }

        /**
         * Sets the value of the {@code "sm"} property (Skinning Method).
         *
         * @param skinningMethod The new value.
         * @return The {@code this} instance for chaining
         */
        public Mesh setSkinningMethod(SkinningMethod skinningMethod) {
            createProperty(CastPropertyID.STRING, "sm", skinningMethod.toString().toLowerCase());
            return this;
        }

        /**
         * Returns the value of the {@code "m"} property (Material).
         *
         * @return The value of the {@code "m"} property
         */
        public Optional<Long> getMaterial() {
            return getProperty("m", Long.class::cast);
        }

        /**
         * Sets the value of the {@code "m"} property (Material).
         *
         * @param material The new value.
         * @return The {@code this} instance for chaining
         */
        public Mesh setMaterial(long material) {
            createProperty(CastPropertyID.INTEGER_64, "m", material);
            return this;
        }
    }

    /**
     * Implementation of the "Hair" node
     */
    public static final class Hair extends CastNode {
        Hair(AtomicLong hasher) {
            super(CastNodeID.HAIR, hasher);
        }

        Hair(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
            super(CastNodeID.HAIR, hash, properties, children);
            // TODO: Validation
        }

        /**
         * Returns the value of the {@code "n"} property (Name).
         *
         * @return The value of the {@code "n"} property
         */
        public Optional<String> getName() {
            return getProperty("n", String.class::cast);
        }

        /**
         * Sets the value of the {@code "n"} property (Name).
         *
         * @param name The new value.
         * @return The {@code this} instance for chaining
         */
        public Hair setName(String name) {
            createProperty(CastPropertyID.STRING, "n", name);
            return this;
        }

        /**
         * Returns the value of the {@code "se"} property (Segments Buffer).
         *
         * @return The value of the {@code "se"} property
         */
        public Buffer getSegmentsBuffer() {
            return getProperty("se", Buffer.class::cast).orElseThrow();
        }

        /**
         * Sets the value of the {@code "se"} property (Segments Buffer).
         *
         * @param segmentsBuffer The new value.
         * @return The {@code this} instance for chaining
         */
        public Hair setSegmentsBuffer(Buffer segmentsBuffer) {
            createIntBufferProperty("se", segmentsBuffer);
            return this;
        }

        /**
         * Returns the value of the {@code "pt"} property (Particle Buffer).
         *
         * @return The value of the {@code "pt"} property
         */
        public FloatBuffer getParticleBuffer() {
            return getProperty("pt", FloatBuffer.class::cast).orElseThrow();
        }

        /**
         * Sets the value of the {@code "pt"} property (Particle Buffer).
         *
         * @param particleBuffer The new value.
         * @return The {@code this} instance for chaining
         */
        public Hair setParticleBuffer(FloatBuffer particleBuffer) {
            createProperty(CastPropertyID.VECTOR_3, "pt", particleBuffer);
            return this;
        }

        /**
         * Returns the value of the {@code "m"} property (Material).
         *
         * @return The value of the {@code "m"} property
         */
        public Optional<Long> getMaterial() {
            return getProperty("m", Long.class::cast);
        }

        /**
         * Sets the value of the {@code "m"} property (Material).
         *
         * @param material The new value.
         * @return The {@code this} instance for chaining
         */
        public Hair setMaterial(long material) {
            createProperty(CastPropertyID.INTEGER_64, "m", material);
            return this;
        }
    }

    /**
     * Implementation of the "BlendShape" node
     */
    public static final class BlendShape extends CastNode {
        BlendShape(AtomicLong hasher) {
            super(CastNodeID.BLEND_SHAPE, hasher);
        }

        BlendShape(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
            super(CastNodeID.BLEND_SHAPE, hash, properties, children);
            // TODO: Validation
        }

        /**
         * Returns the value of the {@code "n"} property (Name).
         *
         * @return The value of the {@code "n"} property
         */
        public String getName() {
            return getProperty("n", String.class::cast).orElseThrow();
        }

        /**
         * Sets the value of the {@code "n"} property (Name).
         *
         * @param name The new value.
         * @return The {@code this} instance for chaining
         */
        public BlendShape setName(String name) {
            createProperty(CastPropertyID.STRING, "n", name);
            return this;
        }

        /**
         * Returns the value of the {@code "b"} property (Base Shape).
         *
         * @return The value of the {@code "b"} property
         */
        public long getBaseShape() {
            return getProperty("b", Long.class::cast).orElseThrow();
        }

        /**
         * Sets the value of the {@code "b"} property (Base Shape).
         *
         * @param baseShape The new value.
         * @return The {@code this} instance for chaining
         */
        public BlendShape setBaseShape(long baseShape) {
            createProperty(CastPropertyID.INTEGER_64, "b", baseShape);
            return this;
        }

        /**
         * Returns the value of the {@code "vi"} property (Target Shape Vertex Indices).
         *
         * @return The value of the {@code "vi"} property
         */
        public Buffer getTargetShapeVertexIndices() {
            return getProperty("vi", Buffer.class::cast).orElseThrow();
        }

        /**
         * Sets the value of the {@code "vi"} property (Target Shape Vertex Indices).
         *
         * @param targetShapeVertexIndices The new value.
         * @return The {@code this} instance for chaining
         */
        public BlendShape setTargetShapeVertexIndices(Buffer targetShapeVertexIndices) {
            createIntBufferProperty("vi", targetShapeVertexIndices);
            return this;
        }

        /**
         * Returns the value of the {@code "vp"} property (Target Shape Vertex Positions).
         *
         * @return The value of the {@code "vp"} property
         */
        public FloatBuffer getTargetShapeVertexPositions() {
            return getProperty("vp", FloatBuffer.class::cast).orElseThrow();
        }

        /**
         * Sets the value of the {@code "vp"} property (Target Shape Vertex Positions).
         *
         * @param targetShapeVertexPositions The new value.
         * @return The {@code this} instance for chaining
         */
        public BlendShape setTargetShapeVertexPositions(FloatBuffer targetShapeVertexPositions) {
            createProperty(CastPropertyID.VECTOR_3, "vp", targetShapeVertexPositions);
            return this;
        }

        /**
         * Returns the value of the {@code "ts"} property (Target Weight Scale).
         *
         * @return The value of the {@code "ts"} property
         */
        public Optional<FloatBuffer> getTargetWeightScale() {
            return getProperty("ts", FloatBuffer.class::cast);
        }

        /**
         * Sets the value of the {@code "ts"} property (Target Weight Scale).
         *
         * @param targetWeightScale The new value.
         * @return The {@code this} instance for chaining
         */
        public BlendShape setTargetWeightScale(FloatBuffer targetWeightScale) {
            createProperty(CastPropertyID.FLOAT, "ts", targetWeightScale);
            return this;
        }
    }

    /**
     * Implementation of the "Skeleton" node
     */
    public static final class Skeleton extends CastNode {
        Skeleton(AtomicLong hasher) {
            super(CastNodeID.SKELETON, hasher);
        }

        Skeleton(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
            super(CastNodeID.SKELETON, hash, properties, children);
            // TODO: Validation
        }

        /**
         * Returns the children of type {@link Bone}.
         *
         * @return The list of Bones
         */
        public List<Bone> getBones() {
            return getChildrenOfType(Bone.class);
        }

        /**
         * Create and add a new instance of type {@link Bone}.
         *
         * @return The new instance
         */
        public Bone createBone() {
            return createChild(new Bone(hasher));
        }

        /**
         * Returns the children of type {@link IkHandle}.
         *
         * @return The list of IkHandles
         */
        public List<IkHandle> getIkHandles() {
            return getChildrenOfType(IkHandle.class);
        }

        /**
         * Create and add a new instance of type {@link IkHandle}.
         *
         * @return The new instance
         */
        public IkHandle createIkHandle() {
            return createChild(new IkHandle(hasher));
        }

        /**
         * Returns the children of type {@link Constraint}.
         *
         * @return The list of Constraints
         */
        public List<Constraint> getConstraints() {
            return getChildrenOfType(Constraint.class);
        }

        /**
         * Create and add a new instance of type {@link Constraint}.
         *
         * @return The new instance
         */
        public Constraint createConstraint() {
            return createChild(new Constraint(hasher));
        }
    }

    /**
     * Implementation of the "Bone" node
     */
    public static final class Bone extends CastNode {
        Bone(AtomicLong hasher) {
            super(CastNodeID.BONE, hasher);
        }

        Bone(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
            super(CastNodeID.BONE, hash, properties, children);
            // TODO: Validation
        }

        /**
         * Returns the value of the {@code "n"} property (Name).
         *
         * @return The value of the {@code "n"} property
         */
        public String getName() {
            return getProperty("n", String.class::cast).orElseThrow();
        }

        /**
         * Sets the value of the {@code "n"} property (Name).
         *
         * @param name The new value.
         * @return The {@code this} instance for chaining
         */
        public Bone setName(String name) {
            createProperty(CastPropertyID.STRING, "n", name);
            return this;
        }

        /**
         * Returns the value of the {@code "p"} property (Parent Index).
         *
         * @return The value of the {@code "p"} property
         */
        public Optional<Integer> getParentIndex() {
            return getProperty("p", Integer.class::cast);
        }

        /**
         * Sets the value of the {@code "p"} property (Parent Index).
         *
         * @param parentIndex The new value.
         * @return The {@code this} instance for chaining
         */
        public Bone setParentIndex(int parentIndex) {
            createProperty(CastPropertyID.INTEGER_32, "p", parentIndex);
            return this;
        }

        /**
         * Returns the value of the {@code "ssc"} property (Segment Scale Compensate).
         *
         * @return The value of the {@code "ssc"} property
         */
        public Optional<Boolean> getSegmentScaleCompensate() {
            return getProperty("ssc", this::parseBoolean);
        }

        /**
         * Sets the value of the {@code "ssc"} property (Segment Scale Compensate).
         *
         * @param segmentScaleCompensate The new value.
         * @return The {@code this} instance for chaining
         */
        public Bone setSegmentScaleCompensate(boolean segmentScaleCompensate) {
            createProperty(CastPropertyID.BYTE, "ssc", segmentScaleCompensate ? 1 : 0);
            return this;
        }

        /**
         * Returns the value of the {@code "lp"} property (Local Position).
         *
         * @return The value of the {@code "lp"} property
         */
        public Optional<Vec3> getLocalPosition() {
            return getProperty("lp", Vec3.class::cast);
        }

        /**
         * Sets the value of the {@code "lp"} property (Local Position).
         *
         * @param localPosition The new value.
         * @return The {@code this} instance for chaining
         */
        public Bone setLocalPosition(Vec3 localPosition) {
            createProperty(CastPropertyID.VECTOR_3, "lp", localPosition);
            return this;
        }

        /**
         * Returns the value of the {@code "lr"} property (Local Rotation).
         *
         * @return The value of the {@code "lr"} property
         */
        public Optional<Vec4> getLocalRotation() {
            return getProperty("lr", Vec4.class::cast);
        }

        /**
         * Sets the value of the {@code "lr"} property (Local Rotation).
         *
         * @param localRotation The new value.
         * @return The {@code this} instance for chaining
         */
        public Bone setLocalRotation(Vec4 localRotation) {
            createProperty(CastPropertyID.VECTOR_4, "lr", localRotation);
            return this;
        }

        /**
         * Returns the value of the {@code "wp"} property (World Position).
         *
         * @return The value of the {@code "wp"} property
         */
        public Optional<Vec3> getWorldPosition() {
            return getProperty("wp", Vec3.class::cast);
        }

        /**
         * Sets the value of the {@code "wp"} property (World Position).
         *
         * @param worldPosition The new value.
         * @return The {@code this} instance for chaining
         */
        public Bone setWorldPosition(Vec3 worldPosition) {
            createProperty(CastPropertyID.VECTOR_3, "wp", worldPosition);
            return this;
        }

        /**
         * Returns the value of the {@code "wr"} property (World Rotation).
         *
         * @return The value of the {@code "wr"} property
         */
        public Optional<Vec4> getWorldRotation() {
            return getProperty("wr", Vec4.class::cast);
        }

        /**
         * Sets the value of the {@code "wr"} property (World Rotation).
         *
         * @param worldRotation The new value.
         * @return The {@code this} instance for chaining
         */
        public Bone setWorldRotation(Vec4 worldRotation) {
            createProperty(CastPropertyID.VECTOR_4, "wr", worldRotation);
            return this;
        }

        /**
         * Returns the value of the {@code "s"} property (Scale).
         *
         * @return The value of the {@code "s"} property
         */
        public Optional<Vec3> getScale() {
            return getProperty("s", Vec3.class::cast);
        }

        /**
         * Sets the value of the {@code "s"} property (Scale).
         *
         * @param scale The new value.
         * @return The {@code this} instance for chaining
         */
        public Bone setScale(Vec3 scale) {
            createProperty(CastPropertyID.VECTOR_3, "s", scale);
            return this;
        }
    }

    /**
     * Implementation of the "IkHandle" node
     */
    public static final class IkHandle extends CastNode {
        IkHandle(AtomicLong hasher) {
            super(CastNodeID.IK_HANDLE, hasher);
        }

        IkHandle(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
            super(CastNodeID.IK_HANDLE, hash, properties, children);
            // TODO: Validation
        }

        /**
         * Returns the value of the {@code "n"} property (Name).
         *
         * @return The value of the {@code "n"} property
         */
        public Optional<String> getName() {
            return getProperty("n", String.class::cast);
        }

        /**
         * Sets the value of the {@code "n"} property (Name).
         *
         * @param name The new value.
         * @return The {@code this} instance for chaining
         */
        public IkHandle setName(String name) {
            createProperty(CastPropertyID.STRING, "n", name);
            return this;
        }

        /**
         * Returns the value of the {@code "sb"} property (Start Bone Hash).
         *
         * @return The value of the {@code "sb"} property
         */
        public long getStartBoneHash() {
            return getProperty("sb", Long.class::cast).orElseThrow();
        }

        /**
         * Sets the value of the {@code "sb"} property (Start Bone Hash).
         *
         * @param startBoneHash The new value.
         * @return The {@code this} instance for chaining
         */
        public IkHandle setStartBoneHash(long startBoneHash) {
            createProperty(CastPropertyID.INTEGER_64, "sb", startBoneHash);
            return this;
        }

        /**
         * Returns the value of the {@code "eb"} property (End Bone Hash).
         *
         * @return The value of the {@code "eb"} property
         */
        public long getEndBoneHash() {
            return getProperty("eb", Long.class::cast).orElseThrow();
        }

        /**
         * Sets the value of the {@code "eb"} property (End Bone Hash).
         *
         * @param endBoneHash The new value.
         * @return The {@code this} instance for chaining
         */
        public IkHandle setEndBoneHash(long endBoneHash) {
            createProperty(CastPropertyID.INTEGER_64, "eb", endBoneHash);
            return this;
        }

        /**
         * Returns the value of the {@code "tb"} property (Target Bone Hash).
         *
         * @return The value of the {@code "tb"} property
         */
        public Optional<Long> getTargetBoneHash() {
            return getProperty("tb", Long.class::cast);
        }

        /**
         * Sets the value of the {@code "tb"} property (Target Bone Hash).
         *
         * @param targetBoneHash The new value.
         * @return The {@code this} instance for chaining
         */
        public IkHandle setTargetBoneHash(long targetBoneHash) {
            createProperty(CastPropertyID.INTEGER_64, "tb", targetBoneHash);
            return this;
        }

        /**
         * Returns the value of the {@code "pv"} property (Pole Vector Bone Hash).
         *
         * @return The value of the {@code "pv"} property
         */
        public Optional<Long> getPoleVectorBoneHash() {
            return getProperty("pv", Long.class::cast);
        }

        /**
         * Sets the value of the {@code "pv"} property (Pole Vector Bone Hash).
         *
         * @param poleVectorBoneHash The new value.
         * @return The {@code this} instance for chaining
         */
        public IkHandle setPoleVectorBoneHash(long poleVectorBoneHash) {
            createProperty(CastPropertyID.INTEGER_64, "pv", poleVectorBoneHash);
            return this;
        }

        /**
         * Returns the value of the {@code "pb"} property (Pole Bone Hash).
         *
         * @return The value of the {@code "pb"} property
         */
        public Optional<Long> getPoleBoneHash() {
            return getProperty("pb", Long.class::cast);
        }

        /**
         * Sets the value of the {@code "pb"} property (Pole Bone Hash).
         *
         * @param poleBoneHash The new value.
         * @return The {@code this} instance for chaining
         */
        public IkHandle setPoleBoneHash(long poleBoneHash) {
            createProperty(CastPropertyID.INTEGER_64, "pb", poleBoneHash);
            return this;
        }

        /**
         * Returns the value of the {@code "tr"} property (Use Target Rotation).
         *
         * @return The value of the {@code "tr"} property
         */
        public Optional<Boolean> getUseTargetRotation() {
            return getProperty("tr", this::parseBoolean);
        }

        /**
         * Sets the value of the {@code "tr"} property (Use Target Rotation).
         *
         * @param useTargetRotation The new value.
         * @return The {@code this} instance for chaining
         */
        public IkHandle setUseTargetRotation(boolean useTargetRotation) {
            createProperty(CastPropertyID.BYTE, "tr", useTargetRotation ? 1 : 0);
            return this;
        }
    }

    /**
     * Implementation of the "Constraint" node
     */
    public static final class Constraint extends CastNode {
        Constraint(AtomicLong hasher) {
            super(CastNodeID.CONSTRAINT, hasher);
        }

        Constraint(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
            super(CastNodeID.CONSTRAINT, hash, properties, children);
            // TODO: Validation
        }

        /**
         * Returns the value of the {@code "n"} property (Name).
         *
         * @return The value of the {@code "n"} property
         */
        public Optional<String> getName() {
            return getProperty("n", String.class::cast);
        }

        /**
         * Sets the value of the {@code "n"} property (Name).
         *
         * @param name The new value.
         * @return The {@code this} instance for chaining
         */
        public Constraint setName(String name) {
            createProperty(CastPropertyID.STRING, "n", name);
            return this;
        }

        /**
         * Returns the value of the {@code "ct"} property (Constraint Type).
         *
         * @return The value of the {@code "ct"} property
         */
        public ConstraintType getConstraintType() {
            return getProperty("ct", ConstraintType::from).orElseThrow();
        }

        /**
         * Sets the value of the {@code "ct"} property (Constraint Type).
         *
         * @param constraintType The new value.
         * @return The {@code this} instance for chaining
         */
        public Constraint setConstraintType(ConstraintType constraintType) {
            createProperty(CastPropertyID.STRING, "ct", constraintType.toString().toLowerCase());
            return this;
        }

        /**
         * Returns the value of the {@code "cb"} property (Constraint Bone Hash).
         *
         * @return The value of the {@code "cb"} property
         */
        public long getConstraintBoneHash() {
            return getProperty("cb", Long.class::cast).orElseThrow();
        }

        /**
         * Sets the value of the {@code "cb"} property (Constraint Bone Hash).
         *
         * @param constraintBoneHash The new value.
         * @return The {@code this} instance for chaining
         */
        public Constraint setConstraintBoneHash(long constraintBoneHash) {
            createProperty(CastPropertyID.INTEGER_64, "cb", constraintBoneHash);
            return this;
        }

        /**
         * Returns the value of the {@code "tb"} property (Target Bone Hash).
         *
         * @return The value of the {@code "tb"} property
         */
        public long getTargetBoneHash() {
            return getProperty("tb", Long.class::cast).orElseThrow();
        }

        /**
         * Sets the value of the {@code "tb"} property (Target Bone Hash).
         *
         * @param targetBoneHash The new value.
         * @return The {@code this} instance for chaining
         */
        public Constraint setTargetBoneHash(long targetBoneHash) {
            createProperty(CastPropertyID.INTEGER_64, "tb", targetBoneHash);
            return this;
        }

        /**
         * Returns the value of the {@code "mo"} property (Maintain Offset).
         *
         * @return The value of the {@code "mo"} property
         */
        public Optional<Boolean> getMaintainOffset() {
            return getProperty("mo", this::parseBoolean);
        }

        /**
         * Sets the value of the {@code "mo"} property (Maintain Offset).
         *
         * @param maintainOffset The new value.
         * @return The {@code this} instance for chaining
         */
        public Constraint setMaintainOffset(boolean maintainOffset) {
            createProperty(CastPropertyID.BYTE, "mo", maintainOffset ? 1 : 0);
            return this;
        }

        /**
         * Returns the value of the {@code "co"} property (Custom Offset).
         *
         * @return The value of the {@code "co"} property
         */
        public Optional<Object> getCustomOffset() {
            return getProperty("co", Object.class::cast);
        }

        /**
         * Sets the value of the {@code "co"} property (Custom Offset).
         *
         * @param customOffset The new value.
         * @return The {@code this} instance for chaining
         */
        public Constraint setCustomOffsetV3(Vec3 customOffset) {
            createProperty(CastPropertyID.VECTOR_3, "co", customOffset);
            return this;
        }

        /**
         * Sets the value of the {@code "co"} property (Custom Offset).
         *
         * @param customOffset The new value.
         * @return The {@code this} instance for chaining
         */
        public Constraint setCustomOffsetV4(Vec4 customOffset) {
            createProperty(CastPropertyID.VECTOR_4, "co", customOffset);
            return this;
        }

        /**
         * Returns the value of the {@code "wt"} property (Weight).
         *
         * @return The value of the {@code "wt"} property
         */
        public Optional<Float> getWeight() {
            return getProperty("wt", Float.class::cast);
        }

        /**
         * Sets the value of the {@code "wt"} property (Weight).
         *
         * @param weight The new value.
         * @return The {@code this} instance for chaining
         */
        public Constraint setWeight(float weight) {
            createProperty(CastPropertyID.FLOAT, "wt", weight);
            return this;
        }

        /**
         * Returns the value of the {@code "sx"} property (Skip X).
         *
         * @return The value of the {@code "sx"} property
         */
        public Optional<Boolean> getSkipX() {
            return getProperty("sx", this::parseBoolean);
        }

        /**
         * Sets the value of the {@code "sx"} property (Skip X).
         *
         * @param skipX The new value.
         * @return The {@code this} instance for chaining
         */
        public Constraint setSkipX(boolean skipX) {
            createProperty(CastPropertyID.BYTE, "sx", skipX ? 1 : 0);
            return this;
        }

        /**
         * Returns the value of the {@code "sy"} property (Skip Y).
         *
         * @return The value of the {@code "sy"} property
         */
        public Optional<Boolean> getSkipY() {
            return getProperty("sy", this::parseBoolean);
        }

        /**
         * Sets the value of the {@code "sy"} property (Skip Y).
         *
         * @param skipY The new value.
         * @return The {@code this} instance for chaining
         */
        public Constraint setSkipY(boolean skipY) {
            createProperty(CastPropertyID.BYTE, "sy", skipY ? 1 : 0);
            return this;
        }

        /**
         * Returns the value of the {@code "sz"} property (Skip Z).
         *
         * @return The value of the {@code "sz"} property
         */
        public Optional<Boolean> getSkipZ() {
            return getProperty("sz", this::parseBoolean);
        }

        /**
         * Sets the value of the {@code "sz"} property (Skip Z).
         *
         * @param skipZ The new value.
         * @return The {@code this} instance for chaining
         */
        public Constraint setSkipZ(boolean skipZ) {
            createProperty(CastPropertyID.BYTE, "sz", skipZ ? 1 : 0);
            return this;
        }
    }

    /**
     * Implementation of the "Animation" node
     */
    public static final class Animation extends CastNode {
        Animation(AtomicLong hasher) {
            super(CastNodeID.ANIMATION, hasher);
        }

        Animation(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
            super(CastNodeID.ANIMATION, hash, properties, children);
            // TODO: Validation
        }

        /**
         * Returns the child of type {@link Skeleton}.
         *
         * @return The single Skeletons
         */
        public Optional<Skeleton> getSkeleton() {
            return getChildOfType(Skeleton.class);
        }

        /**
         * Create and add a new instance of type {@link Skeleton}.
         *
         * @return The new instance
         */
        public Skeleton createSkeleton() {
            return createChild(new Skeleton(hasher));
        }

        /**
         * Returns the children of type {@link Curve}.
         *
         * @return The list of Curves
         */
        public List<Curve> getCurves() {
            return getChildrenOfType(Curve.class);
        }

        /**
         * Create and add a new instance of type {@link Curve}.
         *
         * @return The new instance
         */
        public Curve createCurve() {
            return createChild(new Curve(hasher));
        }

        /**
         * Returns the children of type {@link CurveModeOverride}.
         *
         * @return The list of CurveModeOverrides
         */
        public List<CurveModeOverride> getCurveModeOverrides() {
            return getChildrenOfType(CurveModeOverride.class);
        }

        /**
         * Create and add a new instance of type {@link CurveModeOverride}.
         *
         * @return The new instance
         */
        public CurveModeOverride createCurveModeOverride() {
            return createChild(new CurveModeOverride(hasher));
        }

        /**
         * Returns the children of type {@link NotificationTrack}.
         *
         * @return The list of NotificationTracks
         */
        public List<NotificationTrack> getNotificationTracks() {
            return getChildrenOfType(NotificationTrack.class);
        }

        /**
         * Create and add a new instance of type {@link NotificationTrack}.
         *
         * @return The new instance
         */
        public NotificationTrack createNotificationTrack() {
            return createChild(new NotificationTrack(hasher));
        }

        /**
         * Returns the value of the {@code "n"} property (Name).
         *
         * @return The value of the {@code "n"} property
         */
        public Optional<String> getName() {
            return getProperty("n", String.class::cast);
        }

        /**
         * Sets the value of the {@code "n"} property (Name).
         *
         * @param name The new value.
         * @return The {@code this} instance for chaining
         */
        public Animation setName(String name) {
            createProperty(CastPropertyID.STRING, "n", name);
            return this;
        }

        /**
         * Returns the value of the {@code "fr"} property (Framerate).
         *
         * @return The value of the {@code "fr"} property
         */
        public float getFramerate() {
            return getProperty("fr", Float.class::cast).orElseThrow();
        }

        /**
         * Sets the value of the {@code "fr"} property (Framerate).
         *
         * @param framerate The new value.
         * @return The {@code this} instance for chaining
         */
        public Animation setFramerate(float framerate) {
            createProperty(CastPropertyID.FLOAT, "fr", framerate);
            return this;
        }

        /**
         * Returns the value of the {@code "lo"} property (Looping).
         *
         * @return The value of the {@code "lo"} property
         */
        public Optional<Boolean> getLooping() {
            return getProperty("lo", this::parseBoolean);
        }

        /**
         * Sets the value of the {@code "lo"} property (Looping).
         *
         * @param looping The new value.
         * @return The {@code this} instance for chaining
         */
        public Animation setLooping(boolean looping) {
            createProperty(CastPropertyID.BYTE, "lo", looping ? 1 : 0);
            return this;
        }
    }

    /**
     * Implementation of the "Curve" node
     */
    public static final class Curve extends CastNode {
        Curve(AtomicLong hasher) {
            super(CastNodeID.CURVE, hasher);
        }

        Curve(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
            super(CastNodeID.CURVE, hash, properties, children);
            // TODO: Validation
        }

        /**
         * Returns the value of the {@code "nn"} property (Node Name).
         *
         * @return The value of the {@code "nn"} property
         */
        public String getNodeName() {
            return getProperty("nn", String.class::cast).orElseThrow();
        }

        /**
         * Sets the value of the {@code "nn"} property (Node Name).
         *
         * @param nodeName The new value.
         * @return The {@code this} instance for chaining
         */
        public Curve setNodeName(String nodeName) {
            createProperty(CastPropertyID.STRING, "nn", nodeName);
            return this;
        }

        /**
         * Returns the value of the {@code "kp"} property (Key Property Name).
         *
         * @return The value of the {@code "kp"} property
         */
        public KeyPropertyName getKeyPropertyName() {
            return getProperty("kp", KeyPropertyName::from).orElseThrow();
        }

        /**
         * Sets the value of the {@code "kp"} property (Key Property Name).
         *
         * @param keyPropertyName The new value.
         * @return The {@code this} instance for chaining
         */
        public Curve setKeyPropertyName(KeyPropertyName keyPropertyName) {
            createProperty(CastPropertyID.STRING, "kp", keyPropertyName.toString().toLowerCase());
            return this;
        }

        /**
         * Returns the value of the {@code "kb"} property (Key Frame Buffer).
         *
         * @return The value of the {@code "kb"} property
         */
        public Buffer getKeyFrameBuffer() {
            return getProperty("kb", Buffer.class::cast).orElseThrow();
        }

        /**
         * Sets the value of the {@code "kb"} property (Key Frame Buffer).
         *
         * @param keyFrameBuffer The new value.
         * @return The {@code this} instance for chaining
         */
        public Curve setKeyFrameBuffer(Buffer keyFrameBuffer) {
            createIntBufferProperty("kb", keyFrameBuffer);
            return this;
        }

        /**
         * Returns the value of the {@code "kv"} property (Key Value Buffer).
         *
         * @return The value of the {@code "kv"} property
         */
        public Buffer getKeyValueBuffer() {
            return getProperty("kv", Buffer.class::cast).orElseThrow();
        }

        /**
         * Sets the value of the {@code "kv"} property (Key Value Buffer).
         *
         * @param keyValueBuffer The new value.
         * @return The {@code this} instance for chaining
         */
        public Curve setKeyValueBufferInt(Buffer keyValueBuffer) {
            createIntBufferProperty("kv", keyValueBuffer);
            return this;
        }

        /**
         * Sets the value of the {@code "kv"} property (Key Value Buffer).
         *
         * @param keyValueBuffer The new value.
         * @return The {@code this} instance for chaining
         */
        public Curve setKeyValueBufferF32(FloatBuffer keyValueBuffer) {
            createProperty(CastPropertyID.FLOAT, "kv", keyValueBuffer);
            return this;
        }

        /**
         * Sets the value of the {@code "kv"} property (Key Value Buffer).
         *
         * @param keyValueBuffer The new value.
         * @return The {@code this} instance for chaining
         */
        public Curve setKeyValueBufferV4(FloatBuffer keyValueBuffer) {
            createProperty(CastPropertyID.VECTOR_4, "kv", keyValueBuffer);
            return this;
        }

        /**
         * Returns the value of the {@code "m"} property (Mode).
         *
         * @return The value of the {@code "m"} property
         */
        public Mode getMode() {
            return getProperty("m", Mode::from).orElseThrow();
        }

        /**
         * Sets the value of the {@code "m"} property (Mode).
         *
         * @param mode The new value.
         * @return The {@code this} instance for chaining
         */
        public Curve setMode(Mode mode) {
            createProperty(CastPropertyID.STRING, "m", mode.toString().toLowerCase());
            return this;
        }

        /**
         * Returns the value of the {@code "ab"} property (Additive Blend Weight).
         *
         * @return The value of the {@code "ab"} property
         */
        public Optional<Float> getAdditiveBlendWeight() {
            return getProperty("ab", Float.class::cast);
        }

        /**
         * Sets the value of the {@code "ab"} property (Additive Blend Weight).
         *
         * @param additiveBlendWeight The new value.
         * @return The {@code this} instance for chaining
         */
        public Curve setAdditiveBlendWeight(float additiveBlendWeight) {
            createProperty(CastPropertyID.FLOAT, "ab", additiveBlendWeight);
            return this;
        }
    }

    /**
     * Implementation of the "CurveModeOverride" node
     */
    public static final class CurveModeOverride extends CastNode {
        CurveModeOverride(AtomicLong hasher) {
            super(CastNodeID.CURVE_MODE_OVERRIDE, hasher);
        }

        CurveModeOverride(long hash, Map<String, CastProperty> properties,
                List<CastNode> children) {
            super(CastNodeID.CURVE_MODE_OVERRIDE, hash, properties, children);
            // TODO: Validation
        }

        /**
         * Returns the value of the {@code "nn"} property (Node Name).
         *
         * @return The value of the {@code "nn"} property
         */
        public String getNodeName() {
            return getProperty("nn", String.class::cast).orElseThrow();
        }

        /**
         * Sets the value of the {@code "nn"} property (Node Name).
         *
         * @param nodeName The new value.
         * @return The {@code this} instance for chaining
         */
        public CurveModeOverride setNodeName(String nodeName) {
            createProperty(CastPropertyID.STRING, "nn", nodeName);
            return this;
        }

        /**
         * Returns the value of the {@code "m"} property (Mode).
         *
         * @return The value of the {@code "m"} property
         */
        public Mode getMode() {
            return getProperty("m", Mode::from).orElseThrow();
        }

        /**
         * Sets the value of the {@code "m"} property (Mode).
         *
         * @param mode The new value.
         * @return The {@code this} instance for chaining
         */
        public CurveModeOverride setMode(Mode mode) {
            createProperty(CastPropertyID.STRING, "m", mode.toString().toLowerCase());
            return this;
        }

        /**
         * Returns the value of the {@code "ot"} property (Override Translation Curves).
         *
         * @return The value of the {@code "ot"} property
         */
        public Optional<Boolean> getOverrideTranslationCurves() {
            return getProperty("ot", this::parseBoolean);
        }

        /**
         * Sets the value of the {@code "ot"} property (Override Translation Curves).
         *
         * @param overrideTranslationCurves The new value.
         * @return The {@code this} instance for chaining
         */
        public CurveModeOverride setOverrideTranslationCurves(boolean overrideTranslationCurves) {
            createProperty(CastPropertyID.BYTE, "ot", overrideTranslationCurves ? 1 : 0);
            return this;
        }

        /**
         * Returns the value of the {@code "or"} property (Override Rotation Curves).
         *
         * @return The value of the {@code "or"} property
         */
        public Optional<Boolean> getOverrideRotationCurves() {
            return getProperty("or", this::parseBoolean);
        }

        /**
         * Sets the value of the {@code "or"} property (Override Rotation Curves).
         *
         * @param overrideRotationCurves The new value.
         * @return The {@code this} instance for chaining
         */
        public CurveModeOverride setOverrideRotationCurves(boolean overrideRotationCurves) {
            createProperty(CastPropertyID.BYTE, "or", overrideRotationCurves ? 1 : 0);
            return this;
        }

        /**
         * Returns the value of the {@code "os"} property (Override Scale Curves).
         *
         * @return The value of the {@code "os"} property
         */
        public Optional<Boolean> getOverrideScaleCurves() {
            return getProperty("os", this::parseBoolean);
        }

        /**
         * Sets the value of the {@code "os"} property (Override Scale Curves).
         *
         * @param overrideScaleCurves The new value.
         * @return The {@code this} instance for chaining
         */
        public CurveModeOverride setOverrideScaleCurves(boolean overrideScaleCurves) {
            createProperty(CastPropertyID.BYTE, "os", overrideScaleCurves ? 1 : 0);
            return this;
        }
    }

    /**
     * Implementation of the "NotificationTrack" node
     */
    public static final class NotificationTrack extends CastNode {
        NotificationTrack(AtomicLong hasher) {
            super(CastNodeID.NOTIFICATION_TRACK, hasher);
        }

        NotificationTrack(long hash, Map<String, CastProperty> properties,
                List<CastNode> children) {
            super(CastNodeID.NOTIFICATION_TRACK, hash, properties, children);
            // TODO: Validation
        }

        /**
         * Returns the value of the {@code "n"} property (Name).
         *
         * @return The value of the {@code "n"} property
         */
        public String getName() {
            return getProperty("n", String.class::cast).orElseThrow();
        }

        /**
         * Sets the value of the {@code "n"} property (Name).
         *
         * @param name The new value.
         * @return The {@code this} instance for chaining
         */
        public NotificationTrack setName(String name) {
            createProperty(CastPropertyID.STRING, "n", name);
            return this;
        }

        /**
         * Returns the value of the {@code "kb"} property (Key Frame Buffer).
         *
         * @return The value of the {@code "kb"} property
         */
        public Buffer getKeyFrameBuffer() {
            return getProperty("kb", Buffer.class::cast).orElseThrow();
        }

        /**
         * Sets the value of the {@code "kb"} property (Key Frame Buffer).
         *
         * @param keyFrameBuffer The new value.
         * @return The {@code this} instance for chaining
         */
        public NotificationTrack setKeyFrameBuffer(Buffer keyFrameBuffer) {
            createIntBufferProperty("kb", keyFrameBuffer);
            return this;
        }
    }

    /**
     * Implementation of the "Material" node
     */
    public static final class Material extends CastNode {
        private int extraIndex;

        Material(AtomicLong hasher) {
            super(CastNodeID.MATERIAL, hasher);
        }

        Material(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
            super(CastNodeID.MATERIAL, hash, properties, children);
            // TODO: Validation
        }

        /**
         * Returns the children of type {@link File}.
         *
         * @return The list of Files
         */
        public List<File> getFiles() {
            return getChildrenOfType(File.class);
        }

        /**
         * Create and add a new instance of type {@link File}.
         *
         * @return The new instance
         */
        public File createFile() {
            return createChild(new File(hasher));
        }

        /**
         * Returns the children of type {@link Color}.
         *
         * @return The list of Colors
         */
        public List<Color> getColors() {
            return getChildrenOfType(Color.class);
        }

        /**
         * Create and add a new instance of type {@link Color}.
         *
         * @return The new instance
         */
        public Color createColor() {
            return createChild(new Color(hasher));
        }

        /**
         * Returns the value of the {@code "n"} property (Name).
         *
         * @return The value of the {@code "n"} property
         */
        public String getName() {
            return getProperty("n", String.class::cast).orElseThrow();
        }

        /**
         * Sets the value of the {@code "n"} property (Name).
         *
         * @param name The new value.
         * @return The {@code this} instance for chaining
         */
        public Material setName(String name) {
            createProperty(CastPropertyID.STRING, "n", name);
            return this;
        }

        /**
         * Returns the value of the {@code "t"} property (Type).
         *
         * @return The value of the {@code "t"} property
         */
        public Type getType() {
            return getProperty("t", Type::from).orElseThrow();
        }

        /**
         * Sets the value of the {@code "t"} property (Type).
         *
         * @param type The new value.
         * @return The {@code this} instance for chaining
         */
        public Material setType(Type type) {
            createProperty(CastPropertyID.STRING, "t", type.toString().toLowerCase());
            return this;
        }

        /**
         * Returns the value of the {@code "albedo"} property (Albedo Hash).
         *
         * @return The value of the {@code "albedo"} property
         */
        public Optional<Long> getAlbedoHash() {
            return getProperty("albedo", Long.class::cast);
        }

        /**
         * Sets the value of the {@code "albedo"} property (Albedo Hash).
         *
         * @param albedoHash The new value.
         * @return The {@code this} instance for chaining
         */
        public Material setAlbedoHash(long albedoHash) {
            createProperty(CastPropertyID.INTEGER_64, "albedo", albedoHash);
            return this;
        }

        /**
         * Returns the value of the {@code "diffuse"} property (Diffuse Hash).
         *
         * @return The value of the {@code "diffuse"} property
         */
        public Optional<Long> getDiffuseHash() {
            return getProperty("diffuse", Long.class::cast);
        }

        /**
         * Sets the value of the {@code "diffuse"} property (Diffuse Hash).
         *
         * @param diffuseHash The new value.
         * @return The {@code this} instance for chaining
         */
        public Material setDiffuseHash(long diffuseHash) {
            createProperty(CastPropertyID.INTEGER_64, "diffuse", diffuseHash);
            return this;
        }

        /**
         * Returns the value of the {@code "normal"} property (Normal Hash).
         *
         * @return The value of the {@code "normal"} property
         */
        public Optional<Long> getNormalHash() {
            return getProperty("normal", Long.class::cast);
        }

        /**
         * Sets the value of the {@code "normal"} property (Normal Hash).
         *
         * @param normalHash The new value.
         * @return The {@code this} instance for chaining
         */
        public Material setNormalHash(long normalHash) {
            createProperty(CastPropertyID.INTEGER_64, "normal", normalHash);
            return this;
        }

        /**
         * Returns the value of the {@code "specular"} property (Specular Hash).
         *
         * @return The value of the {@code "specular"} property
         */
        public Optional<Long> getSpecularHash() {
            return getProperty("specular", Long.class::cast);
        }

        /**
         * Sets the value of the {@code "specular"} property (Specular Hash).
         *
         * @param specularHash The new value.
         * @return The {@code this} instance for chaining
         */
        public Material setSpecularHash(long specularHash) {
            createProperty(CastPropertyID.INTEGER_64, "specular", specularHash);
            return this;
        }

        /**
         * Returns the value of the {@code "gloss"} property (Gloss Hash).
         *
         * @return The value of the {@code "gloss"} property
         */
        public Optional<Long> getGlossHash() {
            return getProperty("gloss", Long.class::cast);
        }

        /**
         * Sets the value of the {@code "gloss"} property (Gloss Hash).
         *
         * @param glossHash The new value.
         * @return The {@code this} instance for chaining
         */
        public Material setGlossHash(long glossHash) {
            createProperty(CastPropertyID.INTEGER_64, "gloss", glossHash);
            return this;
        }

        /**
         * Returns the value of the {@code "roughness"} property (Roughness Hash).
         *
         * @return The value of the {@code "roughness"} property
         */
        public Optional<Long> getRoughnessHash() {
            return getProperty("roughness", Long.class::cast);
        }

        /**
         * Sets the value of the {@code "roughness"} property (Roughness Hash).
         *
         * @param roughnessHash The new value.
         * @return The {@code this} instance for chaining
         */
        public Material setRoughnessHash(long roughnessHash) {
            createProperty(CastPropertyID.INTEGER_64, "roughness", roughnessHash);
            return this;
        }

        /**
         * Returns the value of the {@code "emissive"} property (Emissive Hash).
         *
         * @return The value of the {@code "emissive"} property
         */
        public Optional<Long> getEmissiveHash() {
            return getProperty("emissive", Long.class::cast);
        }

        /**
         * Sets the value of the {@code "emissive"} property (Emissive Hash).
         *
         * @param emissiveHash The new value.
         * @return The {@code this} instance for chaining
         */
        public Material setEmissiveHash(long emissiveHash) {
            createProperty(CastPropertyID.INTEGER_64, "emissive", emissiveHash);
            return this;
        }

        /**
         * Returns the value of the {@code "emask"} property (Emissive Mask Hash).
         *
         * @return The value of the {@code "emask"} property
         */
        public Optional<Long> getEmissiveMaskHash() {
            return getProperty("emask", Long.class::cast);
        }

        /**
         * Sets the value of the {@code "emask"} property (Emissive Mask Hash).
         *
         * @param emissiveMaskHash The new value.
         * @return The {@code this} instance for chaining
         */
        public Material setEmissiveMaskHash(long emissiveMaskHash) {
            createProperty(CastPropertyID.INTEGER_64, "emask", emissiveMaskHash);
            return this;
        }

        /**
         * Returns the value of the {@code "ao"} property (Ambient Occlusion Hash).
         *
         * @return The value of the {@code "ao"} property
         */
        public Optional<Long> getAmbientOcclusionHash() {
            return getProperty("ao", Long.class::cast);
        }

        /**
         * Sets the value of the {@code "ao"} property (Ambient Occlusion Hash).
         *
         * @param ambientOcclusionHash The new value.
         * @return The {@code this} instance for chaining
         */
        public Material setAmbientOcclusionHash(long ambientOcclusionHash) {
            createProperty(CastPropertyID.INTEGER_64, "ao", ambientOcclusionHash);
            return this;
        }

        /**
         * Returns the value of the {@code "cavity"} property (Cavity Hash).
         *
         * @return The value of the {@code "cavity"} property
         */
        public Optional<Long> getCavityHash() {
            return getProperty("cavity", Long.class::cast);
        }

        /**
         * Sets the value of the {@code "cavity"} property (Cavity Hash).
         *
         * @param cavityHash The new value.
         * @return The {@code this} instance for chaining
         */
        public Material setCavityHash(long cavityHash) {
            createProperty(CastPropertyID.INTEGER_64, "cavity", cavityHash);
            return this;
        }

        /**
         * Returns the value of the {@code "aniso"} property (Anisotropy Hash).
         *
         * @return The value of the {@code "aniso"} property
         */
        public Optional<Long> getAnisotropyHash() {
            return getProperty("aniso", Long.class::cast);
        }

        /**
         * Sets the value of the {@code "aniso"} property (Anisotropy Hash).
         *
         * @param anisotropyHash The new value.
         * @return The {@code this} instance for chaining
         */
        public Material setAnisotropyHash(long anisotropyHash) {
            createProperty(CastPropertyID.INTEGER_64, "aniso", anisotropyHash);
            return this;
        }

        /**
         * Returns the value of the {@code "extra%d"} property (Extra).
         *
         * @param index The index of the value to get
         * @return The value of the {@code "extra%d"} property
         */
        public Optional<Long> getExtra(int index) {
            return getProperty("String.format(extra%d, index)", Long.class::cast);
        }

        /**
         * Sets the value of the {@code "extra%d"} property (Extra).
         *
         * @param extra The new value.
         * @return The {@code this} instance for chaining
         */
        public Material addExtra(long extra) {
            createProperty(CastPropertyID.INTEGER_64, "extra" + extraIndex++, extra);
            return this;
        }
    }

    /**
     * Implementation of the "File" node
     */
    public static final class File extends CastNode {
        File(AtomicLong hasher) {
            super(CastNodeID.FILE, hasher);
        }

        File(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
            super(CastNodeID.FILE, hash, properties, children);
            // TODO: Validation
        }

        /**
         * Returns the value of the {@code "p"} property (Path).
         *
         * @return The value of the {@code "p"} property
         */
        public String getPath() {
            return getProperty("p", String.class::cast).orElseThrow();
        }

        /**
         * Sets the value of the {@code "p"} property (Path).
         *
         * @param path The new value.
         * @return The {@code this} instance for chaining
         */
        public File setPath(String path) {
            createProperty(CastPropertyID.STRING, "p", path);
            return this;
        }
    }

    /**
     * Implementation of the "Color" node
     */
    public static final class Color extends CastNode {
        Color(AtomicLong hasher) {
            super(CastNodeID.COLOR, hasher);
        }

        Color(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
            super(CastNodeID.COLOR, hash, properties, children);
            // TODO: Validation
        }

        /**
         * Returns the value of the {@code "n"} property (Name).
         *
         * @return The value of the {@code "n"} property
         */
        public Optional<String> getName() {
            return getProperty("n", String.class::cast);
        }

        /**
         * Sets the value of the {@code "n"} property (Name).
         *
         * @param name The new value.
         * @return The {@code this} instance for chaining
         */
        public Color setName(String name) {
            createProperty(CastPropertyID.STRING, "n", name);
            return this;
        }

        /**
         * Returns the value of the {@code "cs"} property (Color Space).
         *
         * @return The value of the {@code "cs"} property
         */
        public Optional<ColorSpace> getColorSpace() {
            return getProperty("cs", ColorSpace::from);
        }

        /**
         * Sets the value of the {@code "cs"} property (Color Space).
         *
         * @param colorSpace The new value.
         * @return The {@code this} instance for chaining
         */
        public Color setColorSpace(ColorSpace colorSpace) {
            createProperty(CastPropertyID.STRING, "cs", colorSpace.toString().toLowerCase());
            return this;
        }

        /**
         * Returns the value of the {@code "rgba"} property (Rgba Color).
         *
         * @return The value of the {@code "rgba"} property
         */
        public Vec4 getRgbaColor() {
            return getProperty("rgba", Vec4.class::cast).orElseThrow();
        }

        /**
         * Sets the value of the {@code "rgba"} property (Rgba Color).
         *
         * @param rgbaColor The new value.
         * @return The {@code this} instance for chaining
         */
        public Color setRgbaColor(Vec4 rgbaColor) {
            createProperty(CastPropertyID.VECTOR_4, "rgba", rgbaColor);
            return this;
        }
    }

    /**
     * Implementation of the "Instance" node
     */
    public static final class Instance extends CastNode {
        Instance(AtomicLong hasher) {
            super(CastNodeID.INSTANCE, hasher);
        }

        Instance(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
            super(CastNodeID.INSTANCE, hash, properties, children);
            // TODO: Validation
        }

        /**
         * Returns the children of type {@link File}.
         *
         * @return The list of Files
         */
        public List<File> getFiles() {
            return getChildrenOfType(File.class);
        }

        /**
         * Create and add a new instance of type {@link File}.
         *
         * @return The new instance
         */
        public File createFile() {
            return createChild(new File(hasher));
        }

        /**
         * Returns the value of the {@code "n"} property (Name).
         *
         * @return The value of the {@code "n"} property
         */
        public Optional<String> getName() {
            return getProperty("n", String.class::cast);
        }

        /**
         * Sets the value of the {@code "n"} property (Name).
         *
         * @param name The new value.
         * @return The {@code this} instance for chaining
         */
        public Instance setName(String name) {
            createProperty(CastPropertyID.STRING, "n", name);
            return this;
        }

        /**
         * Returns the value of the {@code "rf"} property (Reference File).
         *
         * @return The value of the {@code "rf"} property
         */
        public long getReferenceFile() {
            return getProperty("rf", Long.class::cast).orElseThrow();
        }

        /**
         * Sets the value of the {@code "rf"} property (Reference File).
         *
         * @param referenceFile The new value.
         * @return The {@code this} instance for chaining
         */
        public Instance setReferenceFile(long referenceFile) {
            createProperty(CastPropertyID.INTEGER_64, "rf", referenceFile);
            return this;
        }

        /**
         * Returns the value of the {@code "p"} property (Position).
         *
         * @return The value of the {@code "p"} property
         */
        public Vec3 getPosition() {
            return getProperty("p", Vec3.class::cast).orElseThrow();
        }

        /**
         * Sets the value of the {@code "p"} property (Position).
         *
         * @param position The new value.
         * @return The {@code this} instance for chaining
         */
        public Instance setPosition(Vec3 position) {
            createProperty(CastPropertyID.VECTOR_3, "p", position);
            return this;
        }

        /**
         * Returns the value of the {@code "r"} property (Rotation).
         *
         * @return The value of the {@code "r"} property
         */
        public Vec4 getRotation() {
            return getProperty("r", Vec4.class::cast).orElseThrow();
        }

        /**
         * Sets the value of the {@code "r"} property (Rotation).
         *
         * @param rotation The new value.
         * @return The {@code this} instance for chaining
         */
        public Instance setRotation(Vec4 rotation) {
            createProperty(CastPropertyID.VECTOR_4, "r", rotation);
            return this;
        }

        /**
         * Returns the value of the {@code "s"} property (Scale).
         *
         * @return The value of the {@code "s"} property
         */
        public Vec3 getScale() {
            return getProperty("s", Vec3.class::cast).orElseThrow();
        }

        /**
         * Sets the value of the {@code "s"} property (Scale).
         *
         * @param scale The new value.
         * @return The {@code this} instance for chaining
         */
        public Instance setScale(Vec3 scale) {
            createProperty(CastPropertyID.VECTOR_3, "s", scale);
            return this;
        }
    }

    /**
     * Implementation of the "Metadata" node
     */
    public static final class Metadata extends CastNode {
        Metadata(AtomicLong hasher) {
            super(CastNodeID.METADATA, hasher);
        }

        Metadata(long hash, Map<String, CastProperty> properties, List<CastNode> children) {
            super(CastNodeID.METADATA, hash, properties, children);
            // TODO: Validation
        }

        /**
         * Returns the value of the {@code "a"} property (Author).
         *
         * @return The value of the {@code "a"} property
         */
        public Optional<String> getAuthor() {
            return getProperty("a", String.class::cast);
        }

        /**
         * Sets the value of the {@code "a"} property (Author).
         *
         * @param author The new value.
         * @return The {@code this} instance for chaining
         */
        public Metadata setAuthor(String author) {
            createProperty(CastPropertyID.STRING, "a", author);
            return this;
        }

        /**
         * Returns the value of the {@code "s"} property (Software).
         *
         * @return The value of the {@code "s"} property
         */
        public Optional<String> getSoftware() {
            return getProperty("s", String.class::cast);
        }

        /**
         * Sets the value of the {@code "s"} property (Software).
         *
         * @param software The new value.
         * @return The {@code this} instance for chaining
         */
        public Metadata setSoftware(String software) {
            createProperty(CastPropertyID.STRING, "s", software);
            return this;
        }

        /**
         * Returns the value of the {@code "up"} property (Up Axis).
         *
         * @return The value of the {@code "up"} property
         */
        public Optional<UpAxis> getUpAxis() {
            return getProperty("up", UpAxis::from);
        }

        /**
         * Sets the value of the {@code "up"} property (Up Axis).
         *
         * @param upAxis The new value.
         * @return The {@code this} instance for chaining
         */
        public Metadata setUpAxis(UpAxis upAxis) {
            createProperty(CastPropertyID.STRING, "up", upAxis.toString().toLowerCase());
            return this;
        }

        /**
         * Returns the value of the {@code "sr"} property (Scene Root).
         *
         * @return The value of the {@code "sr"} property
         */
        public Optional<String> getSceneRoot() {
            return getProperty("sr", String.class::cast);
        }

        /**
         * Sets the value of the {@code "sr"} property (Scene Root).
         *
         * @param sceneRoot The new value.
         * @return The {@code this} instance for chaining
         */
        public Metadata setSceneRoot(String sceneRoot) {
            createProperty(CastPropertyID.STRING, "sr", sceneRoot);
            return this;
        }
    }
}
