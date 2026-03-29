package be.twofold.tinycast;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

class CastTest {

    public static final String BASIC_CAST = "" +
        "Y2FzdAEAAAABAAAAAAAAAHJvb3SmAAAAVkFMRU5STFoAAAAAAQAAAG1vZGyOAAAAV0FMRU5STFoBAAAA" +
        "AQAAAHMAAQABAAAAblRyaWFuZ2xlAG1lc2hkAAAAWEFMRU5STFoDAAAAAAAAAHMAAQABAAAAblRyaWFu" +
        "Z2xlADN2AgADAAAAdnAAAIC/AACAvwAAAAAAAIA/AACAvwAAAAAAAAAAAACAPwAAAABiAAEAAwAAAGYA" +
        "AQI=";

    @Test
    void testRead() throws Exception {
        byte[] bytes = Base64.getDecoder().decode(BASIC_CAST);
        Cast cast = Cast.read(new ByteArrayInputStream(bytes));
        assertThat(cast).isEqualTo(createCast(false));
    }

    @Test
    void testWrite() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        createCast(false).write(baos);

        assertThat(Base64.getEncoder().encodeToString(baos.toByteArray()))
            .isEqualTo(BASIC_CAST);
    }

    @Test
    void testWriteUnflippedBuffer() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        createCast(true).write(baos);

        // When we use limit to figure out how big the array is, and remaining to write it
        // the calculations get borked and the file is corrupted.
        assertThatNoException()
            .isThrownBy(() -> Cast.read(new ByteArrayInputStream(baos.toByteArray())));
    }


    @Test
    void testFindNodeByHash() {
        Cast cast = createCast(false);
        CastNodes.Root root = (CastNodes.Root) cast.getRootNodes().get(0);
        CastNodes.Model model = root.getModels().get(0);
        CastNodes.Mesh mesh = model.getMeshes().get(0);

        assertThat(cast.findNodeByHash(root.getHash())).contains(root);
        assertThat(cast.findNodeByHash(model.getHash())).contains(model);
        assertThat(cast.findNodeByHash(mesh.getHash())).contains(mesh);

        assertThat(cast.findNodeByHash(mesh.getHash(), CastNodes.Mesh.class)).contains(mesh);
        assertThat(cast.findNodeByHash(mesh.getHash(), CastNodes.Model.class)).isEmpty();
        assertThat(cast.findNodeByHash(-1L)).isEmpty();
    }

    @Test
    void testDeserializedNodeCount() throws Exception {
        Cast cast = Cast.create(0x5A4C524E454C4156L);
        CastNodes.Mesh mesh = cast.createRoot().createModel().createMesh();
        mesh.addVertexUVBuffer(FloatBuffer.wrap(new float[]{0.0f, 0.0f, 1.0f, 0.0f}));
        mesh.addVertexUVBuffer(FloatBuffer.wrap(new float[]{0.0f, 1.0f, 1.0f, 1.0f}));
        mesh.addVertexColorBufferV4(FloatBuffer.wrap(new float[]{1.0f, 1.0f, 1.0f, 1.0f}));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        cast.write(baos);
        Cast deserialized = Cast.read(new ByteArrayInputStream(baos.toByteArray()));

        CastNodes.Mesh deserializedMesh = (CastNodes.Mesh) deserialized.findNodeByHash(mesh.getHash()).orElseThrow();
        assertThat(deserializedMesh.getVertexUVBufferCount()).isEqualTo(2);
        assertThat(deserializedMesh.getVertexColorBufferCount()).isEqualTo(1);
    }

    @Test
    void testAddChildToDeserializedNode() throws Exception {
        byte[] bytes = Base64.getDecoder().decode(BASIC_CAST);
        Cast cast = Cast.read(new ByteArrayInputStream(bytes));
        CastNodes.Root root = (CastNodes.Root) cast.getRootNodes().get(0);
        CastNodes.Model model = root.getModels().get(0);

        CastNodes.Mesh newMesh = model.createMesh().setName("NewMesh");

        assertThat(model.getMeshes()).hasSize(2);
        assertThat(cast.findNodeByHash(newMesh.getHash())).contains(newMesh);
        assertThat(newMesh.getHash()).isNotEqualTo(model.getMeshes().get(0).getHash());
    }

    @Test
    void testWriteAfterMutation() throws Exception {
        Cast cast = createCast(false);
        CastNodes.Root root = (CastNodes.Root) cast.getRootNodes().get(0);
        CastNodes.Model model = root.getModels().get(0);

        ByteArrayOutputStream first = new ByteArrayOutputStream();
        cast.write(first);

        model.createMesh().setName("Second");

        ByteArrayOutputStream second = new ByteArrayOutputStream();
        cast.write(second);

        // The two outputs must differ (second is larger)
        assertThat(second.toByteArray()).isNotEqualTo(first.toByteArray());
        assertThat(second.size()).isGreaterThan(first.size());

        // And the mutated cast must round-trip cleanly
        Cast roundTripped = Cast.read(new ByteArrayInputStream(second.toByteArray()));
        assertThat(roundTripped.getRootNodes().get(0)).isEqualTo(root);
    }

    private Cast createCast(boolean flip) {
        Cast cast = Cast.create(0x5A4C524E454C4156L);
        CastNodes.Root root = cast.createRoot();

        CastNodes.Model model = root.createModel()
            .setName("Triangle");

        float[] positions = new float[]{
            -1.0f, -1.0f, +0.0f,
            +1.0f, -1.0f, +0.0f,
            +0.0f, +1.0f, +0.0f
        };

        // One face (3 indices)
        int[] faces = new int[]{0, 1, 2};

        FloatBuffer positionBuffer = FloatBuffer.wrap(positions);
        if (flip) {
            positionBuffer.position(positionBuffer.limit());
        }
        model.createMesh()
            .setName("Triangle")
            .setVertexPositionBuffer(positionBuffer)
            .setFaceBuffer(IntBuffer.wrap(faces));
        return cast;
    }
}
