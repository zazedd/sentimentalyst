module ui.sentimentalyst {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.fxmisc.richtext;
    requires stanford.corenlp;
    requires de.jensd.fx.glyphs.commons;
    requires de.jensd.fx.glyphs.fontawesome;

    opens ui.sentimentalyst to javafx.fxml;
    exports ui.sentimentalyst;
}