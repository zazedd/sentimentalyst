module ui.sentimentalyst {
    requires javafx.controls;
    requires javafx.fxml;

    opens ui.sentimentalyst to javafx.fxml;
    exports ui.sentimentalyst;
}