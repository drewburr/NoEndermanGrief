package com.github.joelgodofwar.neg.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.github.joelgodofwar.neg.NoEndermanGrief;

/**
 * Handles the toggledebug command
 */
public class ToggleDebugCommandHandler implements CommandHandler {

	@Override
	public boolean handle(NoEndermanGrief plugin, CommandSender sender, String[] args) {
		if (!sender.isOp() && !sender.hasPermission("noendermangrief.toggledebug") &&
			sender instanceof Player && !sender.hasPermission("noendermangrief.op") &&
			!sender.hasPermission("noendermangrief.admin")) {
			sender.sendMessage(NoEndermanGrief.THIS_NAME + " " + plugin.get("neg.message.noperm"));
			return false;
		}

		NoEndermanGrief.debug = !NoEndermanGrief.debug;
		sender.sendMessage(NoEndermanGrief.THIS_NAME + " " +
			plugin.get("neg.message.debugtrue").toString().replace("<boolean>",
			plugin.get("neg.message.boolean." + String.valueOf(NoEndermanGrief.debug).toLowerCase())));

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
