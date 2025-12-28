package com.github.drewburr.mobgriefcontrol.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.github.drewburr.mobgriefcontrol.MobGriefControl;

/**
 * Handles the default help command
 */
public class HelpCommandHandler implements CommandHandler {

	@Override
	public boolean handle(MobGriefControl plugin, CommandSender sender, String[] args) {
		if (!sender.hasPermission("mobgriefcontrol.op") &&
			!sender.hasPermission("mobgriefcontrol.admin") &&
			!sender.isOp()) {
			sender.sendMessage("" + plugin.get("mobgriefcontrol.message.no_perm"));
			return false;
		}

		sender.sendMessage("[]===============[" + "MobGriefControl" + "]===============[]");
		sender.sendMessage(" ");

		if (sender.isOp() || sender.hasPermission("mobgriefcontrol.op") ||
			sender.hasPermission("mobgriefcontrol.admin")) {
			sender.sendMessage(" -<[" + " OP Commands " + "}>-");
			sender.sendMessage(" /mobgriefcontrol update - " + plugin.get("mobgriefcontrol.command.update"));
			sender.sendMessage(" /mobgriefcontrol reload - " + plugin.get("mobgriefcontrol.command.reload"));

			if (sender.isOp() || sender.hasPermission("mobgriefcontrol.toggledebug") ||
				!(sender instanceof Player) || sender.hasPermission("mobgriefcontrol.admin")) {
				sender.sendMessage(" /mobgriefcontrol toggledebug - " + plugin.get("mobgriefcontrol.message.debuguse"));
			}
		}

		if (sender.hasPermission("mobgriefcontrol.admin") || !(sender instanceof Player)) {
			sender.sendMessage(" -<[" + " Admin Commands " + "}>-");
			sender.sendMessage(" /mobgriefcontrol config - Show current configuration");
			sender.sendMessage(" /mobgriefcontrol do_enderman_pickup <true/false> - Toggle enderman block pickup");
			sender.sendMessage(" /mobgriefcontrol do_creeper_explode <true/false> - Toggle creeper explosions");
			sender.sendMessage(" /mobgriefcontrol do_ghast_explode <true/false> - Toggle ghast explosions");
			sender.sendMessage(" /mobgriefcontrol do_wither_explode <true/false> - Toggle wither explosions");
			sender.sendMessage(" /mobgriefcontrol do_dragon_destroy <true/false> - Toggle dragon destruction");
			sender.sendMessage(" /mobgriefcontrol do_zombie_break_doors <true/false> - Toggle zombie door breaking");
			sender.sendMessage(" /mobgriefcontrol do_villager_farm <true/false> - Toggle villager farming");
			sender.sendMessage(" /mobgriefcontrol do_sheep_eat_grass <true/false> - Toggle sheep eating grass");
			sender.sendMessage(" /mobgriefcontrol do_rabbit_eat_crops <true/false> - Toggle rabbit eating crops");
			sender.sendMessage(" /mobgriefcontrol do_fox_pickup_items <true/false> - Toggle fox item pickup");
			sender.sendMessage(" /mobgriefcontrol do_snowgolem_snow_trail <true/false> - Toggle snow golem trails");
			sender.sendMessage(" /mobgriefcontrol do_silverfish_infest_blocks <true/false> - Toggle silverfish infesting");
			sender.sendMessage(" /mobgriefcontrol do_ravager_destroy_crops <true/false> - Toggle ravager crop destruction");
			sender.sendMessage(" /mobgriefcontrol do_endcrystal_explode <true/false> - Toggle end crystal explosions");
		}

		sender.sendMessage("[]===============[" + "MobGriefControl" + "]===============[]");
		return true;
	}

	@Override
	public String getCommandName() {
		return "help";
	}
}
