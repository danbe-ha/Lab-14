package dev.vlabs.vaultguard.logic.storage;

import android.content.Context;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public final class TextDiskManager {

    private TextDiskManager() {}

    public static void writeData(Context context, String docName, String text) throws Exception {
        // Utilisation de MODE_PRIVATE pour la sécurité (seule l'app accède au fichier)
        try (FileOutputStream out = context.openFileOutput(docName, Context.MODE_PRIVATE)) {
            out.write(text.getBytes(StandardCharsets.UTF_8));
        }
    }

    public static String readData(Context context, String docName) throws Exception {
        try (FileInputStream in = context.openFileInput(docName)) {
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            return new String(buffer, StandardCharsets.UTF_8);
        }
    }

    public static boolean removeFile(Context context, String docName) {
        return context.deleteFile(docName);
    }
}