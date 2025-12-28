package com.github.drewburr.mobgriefcontrol.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.github.drewburr.mobgriefcontrol.MobGriefControl;

/**
 * Handles the config command
 */
public class ConfigCommandHandler implements CommandHandler {

	@Override
	public boolean handle(MobGriefControl plugin, CommandSender sender, String[] args) {
		if (!sender.isOp() && !sender.hasPermission("mobgriefcontrol.op") &&
			!sender.hasPermission("mobgriefcontrol.admin") && sender instanceof Player) {
			sender.sendMessage(MobGriefControl.THIS_NAME + " " + plugin.get("mobgriefcontrol.message.noperm"));
			return false;
		}

		sender.sendMessage(MobGriefControl.THIS_NAME + " Current Configuration:");
		sender.sendMessage("  auto_update_check: " + plugin.getConfig().getBoolean("auto_update_check"));
		sender.sendMessage("  debug: " + plugin.getConfig().getBoolean("debug"));
		sender.sendMessage("  lang: " + plugin.getConfig().getString("lang"));
		sender.sendMessage("  console.longpluginname: " + plugin.getConfig().getBoolean("console.longpluginname"));
		sender.sendMessage("  do_enderman_pickup: " + plugin.getConfig().getBoolean("do_enderman_pickup"));
		sender.sendMessage("  do_creeper_explode: " + plugin.getConfig().getBoolean("do_creeper_explode"));
		sender.sendMessage("  do_ghast_explode: " + plugin.getConfig().getBoolean("do_ghast_explode"));
		sender.sendMessage("  do_wither_explode: " + plugin.getConfig().getBoolean("do_wither_explode"));
		sender.sendMessage("  do_dragon_destroy: " + plugin.getConfig().getBoolean("do_dragon_destroy"));
		sender.sendMessage("  do_zombie_break_doors: " + plugin.getConfig().getBoolean("do_zombie_break_doors"));
		sender.sendMessage("  do_villager_farm: " + plugin.getConfig().getBoolean("do_villager_farm"));
		sender.sendMessage("  do_sheep_eat_grass: " + plugin.getConfig().getBoolean("do_sheep_eat_grass"));
		sender.sendMessage("  do_rabbit_eat_crops: " + plugin.getConfig().getBoolean("do_rabbit_eat_crops"));
		sender.sendMessage("  do_fox_pickup_items: " + plugin.getConfig().getBoolean("do_fox_pickup_items"));
		sender.sendMessage("  do_snowgolem_snow_trail: " + plugin.getConfig().getBoolean("do_snowgolem_snow_trail"));
		sender.sendMessage("  do_silverfish_infest_blocks: " + plugin.getConfig().getBoolean("do_silverfish_infest_blocks"));
		sender.sendMessage("  do_ravager_destroy_crops: " + plugin.getConfig().getBoolean("do_ravager_destroy_crops"));
		sender.sendMessage("  do_endcrystal_explode: " + plugin.getConfig().getBoolean("do_endcrystal_explode"));

		return true;
	}

	@Override
	public String getCommandName() {
		return "config";
	}
}
