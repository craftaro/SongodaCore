package com.songoda.core.commands;

import com.songoda.core.compatibility.ServerProject;
import com.songoda.core.compatibility.ServerVersion;
import com.songoda.core.utils.TextUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;

public class CommandManager implements CommandExecutor, TabCompleter {

    private final JavaPlugin plugin;

    private final HashMap<String, SimpleNestedCommand> commands = new HashMap<>();
    private boolean allowLooseCommands = false;

    public CommandManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public Set<String> getCommands() {
        return Collections.unmodifiableSet(commands.keySet());
    }

    public List<String> getSubCommands(String command) {
        SimpleNestedCommand nested = command == null ? null : commands.get(command.toLowerCase());
        return nested == null ? Collections.EMPTY_LIST : nested.children.keySet().stream().collect(Collectors.toList());
    }

    public Set<AbstractCommand> getAllCommands() {
        HashSet<AbstractCommand> all = new HashSet();
        commands.values().stream()
                .filter(c -> c.parent != null && !all.contains(c.parent))
                .forEach(c -> {
                    all.add(c.parent);
                    c.children.values().stream()
                        .filter(s -> !all.contains(s))
                        .forEach(s -> all.add(s));
                });
        return all;
    }

    public CommandManager registerCommandDynamically(String command) {
        CommandManager.registerCommandDynamically(plugin, command, this, this);
        return this;
    }

    /**
     * TODO: Test compatibility. Seems to fail in 1.8
     */
    public SimpleNestedCommand registerCommandDynamically(AbstractCommand abstractCommand) {
        SimpleNestedCommand nested = new SimpleNestedCommand(abstractCommand);
        abstractCommand.getCommands().stream().forEach(cmd -> {
            CommandManager.registerCommandDynamically(plugin, cmd, this, this);
            commands.put(cmd.toLowerCase(), nested);
            PluginCommand pcmd = plugin.getCommand(cmd);
            if(pcmd != null) {
                pcmd.setExecutor(this);
                pcmd.setTabCompleter(this);
            } else {
                plugin.getLogger().warning("Failed to register command: /" + cmd);
            }
        });
        return nested;
    }

    public SimpleNestedCommand addCommand(AbstractCommand abstractCommand) {
        SimpleNestedCommand nested = new SimpleNestedCommand(abstractCommand);
        abstractCommand.getCommands().stream().forEach(cmd -> {
            commands.put(cmd.toLowerCase(), nested);
            PluginCommand pcmd = plugin.getCommand(cmd);
            if(pcmd != null) {
                pcmd.setExecutor(this);
                pcmd.setTabCompleter(this);
            } else {
                plugin.getLogger().warning("Failed to register command: /" + cmd);
            }
        });
        return nested;
    }

    public CommandManager addCommands(AbstractCommand... abstractCommands) {
        for (AbstractCommand abstractCommand : abstractCommands)
            addCommand(abstractCommand);
        return this;
    }

    public CommandManager setExecutor(String command) {
        plugin.getCommand(command).setExecutor(this);
        return this;
    }

    public CommandManager setUseClosestCommand(boolean bool) {
        allowLooseCommands = bool;
        return this;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        // grab the specific command that's being called
        SimpleNestedCommand nested = commands.get(command.getName().toLowerCase());
        if(nested != null) {
            // check to see if we're trying to call a sub-command
            if(args.length != 0 && !nested.children.isEmpty()) {
                String subCmd = getSubCommand(nested, args);
                if(subCmd != null) {
                    // we have a subcommand to use!
                    AbstractCommand sub = nested.children.get(subCmd);
                    // adjust the arguments to match - BREAKING!!
                    int i = subCmd.indexOf(' ') == -1 ? 1 : 2;
                    String[] newArgs = new String[args.length - i];
                    System.arraycopy(args, i, newArgs, 0, newArgs.length);
                    // now process the command
                    processRequirements(sub, commandSender, newArgs);
                    return true;
                }
            }
            // if we've gotten this far, then just use the command we have
            if(nested.parent != null) {
                processRequirements(nested.parent, commandSender, args);
                return true;
            }
        }
        commandSender.sendMessage(TextUtils.formatText("&7The command you entered does not exist or is spelt incorrectly."));
        return true;
    }

