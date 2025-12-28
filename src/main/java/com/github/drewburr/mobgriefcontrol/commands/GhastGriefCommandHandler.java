package com.github.drewburr.mobgriefcontrol.commands;

/**
 * Handles the ghast grief command
 */
public class GhastGriefCommandHandler extends BooleanToggleCommandHandler {

	public GhastGriefCommandHandler() {
		super("do_ghast_explode", "ghast");
	}

	@Override
	public String getCommandName() {
		return "do_ghast_explode";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}
}
