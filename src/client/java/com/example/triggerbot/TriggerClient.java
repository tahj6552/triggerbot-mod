package com.example.triggerbot;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import org.lwjgl.glfw.GLFW;

public class TriggerClient implements ClientModInitializer {
	public static boolean enabled = false;
	private static KeyMapping guiKey;

	@Override
	public void onInitializeClient() {
		guiKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"key.triggerbot.gui",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_PAGE_DOWN,
				KeyMapping.Category.MISC));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (guiKey.consumeClick()) {
				client.setScreen(new TriggerScreen());
			}
			tick(client);
		});
	}

	private static void tick(Minecraft mc) {
		if (!enabled || mc.player == null || mc.level == null
				|| mc.screen != null || mc.gameMode == null) return;

		if (!(mc.hitResult instanceof EntityHitResult hit)) return;
		if (!(hit.getEntity() instanceof Player target)) return;
		if (target == mc.player || !target.isAlive() || target.isSpectator()) return;

		// Wait for a full attack charge so it adapts to whatever item is held
		if (mc.player.getAttackStrengthScale(0.5f) < 1.0f) return;

		mc.gameMode.attack(mc.player, target);
		mc.player.swing(InteractionHand.MAIN_HAND);
	}
}
