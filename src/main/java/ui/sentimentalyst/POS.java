package ui.sentimentalyst;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
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
        String tag;

        int i = 0;
        ArrayList<String> tokens = new ArrayList<>();
        ArrayList<Character> tags = new ArrayList<>();
        for(CoreLabel tok : document.tokens()) {
            if (tok.toString().equals(".")) continue;
            System.out.println(String.format("%s\t%s", tok.word(), tok.tag()));

            tokens.add(tok.word());
            tag = tok.tag();
            if (tag.charAt(0) == 'P' && tag.charAt(1) != 'R') continue; // there are more things starting with P but we only want PR's (pronouns)

            tags.add(tag.charAt(0));
            i++;
        }

        return new ArrayListPair<>(tokens, tags);
    }

    public static void updatePOSStatistics(ArrayListPair<String, Character> res, Label wordstat, Label nounstat, Label verbstat, Label conjstat, Label adjstat, Label advstat, Label prostat, Label detstat) {
        int wordCount = 0, nounCount = 0, verbCount = 0, conjCount = 0, adjCount = 0, advCount = 0, proCount = 0, detCount = 0;
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
                case 'P' -> {
                    proCount++;
                }
                case 'D' -> {
                    detCount++;
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
        prostat.setText(Integer.toString(proCount));
        detstat.setText(Integer.toString(detCount));
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
                        posarea.setStyle(current, next, "-rtfx-background-color: #daF7a6; -fx-fill: black;");
                    }
                    case 'J' -> {
                        posarea.setStyle(current, next, "-rtfx-background-color: #ffe000; -fx-fill: black");
                    }
                    case 'R' -> {
                        posarea.setStyle(current, next, "-rtfx-background-color: #00ff0e; -fx-fill: black");
                    }
                    case 'P' -> {
                        posarea.setStyle(current, next, "-rtfx-background-color: #ca0ef5;");
                    }
                    case 'D' -> {
                        posarea.setStyle(current, next, "-rtfx-background-color: #ff0000;");
                    }
                    default -> {
                        posarea.setStyle(current, next, "-rtfx-background-color: transparent");
                    }
                }
            }
        }
    }


    public static void updatePOS(InlineCssTextArea posarea, Label wordstat, Label nounstat, Label verbstat, Label conjstat, Label adjstat, Label advstat, Label prostat, Label detstat, MFXProgressSpinner progress) {
        String text = posarea.getText();;
        if (text == null || text.length() == 0)
            return;

        progress.setStyle("-fx-opacity: 0");
        String newText = text.replaceAll("\n", ". ");
        ArrayListPair<String, Character> res = execPOSModel(newText);
        updatePOSStatistics(res, wordstat, nounstat, verbstat, conjstat, adjstat, advstat, prostat, detstat);
        updatePOStextArea(res, text, posarea);
    }

    public static void updatePOSTyping(InlineCssTextArea sentarea, MFXProgressSpinner progress) {
        sentarea.setStyle(0, sentarea.getText().length(), "-rtfx-background-color: transparent; -fx-fill: white");
        progress.setStyle("-fx-opacity: 1");
    }
}
