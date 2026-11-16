package it.unibo.deathnote;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.deathnote.api.DeathNote;
import it.unibo.deathnote.impl.DeathNoteImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

final class TestDeathNote {

    private static final int MILLIS_DEATH_DETAILS_STOP = 6100;
    private static final int MILLIS_DEATH_CAUSE_STOP = 100;

    private final List<String> validNames = List.of("Paul", "Jean-Pierre");
    private DeathNote deathNote;

    @BeforeEach
    void deathNoteInit() {
        deathNote = new DeathNoteImpl();
    }

    // 1
    /**
     * Tests that rules number < 0 don't exist.
     */
    @Test
    void testGetRules() {
        for (int i = -1; i <= 0; i++) {
            try {
                deathNote.getRule(i);
                fail("getRule() should be throw an IllegaArgumentExcpetion");
            } catch (final IllegalArgumentException e) {
                assertNotNull(e);
                assertFalse(e.toString().isBlank());
                assertFalse(e.toString().isEmpty());
            }
        }
    }

    // 2
    /**
     * Tests that in RULES there aren't null or blank strings.
     */
    @Test
    void testRules() {
        for (final String rule : DeathNote.RULES) {
            assertNotNull(rule);
            assertFalse(rule.isBlank());
        }
    }

    // 3
    /**
     * Tests that there aren't names first of the inizialization and that there aren't names represented by
     * null, blank or empty strings.
     */
    @Test
    void testWriteHuman() {
        for (final String name : validNames) {
            assertFalse(deathNote.isNameWritten(name));
            deathNote.writeName(name);
            assertTrue(deathNote.isNameWritten(name));
        }
        final List<String> invalidNames = List.of("", " ", "    ");
        for (final String name : invalidNames) {
            deathNote.writeName(name);
            assertFalse(deathNote.isNameWritten(name));
        }
        try {
            deathNote.writeName(null);
            fail("writeName() should be throw a NullPointerExcpetion");
        } catch (final NullPointerException e) { // NOPMD
            assertFalse(deathNote.isNameWritten(null));
        }
    }

    // 4
    /**
     * Tests that the death cause is initilized correctly and that there isn't the possibility to assign again the
     * death cause pasted 40ms.
     */
    @Test 
    void testDeathCause() throws InterruptedException {
        final String deathCause = "karting accident";
        try {
            deathNote.writeDeathCause(deathCause);
            fail("writeDeathCause() should be throw an IllegalStateException");
        } catch (final IllegalStateException e) {
            assertFalse(deathNote.isNameWritten(validNames.getFirst()));
        }
        deathNote.writeName(validNames.getFirst());
        assertEquals(DeathNoteImpl.STANDARD_DEATH_CAUSE, deathNote.getDeathCause(validNames.getFirst()));
        deathNote.writeName(validNames.getLast());
        assertTrue(deathNote.writeDeathCause(deathCause));
        assertEquals(deathCause, deathNote.getDeathCause(validNames.getLast()));
        Thread.sleep(MILLIS_DEATH_CAUSE_STOP);
        assertFalse(deathNote.writeDeathCause("new cause"));
    }

    // 5
    /**
     * Tests that the death deatails are initilized correctly and that there isn't the possibility to assign
     * again the death deatails pasted 6040ms.
     */
    @Test 
    void testDeathDetails() throws InterruptedException {
        final String deathDeteails = "ran for too long";
        try {
            deathNote.writeDetails(deathDeteails);
            fail("writeDetails() should be throw an IllegalStateException");
        } catch (final IllegalStateException e) {
            assertFalse(deathNote.isNameWritten(validNames.getFirst()));
        }
        deathNote.writeName(validNames.getFirst());
        assertEquals(" ", deathNote.getDeathDetails(validNames.getFirst()));
        assertTrue(deathNote.writeDetails(deathDeteails));
        assertEquals(deathDeteails, deathNote.getDeathDetails(validNames.getFirst()));
        deathNote.writeName(validNames.getLast());
        assertTrue(deathNote.writeDetails(deathDeteails));
        assertEquals(deathDeteails, deathNote.getDeathDetails(validNames.getLast()));
        Thread.sleep(MILLIS_DEATH_DETAILS_STOP);
        assertFalse(deathNote.writeDetails("new cause"));
    }
}
