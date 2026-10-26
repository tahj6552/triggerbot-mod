package com.example.triggerbot;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class TriggerScreen extends Screen {
	public TriggerScreen() {
		super(Component.literal("Triggerbot"));
	}

	@Override
	protected void init() {
		addRenderableWidget(Button.builder(label(), button -> {
			TriggerClient.enabled = !TriggerClient.enabled;
			button.setMessage(label());
		}).bounds(this.width / 2 - 60, this.height / 2 - 10, 120, 20).build());
	}

	private Component label() {
		return Component.literal("Triggerbot: " + (TriggerClient.enabled ? "ON" : "OFF"));
	}
}
