package com.github.drewburr.mobgriefcontrol.commands;

/**
 * Handles the pillager patrol spawn command
 */
public class PillagerPatrolCommandHandler extends BooleanToggleCommandHandler {

	public PillagerPatrolCommandHandler() {
		super("spawn_pillager_patrol", "pillager_patrol");
	}

	@Override
	public String getCommandName() {
		return "spawn_pillager_patrol";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}
}
