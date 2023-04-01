package eu.wuttke.comdirect.login;

public enum ChallengeType {

    PHOTOTAN,
    SMSTAN,
    PUSHTAN;

    public static ChallengeType decodeChallengeType(String type) {
        if (type.equals("P_TAN"))
            return PHOTOTAN;
        else if (type.equals("M_TAN"))
            return SMSTAN;
        else if (type.equals("P_TAN_PUSH"))
            return PUSHTAN;
        else
            throw new IllegalArgumentException("unknown challenge type: " + type);
    }
}
