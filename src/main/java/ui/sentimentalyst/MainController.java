package ui.sentimentalyst;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
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

}