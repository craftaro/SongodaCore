package com.songoda.core.commands;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class AbstractCommand {

    private final boolean noConsole;
    private boolean hasArgs = false;

    private final List<String> subCommand = new ArrayList<>();

    protected AbstractCommand(boolean noConsole, String... command) {
        this.subCommand.addAll(Arrays.asList(command));
        this.noConsole = noConsole;
    }

    protected AbstractCommand(boolean noConsole, boolean hasArgs, String... command) {
        this.subCommand.addAll(Arrays.asList(command));

        this.hasArgs = hasArgs;
        this.noConsole = noConsole;
    }

    public final List<String> getCommands() {
        return Collections.unmodifiableList(subCommand);
    }

    public final void addSubCommand(String command) {
        subCommand.add(command);
    }

    protected abstract ReturnType runCommand(CommandSender sender, String... args);

    protected abstract List<String> onTab(CommandSender sender, String... args);

    public abstract String getPermissionNode();

    public abstract String getSyntax();

    public abstract String getDescription();

    public boolean hasArgs() {
        return hasArgs;
    }

    public boolean isNoConsole() {
        return noConsole;
    }

    public static enum ReturnType {SUCCESS, FAILURE, SYNTAX_ERROR}
}

