package ui.sentimentalyst;

import java.text.DecimalFormat;
import java.util.Properties;
import java.util.*;

import edu.stanford.nlp.util.CoreMap;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;

import edu.stanford.nlp.trees.Tree;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import org.fxmisc.richtext.InlineCssTextArea;

public class Sentiment {

    static Properties props;
    static StanfordCoreNLP pipeline;

    public static void initializeSentPipeline() {
        props = new Properties();
        props.setProperty("annotators", "tokenize,parse,sentiment");
        pipeline = new StanfordCoreNLP(props);
    }

    public static ArrayListPair<Integer, String> execLangModel(String text) {
        Annotation annotation = new Annotation(text);
        pipeline.annotate(annotation);

        String sentimentName = null;
        ArrayList<Integer> sentenceScores = new ArrayList<>();

        int i = 0;
        ArrayList<String> sentences = new ArrayList<>();
        for(CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            if (sentence.toString().equals(".")) continue;

            Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            sentenceScores.add(RNNCoreAnnotations.getPredictedClass(tree));
            sentimentName = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            System.out.println(sentimentName + "\t" + sentenceScores.get(i) + "\t" + sentence);
            sentences.add(sentence.toString());

            i++;
        }

        return new ArrayListPair(sentenceScores, sentences);
    }

    public static void updateSentimentLabel(ArrayList<Integer> res, Label labelsentiment, MFXProgressBar positivebar, MFXProgressBar negativebar, Tooltip positivettip, Tooltip negativettip) {
        int count = 0;
        double median = 0, positive = 0, negative = 0;
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");

        for (int sentence : res) {
            median += (double) sentence;

            switch (sentence - 2) {
                case -2, -1 -> {
                    count++;
                    negative += (double) sentence - 2;
                }
                case 1, 2 -> {
                    count++;
                    positive += (double) sentence - 2;
                }
            }
        }

        median /= res.size();
        int score = (int) Math.round(median);

        positive /= (count * 2);
        positivebar.setProgress(positive);

        String formattedPositive = decimalFormat.format(positive);
        positivettip.setText("Positive Score: " + formattedPositive + " / 1");

        negative /= (count * 2);
        negativebar.setProgress(Math.abs(negative));

        String formattedNegative = decimalFormat.format(negative);
        negativettip.setText("Negative Score: " + formattedNegative + " / -1");

        System.out.println(median);

        labelsentiment.setStyle("-fx-opacity: 1");
        switch (score) {
            case 0 -> {
                System.out.println("Overall sentiment: Very Negative");
                labelsentiment.setTextFill(Color.valueOf("#d61327"));
                labelsentiment.setText("Very Negative");
            }
            case 1 -> {
                System.out.println("Overall sentiment: Negative");
                labelsentiment.setTextFill(Color.valueOf("#e37b7c"));
                labelsentiment.setText("Negative");
            }
            case 2 -> {
                System.out.println("Overall sentiment: Neutral");
                labelsentiment.setTextFill(Color.valueOf("#a19f9f"));
                labelsentiment.setText("Neutral");
            }
            case 3 -> {
                System.out.println("Overall sentiment: Positive");
                labelsentiment.setTextFill(Color.valueOf("#a1ffa2"));
                labelsentiment.setText("Positive");
            }
            case 4 -> {
                System.out.println("Overall sentiment: Very Positive");
                labelsentiment.setTextFill(Color.valueOf("#48f542"));
                labelsentiment.setText("Very Positive");
            }
            default -> {
                labelsentiment.setTextFill(Color.valueOf("#ffffff"));
                labelsentiment.setText("?");
            }
        }
    }

    public static void updateSentimentTextArea(ArrayListPair<Integer, String> res, InlineCssTextArea sentarea, String text) {
        String sentence;
        int score, current, next;
        for (int i = 0; i < res.a.size(); i++) {
            sentence = res.b.get(i);
            if (sentence.charAt(sentence.length() - 1) == '.') sentence = sentence.substring(0, sentence.length() - 1);
            System.out.println(sentence);
            current = text.indexOf(sentence);
            while (current >= 0) {
                next = current + sentence.length();
                score = res.a.get(i);
                switch (score) {
                    case 0 -> {
                        sentarea.setStyle(current, next, "-rtfx-background-color: #d61327");
                    }
                    case 1 -> {
                        sentarea.setStyle(current, next, "-rtfx-background-color: #e37b7c");
                    }
                    case 2 -> {
                        sentarea.setStyle(current, next, "-rtfx-background-color: #a19f9f; -fx-fill: black");
                    }
                    case 3 -> {
                        sentarea.setStyle(current, next, "-rtfx-background-color: #a1ffa2; -fx-fill: black");
                    }
                    case 4 -> {
                        sentarea.setStyle(current, next, "-rtfx-background-color: #48f542; -fx-fill: black");
                    }
                    default -> {
                        sentarea.setStyle(current, next, "-rtfx-background-color: transparent");
                    }
                }
                current = text.indexOf(sentence, next);
            }
        }
    }

    public static void updateSentiment(String content, InlineCssTextArea sentarea, Label labelsentiment, MFXProgressSpinner progress, MFXProgressBar positivebar, MFXProgressBar negativebar, Tooltip positivettip, Tooltip negativettip) {
        String text = (content == null) ? sentarea.getText() : content;
        if (text == null || text.length() == 0 || text.equals("\n")) {
            return;
        }

        if (content != null) {
            sentarea.clear();
            sentarea.appendText(content);
        }
        progress.setStyle("-fx-opacity: 0");
        String newText = text.replaceAll("\n", ". ");
        ArrayListPair<Integer, String> res = execLangModel(newText);
        updateSentimentLabel(res.a, labelsentiment, positivebar, negativebar, positivettip, negativettip);
        updateSentimentTextArea(res, sentarea, text);
    }

    public static void updateSentimentTyping(InlineCssTextArea sentarea, Label labelsentiment, MFXProgressSpinner progress) {
        labelsentiment.setTextFill(Color.valueOf("#ffffff"));
        sentarea.setStyle(0, sentarea.getText().length(), "-rtfx-background-color: transparent; -fx-fill: white");
        labelsentiment.setText("?");
        labelsentiment.setStyle("-fx-opacity: 0");
        progress.setStyle("-fx-opacity: 1");
    }
}

