package sample;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by 23878410v on 15/03/17.
 */
public class MessageForm {

        private final Stage dialogStage;
        private final Text txtSubject = new Text();
        private final TextField fieldSubject = new TextField();
        private final TextArea fieldBody = new TextArea();
        public MessageForm(Message message) {
            dialogStage = new Stage();
            dialogStage.initStyle(StageStyle.UTILITY);
            dialogStage.setResizable(false);
            dialogStage.initModality(Modality.NONE);
            dialogStage.setTitle(message.getSubject() + " - "+message.getFrom());

            txtSubject.setText("Subject: ");
            fieldSubject.setText(message.getSubject());
            try {
                fieldBody.setText(message.getTextFromMessage());
            }catch(Exception e){
                e.printStackTrace();
            }

            final HBox hb = new HBox();
            final VBox vb = new VBox();
            hb.setSpacing(5);
            hb.setAlignment(Pos.TOP_LEFT);

            vb.getChildren().add(txtSubject);
            vb.getChildren().add(fieldSubject);
            vb.getChildren().add(fieldBody);

            hb.getChildren().add(vb);

            Scene scene = new Scene(hb);
            dialogStage.setScene(scene);
        }

    public Stage getDialogStage() {
        return dialogStage;
    }
}
