module chat {
    requires javafx.fxml;
    requires javafx.controls;
    requires org.apache.commons.lang3;
    requires org.json;

    opens org.francescoborri.chat.client.ui to javafx.fxml, javafx.graphics;
    opens org.francescoborri.chat.client to javafx.fxml, javafx.graphics;
}