package com.github.joelgodofwar.neg.commands;

/**
 * Handles the enderman grief command
 */
public class EndermanGriefCommandHandler extends BooleanToggleCommandHandler {

	public EndermanGriefCommandHandler() {
		super("enderman_grief", "enderman");
	}

	@Override
	public String getCommandName() {
		return "endermangrief";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"eg"};
	}
}
