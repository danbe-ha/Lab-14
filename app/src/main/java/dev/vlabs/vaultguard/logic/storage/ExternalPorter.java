package dev.vlabs.vaultguard.logic.storage;

import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public final class ExternalPorter {

    private ExternalPorter() {}

    /**
     * Exporte une donnée vers le stockage externe privé.
     * @return Le chemin absolu du fichier créé ou null si le stockage est indisponible.
     */
    public static String exportData(Context context, String docName, String content) throws Exception {
        // null signifie qu'on utilise le dossier racine des fichiers externes de l'app
        File root = context.getExternalFilesDir(null);
        if (root == null) return null;

        File targetFile = new File(root, docName);
        try (FileOutputStream out = new FileOutputStream(targetFile)) {
            out.write(content.getBytes(StandardCharsets.UTF_8));
        }
        return targetFile.getAbsolutePath();
    }

    public static String importData(Context context, String docName) throws Exception {
        File root = context.getExternalFilesDir(null);
        if (root == null) return null;

        File targetFile = new File(root, docName);
        if (!targetFile.exists()) return null;

        try (FileInputStream in = new FileInputStream(targetFile)) {
            byte[] buffer = new byte[(int) targetFile.length()];
            in.read(buffer);
            return new String(buffer, StandardCharsets.UTF_8);
        }
    }

    public static boolean deleteExport(Context context, String docName) {
        File root = context.getExternalFilesDir(null);
        if (root == null) return false;

        File targetFile = new File(root, docName);
        return targetFile.delete();
    }
}