package ui.sentimentalyst;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import org.fxmisc.richtext.InlineCssTextArea;

import static ui.sentimentalyst.POS.updatePOS;
import static ui.sentimentalyst.Sentiment.updateSentiment;
import static ui.sentimentalyst.Sentiment.updateSentimentTyping;

public class MainController {

    @FXML
    InlineCssTextArea sentarea;

    @FXML
    InlineCssTextArea posarea;

    @FXML
    Label labelsentiment;

    @FXML
    Label wordstat;

    @FXML
    Label nounstat;

    @FXML
    Label verbstat;

    @FXML
    Label conjstat;

    @FXML
    Label adjstat;

    @FXML
    Label advstat;

    @FXML
    public void sentimentHandler (KeyEvent event) {
        switch (event.getCode()) {
            case ENTER -> updateSentiment(sentarea, labelsentiment, true);
            case PERIOD, EXCLAMATION_MARK -> updateSentiment(sentarea, labelsentiment, false);
            default -> updateSentimentTyping(labelsentiment);
        }
    }

    public void posHandler (KeyEvent event) {
        switch (event.getCode()) {
            case ENTER, PERIOD, EXCLAMATION_MARK -> updatePOS(posarea, wordstat, nounstat, verbstat, conjstat, adjstat, advstat);
            default -> {}
        }
    }
}