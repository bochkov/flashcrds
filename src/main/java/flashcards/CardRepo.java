package flashcards;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public interface CardRepo {

    int writeTo(BufferedWriter writer) throws IOException;

    int readFrom(BufferedReader reader) throws IOException;

    void add(Card card);

    boolean remove(String id);

    Card random();

    Card findById(String id);

    Card findByDefinition(String definition);

    void resetMistakes();

    List<Card> hardest();

}
