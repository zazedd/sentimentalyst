package ui.sentimentalyst;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import org.fxmisc.richtext.InlineCssTextArea;

import static ui.sentimentalyst.Sentiment.updateSentiment;
import static ui.sentimentalyst.Sentiment.updateSentimentTyping;

public class MainController {

    @FXML
    InlineCssTextArea sentarea;

    @FXML
    InlineCssTextArea langarea;

    @FXML
    Label labelsentiment;

    @FXML
    Label labellang;

    @FXML
    public void sentimentHandler (KeyEvent event) {
        String text = sentarea.getText();
        switch (event.getCode()) {
            case ENTER -> updateSentiment(sentarea, labelsentiment, true);
            case PERIOD, EXCLAMATION_MARK -> updateSentiment(sentarea, labelsentiment, false);
            default -> updateSentimentTyping(labelsentiment);
        }
    }
}