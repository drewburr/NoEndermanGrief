package com.github.drewburr.mobgriefcontrol.commands;

public class WitherGriefCommandHandler extends BooleanToggleCommandHandler {

	public WitherGriefCommandHandler() {
		super("do_wither_explode", "wither");
	}

	@Override
	public String getCommandName() {
		return "do_wither_explode";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}
}
