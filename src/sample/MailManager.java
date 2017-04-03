package sample;

import javax.mail.*;
import java.util.Properties;

/**
 * Created by 23878410v on 08/03/17.
 */
public class MailManager {
    private Session emailSession;
    private Store store;
    private String user;
    private String pass;


    public void main(String pop_host, int pop_port, String smtp_host, int smtp_port, boolean tls, String user, String pass) throws NoSuchProviderException, MessagingException {
        this.user = user;
        this.pass = pass;
        Properties properties = new Properties();
        properties.put("mail.pop3.host", pop_host);
        properties.put("mail.pop3.port", pop_port);

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.host", smtp_host);
        properties.put("mail.smtp.port", smtp_port);

        properties.put("mail.pop3.starttls.enable", tls ? "true" : "false");
        properties.put("mail.smtp.starttls.enable", tls ? "true" : "false");
        emailSession = Session.getDefaultInstance(properties,
                new javax.mail.Authenticator(){
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                user, pass);// Specify the Username and the PassWord
                    }
                });
        store = emailSession.getStore("pop3s");
        store.connect(pop_host, user, pass);

    }

    public Folder[] getFolders() throws MessagingException{
        return store.getDefaultFolder().list("*");
    }

    public Folder getFolder(int i) throws MessagingException {
        Folder[] folders = getFolders();
        return folders[i];
    }

    public Session getEmailSession() {
        return emailSession;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }
}
