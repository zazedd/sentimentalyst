package ui.sentimentalyst;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.fxmisc.richtext.InlineCssTextArea;

import java.util.ArrayList;
import java.util.Properties;

public class POS {

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
        String line;
        int current, next;
        for (int i = 0; i < res.a.size(); i++) {
            String token = res.a.get(i);
            if (token.equals(".") || token.equals("!") || token.equals(",") || token.equals("?")) continue;

            current = text.indexOf(token);
            while (current >= 0) {
                next = current + token.length();
                line = text.substring(current, next);

                switch (res.b.get(i)) {
                    case 'N' -> {
                        System.out.println("Changing line: " + line + " to red");
                        posarea.setStyle(current, next, "-rtfx-background-color: #d61327");
                    }
                    case 'V' -> {
                        System.out.println("Changing line: " + line + " to light red");
                        posarea.setStyle(current, next, "-rtfx-background-color: #e37b7c");
                    }
                    case 'I', 'C' -> {
                        System.out.println("Changing line: " + line + " to transparent");
                        posarea.setStyle(current, next, "-rtfx-background-color: #a19f9f; -fx-fill: black");
                    }
                    case 'J' -> {
                        System.out.println("Changing line: " + line + " to light green");
                        posarea.setStyle(current, next, "-rtfx-background-color: #6fe069; -fx-fill: black");
                    }
                    case 'R' -> {
                        System.out.println("Changing line: " + line + " to green");
                        posarea.setStyle(current, next, "-rtfx-background-color: #48f542 -fx-fill: black");
                    }
                    default -> {
                        posarea.setStyle(current, next, "-rtfx-background-color: transparent");
                    }
                }
                current = text.indexOf(token, next);
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
