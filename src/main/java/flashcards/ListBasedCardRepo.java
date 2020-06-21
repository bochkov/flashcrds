package flashcards;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class ListBasedCardRepo implements CardRepo {

    private static final List<Card> CARDS = new ArrayList<>();
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    @Override
    public int writeTo(BufferedWriter writer) throws IOException {
        int count = 0;
        for (Card card : CARDS) {
            writer.write(card.exportStr());
            ++count;
        }
        return count;
    }

    @Override
    public int readFrom(BufferedReader reader) throws IOException {
        String line;
        int count = 0;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("-:-");
            String id = parts[0].trim();
            String def = parts[1].trim();
            int mistakes = Integer.parseInt(parts[2].trim());
            remove(id);
            add(new Card(id, def, mistakes));
            ++count;
        }
        return count;
    }

    @Override
    public void add(Card card) {
        CARDS.add(card);
    }

    @Override
    public boolean remove(String id) {
        return CARDS.removeIf(card -> card.term().equalsIgnoreCase(id));
    }

    @Override
    public Card random() {
        return CARDS.get(
                RANDOM.nextInt(
                        CARDS.size()
                )
        );
    }

    @Override
    public Card findById(String id) {
        for (Card card : CARDS) {
            if (card.term().equalsIgnoreCase(id))
                return card;
        }
        return null;
    }

    @Override
    public Card findByDefinition(String definition) {
        for (Card card : CARDS) {
            if (card.definition().equalsIgnoreCase(definition))
                return card;
        }
        return null;
    }

    @Override
    public void resetMistakes() {
        for (Card card : CARDS) {
            card.reset();
        }
    }

    @Override
    public List<Card> hardest() {
        List<Card> hard = new ArrayList<>();
        int min = 0;
        for (Card card : CARDS) {
            if (card.mistakes() > min) {
                hard.clear();
                hard.add(card);
                min = card.mistakes();
            } else if (card.mistakes() == min && min != 0) {
                hard.add(card);
            }
        }
        return hard;
    }
}
