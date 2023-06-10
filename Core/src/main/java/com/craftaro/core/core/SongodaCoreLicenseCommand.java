package com.craftaro.core.core;

import com.craftaro.core.commands.AbstractCommand;
import com.craftaro.core.verification.AsyncTokenAcquisitionFlow;
import com.craftaro.core.verification.CraftaroProductVerification;
import com.craftaro.core.verification.ProductVerificationStatus;
import com.craftaro.core.verification.VerificationRequest;
import com.craftaro.core.SongodaCore;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class SongodaCoreLicenseCommand extends AbstractCommand {

    public SongodaCoreLicenseCommand() {
        super(CommandType.CONSOLE_OK, "license");
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        try {
            boolean verificationNeedsAction = false;

            if (SongodaCore.getPlugins().isEmpty()) {
                sender.sendMessage(SongodaCore.getPrefix() + ChatColor.RED + "No plugins found.");
                return ReturnType.SUCCESS;
            }

            sender.sendMessage("");
            for (PluginInfo pl : SongodaCore.getPlugins()) {
                if (pl.verificationStatus == ProductVerificationStatus.ACTION_NEEDED) {
                    verificationNeedsAction = true;
                }

                sender.sendMessage(String.format(
                        ChatColor.YELLOW + "%s" + ChatColor.GRAY + ": %s",
                        pl.getJavaPlugin().getName(),
                        pl.verificationStatus.getColoredFriendlyName()
                ));
            }
            sender.sendMessage("");

            if (verificationNeedsAction) {
                AsyncTokenAcquisitionFlow tokenAcquisitionFlow = CraftaroProductVerification.startAsyncTokenAcquisitionFlow();
                sender.sendMessage(String.format(
                        SongodaCore.getPrefix() + ChatColor.YELLOW + "Please visit " + ChatColor.GOLD + "%s " + ChatColor.YELLOW + "to verify your server.\nI'll be checking again every " + ChatColor.GOLD + "%d seconds " + ChatColor.GRAY + "–" + ChatColor.YELLOW + " You got about " + ChatColor.GOLD + "%d minutes " + ChatColor.YELLOW + "for this.",
                        tokenAcquisitionFlow.getUriForTheUserToVisit(),
                        TimeUnit.MILLISECONDS.toSeconds(VerificationRequest.CHECK_INTERVAL_MILLIS),
                        TimeUnit.MILLISECONDS.toMinutes(VerificationRequest.REQUEST_TTL_MILLIS)
                ));

                tokenAcquisitionFlow.getResultFuture()
                        .whenComplete((successful, throwable) -> {
                            if (throwable != null) {
                                SongodaCore.getLogger().log(Level.WARNING, "Failed to acquire token", throwable);
                                sender.sendMessage(SongodaCore.getPrefix() + ChatColor.RED + "The process failed " + ChatColor.GRAY + "– " + ChatColor.DARK_RED + "Please check the server log for details and try again later.");
                                return;
                            }

                            if (!successful) {
                                sender.sendMessage(SongodaCore.getPrefix() + ChatColor.RED + "The process failed " + ChatColor.GRAY + "–" + ChatColor.DARK_RED + " Please check the server log for details and try again later.");
                                return;
                            }
                            sender.sendMessage(SongodaCore.getPrefix() + ChatColor.GREEN + "The verification process has been completed " + ChatColor.GRAY + "–" + ChatColor.DARK_GREEN + " Please restart your server to finalize the process.");
                        });

                sender.sendMessage("");
            }

            return ReturnType.SUCCESS;
        } catch (IOException ex) {
            SongodaCore.getLogger().log(Level.WARNING, "Failed to check product verification status", ex);
            return ReturnType.FAILURE;
        }
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        return Collections.emptyList();
    }

    @Override
    public String getPermissionNode() {
        return "songoda.admin";
    }

    @Override
    public String getSyntax() {
        return "/craftaro license";
    }

    @Override
    public String getDescription() {
        return "Returns your server's uuid";
    }
}
