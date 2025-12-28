package com.github.drewburr.mobgriefcontrol.commands;

public class SnowGolemTrailCommandHandler extends BooleanToggleCommandHandler {

	public SnowGolemTrailCommandHandler() {
		super("do_snowgolem_snow_trail", "snowgolem");
	}

	@Override
	public String getCommandName() {
		return "do_snowgolem_snow_trail";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}
}
