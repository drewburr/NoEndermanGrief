package com.github.drewburr.mobgriefcontrol.commands;

/**
 * Handles the creeper grief command
 */
public class CreeperGriefCommandHandler extends BooleanToggleCommandHandler {

	public CreeperGriefCommandHandler() {
		super("do_creeper_explode", "creeper");
	}

	@Override
	public String getCommandName() {
		return "do_creeper_explode";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}
}
