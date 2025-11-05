package be.twofold.tinycast;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class Vec3Test {
    @Test
    void testEqualsAndHashCode() {
        EqualsVerifier
            .forClass(Vec3.class)
            .verify();
    }
}
