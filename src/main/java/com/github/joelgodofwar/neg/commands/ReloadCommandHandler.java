package com.github.joelgodofwar.neg.commands;

import java.io.File;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import com.github.joelgodofwar.neg.NoEndermanGrief;
import com.github.joelgodofwar.neg.common.PluginLibrary;
import com.github.joelgodofwar.neg.common.error.Report;

/**
 * Handles the reload command
 */
public class ReloadCommandHandler implements CommandHandler {

	@Override
	public boolean handle(NoEndermanGrief plugin, CommandSender sender, String[] args) {
		if (!sender.hasPermission("noendermangrief.op") && !sender.isOp() &&
			!sender.hasPermission("noendermangrief.admin") && sender instanceof Player) {
			sender.sendMessage("" + plugin.get("neg.message.no_perm"));
			return false;
		}

		YamlConfiguration oldconfig = new YamlConfiguration();
		NoEndermanGrief.LOGGER.log("Checking config file version...");

		try {
			oldconfig.load(new File(plugin.getDataFolder() + "" + File.separatorChar + "config.yml"));
		} catch (Exception exception) {
			NoEndermanGrief.LOGGER.warn("Could not load config.yml");
			NoEndermanGrief.reporter.reportDetailed(plugin, Report.newBuilder(PluginLibrary.REPORT_CANNOT_CHECK_CONFIG).error(exception));
		}

		String configVersion = "1.0.6";
		String checkconfigversion = oldconfig.getString("version", "1.0.0");

		if (checkconfigversion != null) {
			if (!checkconfigversion.equalsIgnoreCase(configVersion)) {
				try {
					plugin.copyFile_Java7(plugin.getDataFolder() + "" + File.separatorChar + "config.yml",
						plugin.getDataFolder() + "" + File.separatorChar + "old_config.yml");
				} catch (Exception exception) {
					NoEndermanGrief.reporter.reportDetailed(plugin, Report.newBuilder(PluginLibrary.REPORT_CANNOT_CHECK_CONFIG).error(exception));
				}

				plugin.saveResource("config.yml", true);

				try {
					plugin.getConfig().load(new File(plugin.getDataFolder(), "config.yml"));
				} catch (Exception exception) {
					NoEndermanGrief.LOGGER.warn("Could not load config.yml");
					NoEndermanGrief.reporter.reportDetailed(plugin, Report.newBuilder(PluginLibrary.REPORT_CANNOT_CHECK_CONFIG).error(exception));
				}

				try {
					oldconfig.load(new File(plugin.getDataFolder(), "old_config.yml"));
				} catch (Exception exception) {
					NoEndermanGrief.LOGGER.warn("Could not load old_config.yml");
					NoEndermanGrief.reporter.reportDetailed(plugin, Report.newBuilder(PluginLibrary.REPORT_CANNOT_LOAD_CONFIG).error(exception));
				}

				plugin.getConfig().set("auto_update_check", oldconfig.get("auto_update_check", true));
				plugin.getConfig().set("debug", oldconfig.get("debug", false));
				plugin.getConfig().set("lang", oldconfig.get("lang", "en_US"));
				plugin.getConfig().set("console.longpluginname", oldconfig.get("console.longpluginname", true));
				plugin.getConfig().set("enderman_grief", oldconfig.get("enderman_grief", false));
				plugin.getConfig().set("skeleton_horse_spawn", oldconfig.get("skeleton_horse_spawn", false));
				plugin.getConfig().set("creeper_grief", oldconfig.get("creeper_grief", false));
				plugin.getConfig().set("wandering_trader_spawn", oldconfig.get("wandering_trader", false));
				plugin.getConfig().set("ghast_grief", oldconfig.get("ghast_grief", false));
				plugin.getConfig().set("phantom_spawn", oldconfig.get("phantom_spawn", false));
				plugin.getConfig().set("pillager_patrol_spawn", oldconfig.get("pillager_patrol_spawn", false));

				try {
					plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));
				} catch (Exception exception) {
					NoEndermanGrief.LOGGER.warn("Could not save old settings to config.yml");
					NoEndermanGrief.reporter.reportDetailed(plugin, Report.newBuilder(PluginLibrary.REPORT_CANNOT_SAVE_CONFIG).error(exception));
				}

				NoEndermanGrief.LOGGER.log("config.yml Updated! old config saved as old_config.yml");
				NoEndermanGrief.LOGGER.log("chance_config.yml saved.");
			} else {
				try {
					plugin.getConfig().load(new File(plugin.getDataFolder(), "config.yml"));
				} catch (Exception exception) {
					NoEndermanGrief.LOGGER.warn("Could not load config.yml");
					NoEndermanGrief.reporter.reportDetailed(plugin, Report.newBuilder(PluginLibrary.REPORT_CANNOT_LOAD_CONFIG).error(exception));
				}
			}
		}

		NoEndermanGrief.LOGGER.log("Loading config file...");
		try {
			plugin.getConfig().load(new File(plugin.getDataFolder(), "config.yml"));
		} catch (Exception exception) {
			NoEndermanGrief.reporter.reportDetailed(plugin, Report.newBuilder(PluginLibrary.REPORT_CANNOT_CHECK_CONFIG).error(exception));
		}

		try {
			plugin.getConfig().load(new File(plugin.getDataFolder(), "config.yml"));
		} catch (Exception exception) {
			NoEndermanGrief.LOGGER.warn("Could not load config.yml");
			NoEndermanGrief.reporter.reportDetailed(plugin, Report.newBuilder(PluginLibrary.REPORT_CANNOT_CHECK_CONFIG).error(exception));
		}

		NoEndermanGrief.debug = plugin.getConfig().getBoolean("debug", false);
		NoEndermanGrief.daLang = plugin.getConfig().getString("lang", "en_US");
		plugin.reloadConfig();

		sender.sendMessage(NoEndermanGrief.THIS_NAME + " has been " + "reloaded");
		return true;
	}

	@Override
	public String getCommandName() {
		return "reload";
	}
}
