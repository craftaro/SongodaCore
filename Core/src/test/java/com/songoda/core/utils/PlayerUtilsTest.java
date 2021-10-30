package com.songoda.core.utils;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayerUtilsTest {
    private ServerMock server;

    @BeforeEach
    void setUp() {
        this.server = MockBukkit.mock();
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
        this.server = null;
    }

    @Test
    void sendMessages() {
        String[] messages = new String[] {"First message", "Second message"};

        PlayerMock player = this.server.addPlayer();

        PlayerUtils.sendMessages(player, messages);
        PlayerUtils.sendMessages(player, Arrays.asList(messages));

        for (int i = 0; i < 2; ++i) {
            assertEquals(messages[0], player.nextMessage());
            assertEquals(messages[1], player.nextMessage());
        }

        assertNull(player.nextMessage());
    }

    @Disabled("Player#hidePlayer can currently not be mocked")
    @Test
    void getVisiblePlayerNames() {
        PlayerMock player = this.server.addPlayer("BasePlayer");
        PlayerMock visiblePlayer = this.server.addPlayer("VisiblePlayer");
        PlayerMock hiddenPlayer = this.server.addPlayer("HiddenPlayer");

        player.hidePlayer(MockBukkit.createMockPlugin(), hiddenPlayer);

        List<String> result = PlayerUtils.getVisiblePlayerNames(player, null);
        assertTrue(result.contains(visiblePlayer.getName()));
        assertFalse(result.contains(hiddenPlayer.getName()));
        assertFalse(result.contains(player.getName()));

        assertEquals(0, PlayerUtils.getVisiblePlayerNames(player, "_").size());
    }

    @Disabled("Player#hidePlayer can currently not be mocked")
    @Test
    void getVisiblePlayerDisplayNames() {
        PlayerMock player = this.server.addPlayer("BasePlayer");
        PlayerMock visiblePlayer = this.server.addPlayer("VisiblePlayer");
        PlayerMock hiddenPlayer = this.server.addPlayer("HiddenPlayer");

        player.setDisplayName("Base");
        visiblePlayer.setDisplayName("Visible");
        hiddenPlayer.setDisplayName("Hidden");

        player.hidePlayer(MockBukkit.createMockPlugin(), hiddenPlayer);

        List<String> result = PlayerUtils.getVisiblePlayerDisplayNames(player, null);
        assertTrue(result.contains(visiblePlayer.getDisplayName()));
        assertFalse(result.contains(hiddenPlayer.getDisplayName()));
        assertFalse(result.contains(player.getDisplayName()));

        assertEquals(0, PlayerUtils.getVisiblePlayerDisplayNames(player, "_").size());
    }

    @Disabled("Player#hidePlayer can currently not be mocked")
    @Test
    void getVisiblePlayers() {
        PlayerMock player = this.server.addPlayer("BasePlayer");
        PlayerMock visiblePlayer = this.server.addPlayer("VisiblePlayer");
        PlayerMock hiddenPlayer = this.server.addPlayer("HiddenPlayer");

        player.hidePlayer(MockBukkit.createMockPlugin(), hiddenPlayer);

        List<Player> result = PlayerUtils.getVisiblePlayers(player, null);
        assertTrue(result.contains(visiblePlayer));
        assertFalse(result.contains(hiddenPlayer));
        assertFalse(result.contains(player));

        assertEquals(0, PlayerUtils.getVisiblePlayers(player, "_").size());
    }

    @Test
    void getAllPlayers() {
        PlayerMock basePlayer = this.server.addPlayer("BasePlayer");
        this.server.addPlayer("Player_1");
        this.server.addPlayer("Player_2");
        this.server.addPlayer("Player3");

        List<String> result = PlayerUtils.getAllPlayers(basePlayer, "");
        assertEquals(3, result.size());
        assertFalse(result.contains(basePlayer.getName()));

        assertTrue(PlayerUtils.getAllPlayers(basePlayer, "_").isEmpty());
        assertEquals(0, PlayerUtils.getAllPlayers(basePlayer, "Player_").size());
    }

    @Test
    void getAllPlayersDisplay() {
        PlayerMock basePlayer = this.server.addPlayer("BasePlayer");
        this.server.addPlayer("Player_1");
        this.server.addPlayer("Player_2");
        this.server.addPlayer("Player3");

        List<String> result = PlayerUtils.getAllPlayersDisplay(basePlayer, "");
        assertEquals(3, result.size());
        assertFalse(result.contains(basePlayer.getDisplayName()));

        assertEquals(2, PlayerUtils.getAllPlayersDisplay(basePlayer, "Player_").size());
        assertEquals(0, PlayerUtils.getAllPlayersDisplay(basePlayer, "_").size());
    }

    @Test
    void findPlayer() {
        Player p3 = this.server.addPlayer("Player");
        Player p1 = this.server.addPlayer("Player_1");
        Player p2 = this.server.addPlayer("_Player_2");

        p1.setDisplayName("p1");
        p2.setDisplayName("p2");
        p3.setDisplayName("p3");

        assertEquals(p1, PlayerUtils.findPlayer("Player_"));
        assertEquals(p2, PlayerUtils.findPlayer("_Play"));
        assertEquals(p3, PlayerUtils.findPlayer("Player"));

        assertEquals(p3, PlayerUtils.findPlayer("p"));
        assertEquals(p1, PlayerUtils.findPlayer("p1"));
        assertEquals(p2, PlayerUtils.findPlayer("p2"));
        assertEquals(p3, PlayerUtils.findPlayer("p3"));
    }

    @Test
    void getRandomPlayer() {
        assertNull(PlayerUtils.getRandomPlayer());

        for (int i = 0; i < 10; ++i) {
            this.server.addPlayer(String.valueOf(i));
        }

        Set<Player> returnedPlayers = new HashSet<>();
        for (int i = 0; i < 50; ++i) {
            if (returnedPlayers.size() >= 5) {
                break;
            }

            returnedPlayers.add(PlayerUtils.getRandomPlayer());
        }

        assertTrue(returnedPlayers.size() >= 5);
    }

    @Test
    void giveItem() {
        Player player = this.server.addPlayer();

        PlayerUtils.giveItem(player, new ItemStack(Material.STONE));
        assertTrue(player.getInventory().contains(Material.STONE, 1));

        PlayerUtils.giveItem(player, new ItemStack(Material.GRASS_BLOCK), new ItemStack(Material.GRASS_BLOCK));
        assertTrue(player.getInventory().contains(Material.GRASS_BLOCK, 2));

        PlayerUtils.giveItem(player, Arrays.asList(new ItemStack(Material.WHEAT_SEEDS), new ItemStack(Material.WHEAT_SEEDS)));
        assertTrue(player.getInventory().contains(Material.WHEAT_SEEDS, 2));
    }

    @Test
    void giveItemOnFullInventory() {
        PlayerMock player = this.server.addPlayer();

        fillInventory(player);

        int entityCount = this.server.getEntities().size();
        PlayerUtils.giveItem(player, new ItemStack(Material.STONE));
        assertEquals(entityCount + 1, this.server.getEntities().size());

        entityCount = this.server.getEntities().size();
        PlayerUtils.giveItem(player, new ItemStack(Material.GRASS_BLOCK), new ItemStack(Material.GRASS_BLOCK));
        assertEquals(entityCount + 2, this.server.getEntities().size());

        entityCount = this.server.getEntities().size();
        PlayerUtils.giveItem(player, Arrays.asList(new ItemStack(Material.WHEAT_SEEDS), new ItemStack(Material.WHEAT_SEEDS), new ItemStack(Material.WHEAT_SEEDS)));
        assertEquals(entityCount + 3, this.server.getEntities().size());
    }

    @Disabled("Test is incomplete")
    @Test
    void getNumberFromPermission() {
        Player player = this.server.addPlayer();

        assertEquals(-1, PlayerUtils.getNumberFromPermission(player, "example.plugin.feature", -1));
    }

    private void fillInventory(Player player) {
        ItemStack[] contents = new ItemStack[player.getInventory().getContents().length];

        for (int i = 0; i < contents.length; ++i) {
            contents[i] = new ItemStack(Material.BARRIER);
        }

        player.getInventory().setContents(contents);
    }
}
