package sample;

/**
 * Created by 23878410v on 08/03/17.
 */
public class User {
    private String email;
    private String password;
    private String host;
    private int port;
    private boolean tls;

    public User(String email, String password, String host, int port, boolean tls) {
        this.email = email;
        this.password = password;
        this.host = host;
        this.port = port;
        this.tls = tls;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public boolean isTls() {
        return tls;
    }
}
