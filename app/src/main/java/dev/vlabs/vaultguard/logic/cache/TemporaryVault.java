package dev.vlabs.vaultguard.logic.cache;

import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public final class TemporaryVault {

    private TemporaryVault() {}

    /**
     * Écrit une donnée dans le dossier de cache de l'application.
     */
    public static void saveToCache(Context context, String fileName, String content) throws Exception {
        // Récupération du dossier cache interne
        File cacheFile = new File(context.getCacheDir(), fileName);

        try (FileOutputStream fos = new FileOutputStream(cacheFile)) {
            fos.write(content.getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * Lit une donnée depuis le cache.
     */
    public static String loadFromCache(Context context, String fileName) throws Exception {
        File cacheFile = new File(context.getCacheDir(), fileName);
        if (!cacheFile.exists()) return null;

        try (FileInputStream fis = new FileInputStream(cacheFile)) {
            byte[] data = new byte[(int) cacheFile.length()];
            fis.read(data);
            return new String(data, StandardCharsets.UTF_8);
        }
    }

    /**
     * Nettoie tout le dossier cache.
     * @return Le nombre de fichiers supprimés.
     */
    public static int clearAllCache(Context context) {
        File cacheDirectory = context.getCacheDir();
        File[] filesList = cacheDirectory.listFiles();

        if (filesList == null) return 0;

        int count = 0;
        for (File f : filesList) {
            if (f.delete()) {
                count++;
            }
        }
        return count;
    }
}