package be.twofold.tinycast;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

class CastTest {

    public static final String BASIC_CAST = "" +
        "Y2FzdAEAAAABAAAAAAAAAHJvb3SmAAAAVkFMRU5STFoAAAAAAQAAAG1vZGyOAAAAV0FMRU5STFoBAAAA" +
        "AQAAAHMAAQABAAAAblRyaWFuZ2xlAG1lc2hkAAAAWEFMRU5STFoDAAAAAAAAAHMAAQABAAAAblRyaWFu" +
        "Z2xlADN2AgADAAAAdnAAAIC/AACAvwAAAAAAAIA/AACAvwAAAAAAAAAAAACAPwAAAABiAAEAAwAAAGYA" +
        "AQI=";

    @Test
    void testRead() throws Exception {
        var bytes = Base64.getDecoder().decode(BASIC_CAST);
        Cast cast = Cast.read(new ByteArrayInputStream(bytes));
        assertThat(cast).isEqualTo(createCast());
    }

    @Test
    void testWrite() throws Exception {
        var baos = new ByteArrayOutputStream();
        createCast().write(baos);

        assertThat(Base64.getEncoder().encodeToString(baos.toByteArray()))
            .isEqualTo(BASIC_CAST);
    }

    private Cast createCast() {
        var cast = Cast.create();
        var root = cast.createRoot();

        var model = root.createModel()
            .setName("Triangle");

        var positions = new float[]{
            -1.0f, -1.0f, +0.0f,
            +1.0f, -1.0f, +0.0f,
            +0.0f, +1.0f, +0.0f
        };

        // One face (3 indices)
        var faces = new int[]{0, 1, 2};

        model.createMesh()
            .setName("Triangle")
            .setVertexPositionBuffer(FloatBuffer.wrap(positions))
            .setFaceBuffer(IntBuffer.wrap(faces));
        return cast;
    }
}
