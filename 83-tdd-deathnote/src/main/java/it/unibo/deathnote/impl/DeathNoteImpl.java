package it.unibo.deathnote.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import it.unibo.deathnote.api.DeathNote;

public class DeathNoteImpl implements DeathNote {
    private final Map<String, Death> deaths = new LinkedHashMap<>();
    private String lastNameAdded = new String(); 
    
    private class Death{
        private final static String PRIMAL_DEATH_CAUSE = "Heart attack";
        private final static int LIMIT_ADD_CAUSE = 40;
        private final static int LIMIT_ADD_DETAILS = 6040;
        private String cause;
        private String details;
        private final long deathWrittenAt;
        private long causeWrittenAt;

        private Death(String cause, String details){
            this.cause = cause;
            this.details = details;
            this.deathWrittenAt = this.causeWrittenAt = System.currentTimeMillis();
        }

        private Death(String cause){
            this(cause, new String());
        }

        private Death(){
            this(PRIMAL_DEATH_CAUSE, new String());
        }
    }

    @Override
    public String getRule(int ruleNumber) {
        if (ruleNumber < 1 || ruleNumber > DeathNote.RULES.size()){
            throw new IllegalArgumentException("Index has to be between 1 and " + DeathNote.RULES.size());
        }
        return DeathNote.RULES.get(ruleNumber - 1);
    }

    @Override
    public void writeName(String name) {
        Objects.requireNonNull(name);
        lastNameAdded = name;
        deaths.put(name, new Death());
    }

    @Override
    public boolean writeDeathCause(String cause) {
        if(lastNameAdded.isEmpty()){
            throw new IllegalStateException("Either the book hasn't been used yet or the cause of death isn't specified");
        }
        if (System.currentTimeMillis() - this.deaths.get(lastNameAdded).deathWrittenAt > Death.LIMIT_ADD_CAUSE){
            return false;
        }
        if(this.deaths.keySet().isEmpty() || Objects.isNull(cause)){
            throw new IllegalStateException("Either the book hasn't been used yet or the cause of death isn't specified");
        }
        this.deaths.get(lastNameAdded).cause = cause;
        this.deaths.get(lastNameAdded).causeWrittenAt = System.currentTimeMillis();
        return true;
    }

    @Override
    public boolean writeDetails(String details) {
        if(lastNameAdded.isEmpty()){
            throw new IllegalStateException("Either the book hasn't been used yet or the details of death aren't specified");
        }
        if(System.currentTimeMillis() - this.deaths.get(lastNameAdded).causeWrittenAt > Death.LIMIT_ADD_DETAILS) {
            return false;
        }
        if(this.deaths.keySet().isEmpty() || Objects.isNull(details)){
            throw new IllegalStateException("Either the book hasn't been used yet or the details of death aren't specified");
        }
        this.deaths.get(lastNameAdded).details = details;
        return true;
    }

    @Override
    public String getDeathCause(String name) {
        if(!isNameWritten(name)){
            throw new IllegalArgumentException(name + "isn't written in the book");
        }
        final String cause = this.deaths.get(name).cause;
        return cause;
    }

    @Override
    public String getDeathDetails(String name) {
        if(!isNameWritten(name)){
            throw new IllegalArgumentException(name + "isn't written in the book");
        }
        final String details = this.deaths.get(name).details;
        return details;
    }

    @Override
    public boolean isNameWritten(String name) {
        return this.deaths.containsKey(name);
    }
    
}
