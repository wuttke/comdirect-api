package eu.wuttke.comdirect.login;

public class InitiateLoginResult {

    /**
     * Type of the challenge (e.g., push TAN, photo TAN)
     */
    private ChallengeType challengeType;

    /**
     * ID of the challenge (needs to be passed with the
     * actual TAN)
     */
    private String challengeId;

    /**
     * Parameter for the challenge, e.g. base64-encoded PNG for photo TAN
     */
    private String challenge;

    /**
     * OAuth tokens (access, refresh token); with expiry
     */
    private Tokens tokens;

    /**
     * Session ID
     */
    private String sessionId;

    public InitiateLoginResult(ChallengeType challengeType, String challengeId, String challenge, Tokens tokens, String sessionId) {
        this.challengeType = challengeType;
        this.challengeId = challengeId;
        this.challenge = challenge;
        this.tokens = tokens;
        this.sessionId = sessionId;
    }

    public ChallengeType getChallengeType() {
        return challengeType;
    }

    public String getChallengeId() {
        return challengeId;
    }

    public String getChallenge() {
        return challenge;
    }

    public Tokens getTokens() {
        return tokens;
    }

    public String getSessionId() {
        return sessionId;
    }

}
