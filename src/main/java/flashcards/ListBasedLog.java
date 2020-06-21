package flashcards;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings("java:S106")
public final class ListBasedLog implements Log {

    private final Scanner scan;
    private final List<String> log = new ArrayList<>();

    public ListBasedLog(InputStream in) {
        this.scan = new Scanner(in);
    }

    @Override
    public String in(String str) {
        log.add(str);
        System.out.println(str);
        String line = scan.nextLine();
        log.add(line);
        return line;
    }

    @Override
    public void out(String str) {
        log.add(str);
        System.out.println(str);
    }

    @Override
    public void writeTo(BufferedWriter writer) throws IOException {
        for (String line : log) {
            writer.write(line);
            writer.write("\n");
        }
    }
}
