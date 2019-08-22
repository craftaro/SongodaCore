package com.songoda.core.library.commands;

import java.util.HashMap;
import java.util.stream.Stream;

public class SimpleNestedCommand {

    final AbstractCommand parent;
    final HashMap<String, AbstractCommand> children = new HashMap();

    protected SimpleNestedCommand(AbstractCommand parent) {
        this.parent = parent;
    }

    public SimpleNestedCommand addSubCommand(AbstractCommand command) {
        command.getCommands().stream().forEach(cmd -> children.put(cmd.toLowerCase(), command));
        return this;
    }

    public SimpleNestedCommand addSubCommands(AbstractCommand... commands) {
        Stream.of(commands).forEach(command -> command.getCommands().stream().forEach(cmd -> children.put(cmd.toLowerCase(), command)));
        return this;
    }

}
