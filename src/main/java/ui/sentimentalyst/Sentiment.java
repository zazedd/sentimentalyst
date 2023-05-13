package ui.sentimentalyst;

import java.io.*;
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

public class Sentiment {

    public static ArrayList<Integer> execLangModel(String text) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,parse,sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        String newStr = text.replaceAll("\n", ". ");

        Annotation annotation = new Annotation(newStr);
        pipeline.annotate(annotation);

        String sentimentName = null;
        ArrayList<Integer> sentenceScores = new ArrayList<>();

        int i = 0;
        for(CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class))
        {
            if (sentence.toString().equals(".")) continue;

            Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            sentenceScores.add(RNNCoreAnnotations.getPredictedClass(tree));
            sentimentName = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            System.out.println(sentimentName + "\t" + sentenceScores.get(i) + "\t" + sentence);

            i++;
        }

        return sentenceScores;
    }

    public static SentimentResult getSentimentResult(String text, Label labelsentiment) {
        if (text == null || text.length() == 0)
            return new SentimentResult("?", new ArrayList<>());

        ArrayList<Integer> res = execLangModel(text);
        double median = 0;

        for (int sentence : res) median += (double) sentence;

        median /= res.size();
        int score = (int) Math.round(median);

        System.out.println(median);

        switch (score) {
            case 0 -> {
                System.out.println("Overall sentiment: Very Negative");
                labelsentiment.setTextFill(Color.valueOf("#d61327"));
                return new SentimentResult("Very Negative", res);
            }
            case 1 -> {
                System.out.println("Overall sentiment: Negative");
                labelsentiment.setTextFill(Color.valueOf("#e37b7c"));
                return new SentimentResult("Negative", res);
            }
            case 2 -> {
                System.out.println("Overall sentiment: Neutral");
                labelsentiment.setTextFill(Color.valueOf("#ffffff"));
                return new SentimentResult("Neutral", res);
            }
            case 3 -> {
                System.out.println("Overall sentiment: Positive");
                labelsentiment.setTextFill(Color.valueOf("#6fe069"));
                return new SentimentResult("Positive", res);
            }
            case 4 -> {
                System.out.println("Overall sentiment: Very Positive");
                labelsentiment.setTextFill(Color.valueOf("#48f542"));
                return new SentimentResult("Very Positive", res);
            }
            default -> {
                labelsentiment.setTextFill(Color.valueOf("#ffffff"));
                return new SentimentResult("?", res);
            }
        }
    }
}
