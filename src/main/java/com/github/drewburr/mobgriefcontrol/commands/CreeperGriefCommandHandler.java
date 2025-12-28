package com.github.drewburr.mobgriefcontrol.commands;

/**
 * Handles the creeper grief command
 */
public class CreeperGriefCommandHandler extends BooleanToggleCommandHandler {

	public CreeperGriefCommandHandler() {
		super("do_creeper_grief", "creeper");
	}

	@Override
	public String getCommandName() {
		return "creepergrief";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}
}
