# tinycast

[![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/jandk/tinycast/maven.yml?logo=github)](https://github.com/jandk/tinycast/actions/workflows/maven.yml)
[![Maven Central Version](https://img.shields.io/maven-central/v/be.twofold/tinycast?logo=apachemaven)](https://central.sonatype.com/artifact/be.twofold/tinycast)
[![License](https://img.shields.io/github/license/jandk/tinycast)](https://opensource.org/licenses/MIT)

A small Java library for reading and writing `.cast` files — the compact scene container create by dtzxporter.

- Tiny API, tiny dependency footprint
- Read and write: streams in, streams out
- Java 11+
- Mirrors the node layout from the Cast spec

For the file format itself, see the reference here:

- Cast format: https://github.com/dtzxporter/cast

## Why

I had an implementation lingering in [Valen](https://github.com/jandk/valen), and turning it into a shared library that
can be reused by others wasn't too much trouble.
Also helps fixing any remaining issues that I had with it, and writing documentation and some basic tests.
Can be used for both reading and writing files — without adopting a full DCC or a heavyweight engine SDK.
`tinycast` gives you a typed, model-centric API that feels like the file: nodes, properties, buffers.

## Installation

Maven coordinates:

```xml
<dependency>
    <groupId>be.twofold</groupId>
    <artifactId>tinycast</artifactId>
    <version>0.1</version>
</dependency>
```

Gradle (Kotlin DSL):

```kts
dependencies {
    implementation("be.twofold:tinycast:0.1")
}
```

Note: Versions and publication may change; check the repository releases for a stable version once tagged.

## Quick start

### Reading a cast file

```java
import be.twofold.tinycast.Cast;
import be.twofold.tinycast.CastException;
import be.twofold.tinycast.CastNode;
import be.twofold.tinycast.CastNodes;

void main() throws IOException, CastException {
    try (InputStream in = Files.newInputStream(Path.of("scene.cast"))) {
        Cast cast = Cast.read(in);
        for (CastNode root : cast) {
            CastNodes.Root scene = (CastNodes.Root) root;
            for (CastNodes.Model model : scene.getModels()) {
                System.out.println("Model: " + model.getName().orElse("<unnamed>"));
                for (CastNodes.Mesh mesh : model.getMeshes()) {
                    FloatBuffer vp = mesh.getVertexPositionBuffer();
                    int vertexCount = vp.capacity() / 3;
                    System.out.println("  Mesh vertices: " + vertexCount);
                }
            }
        }
    }
}

```

### Writing a cast file

```java
import be.twofold.tinycast.Cast;
import be.twofold.tinycast.CastException;
import be.twofold.tinycast.CastNodes;

void main() {
    Cast cast = Cast.create();
    CastNodes.Root root = cast.createRoot();

    CastNodes.Model model = root.createModel()
            .setName("Triangle");

    // A single triangle (3 vertices, xyz per vertex)
    float[] positions = {
            -1f, -1f, 0f,
            1f, -1f, 0f,
            0f, 1f, 0f
    };

    // One face (3 indices)
    int[] faces = {0, 1, 2};

    model.createMesh()
            .setName("Triangle")
            .setVertexPositionBuffer(FloatBuffer.wrap(positions))
            .setFaceBuffer(IntBuffer.wrap(faces))
            .setUVLayerCount(0)
            .setColorLayerCount(0)
            .setMaximumWeightInfluence(0);

    try (OutputStream out = Files.newOutputStream(Path.of("triangle.cast"))) {
        cast.write(out);
    } catch (CastException | IOException e) {
        throw new RuntimeException(e);
    }
}
```

## The mental model

A cast file is a stack of nodes. Each node:

- Has an identifier (the type), a unique 64-bit hash, and a bag of properties
- Can have children (forming a scene graph per root)

`tinycast` maps that 1:1 to Java types under `CastNodes`:

- Root → scenes; contains `Model`, `Animation`, `Instance`, `Metadata`
- Model → optional `Skeleton`, plus `Mesh`, `Hair`, `BlendShape`, `Material`
- Skeleton → `Bone`, `IkHandle`, `Constraint`
- Animation → `Curve`, `CurveModeOverride`, `NotificationTrack`
- Material → `File` and `Color` sub-entries
- Instance → references external files and placement

Hashes are generated for you when you create nodes from a parent via the builder-style helpers, keeping sibling hashes
unique.

## Buffers and types

Properties that are arrays are exposed as NIO buffers:

- Positions, normals, tangents → `FloatBuffer`
- UVs, weights (values) → `FloatBuffer`
- Vertex colors → either `IntBuffer` (packed RGBA) or `FloatBuffer` (vec4)
- Faces/indices → `IntBuffer` or `ShortBuffer` depending on your data
- Hair segments/indices and various integer arrays → `Buffer` variants

To provide data, wrap arrays or use direct buffers:

```java
FloatBuffer vp = FloatBuffer.wrap(new float[]{ /* x, y, z … */});
IntBuffer f = IntBuffer.wrap(new int[]{ /* i0, i1, i2 … */});
```

Strings are UTF‑8, vectors are simple `Vec2`, `Vec3`, `Vec4` value types.

## Compatibility

- Reader expects magic `0x74736163` ("cast") and `version == 1`
- Writer emits the same
- Byte order is little‑endian
- Unknown/unsupported property types will throw `UnsupportedOperationException`

If you bump into a variant in the wild, please file an issue with a sample.

## Caveats (for now)

- No automatic tangent generation or topology helpers — bring your own
- No validation beyond what the spec and the types enforce
- Material extras are passthrough hashes; resolve them in your app

## Acknowledgements

- Format and tooling by dtzxporter: https://github.com/dtzxporter/cast
- Inspiration and style borrowed from my `tiny*` family of libraries

## License

MIT. See `LICENSE`.
