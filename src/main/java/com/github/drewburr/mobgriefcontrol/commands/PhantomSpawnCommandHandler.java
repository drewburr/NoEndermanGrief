package com.github.drewburr.mobgriefcontrol.commands;

/**
 * Handles the phantom spawn command
 */
public class PhantomSpawnCommandHandler extends BooleanToggleCommandHandler {

	public PhantomSpawnCommandHandler() {
		super("spawn_phantom", "phantom");
	}

	@Override
	public String getCommandName() {
		return "spawn_phantom";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}
}
