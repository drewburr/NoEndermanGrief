package com.github.drewburr.mobgriefcontrol.commands;

import org.bukkit.command.CommandSender;
import com.github.drewburr.mobgriefcontrol.MobGriefControl;
import java.util.List;

/**
 * Base interface for command handlers
 */
public interface CommandHandler {

	/**
	 * Handle the command execution
	 * @param plugin The plugin instance
	 * @param sender The command sender
	 * @param args The command arguments
	 * @return true if the command was handled successfully
	 */
	boolean handle(MobGriefControl plugin, CommandSender sender, String[] args);

	/**
	 * Get the command name
	 * @return The command name
	 */
	String getCommandName();

	/**
	 * Get command aliases
	 * @return Array of command aliases
	 */
	default String[] getAliases() {
		return new String[0];
	}

	/**
	 * Get tab completions for this command
	 * @param args The command arguments
	 * @return List of tab completions
	 */
	default List<String> getTabCompletions(String[] args) {
		return null;
	}
}
