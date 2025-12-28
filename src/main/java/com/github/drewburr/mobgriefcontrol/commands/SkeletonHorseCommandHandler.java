package com.github.drewburr.mobgriefcontrol.commands;

/**
 * Handles the skeleton horse spawn command
 */
public class SkeletonHorseCommandHandler extends BooleanToggleCommandHandler {

	public SkeletonHorseCommandHandler() {
		super("spawn_skeleton_horse", "skeleton_horse");
	}

	@Override
	public String getCommandName() {
		return "spawn_skeleton_horse";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}
}
