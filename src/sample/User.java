package sample;

/**
 * Created by 23878410v on 08/03/17.
 */
public class User {
    private String email;
    private String password;
    private String pop_host;
    private int pop_port;
    private String smtp_host;
    private int smtp_port;
    private boolean tls;

    public User(String email, String password, String pop_host, int pop_port, String smtp_host, int smtp_port, boolean tls) {
        this.email = email;
        this.password = password;
        this.pop_host = pop_host;
        this.pop_port = pop_port;
        this.smtp_host = smtp_host;
        this.smtp_port = smtp_port;
        this.tls = tls;
    }

    public String getPop_host() {
        return pop_host;
    }

    public int getPop_port() {
        return pop_port;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getSmtp_host() {
        return smtp_host;
    }

    public int getSmtp_port() {
        return smtp_port;
    }

    public boolean isTls() {
        return tls;
    }
}
