package eu.wuttke.comdirect.login;

import java.util.Date;

public class Tokens {

    private String accessToken;
    private String refreshToken;
    private Date expiry;

    public Tokens(String accessToken, String refreshToken, Date expiry) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiry = expiry;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Date getExpiry() {
        return expiry;
    }
}
