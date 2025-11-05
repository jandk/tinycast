package be.twofold.tinycast;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class Vec4Test {
    @Test
    void testEqualsAndHashCode() {
        EqualsVerifier
            .forClass(Vec4.class)
            .verify();
    }
}
