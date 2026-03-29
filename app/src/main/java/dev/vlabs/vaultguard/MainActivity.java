package dev.vlabs.vaultguard;

import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import dev.vlabs.vaultguard.data.ProfileEntry;
import dev.vlabs.vaultguard.logic.cache.TemporaryVault;
import dev.vlabs.vaultguard.logic.prefs.SecretVault;
import dev.vlabs.vaultguard.logic.prefs.SettingsManager;
import dev.vlabs.vaultguard.logic.storage.JsonVaultStore;
import dev.vlabs.vaultguard.logic.storage.TextDiskManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final List<String> languages = Arrays.asList("fr", "en", "es");
    private EditText inputAlias, inputToken;
    private Spinner dropLang;
    private androidx.appcompat.widget.SwitchCompat toggleTheme;
    private TextView labelResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Liaison UI
        inputAlias = findViewById(R.id.input_alias);
        inputToken = findViewById(R.id.input_secret_token);
        dropLang = findViewById(R.id.drop_lang);
        toggleTheme = findViewById(R.id.toggle_theme);
        labelResult = findViewById(R.id.label_display_result);

        setupSpinner();

        findViewById(R.id.btn_save_all).setOnClickListener(v -> performSave());
        findViewById(R.id.btn_load_data).setOnClickListener(v -> performLoad());
        findViewById(R.id.btn_export_json).setOnClickListener(v -> saveJsonDemo());
        findViewById(R.id.btn_reset_vault).setOnClickListener(v -> fullReset());

        performLoad(); // Charger les données existantes au démarrage
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, languages);
        dropLang.setAdapter(adapter);
    }

    private void performSave() {
        String alias = inputAlias.getText().toString();
        String lang = languages.get(dropLang.getSelectedItemPosition());
        String theme = toggleTheme.isChecked() ? "dark" : "light";

        // Sauvegarde Prefs simples
        SettingsManager.persist(this, alias, lang, theme, false);

        // Sauvegarde Secrète (Chiffrée)
        try {
            String token = inputToken.getText().toString();
            if (!token.isEmpty()) SecretVault.storeToken(this, token);

            // Cache temporaire
            TemporaryVault.saveToCache(this, "last_action.txt", "Update: " + alias);

            labelResult.setText("✅ Sauvegarde réussie !\nPrefs + Token chiffré.");
        } catch (Exception e) {
            labelResult.setText("❌ Erreur : " + e.getMessage());
        }
    }

    private void performLoad() {
        SettingsManager.UserConfig cfg = SettingsManager.fetch(this);
        inputAlias.setText(cfg.alias);
        toggleTheme.setChecked(cfg.theme.equals("dark"));
        dropLang.setSelection(languages.indexOf(cfg.language));

        try {
            String secret = SecretVault.retrieveToken(this);
            int len = secret != null ? secret.length() : 0;
            labelResult.setText("📂 Données chargées.\nSecret trouvé : " + len + " caractères.");
        } catch (Exception e) {
            labelResult.setText("Erreur chargement secret.");
        }
    }

    private void saveJsonDemo() {
        List<ProfileEntry> demoList = new ArrayList<>();
        demoList.add(new ProfileEntry(101, "Admin_Main", 5));
        demoList.add(new ProfileEntry(102, "Guest_User", 1));

        try {
            JsonVaultStore.persistProfiles(this, demoList);
            TextDiskManager.writeData(this, "audit.log", "JSON généré à l'appui du bouton.");
            labelResult.setText("💾 Fichier JSON 'profiles_vault.json' créé dans le stockage interne.");
        } catch (Exception e) {
            labelResult.setText("Erreur JSON : " + e.getMessage());
        }
    }

    private void fullReset() {
        try {
            SettingsManager.reset(this);
            SecretVault.wipe(this);
            TemporaryVault.clearAllCache(this);
            labelResult.setText("⚠️ Nettoyage complet effectué.\nToutes les mémoires sont vides.");
            inputAlias.setText("");
            inputToken.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}