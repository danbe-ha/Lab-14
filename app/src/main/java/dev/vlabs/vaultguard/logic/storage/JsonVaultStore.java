package dev.vlabs.vaultguard.logic.storage;

import android.content.Context;
import dev.vlabs.vaultguard.data.ProfileEntry;
import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class JsonVaultStore {

    private static final String DATA_FILE = "profiles_vault.json";

    private JsonVaultStore() {}

    public static void persistProfiles(Context context, List<ProfileEntry> entries) throws Exception {
        JSONArray rootArray = new JSONArray();
        for (ProfileEntry p : entries) {
            JSONObject item = new JSONObject();
            item.put("uid", p.uid);
            item.put("label", p.label);
            item.put("level", p.level);
            rootArray.put(item);
        }

        String jsonOutput = rootArray.toString();
        try (var stream = context.openFileOutput(DATA_FILE, Context.MODE_PRIVATE)) {
            stream.write(jsonOutput.getBytes(StandardCharsets.UTF_8));
        }
    }

    public static List<ProfileEntry> loadProfiles(Context context) {
        try (var stream = context.openFileInput(DATA_FILE)) {
            byte[] data = new byte[stream.available()];
            stream.read(data);
            String content = new String(data, StandardCharsets.UTF_8);

            JSONArray array = new JSONArray(content);
            List<ProfileEntry> results = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                results.add(new ProfileEntry(
                        obj.getInt("uid"),
                        obj.getString("label"),
                        obj.getInt("level")
                ));
            }
            return results;
        } catch (Exception e) {
            return new ArrayList<>(); // Retourne une liste vide si erreur ou fichier absent
        }
    }
}