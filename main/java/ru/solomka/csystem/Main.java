package ru.solomka.csystem;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import ru.solomka.csystem.commands.CommandManager;
import ru.solomka.csystem.commands.impl.SampleCommand;

public final class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        CommandManager.init(new SampleCommand());
    }

    @Override
    public void onDisable() {}
}
