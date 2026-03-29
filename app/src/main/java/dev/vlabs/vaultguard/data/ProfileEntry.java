package dev.vlabs.vaultguard.data;

public class ProfileEntry {
    public final int uid;
    public final String label;
    public final int level;

    public ProfileEntry(int uid, String label, int level) {
        this.uid = uid;
        this.label = label;
        this.level = level;
    }
}