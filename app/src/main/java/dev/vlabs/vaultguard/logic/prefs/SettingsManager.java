package dev.vlabs.vaultguard.logic.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public final class SettingsManager {

    // Noms modifiés pour l'originalité
    private static final String STORAGE_ID = "vguard_user_configs";
    private static final String ID_USER_ALIAS = "alias_name";
    private static final String ID_LOCALE = "user_language";
    private static final String ID_VISUAL_MODE = "display_theme";

    private SettingsManager() {}

    /**
     * Sauvegarde les préférences utilisateur
     * @param isSync Si true, utilise commit() [synchrone], sinon apply() [asynchrone]
     */
    public static boolean persist(Context context, String alias, String lang, String theme, boolean isSync) {
        SharedPreferences sharedData = context.getSharedPreferences(STORAGE_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor writer = sharedData.edit()
                .putString(ID_USER_ALIAS, alias)
                .putString(ID_LOCALE, lang)
                .putString(ID_VISUAL_MODE, theme);

        if (isSync) {
            // commit() : Écrit immédiatement sur le disque et confirme le succès
            return writer.commit();
        } else {
            // apply() : Écrit en arrière-plan (non-bloquant), recommandé pour l'UI
            writer.apply();
            return true;
        }
    }

    public static UserConfig fetch(Context context) {
        SharedPreferences sharedData = context.getSharedPreferences(STORAGE_ID, Context.MODE_PRIVATE);

        String alias = sharedData.getString(ID_USER_ALIAS, "Anonyme");
        String lang = sharedData.getString(ID_LOCALE, "fr");
        String theme = sharedData.getString(ID_VISUAL_MODE, "light");

        return new UserConfig(alias, lang, theme);
    }

    public static void reset(Context context) {
        SharedPreferences sharedData = context.getSharedPreferences(STORAGE_ID, Context.MODE_PRIVATE);
        sharedData.edit().clear().apply();
    }

    // Conteneur de données (Remplace "Triple" pour plus de clarté)
    public static final class UserConfig {
        public final String alias;
        public final String language;
        public final String theme;

        public UserConfig(String alias, String language, String theme) {
            this.alias = alias;
            this.language = language;
            this.theme = theme;
        }
    }
}