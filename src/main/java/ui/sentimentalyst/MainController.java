package ui.sentimentalyst;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import org.fxmisc.richtext.InlineCssTextArea;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;

import static ui.sentimentalyst.POS.*;
import static ui.sentimentalyst.Sentiment.*;

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
    Label prostat;

    @FXML
    Label detstat;

    @FXML
    MFXProgressSpinner progress;

    @FXML
    MFXProgressSpinner progress2;

    @FXML
    public void sentimentHandler (KeyEvent event) {
        switch (event.getCode()) {
            case ENTER -> updateSentiment(sentarea, labelsentiment, progress, true);
            case PERIOD, EXCLAMATION_MARK -> updateSentiment(sentarea, labelsentiment, progress, false);
            default -> updateSentimentTyping(sentarea, labelsentiment, progress);
        }
    }

    public void posHandler (KeyEvent event) {
        switch (event.getCode()) {
            case ENTER, PERIOD, EXCLAMATION_MARK -> updatePOS(posarea, wordstat, nounstat, verbstat, conjstat, adjstat, advstat);
            default -> updatePOSTyping(sentarea);
        }
    }
}