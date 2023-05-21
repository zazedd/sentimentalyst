package ui.sentimentalyst;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
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
    public void initialize() {
        sentpane.widthProperty().addListener((obs, oldWidth, newWidth) -> resizeArea(sentpane, sentarea));
        sentpane.heightProperty().addListener((obs, oldHeight, newHeight) -> resizeArea(sentpane, sentarea));

        pospane.widthProperty().addListener((obs, oldWidth, newWidth) -> resizeArea(pospane, posarea));
        pospane.heightProperty().addListener((obs, oldHeight, newHeight) -> resizeArea(pospane, posarea));
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
            case ENTER, PERIOD, EXCLAMATION_MARK -> updateSentiment(sentarea, labelsentiment, progress);
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