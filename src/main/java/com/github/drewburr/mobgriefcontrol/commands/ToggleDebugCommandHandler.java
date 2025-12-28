package com.github.drewburr.mobgriefcontrol.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.github.drewburr.mobgriefcontrol.MobGriefControl;

/**
 * Handles the toggledebug command
 */
public class ToggleDebugCommandHandler implements CommandHandler {

	@Override
	public boolean handle(MobGriefControl plugin, CommandSender sender, String[] args) {
		if (!sender.isOp() && !sender.hasPermission("mobgriefcontrol.toggledebug") &&
			sender instanceof Player && !sender.hasPermission("mobgriefcontrol.op") &&
			!sender.hasPermission("mobgriefcontrol.admin")) {
			sender.sendMessage(MobGriefControl.THIS_NAME + " " + plugin.get("mobgriefcontrol.message.noperm"));
			return false;
		}

		MobGriefControl.debug = !MobGriefControl.debug;
		sender.sendMessage(MobGriefControl.THIS_NAME + " " +
			plugin.get("mobgriefcontrol.message.debugtrue").toString().replace("<boolean>",
			plugin.get("mobgriefcontrol.message.boolean." + String.valueOf(MobGriefControl.debug).toLowerCase())));

		return true;
	}

	@Override
	public String getCommandName() {
		return "toggledebug";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"td"};
	}
}
