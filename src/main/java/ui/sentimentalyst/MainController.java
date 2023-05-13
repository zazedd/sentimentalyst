package ui.sentimentalyst;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import org.fxmisc.richtext.InlineCssTextArea;

import static ui.sentimentalyst.Sentiment.updateSentiment;

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
            case ENTER, PERIOD, EXCLAMATION_MARK -> updateSentiment(sentarea, labelsentiment, false);
            default -> updateSentiment(sentarea, labelsentiment, true);
        }
    }
}