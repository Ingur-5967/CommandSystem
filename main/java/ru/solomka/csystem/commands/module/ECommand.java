package ru.solomka.csystem.commands.module;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import ru.solomka.csystem.commands.module.entity.CheckState;
import ru.solomka.csystem.commands.module.entity.TabViewCommand;
import ru.solomka.csystem.commands.module.enums.SenderType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data @AllArgsConstructor
public abstract class ECommand<T extends ECommand<?>> implements ECommandHelper {

    private final SenderType senderType;
    private final String syntax;
    private final int argumentLimit;
    private final String permission;
    private final String[] aliases;
    private final Object[] toViewElementsWrapper;

    public abstract boolean execute(CommandSender sender, String[] args);

    public abstract T getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        CheckState executorCheckState = validExecutor(sender);

        if(!executorCheckState.isValid()) {
            if(executorCheckState.getReason() == CheckState.CallReason.INSTANCE_OF)
                sender.sendMessage(String.format("You're cannot execute this command, maybe you not '%s'", senderType.name()));

            else if(executorCheckState.getReason() == CheckState.CallReason.PERMISSION)
                sender.sendMessage(String.format("You're cannot execute this command, because you don't have permission '%s'", permission));

            return true;
        }

        if((args.length - 1) > argumentLimit) {
            sender.sendMessage(String.format("The length of the input argument is too long! (Allowed length: '%s')", argumentLimit));
            return true;
        }

        return execute(sender, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if((args.length - 1) > argumentLimit)
            return Collections.emptyList();

        if (argumentLimit == 0)
            return toListViewCommandTabs(0);
        else {
            if(getToViewElementsWrapper()[args.length - 1] == null)
                return Collections.emptyList();

            return toListViewCommandTabs(args.length - 1);
        }
    }

    public List<String> toListViewCommandTabs(int index) {
        if (toViewElementsWrapper == null) return null;
        for (Object tabViewElement : toViewElementsWrapper) {
            TabViewCommand tabViewCommand = (TabViewCommand) tabViewElement;

            if (tabViewCommand.getIndex() == index)
                return Arrays.stream(tabViewCommand.getToViews()).map(String::valueOf).collect(Collectors.toList());
        }
        return null;
    }

    public CheckState validExecutor(CommandSender sender) {
        switch (senderType) {
            case CONSOLE: return new CheckState(sender instanceof ConsoleCommandSender, CheckState.CallReason.INSTANCE_OF);
            case PLAYER: {
                if (permission != null && !permission.equals(""))
                    return new CheckState(sender.hasPermission(permission), CheckState.CallReason.PERMISSION);

                return new CheckState(sender instanceof Player, CheckState.CallReason.INSTANCE_OF);
            }
            case PLAYER_OP: return new CheckState(sender instanceof Player && sender.isOp(), CheckState.CallReason.PERMISSION_AND_INSTANCE_OF);
        }
        return new CheckState(false, CheckState.CallReason.OTHER);
    }
}