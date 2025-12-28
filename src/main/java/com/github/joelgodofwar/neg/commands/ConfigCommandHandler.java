package com.github.joelgodofwar.neg.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.github.joelgodofwar.neg.NoEndermanGrief;

/**
 * Handles the config command
 */
public class ConfigCommandHandler implements CommandHandler {

	@Override
	public boolean handle(NoEndermanGrief plugin, CommandSender sender, String[] args) {
		if (!sender.isOp() && !sender.hasPermission("noendermangrief.op") &&
			!sender.hasPermission("noendermangrief.admin") && sender instanceof Player) {
			sender.sendMessage(NoEndermanGrief.THIS_NAME + " " + plugin.get("neg.message.noperm"));
			return false;
		}

		sender.sendMessage(NoEndermanGrief.THIS_NAME + " Current Configuration:");
		sender.sendMessage("  auto_update_check: " + plugin.getConfig().getBoolean("auto_update_check"));
		sender.sendMessage("  debug: " + plugin.getConfig().getBoolean("debug"));
		sender.sendMessage("  lang: " + plugin.getConfig().getString("lang"));
		sender.sendMessage("  console.longpluginname: " + plugin.getConfig().getBoolean("console.longpluginname"));
		sender.sendMessage("  enderman_grief: " + plugin.getConfig().getBoolean("enderman_grief"));
		sender.sendMessage("  skeleton_horse_spawn: " + plugin.getConfig().getBoolean("skeleton_horse_spawn"));
		sender.sendMessage("  creeper_grief: " + plugin.getConfig().getBoolean("creeper_grief"));
		sender.sendMessage("  wandering_trader_spawn: " + plugin.getConfig().getBoolean("wandering_trader_spawn"));
		sender.sendMessage("  ghast_grief: " + plugin.getConfig().getBoolean("ghast_grief"));
		sender.sendMessage("  phantom_spawn: " + plugin.getConfig().getBoolean("phantom_spawn"));
		sender.sendMessage("  pillager_patrol_spawn: " + plugin.getConfig().getBoolean("pillager_patrol_spawn"));

		return true;
	}

	@Override
	public String getCommandName() {
		return "config";
	}
}
