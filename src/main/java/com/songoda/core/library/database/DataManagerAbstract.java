package com.songoda.core.library.database;

import com.songoda.ultimateclaims.claim.Claim;
import com.songoda.ultimateclaims.claim.ClaimSettings;
import com.songoda.ultimateclaims.claim.ClaimedChunk;
import com.songoda.ultimateclaims.member.ClaimMember;
import com.songoda.ultimateclaims.member.ClaimPerm;
import com.songoda.ultimateclaims.member.ClaimPermissions;
import com.songoda.ultimateclaims.member.ClaimRole;
import com.songoda.ultimateclaims.settings.PluginSettings;
import com.songoda.ultimateclaims.utils.ItemSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.sql.*;
import java.util.*;
import java.util.function.Consumer;

public class DataManager {

    private final DatabaseConnector databaseConnector;
    private final Plugin plugin;

    public DataManager(DatabaseConnector databaseConnector, Plugin plugin) {
        this.databaseConnector = databaseConnector;
        this.plugin = plugin;
    }

    /**
     * @return the prefix to be used by all table names
     */
    public String getTablePrefix() {
        return this.plugin.getDescription().getName().toLowerCase() + '_';
    }

    public void createOrUpdatePluginSettings(PluginSettings pluginSettings) {
        this.async(() -> this.databaseConnector.connect(connection -> {
            // first check to see if there is a data row for plugin settings
            String selectPluginSettings = "SELECT * FROM " + this.getTablePrefix() + "plugin_settings";
            try (Statement statement = connection.createStatement()) {
                ResultSet result = statement.executeQuery(selectPluginSettings);
                if (!result.next()) {
                    // no data, so let's make some!
                    String createPluginSettings = "INSERT INTO " + this.getTablePrefix() + "plugin_settings (spawn_world, spawn_x, spawn_y, spawn_z, spawn_pitch, spawn_yaw) VALUES (?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement createStatement = connection.prepareStatement(createPluginSettings)) {
                        createStatement.setString(1, null);
                        createStatement.setNull(2, Types.DOUBLE);
                        createStatement.setNull(3, Types.DOUBLE);
                        createStatement.setNull(4, Types.DOUBLE);
                        createStatement.setNull(5, Types.DOUBLE);
                        createStatement.setNull(6, Types.DOUBLE);
                        createStatement.executeUpdate();
                    }
                }
            }

            String updatePluginSettings = "UPDATE " + this.getTablePrefix() + "plugin_settings "
                    + "SET spawn_world = ?, spawn_x = ?, spawn_y = ?, spawn_z = ?, spawn_pitch = ?, spawn_yaw = ?";
            try (PreparedStatement statement = connection.prepareStatement(updatePluginSettings)) {
                if (pluginSettings.getSpawnPoint() != null) {
                    statement.setString(1, pluginSettings.getSpawnPoint().getWorld().getName());
                    statement.setDouble(2, pluginSettings.getSpawnPoint().getX());
                    statement.setDouble(3, pluginSettings.getSpawnPoint().getY());
                    statement.setDouble(4, pluginSettings.getSpawnPoint().getZ());
                    statement.setDouble(5, pluginSettings.getSpawnPoint().getPitch());
                    statement.setDouble(6, pluginSettings.getSpawnPoint().getYaw());
                } else {
                    statement.setString(1, null);
                    statement.setNull(2, Types.DOUBLE);
                    statement.setNull(3, Types.DOUBLE);
                    statement.setNull(4, Types.DOUBLE);
                    statement.setNull(5, Types.DOUBLE);
                    statement.setNull(6, Types.DOUBLE);
                }
                statement.executeUpdate();
            }
        }));
    }

    public void createClaim(Claim claim) {
        this.async(() -> this.databaseConnector.connect(connection -> {
            String createClaim = "INSERT INTO " + this.getTablePrefix() + "claim (name, power, eco_bal, locked) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(createClaim)) {
                statement.setString(1, claim.getName());
                statement.setInt(2, claim.getPowerCell().getCurrentPower());
                statement.setDouble(3, claim.getPowerCell().getEconomyBalance());
                statement.setInt(4, claim.isLocked() ? 1 : 0);
                statement.executeUpdate();
            }

            int claimId = this.lastInsertedId(connection);

            this.sync(() -> claim.setId(claimId));

            String createMemberOwner = "INSERT INTO " + this.getTablePrefix() + "member (claim_id, player_uuid, role, play_time, member_since) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(createMemberOwner)) {
                statement.setInt(1, claimId);
                statement.setString(2, claim.getOwner().getUniqueId().toString());
                statement.setInt(3, claim.getOwner().getRole().getIndex());
                statement.setLong(4, claim.getOwner().getPlayTime());
                statement.setLong(5, claim.getOwner().getMemberSince());
                statement.executeUpdate();
            }

            ClaimedChunk chunk = claim.getFirstClaimedChunk();

            String createChunk = "INSERT INTO " + this.getTablePrefix() + "chunk (claim_id, world, x, z) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(createChunk)) {
                statement.setInt(1, claimId);
                statement.setString(2, chunk.getChunk().getWorld().getName());
                statement.setInt(3, chunk.getChunk().getX());
                statement.setInt(4, chunk.getChunk().getZ());
                statement.executeUpdate();
            }

            String createSettings = "INSERT INTO " + this.getTablePrefix() + "settings (claim_id, hostile_mob_spawning, fire_spread, mob_griefing, leaf_decay, pvp) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(createSettings)) {
                statement.setInt(1, claimId);
                statement.setInt(2, claim.getClaimSettings().isHostileMobSpawning() ? 1 : 0);
                statement.setInt(3, claim.getClaimSettings().isFireSpread() ? 1 : 0);
                statement.setInt(4, claim.getClaimSettings().isMobGriefingAllowed() ? 1 : 0);
                statement.setInt(5, claim.getClaimSettings().isLeafDecay() ? 1 : 0);
                statement.setInt(6, claim.getClaimSettings().isPvp() ? 1 : 0);
                statement.executeUpdate();
            }

            String createMemberPermissions = "INSERT INTO " + this.getTablePrefix() + "permissions (claim_id, type, interact, break, place, mob_kill, redstone, doors) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(createMemberPermissions)) {
                statement.setInt(1, claimId);
                statement.setString(2, "member");
                statement.setInt(3, claim.getMemberPermissions().hasPermission(ClaimPerm.INTERACT) ? 1 : 0);
                statement.setInt(4, claim.getMemberPermissions().hasPermission(ClaimPerm.BREAK) ? 1 : 0);
                statement.setInt(5, claim.getMemberPermissions().hasPermission(ClaimPerm.PLACE) ? 1 : 0);
                statement.setInt(6, claim.getMemberPermissions().hasPermission(ClaimPerm.MOB_KILLING) ? 1 : 0);
                statement.setInt(7, claim.getMemberPermissions().hasPermission(ClaimPerm.REDSTONE) ? 1 : 0);
                statement.setInt(8, claim.getMemberPermissions().hasPermission(ClaimPerm.DOORS) ? 1 : 0);
                statement.executeUpdate();
            }

            String createVisitorPermissions = "INSERT INTO " + this.getTablePrefix() + "permissions (claim_id, type, interact, break, place, mob_kill, redstone, doors) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(createVisitorPermissions)) {
                statement.setInt(1, claimId);
                statement.setString(2, "visitor");
                statement.setInt(3, claim.getVisitorPermissions().hasPermission(ClaimPerm.INTERACT) ? 1 : 0);
                statement.setInt(4, claim.getVisitorPermissions().hasPermission(ClaimPerm.BREAK) ? 1 : 0);
                statement.setInt(5, claim.getVisitorPermissions().hasPermission(ClaimPerm.PLACE) ? 1 : 0);
                statement.setInt(6, claim.getVisitorPermissions().hasPermission(ClaimPerm.MOB_KILLING) ? 1 : 0);
                statement.setInt(7, claim.getVisitorPermissions().hasPermission(ClaimPerm.REDSTONE) ? 1 : 0);
                statement.setInt(8, claim.getVisitorPermissions().hasPermission(ClaimPerm.DOORS) ? 1 : 0);
                statement.executeUpdate();
            }
        }));
    }

    public void updateClaim(Claim claim) {
        this.async(() -> this.databaseConnector.connect(connection -> {
            String updateClaim = "UPDATE " + this.getTablePrefix() + "claim SET name = ?, power = ?, eco_bal = ?, locked = ?, home_world = ?, home_x = ?, home_y = ?, home_z = ?, home_pitch = ?, home_yaw = ?, powercell_world = ?, powercell_x = ?, powercell_y = ?, powercell_z = ?, powercell_inventory = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(updateClaim)) {
                statement.setString(1, claim.getName());
                statement.setInt(2, claim.getPowerCell().getCurrentPower());
                statement.setDouble(3, claim.getPowerCell().getEconomyBalance());
                statement.setInt(4, claim.isLocked() ? 1 : 0);

                if (claim.getHome() != null) {
                    Location location = claim.getHome();
                    statement.setString(5, location.getWorld().getName());
                    statement.setDouble(6, location.getX());
                    statement.setDouble(7, location.getY());
                    statement.setDouble(8, location.getZ());
                    statement.setDouble(9, location.getPitch());
                    statement.setDouble(10, location.getYaw());
                } else {
                    statement.setString(5, null);
                    statement.setNull(6, Types.DOUBLE);
                    statement.setNull(7, Types.DOUBLE);
                    statement.setNull(8, Types.DOUBLE);
                    statement.setNull(9, Types.DOUBLE);
                    statement.setNull(10, Types.DOUBLE);
                }

                if (claim.getPowerCell().hasLocation()) {
                    Location location = claim.getPowerCell().getLocation();
                    statement.setString(11, location.getWorld().getName());
                    statement.setInt(12, location.getBlockX());
                    statement.setInt(13, location.getBlockY());
                    statement.setInt(14, location.getBlockZ());
                    statement.setString(15, ItemSerializer.toBase64(claim.getPowerCell().getItems()));
                } else {
                    statement.setString(11, null);
                    statement.setNull(12, Types.INTEGER);
                    statement.setNull(13, Types.INTEGER);
                    statement.setNull(14, Types.INTEGER);
                    statement.setString(15, null);
                }

                statement.setInt(16, claim.getId());
                statement.executeUpdate();
            }
        }));
    }

    public void bulkUpdateClaims(Collection<Claim> claims) {
        this.databaseConnector.connect(connection -> {
            String updateClaim = "UPDATE " + this.getTablePrefix() + "claim SET name = ?, power = ?, eco_bal = ?, locked = ?, home_world = ?, home_x = ?, home_y = ?, home_z = ?, home_pitch = ?, home_yaw = ?, powercell_world = ?, powercell_x = ?, powercell_y = ?, powercell_z = ?, powercell_inventory = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(updateClaim)) {
                for (Claim claim : claims) {
                    statement.setString(1, claim.getName());
                    statement.setInt(2, claim.getPowerCell().getCurrentPower());
                    statement.setDouble(3, claim.getPowerCell().getEconomyBalance());
                    statement.setInt(4, claim.isLocked() ? 1 : 0);

                    if (claim.getHome() != null) {
                        Location location = claim.getHome();
                        statement.setString(5, location.getWorld().getName());
                        statement.setDouble(6, location.getX());
                        statement.setDouble(7, location.getY());
                        statement.setDouble(8, location.getZ());
                        statement.setDouble(9, location.getPitch());
                        statement.setDouble(10, location.getYaw());
                    } else {
                        statement.setString(5, null);
                        statement.setNull(6, Types.DOUBLE);
                        statement.setNull(7, Types.DOUBLE);
                        statement.setNull(8, Types.DOUBLE);
                        statement.setNull(9, Types.DOUBLE);
                        statement.setNull(10, Types.DOUBLE);
                    }

                    if (claim.getPowerCell().hasLocation()) {
                        Location location = claim.getPowerCell().getLocation();
                        statement.setString(11, location.getWorld().getName());
                        statement.setInt(12, location.getBlockX());
                        statement.setInt(13, location.getBlockY());
                        statement.setInt(14, location.getBlockZ());
                        statement.setString(15, ItemSerializer.toBase64(claim.getPowerCell().getItems()));
                    } else {
                        statement.setString(11, null);
                        statement.setNull(12, Types.INTEGER);
                        statement.setNull(13, Types.INTEGER);
                        statement.setNull(14, Types.INTEGER);
                        statement.setString(15, null);
                    }

                    statement.setInt(16, claim.getId());
                    statement.addBatch();
                }

                statement.executeBatch();
            }

            String updateMember = "UPDATE " + this.getTablePrefix() + "member SET play_time = ?, player_name = ? WHERE claim_id = ? AND player_uuid = ?";
            try (PreparedStatement statement = connection.prepareStatement(updateMember)) {
                for (Claim claim : claims) {
                    for (ClaimMember member : claim.getOwnerAndMembers()) {
                        statement.setLong(1, member.getPlayTime());
                        if(member.getName() == null)
                            statement.setNull(2, Types.VARCHAR);
                        else
                            statement.setString(2, member.getName());
                        statement.setInt(3, claim.getId());
                        statement.setString(4, member.getUniqueId().toString());
                        statement.addBatch();
                    }
                }

                statement.executeBatch();
            }
        });
    }

    public void deleteClaim(Claim claim) {
        this.async(() -> this.databaseConnector.connect(connection -> {
            String deleteClaim = "DELETE FROM " + this.getTablePrefix() + "claim WHERE id = ?";
            String deleteMembers = "DELETE FROM " + this.getTablePrefix() + "member WHERE claim_id = ?";
            String deleteBans = "DELETE FROM " + this.getTablePrefix() + "ban WHERE claim_id = ?";
            String deleteChunks = "DELETE FROM " + this.getTablePrefix() + "chunk WHERE claim_id = ?";
            String deleteSettings = "DELETE FROM " + this.getTablePrefix() + "settings WHERE claim_id = ?";
            String deletePermissions = "DELETE FROM " + this.getTablePrefix() + "permissions WHERE claim_id = ?";

            try (PreparedStatement statement = connection.prepareStatement(deleteClaim)) {
                statement.setInt(1, claim.getId());
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(deleteMembers)) {
                statement.setInt(1, claim.getId());
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(deleteBans)) {
                statement.setInt(1, claim.getId());
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(deleteChunks)) {
                statement.setInt(1, claim.getId());
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(deleteSettings)) {
                statement.setInt(1, claim.getId());
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(deletePermissions)) {
                statement.setInt(1, claim.getId());
                statement.executeUpdate();
            }
        }));
    }

    public void createMember(ClaimMember member) {
        this.async(() -> this.databaseConnector.connect(connection -> {
            String createMember = "INSERT INTO " + this.getTablePrefix() + "member (claim_id, player_uuid, player_name, role, play_time, member_since) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(createMember)) {
                statement.setInt(1, member.getClaim().getId());
                statement.setString(2, member.getUniqueId().toString());
                if(member.getName() == null)
                    statement.setNull(3, Types.VARCHAR);
                else
                    statement.setString(3, member.getName());
                statement.setInt(4, member.getRole().getIndex());
                statement.setLong(5, member.getPlayTime());
                statement.setLong(6, member.getMemberSince());
                statement.executeUpdate();
            }
        }));
    }

    public void deleteMember(ClaimMember member) {
        this.async(() -> this.databaseConnector.connect(connection -> {
            String deleteMember = "DELETE FROM " + this.getTablePrefix() + "member WHERE player_uuid = ? AND claim_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(deleteMember)) {
                statement.setString(1, member.getUniqueId().toString());
                statement.setInt(2, member.getClaim().getId());
                statement.executeUpdate();
            }
        }));
    }

    public void createBan(Claim claim, UUID playerUUID) {
        this.async(() -> this.databaseConnector.connect(connection -> {
            String createBan = "INSERT INTO " + this.getTablePrefix() + "ban (claim_id, player_uuid) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(createBan)) {
                statement.setInt(1, claim.getId());
                statement.setString(2, playerUUID.toString());
                statement.executeUpdate();
            }
        }));
    }

    public void deleteBan(Claim claim, UUID playerUUID) {
        this.async(() -> this.databaseConnector.connect(connection -> {
            String deleteBan = "DELETE FROM " + this.getTablePrefix() + "ban WHERE claim_id = ? AND player_uuid = ?";
            try (PreparedStatement statement = connection.prepareStatement(deleteBan)) {
                statement.setInt(1, claim.getId());
                statement.setString(2, playerUUID.toString());
                statement.executeUpdate();
            }
        }));
    }

    public void createChunk(ClaimedChunk chunk) {
        this.async(() -> this.databaseConnector.connect(connection -> {
            String createChunk = "INSERT INTO " + this.getTablePrefix() + "chunk (claim_id, world, x, z) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(createChunk)) {
                statement.setInt(1, chunk.getClaim().getId());
                statement.setString(2, chunk.getWorld());
                statement.setInt(3, chunk.getX());
                statement.setInt(4, chunk.getZ());
                statement.executeUpdate();
            }
        }));
    }

    public void deleteChunk(ClaimedChunk chunk) {
        this.async(() -> this.databaseConnector.connect(connection -> {
            String deleteChunk = "DELETE FROM " + this.getTablePrefix() + "chunk WHERE claim_id = ? AND world = ? AND x = ? and z = ?";
            try (PreparedStatement statement = connection.prepareStatement(deleteChunk)) {
                statement.setInt(1, chunk.getClaim().getId());
                statement.setString(2, chunk.getWorld());
                statement.setInt(3, chunk.getX());
                statement.setInt(4, chunk.getZ());
                statement.executeUpdate();
            }
        }));
    }

    public void updateSettings(Claim claim, ClaimSettings settings) {
        this.async(() -> this.databaseConnector.connect(connection -> {
            String updateClaim = "UPDATE " + this.getTablePrefix() + "settings SET hostile_mob_spawning = ?, fire_spread = ?, mob_griefing = ?, leaf_decay = ?, pvp = ? WHERE claim_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(updateClaim)) {
                statement.setInt(1, settings.isHostileMobSpawning() ? 1 : 0);
                statement.setInt(2, settings.isFireSpread() ? 1 : 0);
                statement.setInt(3, settings.isMobGriefingAllowed() ? 1 : 0);
                statement.setInt(4, settings.isLeafDecay() ? 1 : 0);
                statement.setInt(5, settings.isPvp() ? 1 : 0);
                statement.setInt(6, claim.getId());
                statement.executeUpdate();
            }
        }));
    }

    public void updatePermissions(Claim claim, ClaimPermissions permissions, ClaimRole role) {
        this.async(() -> this.databaseConnector.connect(connection -> {
            String updateClaim = "UPDATE " + this.getTablePrefix() + "permissions SET interact = ?, break = ?, place = ?, mob_kill = ?, redstone = ?, doors = ? WHERE claim_id = ? AND type = ?";
            try (PreparedStatement statement = connection.prepareStatement(updateClaim)) {
                statement.setInt(1, permissions.hasPermission(ClaimPerm.INTERACT) ? 1 : 0);
                statement.setInt(2, permissions.hasPermission(ClaimPerm.BREAK) ? 1 : 0);
                statement.setInt(3, permissions.hasPermission(ClaimPerm.PLACE) ? 1 : 0);
                statement.setInt(4, permissions.hasPermission(ClaimPerm.MOB_KILLING) ? 1 : 0);
                statement.setInt(5, permissions.hasPermission(ClaimPerm.REDSTONE) ? 1 : 0);
                statement.setInt(6, permissions.hasPermission(ClaimPerm.DOORS) ? 1 : 0);
                statement.setInt(7, claim.getId());
                statement.setString(8, role.name().toLowerCase());
                statement.executeUpdate();
            }
        }));
    }

    public void getPluginSettings(Consumer<PluginSettings> callback) {
        this.async(() -> this.databaseConnector.connect(connection -> {
            String selectPluginSettings = "SELECT * FROM " + this.getTablePrefix() + "plugin_settings";
            try (Statement statement = connection.createStatement()) {
                ResultSet result = statement.executeQuery(selectPluginSettings);

                PluginSettings pluginSettings = new PluginSettings();
                if (result.next()) {
                    String world = result.getString("spawn_world");
                    if (world != null) {
                        double x = result.getDouble("spawn_x");
                        double y = result.getDouble("spawn_y");
                        double z = result.getDouble("spawn_z");
                        double pitch = result.getDouble("spawn_pitch");
                        double yaw = result.getDouble("spawn_yaw");
                        Location spawnPoint = new Location(Bukkit.getWorld(world), x, y, z, (float) pitch, (float) yaw);

                        pluginSettings.setSpawnPoint(spawnPoint);
                    }
                }

                this.sync(() -> callback.accept(pluginSettings));
            }
        }));
    }

    public void getClaims(Consumer<Map<UUID, Claim>> callback) {
        this.async(() -> this.databaseConnector.connect(connection -> {
            String selectClaims = "SELECT * FROM " + this.getTablePrefix() + "claim";
            String selectMembers = "SELECT * FROM " + this.getTablePrefix() + "member";
            String selectBans = "SELECT * FROM " + this.getTablePrefix() + "ban";
            String selectChunks = "SELECT * FROM " + this.getTablePrefix() + "chunk";
            String selectSettings = "SELECT * FROM " + this.getTablePrefix() + "settings";
            String selectPermissions = "SELECT * FROM " + this.getTablePrefix() + "permissions";

            Map<Integer, Claim> claims = new HashMap<>();

            try (Statement statement = connection.createStatement()) {
                ResultSet result = statement.executeQuery(selectClaims);
                while (result.next()) {
                    Claim claim = new Claim();

                    int claimId = result.getInt("id");
                    claim.setId(claimId);
                    claim.setName(result.getString("name"));

                    String homeWorld = result.getString("home_world");
                    if (homeWorld != null) {
                        double x = result.getDouble("home_x");
                        double y = result.getDouble("home_y");
                        double z = result.getDouble("home_z");
                        double pitch = result.getDouble("home_pitch");
                        double yaw = result.getDouble("home_yaw");
                        Location location = new Location(Bukkit.getWorld(homeWorld), x, y, z, (float) yaw, (float) pitch);
                        claim.setHome(location);
                    }

                    String powercellWorld = result.getString("powercell_world");
                    if (powercellWorld != null) {
                        double x = result.getDouble("powercell_x");
                        double y = result.getDouble("powercell_y");
                        double z = result.getDouble("powercell_z");
                        Location location = new Location(Bukkit.getWorld(powercellWorld), x, y, z);
                        claim.getPowerCell().setLocation(location);

                        List<ItemStack> items = ItemSerializer.fromBase64(result.getString("powercell_inventory"));
                        claim.getPowerCell().setItems(items);
                    }

                    claim.getPowerCell().setCurrentPower(result.getInt("power"));
                    claim.getPowerCell().setEconomyBalance(result.getDouble("eco_bal"));
                    claim.setLocked(result.getInt("locked") == 1);

                    claims.put(claimId, claim);
                }
            }

            try (Statement statement = connection.createStatement()) {
                ResultSet result = statement.executeQuery(selectMembers);
                while (result.next()) {
                    int claimId = result.getInt("claim_id");
                    Claim claim = claims.get(claimId);
                    if (claim == null)
                        continue;

                    UUID playerUUID = UUID.fromString(result.getString("player_uuid"));
                    ClaimRole role = ClaimRole.fromIndex(result.getInt("role"));

                    ClaimMember claimMember = new ClaimMember(claim, playerUUID, result.getString("player_name"), role);
                    claimMember.setPlayTime(result.getLong("play_time"));
                    claimMember.setMemberSince(result.getLong("member_since"));

                    claim.addMember(claimMember);

                    if (claimMember.getRole() == ClaimRole.OWNER)
                        claim.setOwner(claimMember);
                }
            }

            try (Statement statement = connection.createStatement()) {
                ResultSet result = statement.executeQuery(selectBans);
                while (result.next()) {
                    int claimId = result.getInt("claim_id");
                    Claim claim = claims.get(claimId);
                    if (claim == null)
                        continue;

                    UUID playerUUID = UUID.fromString(result.getString("player_uuid"));
                    claim.banPlayer(playerUUID);
                }
            }

            try (Statement statement = connection.createStatement()) {
                ResultSet result = statement.executeQuery(selectChunks);
                while (result.next()) {
                    int claimId = result.getInt("claim_id");
                    Claim claim = claims.get(claimId);
                    if (claim == null)
                        continue;

                    String world = result.getString("world");
                    int x = result.getInt("x");
                    int z = result.getInt("z");

                    claim.addClaimedChunk(world, x, z);
                }
            }

            try (Statement statement = connection.createStatement()) {
                ResultSet result = statement.executeQuery(selectSettings);
                while (result.next()) {
                    int claimId = result.getInt("claim_id");
                    Claim claim = claims.get(claimId);
                    if (claim == null)
                        continue;

                    claim.getClaimSettings()
                            .setHostileMobSpawning(result.getInt("hostile_mob_spawning") == 1)
                            .setFireSpread(result.getInt("fire_spread") == 1)
                            .setMobGriefingAllowed(result.getInt("mob_griefing") == 1)
                            .setLeafDecay(result.getInt("leaf_decay") == 1)
                            .setPvp(result.getInt("pvp") == 1);
                }
            }

            try (Statement statement = connection.createStatement()) {
                ResultSet result = statement.executeQuery(selectPermissions);
                while (result.next()) {
                    int claimId = result.getInt("claim_id");
                    Claim claim = claims.get(claimId);
                    if (claim == null)
                        continue;

                    ClaimPermissions permissions = new ClaimPermissions()
                            .setCanInteract(result.getInt("interact") == 1)
                            .setCanBreak(result.getInt("break") == 1)
                            .setCanPlace(result.getInt("place") == 1)
                            .setCanMobKill(result.getInt("mob_kill") == 1)
                            .setCanRedstone(result.getInt("redstone") == 1)
                            .setCanDoors(result.getInt("doors") == 1);

                    String type = result.getString("type");
                    switch (type) {
                        case "member":
                            claim.setMemberPermissions(permissions);
                            break;
                        case "visitor":
                            claim.setVisitorPermissions(permissions);
                            break;
                    }
                }
            }

            Map<UUID, Claim> returnClaims = new HashMap<>();
            for (Claim claim : claims.values())
                returnClaims.put(claim.getOwner().getUniqueId(), claim);

            this.sync(() -> callback.accept(returnClaims));
        }));
    }

    private int lastInsertedId(Connection connection) {
        String query;
        if (this.databaseConnector instanceof SQLiteConnector) {
            query = "SELECT last_insert_rowid()";
        } else {
            query = "SELECT LAST_INSERT_ID()";
        }

        try (Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(query);
            result.next();
            return result.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void async(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, runnable);
    }

    public void sync(Runnable runnable) {
        Bukkit.getScheduler().runTask(this.plugin, runnable);
    }

}
