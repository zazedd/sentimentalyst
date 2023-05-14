package ui.sentimentalyst;

import java.util.Properties;
import java.util.*;

import edu.stanford.nlp.util.CoreMap;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;

import edu.stanford.nlp.trees.Tree;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.fxmisc.richtext.InlineCssTextArea;

public class Sentiment {

    public static ArrayListPair execLangModel(String text) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,parse,sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        Annotation annotation = new Annotation(text);
        pipeline.annotate(annotation);

        String sentimentName = null;
        ArrayList<Integer> sentenceScores = new ArrayList<>();

        int i = 0;
        ArrayList<String> sentences = new ArrayList<>();
        for(CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class))
        {
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

    public static void updateSentimentLabel(ArrayList<Integer> res, Label labelsentiment) {
        double median = 0;

        for (int sentence : res) median += (double) sentence;

        median /= res.size();
        int score = (int) Math.round(median);

        System.out.println(median);

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
                labelsentiment.setTextFill(Color.valueOf("#ffffff"));
                labelsentiment.setText("Neutral");
            }
            case 3 -> {
                System.out.println("Overall sentiment: Positive");
                labelsentiment.setTextFill(Color.valueOf("#6fe069"));
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

    public static void updateSentimentTextArea(ArrayListPair res, InlineCssTextArea sentarea, String text, boolean isEnter) {
        String sentence, line;
        int score, current, next;
        for (int i = 0; i < res.a.size(); i++) {
            sentence = (isEnter) ? res.b.get(i).substring(0, res.b.get(i).length() - 1) : res.b.get(i);
            System.out.println(sentence);
            current = text.indexOf(sentence);
            while (current >= 0) {
                next = current + sentence.length();
                score = res.a.get(i);
                line = text.substring(current, next);
                switch (score) {
                    case 0 -> {
                        System.out.println("Changing line: " + line + " to red");
                        sentarea.setStyle(current, next, "-rtfx-background-color: #d61327");
                    }
                    case 1 -> {
                        System.out.println("Changing line: " + line + " to light red");
                        sentarea.setStyle(current, next, "-rtfx-background-color: #e37b7c");
                    }
                    case 2 -> {
                        System.out.println("Changing line: " + line + " to transparent");
                        sentarea.setStyle(current, next, "-rtfx-background-color: #a19f9f; -fx-fill: black");
                    }
                    case 3 -> {
                        System.out.println("Changing line: " + line + " to light green");
                        sentarea.setStyle(current, next, "-rtfx-background-color: #6fe069; -fx-fill: black");
                    }
                    case 4 -> {
                        System.out.println("Changing line: " + line + " to green");
                        sentarea.setStyle(current, next, "-rtfx-background-color: #48f542 -fx-fill: black");
                    }
                    default -> {
                        sentarea.setStyle(current, next, "-rtfx-background-color: transparent");
                    }
                }
                current = text.indexOf(sentence, next);
            }
        }
    }

    public static void updateSentiment(InlineCssTextArea sentarea, Label labelsentiment, boolean isEnter) {
        String text = sentarea.getText();;
        if (text == null || text.length() == 0)
            return;

        String newText = text.replaceAll("\n", ". ");
        ArrayListPair res = execLangModel(newText);
        updateSentimentLabel(res.a, labelsentiment);
        updateSentimentTextArea(res, sentarea, text, isEnter);
    }

    public static void updateSentimentTyping(Label labelsentiment) {
        labelsentiment.setTextFill(Color.valueOf("#ffffff"));
        labelsentiment.setText("?");
        return;
    }
}

class ArrayListPair {
    ArrayList<Integer> a;
    ArrayList<String> b;

    public ArrayListPair(ArrayList<Integer> a, ArrayList<String> b) {
        this.a = a;
        this.b = b;
    }
}