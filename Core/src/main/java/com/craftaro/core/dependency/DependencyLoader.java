package com.craftaro.core.dependency;

import com.craftaro.core.CraftaroCoreConstants;
import com.craftaro.core.SongodaCore;
import com.georgev22.api.libraryloader.LibraryLoader;
import com.georgev22.api.libraryloader.exceptions.InvalidDependencyException;
import com.georgev22.api.libraryloader.exceptions.UnknownDependencyException;
import me.lucko.jarrelocator.JarRelocator;
import me.lucko.jarrelocator.Relocation;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DependencyLoader {
    private static final int DEPENDENCY_VERSION = 1;

    private final LibraryLoader libraryLoader;

    public DependencyLoader(Plugin plugin) {
        this.libraryLoader = new LibraryLoader(
                DependencyLoader.class.getClassLoader(),
                new File(plugin.getDataFolder().getParentFile(), CraftaroCoreConstants.getProjectName() + "/dependencies/v" + DEPENDENCY_VERSION),
                SongodaCore.getLogger()
        );
    }

    public void loadDependencies(Collection<Dependency> dependencies) throws IOException {
        for (Dependency dependency : dependencies) {
            loadDependency(dependency);
        }
    }

    public void loadDependency(Dependency dependency) throws IOException {
        String name = dependency.getArtifactId() + "-" + dependency.getVersion();
        File outputFile = new File(this.libraryLoader.getLibFolder(), dependency.getGroupId().replace(".", File.separator) + File.separator + dependency.getArtifactId().replace(".", File.separator) + File.separator + dependency.getVersion() + File.separator + "raw-" + name + ".jar");
        File relocatedFile = new File(outputFile.getParentFile(), name.replace("raw-", "") + ".jar");
        if (relocatedFile.exists()) {
            if (isLoaded(relocatedFile)) {
                return;
            }

            loadJarIntoClasspath(relocatedFile, dependency);
            return;
        }

        SongodaCore.getLogger().info(String.format("Downloading dependency %s:%s:%s from %s", dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion(), dependency.getRepositoryUrl()));
        Files.createDirectories(outputFile.getParentFile().toPath());
        try (InputStream is = new URL(dependency.buildArtifactUrl()).openStream()) {
            Files.copy(is, outputFile.toPath());
        }

        loadJarIntoClasspath(outputFile, dependency);
    }

    public void loadJarIntoClasspath(File file, Dependency dependency) throws IOException {
        if (!isRelocated(file) && dependency.shouldRelocate()) {
            SongodaCore.getLogger().info("Loading dependency for relocation " + file);
            // relocate package to com.craftaro.core.third_party to avoid conflicts
            List<Relocation> relocations = new ArrayList<>();

            for (com.craftaro.core.dependency.Relocation r : dependency.getRelocations()) {
                relocations.add(new Relocation(r.getFrom(), r.getTo()));
            }

            // Relocate the classes
            File finalJar = new File(file.getParentFile(), file.getName().replace("raw-", ""));
            JarRelocator relocator = new JarRelocator(file, finalJar, relocations);
            try {
                relocator.run();
                SongodaCore.getLogger().info("Relocated dependency " + file);

                // Delete the old jar
                Files.deleteIfExists(file.toPath());
            } catch (Exception e) {
                SongodaCore.getLogger().severe("Failed to relocate dependency " + file);
                if (e.getMessage().contains("zip file is empty")) {
                    SongodaCore.getLogger().severe("Try deleting '" + this.libraryLoader.getLibFolder().getParent() + "' and restarting the server");
                }

                // Delete the new jar cuz it's probably corrupted
                Files.deleteIfExists(finalJar.toPath());
                throw e;
            }
        }

        try {
            this.libraryLoader.load(new LibraryLoader.Dependency(dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion(), dependency.getRepositoryUrl()), true);
        } catch (InvalidDependencyException ignored) {
            // Already loaded
        } catch (UnknownDependencyException ex) {
            throw new RuntimeException(ex);
        }
        SongodaCore.getLogger().info("----------------------------");
    }

    private boolean isRelocated(File jarFile) throws IOException {
        try (ZipFile zipFile = new ZipFile(jarFile)) {
            Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
            while (zipEntries.hasMoreElements()) {
                ZipEntry entry = zipEntries.nextElement();
                if (entry.getName().startsWith("com/craftaro/third_party")) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Finds the first .class file in the jar and check if it's loaded
     */
    private boolean isLoaded(File jarFile) throws IOException {
        try (ZipFile zipFile = new ZipFile(jarFile)) {
            Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
            while (zipEntries.hasMoreElements()) {
                ZipEntry zipEntry = zipEntries.nextElement();
                if (zipEntry.getName().startsWith("META-INF")) {
                    continue;
                }
                if (!zipEntry.getName().endsWith(".class")) {
                    continue;
                }

                String className = zipEntry.getName()
                        .substring(0, zipEntry.getName().length() - ".class".length())
                        .replace("/", ".");
                try {
                    Class.forName(className);
                    return true;
                } catch (Throwable th) {
                    return false;
                }
            }
        }

        return false;
    }

    public static int getDependencyVersion() {
        return DEPENDENCY_VERSION;
    }
}