    private String getSubCommand(SimpleNestedCommand nested, String[] args) {
        String cmd = args[0].toLowerCase();
        if(nested.children.containsKey(cmd))
            return cmd;
        String match = null;
        // support for two-argument subcommands
        if(args.length >= 2 && nested.children.keySet().stream().anyMatch(k -> k.indexOf(' ') != -1)) {
            cmd = String.join(" ", args[0], args[1]);
            if(nested.children.containsKey(cmd))
                return cmd;
        }
        // if we don't have a subcommand, should we search for one?
        if (allowLooseCommands) {
            // do a "closest match"
            int count = 0;
            for (String c : nested.children.keySet()) {
                if (c.startsWith(cmd)) {
                    match = c;
                    if (++count > 1) {
                        // there can only be one!
                        match = null;
                        break;
                    }
                }
            }
        }
        return match;
    }

    private void processRequirements(AbstractCommand command, CommandSender sender, String[] args) {
        if (!(sender instanceof Player) && command.isNoConsole()) {
            sender.sendMessage("&cYou must be a player to use this command...");
            return;
        }
        if (command.getPermissionNode() == null || sender.hasPermission(command.getPermissionNode())) {
            AbstractCommand.ReturnType returnType = command.runCommand(sender, args);
            if (returnType == AbstractCommand.ReturnType.SYNTAX_ERROR) {
                sender.sendMessage(TextUtils.formatText("&cInvalid Syntax!"));
                sender.sendMessage(TextUtils.formatText("&7The valid syntax is: &6" + command.getSyntax() + "&7."));
            }
            return;
        }
        sender.sendMessage(TextUtils.formatText("&cYou do not have permission to do that."));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // grab the specific command that's being called
        SimpleNestedCommand nested = commands.get(command.getName().toLowerCase());
        if(nested != null) {
            if(args.length == 0) {
                return nested.parent != null ? nested.parent.onTab(sender, args) : null;
            }
            // check for each sub-command that they have access to
            final boolean op = sender.isOp();
            final boolean console = !(sender instanceof Player);
            if(args.length == 1) {
                // suggest sub-commands that this user has access to
                final String arg = args[0].toLowerCase();
                return nested.children.entrySet().stream()
                        .filter(e -> !console || !e.getValue().isNoConsole())
                        .filter(e -> e.getKey().startsWith(arg))
                        .filter(e -> op || e.getValue().getPermissionNode() == null || sender.hasPermission(e.getValue().getPermissionNode()))
                        .map(e -> e.getKey())
                        .collect(Collectors.toList());
            } else {
                // more than one arg, let's check to see if we have a command here
                String subCmd = getSubCommand(nested, args);
                AbstractCommand sub;
                if(subCmd != null && (sub = nested.children.get(subCmd)) != null
                        && (!console || !sub.isNoConsole())
                        && (op || sub.getPermissionNode() == null || sender.hasPermission(sub.getPermissionNode()))) {
                    // adjust the arguments to match - BREAKING!!
                    int i = subCmd.indexOf(' ') == -1 ? 1 : 2;
                    String[] newArgs = new String[args.length - i];
                    System.arraycopy(args, i, newArgs, 0, newArgs.length);
                    // we're good to go!
                    return fetchList(sub, newArgs, sender);
                }
            }
        }
        return Collections.EMPTY_LIST;
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

    public static void registerCommandDynamically(Plugin plugin, String command, CommandExecutor executor, TabCompleter tabManager) {
        try {
            // Retrieve the SimpleCommandMap from the server
            Class<?> classCraftServer = Bukkit.getServer().getClass();
            Field fieldCommandMap = classCraftServer.getDeclaredField("commandMap");
            fieldCommandMap.setAccessible(true);
            SimpleCommandMap commandMap = (SimpleCommandMap) fieldCommandMap.get(Bukkit.getServer());

            // Construct a new Command object
            Constructor<PluginCommand> constructorPluginCommand = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructorPluginCommand.setAccessible(true);
            PluginCommand commandObject = constructorPluginCommand.newInstance(command, plugin);

            // If we're on Paper 1.8, we need to register timings (spigot creates timings on init, paper creates it on register)
            // later versions of paper create timings if needed when the command is executed
            if(ServerProject.isServer(ServerProject.PAPER) && ServerVersion.isServerVersionBelow(ServerVersion.V1_9)) {
                commandObject.timings = co.aikar.timings.TimingsManager.getCommandTiming(plugin.getName().toLowerCase(), commandObject);
            }

            // Set command action
            commandObject.setExecutor(executor);

            // Set tab complete
            commandObject.setTabCompleter(tabManager);

            // Register the command
            Field fieldKnownCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");
            fieldKnownCommands.setAccessible(true);
            Map<String, Command> knownCommands = (Map<String, Command>) fieldKnownCommands.get(commandMap);
            knownCommands.put(command, commandObject);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }
}
