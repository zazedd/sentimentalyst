package ui.sentimentalyst;

import io.github.palexdev.materialfx.beans.NumberRange;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import org.fxmisc.richtext.InlineCssTextArea;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;

import java.util.Arrays;

import static ui.sentimentalyst.POS.*;
import static ui.sentimentalyst.Sentiment.*;

public class MainController {

    @FXML
    AnchorPane mainpane;

    @FXML
    Pane sentpane, pospane;

    @FXML
    InlineCssTextArea sentarea, posarea;

    @FXML
    Label labelsentiment, wordstat, nounstat, verbstat, conjstat, adjstat, advstat, prostat, detstat;

    @FXML
    MFXProgressSpinner progress, progress2;

    @FXML
    MFXProgressBar positivebar, negativebar;

    Tooltip positivettip, negativettip;

    @FXML
    public void initialize() {
        sentpane.widthProperty().addListener((obs, oldWidth, newWidth) -> resizeArea(sentpane, sentarea));
        sentpane.heightProperty().addListener((obs, oldHeight, newHeight) -> resizeArea(sentpane, sentarea));

        pospane.widthProperty().addListener((obs, oldWidth, newWidth) -> resizeArea(pospane, posarea));
        pospane.heightProperty().addListener((obs, oldHeight, newHeight) -> resizeArea(pospane, posarea));

        positivebar.getRanges1().add(NumberRange.of(0.0, 0.40));
        positivebar.getRanges2().add(NumberRange.of(0.41, 0.80));
        positivebar.getRanges3().add(NumberRange.of(0.81, 2.0));
        positivettip = new Tooltip("Positive Sentiment");
        positivettip.setShowDelay(Duration.millis(300));
        positivebar.setTooltip(positivettip);

        negativebar.getRanges1().add(NumberRange.of(0.0, 0.40));
        negativebar.getRanges2().add(NumberRange.of(0.41, 0.80));
        negativebar.getRanges3().add(NumberRange.of(0.81, 2.0));
        negativettip = new Tooltip("Negative Sentiment");
        negativettip.setShowDelay(Duration.millis(300));
        negativebar.setTooltip(negativettip);
    }


    @FXML
    private void resizeArea(Pane pane, InlineCssTextArea area) {
        double containerWidth = pane.getWidth() - 82;
        double containerHeight = pane.getHeight() - 86;

        area.setPrefWidth(containerWidth);
        area.setPrefHeight(containerHeight);
    }

    @FXML
    public void sentimentHandler (KeyEvent event) {
        switch (event.getCode()) {
            case ENTER, PERIOD, EXCLAMATION_MARK -> updateSentiment(sentarea, labelsentiment, progress, positivebar, negativebar, positivettip, negativettip);
            default -> updateSentimentTyping(sentarea, labelsentiment, progress);
        }
    }

    @FXML
    public void posHandler (KeyEvent event) {
        switch (event.getCode()) {
            case ENTER, PERIOD, EXCLAMATION_MARK -> updatePOS(posarea, wordstat, nounstat, verbstat, conjstat, adjstat, advstat, prostat, detstat, progress2);
            default -> updatePOSTyping(posarea, progress2);
        }
    }
}