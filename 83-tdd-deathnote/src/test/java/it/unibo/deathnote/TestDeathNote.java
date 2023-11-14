package it.unibo.deathnote;

import it.unibo.deathnote.api.DeathNote;
import it.unibo.deathnote.impl.DeathNoteImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestDeathNote {
    private static DeathNote deathNote;
    private final static String name = "Matteo";
    private final static String name2 = "Filippo";

    @BeforeEach
    public void setUp(){
        this.deathNote = new DeathNoteImpl();
    }

    private static void tryRules(final int nRule){
        try {
            deathNote.getRule(nRule);
            fail("Even though the index was out of bound getRule() didn't throw any excepion");
        } catch (IllegalArgumentException e) {
            assertEquals("Index has to be between 1 and " + DeathNote.RULES.size(), e.getMessage());
        }
    }

    @Test
    public void testRulesCall(){
        tryRules(0);
        tryRules(-1);
    }

    @Test
    public void testRulesContent(){
        for (String s : DeathNote.RULES) {
            assertNotNull(s);
            assertFalse(s.isBlank());
        }
    }

    @Test
    public void testHuman(){
        assertFalse(deathNote.isNameWritten(name));
        deathNote.writeName(name);
        assertTrue(deathNote.isNameWritten(name));
        assertFalse(deathNote.isNameWritten(name2));
        assertFalse(deathNote.isNameWritten(""));
    }

    @Test
    public void testCause() throws InterruptedException {
        final String cause = "Throat sliced";
        try {
            deathNote.writeDeathCause("Heart attack");
            fail("writeDeathCause() should throw an IllegalStateException but it doesn't");
        } catch (IllegalStateException e) {
            assertEquals("Either the book hasn't been used yet or the cause of death isn't specified", e.getMessage());
        }
        deathNote.writeName(name);
        assertEquals("Heart attack", deathNote.getDeathCause(name));
        deathNote.writeName(name2);
        assertTrue(deathNote.writeDeathCause(cause));
        assertEquals("Throat sliced", deathNote.getDeathCause(name2));
        Thread.sleep(100);
        deathNote.writeDeathCause("Kartring accident");
        assertEquals("Throat sliced", deathNote.getDeathCause(name2));
    }

    @Test
    public void testDetails() throws InterruptedException {
        final String details = "Ran for too long";
        try {
            deathNote.writeDetails(details);
            fail("writeDetails() should throw an IllegalStateException but it doesn't");
        } catch (IllegalStateException e) {
            assertEquals("Either the book hasn't been used yet or the details of death aren't specified", e.getMessage());
        }
        deathNote.writeName(name);
        assertEquals("", deathNote.getDeathDetails(name));
        assertTrue(deathNote.writeDetails(details));
        assertEquals("Ran for too long", deathNote.getDeathDetails(name));
        deathNote.writeName(name2);
        Thread.sleep(6100);
        deathNote.writeDetails(details);
        assertEquals("", deathNote.getDeathDetails(name2));
    }
}