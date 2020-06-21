package flashcards;

import java.io.BufferedWriter;
import java.io.IOException;

public interface Log {

    String in(String str);

    void out(String str);

    void writeTo(BufferedWriter writer) throws IOException;

}
