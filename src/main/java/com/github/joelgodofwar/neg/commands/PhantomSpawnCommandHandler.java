package com.github.joelgodofwar.neg.commands;

/**
 * Handles the phantom spawn command
 */
public class PhantomSpawnCommandHandler extends BooleanToggleCommandHandler {

	public PhantomSpawnCommandHandler() {
		super("phantom_spawn", "phantom");
	}

	@Override
	public String getCommandName() {
		return "phantomgrief";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"pg"};
	}
}
