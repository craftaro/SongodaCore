package com.songoda.core.verification;

import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;

final class VerificationTokenFileManager {
    static VerificationToken loadVerificationToken() throws IOException {
        File verificationTokenFile = getVerificationTokenFile();
        if (!verificationTokenFile.exists()) {
            return null;
        }

        String base64TokenString = new String(Files.readAllBytes(verificationTokenFile.toPath()), StandardCharsets.UTF_8);
        String jsonTokenString = new String(Base64.getDecoder().decode(base64TokenString), StandardCharsets.UTF_8);
        return VerificationToken.fromJson(jsonTokenString);
    }

    static void saveVerificationToken(VerificationToken token) throws IOException {
        File verificationTokenFile = getVerificationTokenFile();
        Files.createDirectories(verificationTokenFile.getParentFile().toPath());

        String base64TokenString = Base64.getEncoder().encodeToString(token.toJson().getBytes(StandardCharsets.UTF_8));
        Files.write(verificationTokenFile.toPath(), base64TokenString.getBytes(StandardCharsets.UTF_8));
    }

    static void deleteVerificationTokenFile() throws IOException {
        File verificationTokenFile = getVerificationTokenFile();
        Files.deleteIfExists(verificationTokenFile.toPath());
    }

    private static File getVerificationTokenFile() {
        return new File(getCraftaroDirectory(), "verification");
    }

    private static File getCraftaroDirectory() {
        File pluginsDirectory = Bukkit.getPluginManager().getPlugins()[0].getDataFolder().getParentFile();
        return new File(pluginsDirectory, "Craftaro");
    }
}
