package be.twofold.tinycast;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class Vec2Test {
    @Test
    void testEqualsAndHashCode() {
        EqualsVerifier
            .forClass(Vec2.class)
            .verify();
    }
}
