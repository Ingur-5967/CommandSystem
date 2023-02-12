package ru.solomka.csystem.commands.impl;

import org.bukkit.command.CommandSender;
import ru.solomka.csystem.commands.module.ECommand;
import ru.solomka.csystem.commands.module.enums.SenderType;
import ru.solomka.csystem.commands.module.entity.TabViewCommand;

public class SampleCommand extends ECommand<SampleCommand> {

    public SampleCommand() {
        super(
                SenderType.PLAYER,
                "duel",
                1,
                "sample.use",
                null,
                new Object[]{
                        new TabViewCommand(0, new Object[]{"sample1_1", "sample1_2", "sample1_3"}),
                        new TabViewCommand(1, new Object[]{"sample2_1", "sample2_2", "sample3_3"})
                }
        );
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage("Hello, World!");
        return true;
    }

    @Override
    public SampleCommand getInstance() {
        return this;
    }
}
