package com.github.joelgodofwar.neg.commands;

/**
 * Handles the creeper grief command
 */
public class CreeperGriefCommandHandler extends BooleanToggleCommandHandler {

	public CreeperGriefCommandHandler() {
		super("creeper_grief", "creeper");
	}

	@Override
	public String getCommandName() {
		return "creepergrief";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"cg"};
	}
}
