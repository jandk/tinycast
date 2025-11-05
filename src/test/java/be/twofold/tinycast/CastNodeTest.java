package be.twofold.tinycast;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicLong;

class CastNodeTest {
    @Test
    void testEqualsAndHashCode() {
        EqualsVerifier
            .forClass(CastNode.class)
            .suppress(Warning.NULL_FIELDS)
            .withIgnoredFields("hasher")
            .withPrefabValues(CastNode.class,
                new CastNodes.Color(new AtomicLong()),
                new CastNodes.File(new AtomicLong()))
            .verify();
    }
}
