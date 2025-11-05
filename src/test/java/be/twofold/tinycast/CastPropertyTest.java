package be.twofold.tinycast;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class CastPropertyTest {
    @Test
    void testEqualsAndHashCode() {
        EqualsVerifier
            .forClass(CastProperty.class)
            .suppress(Warning.NULL_FIELDS)
            .verify();
    }
}
