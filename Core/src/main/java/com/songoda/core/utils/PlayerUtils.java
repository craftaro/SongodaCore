package com.songoda.core.utils;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.Set;

public class PlayerUtils {

    public static int getNumberFromPermission(Player player, String permission, int def) {
        final Set<PermissionAttachmentInfo> permissions = player.getEffectivePermissions();

        boolean set = false;
        int highest = 0;

        for (PermissionAttachmentInfo info : permissions) {
            final String perm = info.getPermission();

            if (!perm.startsWith(permission)) {
                continue;
            }

            final int index = perm.lastIndexOf('.');

            if (index == -1 || index == perm.length()) {
                continue;
            }

            String numStr = perm.substring(perm.lastIndexOf('.') + 1);
            if (numStr.equals("*")) {
                return def;
            }

            final int number = Integer.parseInt(numStr);

            if (number >= highest) {
                highest = number;
                set = true;
            }
        }

        return set ? highest : def;
    }

}
