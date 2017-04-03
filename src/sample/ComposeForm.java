package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by 23878410v on 15/03/17.
 */
public class ComposeForm {

        private final Stage dialogStage;
        private final Text txtDestination = new Text();
        private final TextField fieldDestination = new TextField();
        private final Text txtSubject = new Text();
        private final TextField fieldSubject = new TextField();
        private final TextArea fieldBody = new TextArea();
        private final Button sendButton = new Button();
        private final MailManager manager;
        public ComposeForm(MailManager man) {
            this.manager = man;
            dialogStage = new Stage();
            dialogStage.initStyle(StageStyle.UTILITY);
            dialogStage.setResizable(false);
            dialogStage.initModality(Modality.NONE);
            dialogStage.setTitle("Compose Message");
            txtDestination.setText("TO: ");
            txtSubject.setText("Subject: ");

            sendButton.setText("Send E-Mail");

            sendButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        MimeMessage message = new MimeMessage(manager.getEmailSession());
                        message.setFrom(new InternetAddress(manager.getUser()));
                        message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(fieldDestination.getText()));
                        message.setSubject(fieldSubject.getText());
                        message.setContent(fieldBody.getText(), "text/html");
                        Transport.send(message);
                        dialogStage.close();
                    }catch(AddressException e){
                        e.printStackTrace();
                    }catch(MessagingException e){
                        e.printStackTrace();
                    }
                }
            });

            final HBox hb = new HBox();
            final VBox vb = new VBox();
            hb.setSpacing(5);
            hb.setAlignment(Pos.TOP_LEFT);

            vb.getChildren().add(txtDestination);
            vb.getChildren().add(fieldDestination);
            vb.getChildren().add(txtSubject);
            vb.getChildren().add(fieldSubject);
            vb.getChildren().add(fieldBody);
            vb.getChildren().add(sendButton);


            hb.getChildren().add(vb);

            Scene scene = new Scene(hb);
            dialogStage.setScene(scene);
        }

    public Stage getDialogStage() {
        return dialogStage;
    }
}
