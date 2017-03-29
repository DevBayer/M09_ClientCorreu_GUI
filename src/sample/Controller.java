package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.mail.Folder;
import javax.mail.MessagingException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @FXML
    public void initialize() throws MessagingException{
        System.out.println("initialized Controller");
        dialogLogin();
        manager = new MailManager();
        manager.main(user.getHost(), user.getPort(), user.isTls(), user.getEmail(), user.getPassword());
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
        dialog.setTitle("Sign in");
        dialog.setHeaderText("Enter a e-mail account.");
        dialog.setResizable(true);

        ToggleGroup groupDefaults = new ToggleGroup();
        RadioButton rb1 = new RadioButton("GMail");
        rb1.setUserData("gmail");
        rb1.setToggleGroup(groupDefaults);
        RadioButton rb2 = new RadioButton("Hotmail/Microsoft");
        rb2.setUserData("hotmail");
        rb2.setToggleGroup(groupDefaults);
        RadioButton rb3 = new RadioButton("Custom");
        rb3.setUserData("custom");
        rb3.setToggleGroup(groupDefaults);
        HBox gridDefaults = HBoxBuilder.create().spacing(30.0).padding(new Insets(5, 5, 5, 5)).children(rb1, rb2, rb3).build();

        TitledPane tp = new TitledPane();
        tp.setVisible(false);
        tp.setText("Custom Settings");
        GridPane grid2 = new GridPane();

        Label label4 = new Label("POP3: ");
        grid2.add(label4, 1, 1);
        TextField textSMTP = new TextField();
        grid2.add(textSMTP, 1, 2);
        Label label5 = new Label("PORT: ");
        grid2.add(label5, 3, 1);
        TextField textPORT = new TextField();
        grid2.add(textPORT, 3, 2);
        CheckBox cb2 = new CheckBox("Start TLS");
        grid2.add(cb2, 1, 3);
        tp.setContent(grid2);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        Label label1 = new Label("E-mail: ");
        grid.add(label1, 1, 1);
        final TextField text1 = new TextField();
        text1.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(isValid(text1.getText())){
                    if(text1.getText().contains("@gmail.com")){
                        rb1.setSelected(true);
                        tp.setVisible(false);
                    }else if(text1.getText().contains("@hotmail.com")
                            || text1.getText().contains("@hotmail.es")
                            || text1.getText().contains("@microsoft.com")
                            || text1.getText().contains("@outlook.com")){
                        rb2.setSelected(true);
                        tp.setVisible(false);
                    }else{
                        rb3.setSelected(true);
                        tp.setVisible(true);
                    }
                }else{
                    rb1.setSelected(false);
                    rb2.setSelected(false);
                    rb3.setSelected(false);
                    tp.setVisible(false);
                }
            }
        });
        grid.add(text1, 2, 1);

        groupDefaults.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {

                if (groupDefaults.getSelectedToggle() != null) {
                    String str = groupDefaults.getSelectedToggle().getUserData().toString();
                    if(str.equals("custom")){
                        tp.setVisible(true);
                    }else{
                        tp.setVisible(false);
                    }

                }

            }
        });

        Label label2 = new Label("Password: ");
        grid.add(label2, 1, 2);
        PasswordField text2 = new PasswordField();
        grid.add(text2, 2, 2);


        grid.add(gridDefaults, 2, 3);
        grid.add(tp, 2, 4);


        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        dialog.setResultConverter(new Callback<ButtonType, User>() {
            @Override
            public User call(ButtonType b) {

                if (b == buttonTypeOk) {
                    if(rb1.isSelected()) {
                        return new User(text1.getText(), text2.getText(), "pop.gmail.com", 995, true);
                    }else if(rb2.isSelected()){
                        return new User(text1.getText(), text2.getText(), "pop-mail.outlook.com", 995, true);
                    }else if(rb3.isSelected()){
                        return new User(text1.getText(), text2.getText(), textSMTP.getText(), Integer.parseInt(textPORT.getText()), cb2.isSelected());
                    }
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

    private boolean isValid(String mail){
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(mail);
        return matcher.find();
    }

}
