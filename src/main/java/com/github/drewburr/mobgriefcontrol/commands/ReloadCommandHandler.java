package com.github.drewburr.mobgriefcontrol.commands;

import java.io.File;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import com.github.drewburr.mobgriefcontrol.MobGriefControl;
import com.github.drewburr.mobgriefcontrol.common.PluginLibrary;
import com.github.drewburr.mobgriefcontrol.common.error.Report;

/**
 * Handles the reload command
 */
public class ReloadCommandHandler implements CommandHandler {

	@Override
	public boolean handle(MobGriefControl plugin, CommandSender sender, String[] args) {
		if (!sender.hasPermission("mobgriefcontrol.op") && !sender.isOp() &&
			!sender.hasPermission("mobgriefcontrol.admin") && sender instanceof Player) {
			sender.sendMessage("" + plugin.get("mobgriefcontrol.message.no_perm"));
			return false;
		}

		YamlConfiguration oldconfig = new YamlConfiguration();
		MobGriefControl.LOGGER.log("Checking config file version...");

		try {
			oldconfig.load(new File(plugin.getDataFolder() + "" + File.separatorChar + "config.yml"));
		} catch (Exception exception) {
			MobGriefControl.LOGGER.warn("Could not load config.yml");
			MobGriefControl.reporter.reportDetailed(plugin, Report.newBuilder(PluginLibrary.REPORT_CANNOT_CHECK_CONFIG).error(exception));
		}

		String configVersion = "2.0.0";
		String checkconfigversion = oldconfig.getString("version", "1.0.0");

		if (checkconfigversion != null) {
			if (!checkconfigversion.equalsIgnoreCase(configVersion)) {
				try {
					plugin.copyFile_Java7(plugin.getDataFolder() + "" + File.separatorChar + "config.yml",
						plugin.getDataFolder() + "" + File.separatorChar + "old_config.yml");
				} catch (Exception exception) {
					MobGriefControl.reporter.reportDetailed(plugin, Report.newBuilder(PluginLibrary.REPORT_CANNOT_CHECK_CONFIG).error(exception));
				}

				plugin.saveResource("config.yml", true);

				try {
					plugin.getConfig().load(new File(plugin.getDataFolder(), "config.yml"));
				} catch (Exception exception) {
					MobGriefControl.LOGGER.warn("Could not load config.yml");
					MobGriefControl.reporter.reportDetailed(plugin, Report.newBuilder(PluginLibrary.REPORT_CANNOT_CHECK_CONFIG).error(exception));
				}

				try {
					oldconfig.load(new File(plugin.getDataFolder(), "old_config.yml"));
				} catch (Exception exception) {
					MobGriefControl.LOGGER.warn("Could not load old_config.yml");
					MobGriefControl.reporter.reportDetailed(plugin, Report.newBuilder(PluginLibrary.REPORT_CANNOT_LOAD_CONFIG).error(exception));
				}

				plugin.getConfig().set("auto_update_check", oldconfig.get("auto_update_check", true));
				plugin.getConfig().set("debug", oldconfig.get("debug", false));
				plugin.getConfig().set("lang", oldconfig.get("lang", "en_US"));
				// Migrate old keys to new descriptive names
				plugin.getConfig().set("do_enderman_pickup", oldconfig.get("do_enderman_grief", true));
				plugin.getConfig().set("do_creeper_explode", oldconfig.get("do_creeper_grief", true));
				plugin.getConfig().set("do_ghast_explode", oldconfig.get("do_ghast_grief", true));
				plugin.getConfig().set("do_wither_explode", oldconfig.get("do_wither_grief", true));
				plugin.getConfig().set("do_dragon_destroy", oldconfig.get("do_dragon_grief", true));
				plugin.getConfig().set("do_zombie_break_doors", oldconfig.get("do_zombie_door_break", true));
				plugin.getConfig().set("do_villager_farm", oldconfig.get("do_villager_farming", true));
				plugin.getConfig().set("do_sheep_eat_grass", oldconfig.get("do_sheep_eat_grass", true));
				plugin.getConfig().set("do_rabbit_eat_crops", oldconfig.get("do_rabbit_eat_crops", true));
				plugin.getConfig().set("do_fox_pickup_items", oldconfig.get("do_fox_pickup", true));
				plugin.getConfig().set("do_snowgolem_snow_trail", oldconfig.get("do_snowgolem_trail", true));
				plugin.getConfig().set("do_silverfish_infest_blocks", oldconfig.get("do_silverfish_blocks", true));
				plugin.getConfig().set("do_ravager_destroy_crops", oldconfig.get("do_ravager_grief", true));
				plugin.getConfig().set("do_endcrystal_explode", oldconfig.get("do_endcrystal_grief", true));

				try {
					plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));
				} catch (Exception exception) {
					MobGriefControl.LOGGER.warn("Could not save old settings to config.yml");
					MobGriefControl.reporter.reportDetailed(plugin, Report.newBuilder(PluginLibrary.REPORT_CANNOT_SAVE_CONFIG).error(exception));
				}

				MobGriefControl.LOGGER.log("config.yml Updated! old config saved as old_config.yml");
				MobGriefControl.LOGGER.log("chance_config.yml saved.");
			} else {
				try {
					plugin.getConfig().load(new File(plugin.getDataFolder(), "config.yml"));
				} catch (Exception exception) {
					MobGriefControl.LOGGER.warn("Could not load config.yml");
					MobGriefControl.reporter.reportDetailed(plugin, Report.newBuilder(PluginLibrary.REPORT_CANNOT_LOAD_CONFIG).error(exception));
				}
			}
		}

		MobGriefControl.LOGGER.log("Loading config file...");
		try {
			plugin.getConfig().load(new File(plugin.getDataFolder(), "config.yml"));
		} catch (Exception exception) {
			MobGriefControl.reporter.reportDetailed(plugin, Report.newBuilder(PluginLibrary.REPORT_CANNOT_CHECK_CONFIG).error(exception));
		}

		try {
			plugin.getConfig().load(new File(plugin.getDataFolder(), "config.yml"));
		} catch (Exception exception) {
			MobGriefControl.LOGGER.warn("Could not load config.yml");
			MobGriefControl.reporter.reportDetailed(plugin, Report.newBuilder(PluginLibrary.REPORT_CANNOT_CHECK_CONFIG).error(exception));
		}

		MobGriefControl.debug = plugin.getConfig().getBoolean("debug", false);
		MobGriefControl.daLang = plugin.getConfig().getString("lang", "en_US");
		plugin.reloadConfig();

		sender.sendMessage(MobGriefControl.THIS_NAME + " has been " + "reloaded");
		return true;
	}

	@Override
	public String getCommandName() {
		return "reload";
	}
}
