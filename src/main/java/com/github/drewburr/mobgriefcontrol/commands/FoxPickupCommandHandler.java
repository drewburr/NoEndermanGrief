package com.github.drewburr.mobgriefcontrol.commands;

public class FoxPickupCommandHandler extends BooleanToggleCommandHandler {

	public FoxPickupCommandHandler() {
		super("do_fox_pickup_items", "fox");
	}

	@Override
	public String getCommandName() {
		return "do_fox_pickup_items";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}
}
