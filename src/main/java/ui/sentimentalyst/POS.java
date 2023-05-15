package ui.sentimentalyst;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import javafx.scene.control.Label;
import org.fxmisc.richtext.InlineCssTextArea;

import java.util.ArrayList;
import java.util.Properties;

public class POS {

    //TODO Pronouns

    public static ArrayListPair<String, Character> execPOSModel(String text) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,pos");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        CoreDocument document = pipeline.processToCoreDocument(text);

        int i = 0;
        ArrayList<String> tokens = new ArrayList<>();
        ArrayList<Character> tags = new ArrayList<>();
        for(CoreLabel tok : document.tokens()) {
            if (tok.toString().equals(".")) continue;
            System.out.println(String.format("%s\t%s", tok.word(), tok.tag()));

            tokens.add(tok.word());
            tags.add(tok.tag().charAt(0));
            i++;
        }

        return new ArrayListPair<>(tokens, tags);
    }

    public static void updatePOSStatistics(ArrayListPair<String, Character> res, Label wordstat, Label nounstat, Label verbstat, Label conjstat, Label adjstat, Label advstat) {
        int wordCount = 0, nounCount = 0, verbCount = 0, conjCount = 0, adjCount = 0, advCount = 0;
        for (int i = 0; i < res.a.size(); i++) {
            String token = res.a.get(i);
            if (token.equals(".") || token.equals("!") || token.equals(",") || token.equals("?")) continue;

            wordCount++;
            switch (res.b.get(i)) {
                case 'N' -> {
                    nounCount++;
                }
                case 'V' -> {
                    verbCount++;
                }
                case 'I', 'C' -> {
                    conjCount++;
                }
                case 'J' -> {
                    adjCount++;
                }
                case 'R' -> {
                    advCount++;
                }
                default -> {}
            }
        }

        wordstat.setText(Integer.toString(wordCount));
        nounstat.setText(Integer.toString(nounCount));
        verbstat.setText(Integer.toString(verbCount));
        conjstat.setText(Integer.toString(conjCount));
        adjstat.setText(Integer.toString(adjCount));
        advstat.setText(Integer.toString(advCount));
    }

    public static void updatePOStextArea(ArrayListPair<String, Character> res, String text, InlineCssTextArea posarea) {
        int current, next, searchFrom = 0;
        for (int i = 0; i < res.a.size(); i++) {
            String token = res.a.get(i);
            if (token.equals(".") || token.equals("!") || token.equals(",") || token.equals("?")) continue;

            current = text.indexOf(token, searchFrom);
            while (current >= 0) {
                next = current + token.length();

                switch (res.b.get(i)) {
                    case 'N' -> {
                        posarea.setStyle(current, next, "-rtfx-background-color: #25d9fe");
                    }
                    case 'V' -> {
                        posarea.setStyle(current, next, "-rtfx-background-color: #f5410e; -fx-fill: black");
                    }
                    case 'I', 'C' -> {
                        posarea.setStyle(current, next, "-rtfx-background-color: #ca0ef5; -fx-fill: black");
                    }
                    case 'J' -> {
                        posarea.setStyle(current, next, "-rtfx-background-color: #ffe000; -fx-fill: black");
                    }
                    case 'R' -> {
                        posarea.setStyle(current, next, "-rtfx-background-color: #00ff0e; -fx-fill: black");
                    }
                    default -> {
                        posarea.setStyle(current, next, "-rtfx-background-color: transparent");
                    }
                }

                searchFrom += token.length();
                System.out.println(searchFrom);
                current = text.indexOf(token, searchFrom);
            }

        }
    }

    public static void updatePOS(InlineCssTextArea posarea, Label wordstat, Label nounstat, Label verbstat, Label conjstat, Label adjstat, Label advstat) {
        String text = posarea.getText();;
        if (text == null || text.length() == 0)
            return;

        String newText = text.replaceAll("\n", ". ");
        ArrayListPair<String, Character> res = execPOSModel(newText);
        updatePOSStatistics(res, wordstat, nounstat, verbstat, conjstat, adjstat, advstat);
        updatePOStextArea(res, text, posarea);
    }

    public static void updatePOSTyping(InlineCssTextArea sentarea) {
        sentarea.setStyle(0, sentarea.getText().length(), "-rtfx-background-color: transparent; -fx-fill: white");
    }
}
