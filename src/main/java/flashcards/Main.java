package flashcards;

import java.io.*;
import java.util.List;
import java.util.StringJoiner;

@SuppressWarnings({"java:S1148", "java:S1192"})
public class Main {

    private final CardRepo repo = new ListBasedCardRepo();
    private final Log log = new ListBasedLog(System.in);

    private void add() {
        String id = log.in("The card:");
        if (repo.findById(id) != null) {
            log.out(String.format("The card \"%s\" already exists.", id));
            return;
        }
        String def = log.in("The definition of the card:");
        if (repo.findByDefinition(def) != null) {
            log.out(String.format("The definition \"%s\" already exists.", def));
            return;
        }
        Card card = new Card(id, def);
        repo.add(card);
        log.out(String.format("The pair %s has been added.", card));
    }

    private void remove() {
        String id = log.in("The card:");
        log.out(
                repo.remove(id) ?
                        "The card has been removed." :
                        String.format("Can't remove \"%s\": there is no such card.", id)
        );
    }

    private void imp() {
        File file = new File(log.in("File name:"));
        imp(file);
    }

    private void imp(File file) {
        if (file != null) {
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    int read = repo.readFrom(reader);
                    log.out(String.format("%d cards have been loaded.", read));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                log.out("File not found.");
            }
        }
    }


    private void exp() {
        File file = new File(log.in("File name:"));
        exp(file);
    }

    private void exp(File file) {
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                int write = repo.writeTo(writer);
                log.out(String.format("%d cards have been saved.", write));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void ask() {
        int count = Integer.parseInt(log.in("How many times to ask?"));
        for (int i = 0; i < count; ++i) {
            Card card = repo.random();
            String answer = log.in(String.format("Print definition of \"%s\":", card.term()));
            if (answer.equalsIgnoreCase(card.definition())) {
                log.out("Correct answer.");
            } else {
                Card tmp = repo.findByDefinition(answer);
                log.out(
                        tmp == null ?
                                String.format("Wrong answer. The correct one is \"%s\"", card.definition()) :
                                String.format("Wrong answer. The correct one is \"%s\", you've just written the definition of \"%s\".", card.definition(), tmp.term())

                );
                card.incMistakes();
            }
        }
    }

    private void exit() {
        log.out("Bye bye!");
    }

    private void saveLog() {
        File file = new File(log.in("File name:"));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            log.writeTo(writer);
            log.out("The log has been saved");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void hardestCard() {
        List<Card> hard = repo.hardest();
        if (hard.isEmpty()) {
            log.out("There are no cards with errors.");
        } else if (hard.size() == 1) {
            Card c = hard.get(0);
            log.out(String.format("The hardest card is \"%s\". You have %d errors of answering it.", c.term(), c.mistakes()));
        } else {
            StringJoiner join = new StringJoiner(", ");
            for (Card card : hard) {
                join.add(String.format("\"%s\"", card.term()));
            }
            log.out(String.format("The hardest card are %s. You have %d errors answering them.", join.toString(), hard.get(0).mistakes()));

        }
    }

    private void resetStats() {
        repo.resetMistakes();
        log.out("Card statistics has been reset.");
    }

    public void start(File importFile, File exportFile) {
        imp(importFile);
        while (true) {
            String cmd = log.in("Input the action (add, remove, import, export, ask, exit):");
            switch (cmd) {
                case "add":
                    add();
                    break;
                case "remove":
                    remove();
                    break;
                case "import":
                    imp();
                    break;
                case "export":
                    exp();
                    break;
                case "ask":
                    ask();
                    break;
                case "exit":
                    exit();
                    exp(exportFile);
                    return;
                case "log":
                    saveLog();
                    break;
                case "hardest card":
                    hardestCard();
                    break;
                case "reset stats":
                    resetStats();
                    break;
                default:
                    break;
            }
        }
    }

    public void main(String[] args) {
        File importFile = null;
        File exportFile = null;
        for (int i = 0; i < args.length; ++i) {
            if ("-import".equals(args[i]))
                importFile = new File(args[i + 1]);
            if ("-export".equals(args[i]))
                exportFile = new File(args[i + 1]);
        }
        new Main().start(importFile, exportFile);
    }
}
