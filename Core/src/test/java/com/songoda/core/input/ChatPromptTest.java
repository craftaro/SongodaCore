package com.songoda.core.input;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.MockPlugin;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled("BukkitMock seems to cause some troubles here, skipping for now")
class ChatPromptTest {
    private final String inputMsg = "&eInput-Message";

    private ServerMock server;
    private MockPlugin plugin;
    private PlayerMock player;

    @BeforeEach
    void setUp() {
        this.server = MockBukkit.mock();
        this.plugin = MockBukkit.createMockPlugin();
        this.player = this.server.addPlayer();
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void showPrompt() throws InterruptedException {
        List<String> chatInputs = new ArrayList<>(1);

        ChatPrompt.showPrompt(this.plugin, this.player, event -> {
            assertEquals(this.player, event.getPlayer());

            chatInputs.add(event.getMessage());
        });

        this.player.chat(this.inputMsg);
        Thread.sleep(1000);
        // this.server.getScheduler().waitAsyncTasksFinished() // does not wait for async events

        String playerReceivedMsg = this.player.nextMessage();

        assertNotNull(playerReceivedMsg);
        assertTrue(playerReceivedMsg.endsWith(this.inputMsg));

        assertEquals(1, chatInputs.size());
        assertEquals(this.inputMsg, chatInputs.get(0));
    }

    /* FIXME: Something is still running in the background and prevents the test from finishing */
    @Disabled("Scheduling mock seems bugged, skipping for now")
    @Test
    void showPromptWithTimeout() {
        AtomicBoolean calledOnClose = new AtomicBoolean(false);

        ChatPrompt.showPrompt(this.plugin, this.player, event -> {
                })
                .setOnClose(() -> calledOnClose.set(true))
                .setTimeOut(this.player, 40);

        this.server.getScheduler().performTicks(40);

        String playerReceivedMsg = this.player.nextMessage();

        assertNotNull(playerReceivedMsg);
        assertTrue(playerReceivedMsg.contains("timed out"));

        this.server.getScheduler().performOneTick();
        assertTrue(calledOnClose.get());
    }

    @Test
    void cancelPrompt() {
        AtomicBoolean calledOnCancel = new AtomicBoolean(false);
        AtomicBoolean calledHandler = new AtomicBoolean(false);

        ChatPrompt prompt = ChatPrompt.showPrompt(plugin, player, (event) -> calledHandler.set(true));
        prompt.setOnCancel(() -> calledOnCancel.set(true));

        this.server.dispatchCommand(player, "cancel");
        // this.player.chat("/cancel");
//        Thread.sleep(1000);
        // this.server.getScheduler().waitAsyncTasksFinished() // does not wait for async events

        System.out.println(this.player.nextMessage());

//        assertTrue(player.nextMessage().endsWith("/cancel"));

        assertTrue(calledOnCancel.get());
        assertFalse(calledHandler.get());
    }

    @Test
    void isRegistered() {
        assertFalse(ChatPrompt.isRegistered(this.player));

        ChatPrompt.showPrompt(this.plugin, this.player, (event) -> {
        });

        assertTrue(ChatPrompt.isRegistered(this.player));
    }

    @Test
    void unregister() {
        assertFalse(ChatPrompt.unregister(this.player));

        ChatPrompt.showPrompt(this.plugin, this.player, (event) -> {
        });

        assertTrue(ChatPrompt.unregister(this.player));
        assertFalse(ChatPrompt.unregister(this.player));
    }
}
