package com.songoda.core.library.commands;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TabManager implements TabCompleter {

    private final CommandManager commandManager;

    TabManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] strings) {
        for (AbstractCommand abstractCommand : commandManager.getCommands()) {
            if (command.getPermission() != null && !sender.hasPermission(command.getPermission())) continue;
            if (abstractCommand.getCommand() != null && abstractCommand.getCommand().equalsIgnoreCase(command.getName()) && !abstractCommand.hasArgs()) {
                if (strings.length != 1) continue;
                List<String> subs = new ArrayList<>();
                for (AbstractCommand ac : commandManager.getCommands()) {
                    if (ac.getSubCommand() == null) continue;
                    subs.addAll(ac.getSubCommand());
                }
                subs.removeIf(s -> !s.toLowerCase().startsWith(strings[0].toLowerCase()));
                return subs;
            } else if (strings.length != 0 && abstractCommand.getParent() != null && abstractCommand.getParent().getCommand().equalsIgnoreCase(command.getName())
                    || abstractCommand.hasArgs() && abstractCommand.getCommand().equalsIgnoreCase(command.getName())) {
                String[] args = abstractCommand.hasArgs() ? (String[]) ArrayUtils.add(strings, 0, command.getName()) : strings;
                String cmd = abstractCommand.hasArgs() ? command.getName() : args[0];
                String cmd2 = args.length >= 2 ? String.join(" ", args[0], args[1]) : null;
                if (!abstractCommand.hasArgs()) {
                    for (String cmds : abstractCommand.getSubCommand()) {
                        if (cmd.equalsIgnoreCase(cmds) || (cmd2 != null && cmd2.equalsIgnoreCase(cmds))) {
                            return fetchList(abstractCommand, args, sender);
                        }
                    }
                } else {
                    return fetchList(abstractCommand, args, sender);
                }
            }
        }
        return new ArrayList<>();
    }

    private List<String> fetchList(AbstractCommand abstractCommand, String[] args, CommandSender sender) {
        List<String> list = abstractCommand.onTab(sender, args);
        String str = args[args.length - 1];
        if (list != null && str != null && str.length() >= 1) {
            try {
                list.removeIf(s -> !s.toLowerCase().startsWith(str.toLowerCase()));
            } catch (UnsupportedOperationException ignored) {
            }
        }
        return list;
    }
}
