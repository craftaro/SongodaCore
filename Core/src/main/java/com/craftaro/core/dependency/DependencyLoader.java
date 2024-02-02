package com.craftaro.core.dependency;

import com.craftaro.core.SongodaCore;
import com.georgev22.api.libraryloader.LibraryLoader;
import me.lucko.jarrelocator.JarRelocator;
import me.lucko.jarrelocator.Relocation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DependencyLoader {
    public static final String DEPENDENCY_VERSION = "v1";
    private static final LibraryLoader libraryLoader = new LibraryLoader(DependencyLoader.class.getClassLoader(), new File("plugins/CraftaroCore/dependencies/" + DEPENDENCY_VERSION), SongodaCore.getLogger());

    public static LibraryLoader getLibraryLoader() {
        return libraryLoader;
    }

    public static void loadDependencies(Set<Dependency> dependencies) {
        for (Dependency dependency : dependencies) {
            loadDependency(dependency);
        }
    }

    public static void loadDependency(Dependency dependency) {
        String repositoryUrl = dependency.getRepositoryUrl();
        String groupId = dependency.getGroupId();
        String artifactId = dependency.getArtifactId();
        String version = dependency.getVersion();
        //Download dependency from the repositoryUrl
        //Check if we have the dependency downloaded already
        String name = dependency.getArtifactId() + "-" + dependency.getVersion();

        File outputFile = new File(libraryLoader.getLibFolder(), dependency.getGroupId().replace(".", File.separator) + File.separator + dependency.getArtifactId().replace(".", File.separator) + File.separator + dependency.getVersion() + File.separator + "raw-" + name + ".jar");
        File relocatedFile = new File(outputFile.getParentFile(), name.replace("raw-", "") + ".jar");
        if (relocatedFile.exists()) {
            //Check if the file is already loaded to the classpath
            if (isLoaded(relocatedFile)) {
                return;
            }

            //Load dependency into the classpath
            loadJarIntoClasspath(relocatedFile, dependency);
            return;
        }

        try {
            SongodaCore.getLogger().info("Downloading dependency " + groupId + ":" + artifactId + ":" + version + " from " + repositoryUrl);
            // Construct the URL for the artifact in the Maven repository
            String artifactUrl = repositoryUrl + "/" +
                    groupId.replace('.', '/') + "/" +
                    artifactId + "/" +
                    version + "/" +
                    artifactId + "-" + version + ".jar";

            URL url = new URL(artifactUrl);
            URLConnection connection = url.openConnection();
            InputStream in = connection.getInputStream();

            // Define the output file

            outputFile.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(outputFile);

            // Read from the input stream and write to the output stream
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }

            // Close both streams
            in.close();
            out.close();

            //Load dependency into the classpath
            SongodaCore.getLogger().info("Downloaded dependency " + groupId + ":" + artifactId + ":" + version);
            loadJarIntoClasspath(outputFile, dependency);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadJarIntoClasspath(File file, Dependency dependency) {
        if (!isRelocated(file) && dependency.shouldRelocate()) {
            SongodaCore.getLogger().info("Loading dependency for relocation " + file);
            //relocate package to com.craftaro.core.third_party to avoid conflicts
            List<Relocation> relocations = new ArrayList<>();

            for (com.craftaro.core.dependency.Relocation r : dependency.getRelocations()) {
                relocations.add(new Relocation(r.getFrom(), r.getTo()));
            }

            //Relocate the classes
            File finalJar = new File(file.getParentFile(), file.getName().replace("raw-", ""));
            JarRelocator relocator = new JarRelocator(file, finalJar, relocations);
            try {
                relocator.run();
                SongodaCore.getLogger().info("Relocated dependency " + file);
                //Delete the old jar
                file.delete();
            } catch (Exception e) {
                SongodaCore.getLogger().severe("Failed to relocate dependency1 " + file);
                if (e.getMessage().contains("zip file is empty")) {
                    SongodaCore.getLogger().severe("Try deleting the 'server root/craftaro' folder and restarting the server");
                }
                e.printStackTrace();
                //Delete the new jar cuz it's probably corrupted
                finalJar.delete();
            }

        }
        try {
            libraryLoader.load(new LibraryLoader.Dependency(dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion(), dependency.getRepositoryUrl()), true);
        } catch (Exception ignored) {
            //already loaded
        }
        SongodaCore.getLogger().info("----------------------------");
    }

    private static boolean isRelocated(File file) {
        try (ZipFile zipFile = new ZipFile(file)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();

                if (entry.getName().startsWith("com/craftaro/third_party")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean isLoaded(File file) {
        //Find the first class file in the jar and try Class.forName
        try (ZipFile zipFile = new ZipFile(file)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.getName().startsWith("META-INF")) {
                    continue;
                }

                if (entry.getName().endsWith(".class")) {
                    String className = entry.getName().replace("/", ".").replace(".class", "");
                    try {
                        Class.forName(className);
                        return true;
                    } catch (Exception | Error e) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
