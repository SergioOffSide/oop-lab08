package it.unibo.deathnote.impl;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import it.unibo.deathnote.api.DeathNote;

/**
 * DeathNoteImpl implements DeathNote interface.
 */
public class DeathNoteImpl implements DeathNote {

    public static final String STANDARD_DEATH_CAUSE = "heart attack";

    private static final int MIN_INDEX = 1;
    private static final long MILLIS_DEATH_CAUSE_CAP = 4_000L;
    private static final long MILLIS_DEATH_DETAILS_CAP = 6_040L;
    private static final String NO_HUMAN_FOUND_ERROR_MESSAGE = "No person written with this name: ";

    private final Set<Human> deathNotes = new LinkedHashSet<>();
    private String lastNameWritten;

    /** 
     * {@inheritDoc}
     */
    @Override
    public String getRule(final int ruleNumber) {
        if (ruleNumber < MIN_INDEX || ruleNumber > RULES.size()) {
            throw new IllegalArgumentException(
                "The rule number can't be less than 1 or grater than "
                + RULES.size() 
                + ", actually parameter is: "
                + ruleNumber
            );
        }
        return RULES.get(ruleNumber - MIN_INDEX);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeName(final String name) {
        Objects.requireNonNull(name, "Human's name can't be null");
        if (!name.isBlank()) {
            deathNotes.add(new Human(name));
            lastNameWritten = name;
        }
    }

    private Human getLastWrittenHuman(final String text, final long millisTimeCap) {
        if (deathNotes.isEmpty() || text == null) {
            throw new IllegalStateException("No name already written or a null string has been passed");
        }
        final Human human = getHuman(lastNameWritten);
        if (human != null && System.currentTimeMillis() - human.deathTime <= millisTimeCap) {
            lastNameWritten = "";
            return human;
        }
        return null;
    } 

   /**
    * {@inheritDoc}
    */
   @Override
    public boolean writeDeathCause(final String cause) {
        final Human pointedPerson = getLastWrittenHuman(cause, MILLIS_DEATH_CAUSE_CAP);
        if (pointedPerson != null) {
            pointedPerson.deathCause = cause;
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean writeDetails(final String details) {
        final Human pointedPerson = getLastWrittenHuman(details, MILLIS_DEATH_DETAILS_CAP);
        if (pointedPerson != null) {
            pointedPerson.deathDetails = details;
            return true;
        }
        return false;
    }

    private Human getHuman(final String name) {
        final var iterator = deathNotes.iterator();
        while (iterator.hasNext()) {
            final Human human = iterator.next();
            if (human.name.equals(name)) {
                return human;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDeathCause(final String name) {
        final Human human = getHuman(name);
        if (human == null) {
            throw new IllegalArgumentException(NO_HUMAN_FOUND_ERROR_MESSAGE + name);
        }
        return human.deathCause;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDeathDetails(final String name) {
        final Human human = getHuman(name);
        if (human == null) {
            throw new IllegalArgumentException(NO_HUMAN_FOUND_ERROR_MESSAGE + name);
        }
        return human.deathDetails;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNameWritten(final String name) {
        return getHuman(name) != null;
    }

    private class Human {
        private final String name;
        private final long deathTime;
        private String deathCause = STANDARD_DEATH_CAUSE;
        private String deathDetails = " ";

        Human(final String name) {
            this.name = name;
            this.deathTime = System.currentTimeMillis();
        }
    }
}
