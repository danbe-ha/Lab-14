package dev.vlabs.vaultguard.logic.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

public final class SecretVault {

    // Noms modifiés pour l'originalité
    private static final String VAULT_NAME = "vguard_encrypted_storage";
    private static final String ID_SECRET_TOKEN = "vault_access_token";

    private SecretVault() {}

    // Méthode privée pour configurer le moteur de chiffrement
    private static SharedPreferences getSecureContainer(Context context) throws Exception {
        // Génère ou récupère la clé maîtresse stockée dans le matériel (Keystore)
        MasterKey hardwareKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

        return EncryptedSharedPreferences.create(
                context,
                VAULT_NAME,
                hardwareKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, // Chiffre la CLÉ
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM // Chiffre la VALEUR
        );
    }

    public static void storeToken(Context context, String token) throws Exception {
        // Règle de sécurité : On ne Log jamais le contenu du token !
        getSecureContainer(context).edit().putString(ID_SECRET_TOKEN, token).apply();
    }

    public static String retrieveToken(Context context) throws Exception {
        return getSecureContainer(context).getString(ID_SECRET_TOKEN, "");
    }

    public static void wipe(Context context) throws Exception {
        getSecureContainer(context).edit().clear().apply();
    }
}