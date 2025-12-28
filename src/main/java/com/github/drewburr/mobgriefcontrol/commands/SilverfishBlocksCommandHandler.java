package com.github.drewburr.mobgriefcontrol.commands;

public class SilverfishBlocksCommandHandler extends BooleanToggleCommandHandler {

	public SilverfishBlocksCommandHandler() {
		super("do_silverfish_infest_blocks", "silverfish");
	}

	@Override
	public String getCommandName() {
		return "do_silverfish_infest_blocks";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}
}
