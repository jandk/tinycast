package be.twofold.tinycast.generator;

import be.twofold.tinycast.*;
import be.twofold.tinycast.generator.model.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

final class TypeGenerator {
    private static final RawType RootType = new RawType(
        "Root",
        "Model, Animation, Instance, Metadata",
        ""
    );
    private static final RawType ModelType = new RawType(
        "Model",
        "Skeleton, Mesh, Hair, Blend Shape, Material",
        "Name (n) 	String (s) 	False 	False\n" +
            "Position (p) 	Vector 3 (v3) 	False 	False\n" +
            "Rotation (r) 	Vector 4 (v4) 	False 	False\n" +
            "Scale (s) 	Vector 3 (v3) 	False 	False\n"
    );
    private static final RawType MeshType = new RawType(
        "Mesh",
        "",
        "Name (n) 	String (s) 	False 	False\n" +
            "Vertex Position Buffer (vp) 	Vector 3 (v3) 	True 	True\n" +
            "Vertex Normal Buffer (vn) 	Vector 3 (v3) 	True 	False\n" +
            "Vertex Tangent Buffer (vt) 	Vector 3 (v3) 	True 	False\n" +
            "Vertex Color Buffer (c%d) 	Integer 32 (i), Vector 4 (v4) 	True 	False\n" +
            "Vertex UV Buffer (u%d) 	Vector 2 (v2) 	True 	False\n" +
            "Vertex Weight Bone Buffer (wb) 	Integer 32 (i), Short (h), Byte (b) 	True 	False\n" +
            "Vertex Weight Value Buffer (wv) 	Float (f) 	True 	False\n" +
            "Face Buffer (f) 	Integer 32 (i), Short (h), Byte (b) 	True 	True\n" +
            "Color Layer Count (cl) 	Integer 32 (i), Short (h), Byte (b) 	False 	True if has color layers else False\n" +
            "UV Layer Count (ul) 	Integer 32 (i), Short (h), Byte (b) 	False 	True if has uv layers else False\n" +
            "Maximum Weight Influence (mi) 	Integer 32 (i), Short (h), Byte (b) 	False 	True if has weights else False\n" +
            "Skinning Method (sm) 	String (s) [linear, quaternion] 	False 	False\n" +
            "Material (Hash of CastNode:Material) (m) 	Integer 64 (l) 	False 	False\n"
    );
    private static final RawType HairType = new RawType(
        "Hair",
        "",
        "Name (n) 	String (s) 	False 	False\n" +
            "Segments Buffer (se) 	Integer 32 (i), Short (h), Byte (b) 	True 	True\n" +
            "Particle Buffer (pt) 	Vector 3 (v3) 	True 	True\n" +
            "Material (Hash of CastNode:Material) (m) 	Integer 64 (l) 	False 	False\n"
    );
    private static final RawType BlendShapeType = new RawType(
        "Blend Shape",
        "",
        "Name (n) 	String (s) 	False 	True\n" +
            "Base Shape (Hash of CastNode:Mesh) (b) 	Integer 64 (l) 	False 	True\n" +
            "Target Shape Vertex Indices (vi) 	Byte (b), Short (h), Integer 32 (i) 	True 	True\n" +
            "Target Shape Vertex Positions (vp) 	Vector 3 (v3) 	True 	True\n" +
            "Target Weight Scale (ts) 	Float (f) 	True 	False\n"
    );
    private static final RawType SkeletonType = new RawType(
        "Skeleton",
        "Bone, IK Handle, Constraint",
        ""
    );
    private static final RawType BoneType = new RawType(
        "Bone",
        "",
        "Name (n) 	String (s) 	False 	True\n" +
            "Parent Index (p) 	Integer 32 (i) 	False 	False\n" +
            "Segment Scale Compensate (ssc) 	Byte (b) [True, False] 	False 	False\n" +
            "Local Position (lp) 	Vector 3 (v3) 	False 	False\n" +
            "Local Rotation (lr) 	Vector 4 (v4) 	False 	False\n" +
            "World Position (wp) 	Vector 3 (v3) 	False 	False\n" +
            "World Rotation (wr) 	Vector 4 (v4) 	False 	False\n" +
            "Scale (s) 	Vector 3 (v3) 	False 	False\n"
    );
    private static final RawType IKHandleType = new RawType(
        "IK Handle",
        "",
        "Name (n) 	String (s) 	False 	False\n" +
            "Start Bone Hash (sb) 	Integer 64 (l) 	False 	True\n" +
            "End Bone Hash (eb) 	Integer 64 (l) 	False 	True\n" +
            "Target Bone Hash (tb) 	Integer 64 (l) 	False 	False\n" +
            "Pole Vector Bone Hash (pv) 	Integer 64 (l) 	False 	False\n" +
            "Pole Bone Hash (pb) 	Integer 64 (l) 	False 	False\n" +
            "Use Target Rotation (tr) 	Byte (b) [True, False] 	False 	False\n"
    );
    private static final RawType ConstraintType = new RawType(
        "Constraint",
        "",
        "Name (n) 	String (s) 	False 	False\n" +
            "Constraint Type (ct) 	String (s) [pt, or, sc] 	False 	True\n" +
            "Constraint Bone Hash (cb) 	Integer 64 (l) 	False 	True\n" +
            "Target Bone Hash (tb) 	Integer 64 (l) 	False 	True\n" +
            "Maintain Offset (mo) 	Byte (b) [True, False] 	False 	False\n" +
            "Custom Offset (co) 	Vector3 (v3), Vector 4 (v4) 	False 	False\n" +
            "Weight (wt) 	Float (f) 	False 	False\n" +
            "Skip X (sx) 	Byte (b) [True, False] 	False 	False\n" +
            "Skip Y (sy) 	Byte (b) [True, False] 	False 	False\n" +
            "Skip Z (sz) 	Byte (b) [True, False] 	False 	False\n"
    );
    private static final RawType MaterialType = new RawType(
        "Material",
        "File, Color",
        "Name (n) 	String (s) 	False 	True\n" +
            "Type (t) 	String (s) [pbr] 	False 	True\n" +
            "Albedo Hash (albedo) 	Integer 64 (l) 	False 	False\n" +
            "Diffuse Hash (diffuse) 	Integer 64 (l) 	False 	False\n" +
            "Normal Hash (normal) 	Integer 64 (l) 	False 	False\n" +
            "Specular Hash (specular) 	Integer 64 (l) 	False 	False\n" +
            "Gloss Hash (gloss) 	Integer 64 (l) 	False 	False\n" +
            "Roughness Hash (roughness) 	Integer 64 (l) 	False 	False\n" +
            "Emissive Hash (emissive) 	Integer 64 (l) 	False 	False\n" +
            "Emissive Mask Hash (emask) 	Integer 64 (l) 	False 	False\n" +
            "Ambient Occlusion Hash (ao) 	Integer 64 (l) 	False 	False\n" +
            "Cavity Hash (cavity) 	Integer 64 (l) 	False 	False\n" +
            "Anisotropy Hash (aniso) 	Integer 64 (l) 	False 	False\n" +
            "Extra (x) Hash (extra%d) 	Integer 64 (l) 	False 	False\n"
    );
    private static final RawType FileType = new RawType(
        "File",
        "",
        "Path (p) 	String (s) 	False 	True\n"
    );
    private static final RawType ColorType = new RawType(
        "Color",
        "",
        "Name (n) 	String (s) 	False 	False\n" +
            "Color Space (cs) 	String (s) [srgb, linear] 	False 	False\n" +
            "Rgba Color (rgba) 	Vector 4 (v4) 	False 	True\n"
    );
    private static final RawType AnimationType = new RawType(
        "Animation",
        "Skeleton, Curve, Curve Mode Override, Notification Track",
        "Name (n) 	String (s) 	False 	False\n" +
            "Framerate (fr) 	Float (f) 	False 	True\n" +
            "Looping (lo) 	Byte (b) [True, False] 	False 	False\n"
    );
    private static final RawType CurveType = new RawType(
        "Curve",
        "",
        "Node Name (nn) 	String (s) 	False 	True\n" +
            "Key Property Name (kp) 	String (s) [rq, tx, ty, tz, sx, sy, sz, bs, vb] 	False 	True\n" +
            "Key Frame Buffer (kb) 	Byte (b), Short (h), Integer 32 (i) 	True 	True\n" +
            "Key Value Buffer (kv) 	Byte (b), Short (h), Integer 32 (i), Float (f), Vector 4 (v4) 	True 	True\n" +
            "Mode (m) 	String (s) [additive, absolute, relative] 	False 	True\n" +
            "Additive Blend Weight (ab) 	Float (f) 	False 	False\n"
    );
    private static final RawType CurveModeOverrideType = new RawType(
        "Curve Mode Override",
        "",
        "Node Name (nn) 	String (s) 	False 	True\n" +
            "Mode (m) 	String (s) [additive, absolute, relative] 	False 	True\n" +
            "Override Translation Curves (ot) 	Byte (b) [True, False] 	False 	False\n" +
            "Override Rotation Curves (or) 	Byte (b) [True, False] 	False 	False\n" +
            "Override Scale Curves (os) 	Byte (b) [True, False] 	False 	False\n"
    );
    private static final RawType NotificationTrackType = new RawType(
        "Notification Track",
        "",
        "Name (n) 	String (s) 	False 	True\n" +
            "Key Frame Buffer (kb) 	Byte (b), Short (h), Integer 32 (i) 	True 	True\n"
    );
    private static final RawType InstanceType = new RawType(
        "Instance",
        "File",
        "Name (n) 	String (s) 	False 	False\n" +
            "Reference File (Hash of CastNode:File) (rf) 	Integer 64 (l) 	False 	True\n" +
            "Position (p) 	Vector 3 (v3) 	False 	True\n" +
            "Rotation (r) 	Vector 4 (v4) 	False 	True\n" +
            "Scale (s) 	Vector 3 (v3) 	False 	True\n"
    );
    private static final RawType MetadataType = new RawType(
        "Metadata",
        "",
        "Author (a) 	String (s) 	False 	False\n" +
            "Software (s) 	String (s) 	False 	False\n" +
            "Up Axis (up) 	String (s) [x, y, z] 	False 	False\n" +
            "Scene Root (sr) 	String (s) 	False 	False\n"
    );

    private static final List<RawType> RawTypes = List.of(
        RootType,
        ModelType,
        MeshType,
        HairType,
        BlendShapeType,
        SkeletonType,
        BoneType,
        IKHandleType,
        ConstraintType,
        AnimationType,
        CurveType,
        CurveModeOverrideType,
        NotificationTrackType,
        MaterialType,
        FileType,
        ColorType,
        InstanceType,
        MetadataType
    );

    public static void main(String[] args) throws IOException {
        List<TypeDef> types = RawTypes.stream()
            .map(TypeParser::parse)
            .collect(Collectors.toList());

        Map<CastNodeID, List<String>> arrayTypes = new LinkedHashMap<>();
        for (TypeDef type : types) {
            for (PropertyDef property : type.properties()) {
                if (property.isArray()) {
                    arrayTypes.computeIfAbsent(type.type(), __ -> new ArrayList<>()).add(property.key());
                }
            }
        }

        for (var type : arrayTypes.entrySet()) {
            String props = type.getValue().stream()
                .collect(Collectors.joining("\", \"", "\"", "\""));
            System.out.println("CastNodeID." + type.getKey() + ", Set.of(" + props + "),");
        }

        new TypeClassWriter().generate(types);
    }
}
