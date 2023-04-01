package eu.wuttke.comdirect.util;

public class ComdirectException extends Exception {

    private final int responseStatus;
    private final String details;

    public ComdirectException(String message, int responseStatus, String details) {
        super(message);
        this.responseStatus = responseStatus;
        this.details = details;
    }

    public int getResponseStatus() {
        return responseStatus;
    }

    public String getDetails() {
        return details;
    }

}
