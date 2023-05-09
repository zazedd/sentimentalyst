module ui.sentimentalyst {
    requires javafx.controls;
    requires javafx.fxml;
    requires stanford.corenlp;


    opens ui.sentimentalyst to javafx.fxml;
    exports ui.sentimentalyst;
}