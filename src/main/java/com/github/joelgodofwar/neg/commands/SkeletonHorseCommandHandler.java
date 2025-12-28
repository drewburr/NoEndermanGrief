package com.github.joelgodofwar.neg.commands;

/**
 * Handles the skeleton horse spawn command
 */
public class SkeletonHorseCommandHandler extends BooleanToggleCommandHandler {

	public SkeletonHorseCommandHandler() {
		super("skeleton_horse_spawn", "skeleton_horse");
	}

	@Override
	public String getCommandName() {
		return "skeletonhorse";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"sh"};
	}
}
