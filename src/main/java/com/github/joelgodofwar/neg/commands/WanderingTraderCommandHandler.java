package com.github.joelgodofwar.neg.commands;

/**
 * Handles the wandering trader spawn command
 */
public class WanderingTraderCommandHandler extends BooleanToggleCommandHandler {

	public WanderingTraderCommandHandler() {
		super("wandering_trader_spawn", "wandering_trader");
	}

	@Override
	public String getCommandName() {
		return "wanderingtrader";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"wt"};
	}
}
