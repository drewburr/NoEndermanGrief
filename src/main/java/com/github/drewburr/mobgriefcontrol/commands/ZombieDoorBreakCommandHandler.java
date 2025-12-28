package com.github.drewburr.mobgriefcontrol.commands;

public class ZombieDoorBreakCommandHandler extends BooleanToggleCommandHandler {

	public ZombieDoorBreakCommandHandler() {
		super("do_zombie_break_doors", "zombie");
	}

	@Override
	public String getCommandName() {
		return "do_zombie_break_doors";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}
}
