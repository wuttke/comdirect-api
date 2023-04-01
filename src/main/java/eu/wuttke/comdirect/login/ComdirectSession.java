package eu.wuttke.comdirect.login;

public class ComdirectSession {
    
    /**
     * OAuth tokens (access, refresh token); with expiry
     */
    private Tokens tokens;

    /**
     * Session ID
     */
    private String sessionId;

    public ComdirectSession(Tokens tokens, String sessionId) {
        this.tokens = tokens;
        this.sessionId = sessionId;
    }

    public Tokens getTokens() {
        return tokens;
    }

    public String getSessionId() {
        return sessionId;
    }

}
