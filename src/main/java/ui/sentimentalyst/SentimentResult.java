package ui.sentimentalyst;

import java.util.ArrayList;

public class SentimentResult {

    String sentimentScore;

    ArrayList<Integer> sentenceScores;

    public SentimentResult(String sentimentScore, ArrayList<Integer> sentenceScores) {
        this.sentimentScore = sentimentScore;
        this.sentenceScores = sentenceScores;
    }
}