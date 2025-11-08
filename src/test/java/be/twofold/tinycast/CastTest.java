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

        var positionBuffer = FloatBuffer.wrap(positions);
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
