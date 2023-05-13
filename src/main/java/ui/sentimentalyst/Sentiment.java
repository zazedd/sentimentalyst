package ui.sentimentalyst;

import java.io.*;
import java.util.Properties;
import java.util.*;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;

public class Sentiment {

    public static String execLangModel(String text) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,parse,sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Annotation annotation = pipeline.process(text);

        final String[] sentimentName = {null};

        annotation.get(CoreAnnotations.SentencesAnnotation.class).forEach(sentence -> {
            var tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            var sentimentInt = RNNCoreAnnotations.getPredictedClass(tree);
            sentimentName[0] = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            System.out.println(sentimentName[0] + "\t" + sentimentInt + "\t" + sentence);
        });

        return sentimentName[0];
    }

}
