package be.twofold.tinycast;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;

class CastNodeTest {
    @Test
    void testEqualsAndHashCode() {
        EqualsVerifier
            .forClass(CastNode.class)
            .suppress(Warning.NULL_FIELDS)
            .withIgnoredFields("hasher")
            .withIgnoredFields("cachedLength")
            .withPrefabValues(CastNode.class,
                new CastNodes.Color(new AtomicLong()),
                new CastNodes.File(new AtomicLong()))
            .verify();
    }

    @Test
    void testRemoveChild() {
        AtomicLong hasher = new AtomicLong();
        CastNodes.Model model = new CastNodes.Model(hasher);
        CastNodes.Mesh mesh = model.createMesh();

        assertThat(model.removeChild(mesh)).isTrue();
        assertThat(model.getMeshes()).isEmpty();
        assertThat(model.removeChild(mesh)).isFalse();
    }
}
