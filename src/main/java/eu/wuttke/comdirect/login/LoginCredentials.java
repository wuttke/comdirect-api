package eu.wuttke.comdirect.login;

public class LoginCredentials {

    /**
     * Comdirect REST API Client ID
     */
    private String clientId;

    /**
     * Comdirest REST API Client Secret
     */
    private String clientSecret;

    /**
     * User/login name ("Benutzername/Zugangsnummer")
     */
    private String userName;

    /**
     * Password ("Passwort/PIN")
     */
    private String password;

    public LoginCredentials(String clientId, String clientSecret, String userName, String password) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.userName = userName;
        this.password = password;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

}
