package com.github.drewburr.mobgriefcontrol.commands;

/**
 * Handles the wandering trader spawn command
 */
public class WanderingTraderCommandHandler extends BooleanToggleCommandHandler {

	public WanderingTraderCommandHandler() {
		super("spawn_wandering_trader", "wandering_trader");
	}

	@Override
	public String getCommandName() {
		return "spawn_wandering_trader";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}
}
