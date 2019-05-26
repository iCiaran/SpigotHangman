package uk.co.ciaranmoran.hangman;

import org.bukkit.plugin.java.JavaPlugin;

public class Hangman extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getCommand("hangman").setExecutor(new CommandHangman());
    }
}
