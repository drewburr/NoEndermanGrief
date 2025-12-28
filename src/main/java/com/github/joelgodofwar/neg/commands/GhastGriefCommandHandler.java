package com.github.joelgodofwar.neg.commands;

/**
 * Handles the ghast grief command
 */
public class GhastGriefCommandHandler extends BooleanToggleCommandHandler {

	public GhastGriefCommandHandler() {
		super("ghast_grief", "ghast");
	}

	@Override
	public String getCommandName() {
		return "ghastgrief";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"gg"};
	}
}
