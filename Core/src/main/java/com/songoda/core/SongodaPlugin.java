package com.songoda.core;

import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import com.songoda.core.actions.ActionManager;
import com.songoda.core.builtin.SongodaCoreCommand;
import com.songoda.core.placeholder.IPlaceholderResolver;
import com.songoda.core.placeholder.NoPluginResolver;
import com.songoda.core.placeholder.PlaceholderAPIResolver;
import de.tr7zw.changeme.nbtapi.utils.MinecraftVersion;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public abstract class SongodaPlugin extends JavaPlugin {

    static {
        /* NBT-API */
        MinecraftVersion.getLogger().setLevel(Level.WARNING);
        MinecraftVersion.disableUpdateCheck();
    }

    private BukkitCommandHandler commandManager;
    private BukkitAudiences adventure;
    private IPlaceholderResolver placeholderResolver;
    private ActionManager actionManager;
    private TaskChainFactory taskChainFactory;

    public abstract void onPluginEnable();
    public abstract void onPluginDisable();

    protected abstract int getPluginId();
    protected abstract String getPluginIcon();

    @Override
    public final void onEnable() {
        SongodaCore.getInstance().registerPlugin(this, getPluginId(), getPluginIcon());

        this.commandManager = BukkitCommandHandler.create(this);
        this.adventure = BukkitAudiences.create(this);
        this.actionManager = new ActionManager(this);
        this.taskChainFactory = BukkitTaskChainFactory.create(this);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            this.placeholderResolver = new PlaceholderAPIResolver();
        }else{
            this.placeholderResolver = new NoPluginResolver();
        }

        onPluginEnable();
    }

    /**
     * Create a configuration file that does NOT update.
     * @param file File to create.
     * @return The configuration file created.
     */
    public YamlDocument createConfig(File file) {
        try {
            return YamlDocument.create(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Create a configuration file that automatically updates. Requires config-version to be defined.
     * @param file File to create.
     * @return The configuration file created.
     */
    public YamlDocument createUpdatingConfig(File file) {
        try {
            return YamlDocument.create(file, getResource(file.getName()),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")).build());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public final void onDisable() {
        onPluginDisable();
    }

    /**
     * Use {@link dev.dejvokep.boostedyaml.YamlDocument} instead.
     */
    @Deprecated
    @Override
    public @NotNull FileConfiguration getConfig() {
        return super.getConfig();
    }

    /**
     * Use {@link dev.dejvokep.boostedyaml.YamlDocument} instead.
     */
    @Deprecated
    @Override
    public void reloadConfig() {
    }

    /**
     * Use {@link dev.dejvokep.boostedyaml.YamlDocument} instead.
     */
    @Deprecated
    @Override
    public void saveConfig() {
    }

    public BukkitCommandHandler getCommandManager() {
        return commandManager;
    }

    public BukkitAudiences getAdventure() {
        return adventure;
    }

    public IPlaceholderResolver getPlaceholderResolver() {
        return placeholderResolver;
    }

    public ActionManager getActionManager() {
        return actionManager;
    }

    public <T> TaskChain<T> newChain() {
        return taskChainFactory.newChain();
    }

    public <T> TaskChain<T> newSharedChain(String name) {
        return taskChainFactory.newSharedChain(name);
    }
}
