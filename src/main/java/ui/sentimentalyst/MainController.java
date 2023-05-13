package ui.sentimentalyst;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import org.fxmisc.richtext.InlineCssTextArea;

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
        String result = switch (event.getCode()) {
            case ENTER, PERIOD, EXCLAMATION_MARK -> Sentiment.getSentimentResult(text, labelsentiment).sentimentScore;
            default -> "?";
        };

        if (result.equals("?"))
            labelsentiment.setTextFill(Color.valueOf("#ffffff"));

        labelsentiment.setText(result);
    }
}