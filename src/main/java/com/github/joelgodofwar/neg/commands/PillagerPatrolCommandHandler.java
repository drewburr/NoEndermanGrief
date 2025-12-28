package com.github.joelgodofwar.neg.commands;

/**
 * Handles the pillager patrol spawn command
 */
public class PillagerPatrolCommandHandler extends BooleanToggleCommandHandler {

	public PillagerPatrolCommandHandler() {
		super("pillager_patrol_spawn", "pillager_patrol");
	}

	@Override
	public String getCommandName() {
		return "pillagerpatrol";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"pp"};
	}
}
