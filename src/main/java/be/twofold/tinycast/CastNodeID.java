package be.twofold.tinycast;

/**
 * Enumeration of registered Cast node type identifiers.
 * <p>
 * Each identifier represents a specific node type in the Cast file format and is stored
 * as a 32-bit integer value. These identifiers are used during serialization and
 * deserialization to determine which class a node uses and how to handle its data.
 * <p>
 * The integer values are stored in little-endian byte order and often correspond to
 * ASCII character sequences when interpreted as bytes (e.g., ROOT = "root" in reverse byte order).
 *
 * @see CastNode
 */
public enum CastNodeID {
    /**
     * Root node identifier (0x746f6f72).
     * <p>
     * Root nodes represent scenes in a Cast file and can contain child nodes such as
     * Model, Animation, Instance, and Metadata. A Cast file consists of a collection
     * of root nodes, and all children of a root node must have unique hashes.
     */
    ROOT(0x746f6f72),

    /**
     * Model node identifier (0x6c646f6d).
     * <p>
     * Model nodes represent 3D models and can contain Skeleton, Mesh, Hair, BlendShape,
     * and Material child nodes. They support position, rotation, and scale properties
     * that modify the model and its children in world space.
     */
    MODEL(0x6c646f6d),

    /**
     * Mesh node identifier (0x6873656d).
     * <p>
     * Mesh nodes contain geometry data including vertex positions, normals, tangents,
     * colors, UVs, weights, and face indices. They support skinning and can reference
     * material nodes via hash.
     */
    MESH(0x6873656d),

    /**
     * Hair node identifier (0x72696168).
     * <p>
     * Hair nodes represent strand-based geometry with segments and particles.
     * Each strand has a segment count, with particles stored in world space.
     */
    HAIR(0x72696168),

    /**
     * Blend shape node identifier (0x68736c62).
     * <p>
     * Blend shape nodes (also known as morph targets or shape keys) define deformations
     * of a base mesh. They reference a base mesh and contain vertex indices and target
     * positions for vertices that differ from the base shape.
     */
    BLEND_SHAPE(0x68736c62),

    /**
     * Skeleton node identifier (0x6c656b73).
     * <p>
     * Skeleton nodes contain hierarchical bone structures and can have Bone, IKHandle,
     * and Constraint child nodes. A model may have at most one skeleton node.
     */
    SKELETON(0x6c656b73),

    /**
     * Bone node identifier (0x656e6f62).
     * <p>
     * Bone nodes represent individual bones in a skeleton hierarchy. They contain
     * local and world transform data (position, rotation, scale) and support
     * segment scale compensation.
     */
    BONE(0x656e6f62),

    /**
     * IK handle node identifier (0x64686b69).
     * <p>
     * IK (Inverse Kinematics) handle nodes define IK chains with start and end bones,
     * optional target and pole vector bones, and support for target rotation.
     */
    IK_HANDLE(0x64686b69),

    /**
     * Constraint node identifier (0x74736e63).
     * <p>
     * Constraint nodes define relationships between bones, such as point constraints
     * (translations), orient constraints (rotations), and scale constraints. They
     * support maintain offset, custom offsets, weights, and per-axis skipping.
     */
    CONSTRAINT(0x74736e63),

    /**
     * Animation node identifier (0x6d696e61).
     * <p>
     * Animation nodes contain animation data including framerate, looping settings,
     * and child nodes such as Curve, CurveModeOverride, NotificationTrack, and Skeleton.
     */
    ANIMATION(0x6d696e61),

    /**
     * Curve node identifier (0x76727563).
     * <p>
     * Curve nodes contain animation curves for properties such as rotation, translation,
     * scale, blend shape weights, and visibility. They define keyframes with frame numbers
     * and values, and support different modes (additive, absolute, relative).
     */
    CURVE(0x76727563),

    /**
     * Curve mode override node identifier (0x564f4d43).
     * <p>
     * Curve mode override nodes change the interpolation mode for a node and all its
     * descendants. They can override translation, rotation, and scale curve modes.
     */
    CURVE_MODE_OVERRIDE(0x564f4d43),

    /**
     * Notification track node identifier (0x6669746e).
     * <p>
     * Notification track nodes define events or notifications at specific keyframes
     * during animation playback.
     */
    NOTIFICATION_TRACK(0x6669746e),

    /**
     * Material node identifier (0x6c74616d).
     * <p>
     * Material nodes define surface properties for rendering. They contain properties
     * that reference File and Color nodes via hash for textures like albedo, normal,
     * specular, roughness, emissive, and more. Currently supports PBR material type.
     */
    MATERIAL(0x6c74616d),

    /**
     * File node identifier (0x656c6966).
     * <p>
     * File nodes represent file references, typically for textures. They contain a
     * path property and can be children of Material or Instance nodes.
     */
    FILE(0x656c6966),

    /**
     * Color node identifier (0x726c6f63).
     * <p>
     * Color nodes represent RGBA color values and can be used in materials as an
     * alternative to texture files. They support sRGB and linear color spaces.
     */
    COLOR(0x726c6f63),

    /**
     * Instance node identifier (0x74736e69).
     * <p>
     * Instance nodes reference external Cast files and include position, rotation,
     * and scale transforms. They enable large scene building by instancing content
     * from other files.
     */
    INSTANCE(0x74736e69),

    /**
     * Metadata node identifier (0x6174656d).
     * <p>
     * Metadata nodes contain information about the Cast file such as author, software,
     * up axis, and scene root directory. They are used for tagging and providing hints
     * to importing software but have no functional effect on the scene data.
     */
    METADATA(0x6174656d),
    ;

    private final int id;

    CastNodeID(int id) {
        this.id = id;
    }

    /**
     * Returns the 32-bit little-endian identifier for this node type.
     * <p>
     * The value is written to and read from Cast streams to identify the node
     * kind during serialization/deserialization.
     *
     * @return the integer identifier associated with this node type
     */
    public int getId() {
        return id;
    }

    /**
     * Resolves a {@code CastNodeID} from its 32-bit little-endian integer value.
     * <p>
     * If the value is not recognized as a registered node type, a {@link CastException}
     * is thrown instead of returning {@code null}.
     *
     * @param value the integer identifier read from a Cast stream
     * @return the matching {@code CastNodeID}
     * @throws CastException if the value does not map to a known node type
     */
    public static CastNodeID fromValue(int value) throws CastException {
        switch (value) {
            case 0x746f6f72:
                return ROOT;
            case 0x6c646f6d:
                return MODEL;
            case 0x6873656d:
                return MESH;
            case 0x72696168:
                return HAIR;
            case 0x68736c62:
                return BLEND_SHAPE;
            case 0x6c656b73:
                return SKELETON;
            case 0x656e6f62:
                return BONE;
            case 0x64686b69:
                return IK_HANDLE;
            case 0x74736e63:
                return CONSTRAINT;
            case 0x6d696e61:
                return ANIMATION;
            case 0x76727563:
                return CURVE;
            case 0x564f4d43:
                return CURVE_MODE_OVERRIDE;
            case 0x6669746e:
                return NOTIFICATION_TRACK;
            case 0x6c74616d:
                return MATERIAL;
            case 0x656c6966:
                return FILE;
            case 0x726c6f63:
                return COLOR;
            case 0x74736e69:
                return INSTANCE;
            case 0x6174656d:
                return METADATA;
            default:
                throw new CastException("Unknown CastNodeID: " + value);
        }
    }
}
