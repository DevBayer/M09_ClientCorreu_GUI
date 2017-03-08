package sample;

/**
 * Created by 23878410v on 08/03/17.
 */
public class Message {
    private String subject;
    private String from;
    private String body;
    private String date;

    public Message() {
    }

    public Message(String subject, String from, String body, String date) {
        this.subject = subject;
        this.from = from;
        this.body = body;
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
