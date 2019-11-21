package parser;

import java.util.Locale;

public class ResourceBundleForLibrary {

    public static java.util.ResourceBundle RB;

    private static String language;

    public static java.util.ResourceBundle getRB(String lang) {
        if (RB == null) {
            resourceBundle(lang);
        }
        if (!language.equals(lang)) {
            resourceBundle(lang);
        }
        return RB;
    }

    private static java.util.ResourceBundle resourceBundle(String lang) {
        language = lang;
        RB = java.util.ResourceBundle.getBundle("messages/localization", new Locale(lang));
        return RB;
    }
}
