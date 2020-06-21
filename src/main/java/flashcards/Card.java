package flashcards;

import java.util.concurrent.atomic.AtomicInteger;

public final class Card {

    private final String term;
    private final String definition;
    private final AtomicInteger mistakes;

    public Card(String term, String definition) {
        this(term, definition, 0);
    }

    public Card(String term, String definition, int mistakes) {
        this.term = term;
        this.definition = definition;
        this.mistakes = new AtomicInteger(mistakes);
    }

    public String term() {
        return term;
    }

    public String definition() {
        return definition;
    }

    public int mistakes() {
        return mistakes.get();
    }

    public void incMistakes() {
        mistakes.incrementAndGet();
    }

    public void reset() {
        mistakes.set(0);
    }

    @Override
    public String toString() {
        return String.format("(\"%s\":\"%s\")", term, definition);
    }

    public String exportStr() {
        return String.format("%s -:- %s -:- %d%n", term, definition, mistakes.get());
    }
}
