module ui.sentimentalyst {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.fxmisc.richtext;
    requires stanford.corenlp;

    opens ui.sentimentalyst to javafx.fxml;
    exports ui.sentimentalyst;
}