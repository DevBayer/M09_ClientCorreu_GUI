package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Callback;

import javax.mail.Folder;
import javax.mail.MessagingException;
import java.util.Optional;

public class Controller {
    private User user;
    private MailManager manager;

    @FXML
    ListView lvFolders;

    @FXML
    TableView twMessages;

    @FXML
    Text txtToolbar;

    @FXML
    ProgressBar progressbar;

    @FXML
    public void initialize() throws MessagingException{
        System.out.println("initialized Controller");
        dialogLogin();
        manager = new MailManager();
        manager.main("pop.gmail.com", user.getEmail(), user.getPassword());
        init();
        initTable();
        setTableMessages(0);
    }

    @FXML
    private void setTableMessages(int i) throws MessagingException, IllegalStateException {
        final ObservableList<Message> data = FXCollections.observableArrayList();
        // In real life this task would do something useful and return
        // some meaningful result:
        //ProgressForm pForm = new ProgressForm();
        txtToolbar.setText("Obteniendo mensajes...");
        //pForm.getDialogStage().show();
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws InterruptedException {
                    try {
                    Folder folder = manager.getFolder(i);
                    folder.open(Folder.READ_ONLY);
                        int count = 0;
                    for (javax.mail.Message m : folder.getMessages()) {
                        Message message = new Message();
                        message.setFrom(m.getFrom()[0].toString());
                        message.setSubject(m.getSubject());
                        message.setDate(m.getSentDate());
                        message.setContent(m.getContent());
                        message.setContenttype(m.getContentType());
                        data.add(message);
                        updateProgress(10, 10);
                        twMessages.getItems().add(message);
                        twMessages.refresh();
                        count++;
                        txtToolbar.setText("Recibiendo: "+count+"/"+folder.getMessages().length);
                        progressbar.setProgress((count*1)/folder.getMessages().length);
                    }
                    folder.close(true);
                        /*
                        Platform.runLater(() -> {
                            pForm.getDialogStage().close();
                        });*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                return null ;
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    @FXML
    public void init() throws MessagingException{
        Folder[] folders = manager.getFolders();
        ObservableList<String> items = FXCollections.observableArrayList ();
        for (int i = 0; i < folders.length; i++) {
            Folder x = folders[i];
            x.open(Folder.READ_ONLY);
            items.add(x.getName()+" ("+x.getMessageCount()+")");
            x.close(true);
        }
        lvFolders.setItems(items);
        lvFolders.refresh();
    }

    @FXML
    public void initTable(){
        TableColumn idCol = (TableColumn) twMessages.getColumns().get(0);
        idCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Message, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Message, String> p) {
                return new SimpleStringProperty(""+p.getValue().getSubject());
            }
        });
        TableColumn idCol1 = (TableColumn) twMessages.getColumns().get(1);
        idCol1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Message, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Message, String> p) {
                return new SimpleStringProperty(""+p.getValue().getFrom());
            }
        });
        TableColumn idCol2 = (TableColumn) twMessages.getColumns().get(2);
        idCol2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Message, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Message, String> p) {
                return new SimpleStringProperty(""+p.getValue().getDate());
            }
        });


        twMessages.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.isPrimaryButtonDown() && event.getClickCount() == 2){
                    Message message = (Message) twMessages.getSelectionModel().getSelectedItem();
                    if(message != null) {
                        MessageForm fMessage = new MessageForm(message);
                        fMessage.getDialogStage().show();
                    }
                }
            }
        });

    }

    private void dialogLogin(){
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Iniciar sesión");
        dialog.setHeaderText("Enter a e-mail account.");
        dialog.setResizable(true);

        Label label1 = new Label("E-mail: ");
        Label label2 = new Label("Password: ");
        TextField text1 = new TextField();
        PasswordField text2 = new PasswordField();

        GridPane grid = new GridPane();
        grid.add(label1, 1, 1);
        grid.add(text1, 2, 1);
        grid.add(label2, 1, 2);
        grid.add(text2, 2, 2);
        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        dialog.setResultConverter(new Callback<ButtonType, User>() {
            @Override
            public User call(ButtonType b) {

                if (b == buttonTypeOk) {

                    return new User(text1.getText(), text2.getText());
                }

                return null;
            }
        });

        Optional<User> result = dialog.showAndWait();

        if (result.isPresent()) {
            user = result.get();
        }else{
            System.exit(0);
        }
    }

}
