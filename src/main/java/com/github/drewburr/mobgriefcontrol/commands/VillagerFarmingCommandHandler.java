package com.github.drewburr.mobgriefcontrol.commands;

public class VillagerFarmingCommandHandler extends BooleanToggleCommandHandler {

	public VillagerFarmingCommandHandler() {
		super("do_villager_farm", "villager");
	}

	@Override
	public String getCommandName() {
		return "do_villager_farm";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}
}
