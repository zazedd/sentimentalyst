package ui.sentimentalyst;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import javafx.scene.control.Label;
import org.fxmisc.richtext.InlineCssTextArea;

import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class POS {

    //TODO Pronouns
    static Properties props;
    static StanfordCoreNLP pipeline;

    public static void initializePOSPipeline() {
        props = new Properties();
        props.setProperty("annotators", "tokenize,pos");
        pipeline = new StanfordCoreNLP(props);
    }

    public static ArrayListPair<String, Character> execPOSModel(String text) {
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
        for (int i = 0; i < res.a.size(); i++) {
            String token = res.a.get(i);
            if (token.equals(".") || token.equals("!") || token.equals(",") || token.equals("?")) continue;

            String regex = "\\b" + Pattern.quote(token) + "\\b";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);

            while (matcher.find()) {
                int current = matcher.start();
                int next = matcher.end();

                switch (res.b.get(i)) {
                    case 'N' -> {
                        posarea.setStyle(current, next, "-rtfx-background-color: #25d9fe; -fx-fill: black");
                    }
                    case 'V' -> {
                        posarea.setStyle(current, next, "-rtfx-background-color: #f5410e");
                    }
                    case 'I', 'C' -> {
                        posarea.setStyle(current, next, "-rtfx-background-color: #ca0ef5");
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
