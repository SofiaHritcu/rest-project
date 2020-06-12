package GUI;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MessageAlert {
    static void showMessage(Stage owner, Alert.AlertType type, String header, String text) {
        Alert message = new Alert(type);
        message.setHeaderText(header);
        message.setContentText(text);
        message.initOwner(owner);
        message.initStyle(StageStyle.TRANSPARENT);
        message.showAndWait();
    }

    static void showErrorMessage(Stage owner, String text) {
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.initOwner(owner);
        message.setTitle("Mesaj eroare");
        message.setContentText(text);
        message.initStyle(StageStyle.TRANSPARENT);
        message.showAndWait();
    }
}