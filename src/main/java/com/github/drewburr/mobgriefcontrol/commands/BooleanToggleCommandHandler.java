package com.github.drewburr.mobgriefcontrol.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.github.drewburr.mobgriefcontrol.MobGriefControl;
import com.github.drewburr.mobgriefcontrol.common.PluginLibrary;
import com.github.drewburr.mobgriefcontrol.common.error.Report;

/**
 * Base class for boolean toggle commands (entity grief/spawn settings)
 */
public abstract class BooleanToggleCommandHandler implements CommandHandler {

	private final String configKey;
	private final String entityName;

	protected BooleanToggleCommandHandler(String configKey, String entityName) {
		this.configKey = configKey;
		this.entityName = entityName;
	}

	@Override
	public boolean handle(MobGriefControl plugin, CommandSender sender, String[] args) {
		// Show current setting if no argument provided
		if (args.length <= 1) {
			sender.sendMessage(MobGriefControl.THIS_NAME + " " +
				plugin.get("mobgriefcontrol.entity." + entityName + ".current")
				.toString().replace("[setting]", "" + plugin.getConfig().getBoolean(configKey, false)));
			return true;
		}

		// Check permissions
		if (sender instanceof Player && !sender.hasPermission("mobgriefcontrol.admin")) {
			sender.sendMessage("" + plugin.get("mobgriefcontrol.message.noperm"));
			return false;
		}

		// Validate boolean argument
		if (!args[1].equalsIgnoreCase("true") && !args[1].equalsIgnoreCase("false")) {
			sender.sendMessage(MobGriefControl.THIS_NAME + " " + plugin.get("mobgriefcontrol.var.boolean") + ": /mobgriefcontrol " + getCommandName() + " True/False");
			return false;
		}

		// Set the config value
		plugin.getConfig().set(configKey, Boolean.parseBoolean(args[1]));
		plugin.saveConfig();

		sender.sendMessage(MobGriefControl.THIS_NAME + " " + plugin.get("mobgriefcontrol.entity." + entityName + ".set") + " " + args[1]);

		// Log the change
		if (args[1].equalsIgnoreCase("false")) {
			MobGriefControl.LOGGER.log(configKey + " has been set to " + args[1]);
		} else {
			MobGriefControl.LOGGER.log(configKey + " has been set to " + args[1]);
		}

		try {
			plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));
		} catch (Exception exception) {
			MobGriefControl.LOGGER.warn("Could not save settings to config.yml");
			MobGriefControl.reporter.reportDetailed(plugin, Report.newBuilder(PluginLibrary.REPORT_CANNOT_SAVE_CONFIG).error(exception));
		}

		return true;
	}

	@Override
	public List<String> getTabCompletions(String[] args) {
		List<String> completions = new ArrayList<>();
		if (args.length == 2) {
			completions.add("true");
			completions.add("false");
		}
		return completions;
	}
}
