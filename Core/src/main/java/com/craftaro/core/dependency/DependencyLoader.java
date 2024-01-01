package com.craftaro.core.dependency;

import com.craftaro.core.SongodaPlugin;
import com.georgev22.api.libraryloader.LibraryLoader;
import com.georgev22.api.libraryloader.exceptions.InvalidDependencyException;
import com.georgev22.api.libraryloader.exceptions.UnknownDependencyException;
import me.lucko.jarrelocator.JarRelocator;
import me.lucko.jarrelocator.Relocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class DependencyLoader {

    private static final Logger logger = LoggerFactory.getLogger(DependencyLoader.class);
    private static final Set<Dependency> loadedDependencies = new HashSet<>();
    private static LibraryLoader libraryLoader;

    public static LibraryLoader getLibraryLoader() {
        return libraryLoader;
    }

    public static void initParentClassLoader(ClassLoader parent) {
        libraryLoader = new LibraryLoader(parent, new File("craftaro"));
    }

    public static void loadDependencies(Set<Dependency> dependencies) {
        for (Dependency dependency : dependencies) {
            if (loadedDependencies.contains(dependency)) {
                continue;
            }
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
            //Load dependency into the classpath
            loadJarIntoClasspath(relocatedFile, dependency);
            return;
        }

        try {
            logger.info("[CraftaroCore] Downloading dependency " + groupId + ":" + artifactId + ":" + version + " from " + repositoryUrl);
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
            logger.info("[CraftaroCore] Loading dependency " + groupId + ":" + artifactId + ":" + version);
            loadJarIntoClasspath(outputFile, dependency);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadJarIntoClasspath(File file, Dependency dependency) {
        try {
            if (!isRelocated(file) && dependency.relocate()) {
                logger.info("[CraftaroCore] Loading dependency for relocation " + file);
                //relocate package to com.craftaro.core.third_party to avoid conflicts
                try {
                    List<Relocation> relocations = new ArrayList<>();
                    //Create a new zip file with the relocated classes
                    ZipEntry entry;
                    String com = "com.";
                    String craftaro = "craftaro.";
                    String third_party = "third_party.";
                    String relocation = com + craftaro + third_party;

                    if (dependency.getRelocation() != null) {
                        relocations.add(new Relocation(dependency.getRelocation().getFrom(), dependency.getRelocation().getTo()));
                    } else {
                        //Use artifactId as the relocation
                        relocations.add(new Relocation(dependency.getGroupId(), relocation + dependency.getGroupId()));
                    }

                    //Relocate the classes
                    File finalJar = new File(file.getParentFile(), file.getName().replace("raw-", ""));
                    JarRelocator relocator = new JarRelocator(file, finalJar, relocations);
                    try {
                        relocator.run();
                    } catch (Exception e) {
                        logger.error("[CraftaroCore] Failed to relocate dependency " + file, e);
                    }
                    //Delete the old jar
                    file.delete();

                } catch (Exception e) {
                    logger.error("[CraftaroCore] Failed to relocate dependency " + file, e);
                }
            }

            libraryLoader.load(new LibraryLoader.Dependency(dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion(), dependency.getRepositoryUrl()), true);

        } catch (Exception e) {
            logger.error("[CraftaroCore] Failed to load dependency " + file, e);
        }
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
}
