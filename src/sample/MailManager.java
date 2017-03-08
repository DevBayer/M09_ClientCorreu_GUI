package sample;

import javax.mail.*;
import java.util.Properties;

/**
 * Created by 23878410v on 08/03/17.
 */
public class MailManager {
    private Session emailSession;
    private Store store;


    public void main(String host, String user, String pass) throws NoSuchProviderException, MessagingException {
        Properties properties = new Properties();
        properties.put("mail.pop3.host", host);
        properties.put("mail.pop3.port", "995");
        properties.put("mail.pop3.starttls.enable", "true");
        emailSession = Session.getDefaultInstance(properties);
        store = emailSession.getStore("pop3s");
        store.connect(host, user, pass);

    }

    public Folder[] getFolders() throws MessagingException{
        return store.getPersonalNamespaces();
    }

    public Folder getFolder(int i) throws MessagingException {
        Folder[] folders = getFolders();
        return folders[i];
    }

}
