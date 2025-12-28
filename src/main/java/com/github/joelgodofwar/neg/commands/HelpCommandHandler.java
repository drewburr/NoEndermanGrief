package com.github.joelgodofwar.neg.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.github.joelgodofwar.neg.NoEndermanGrief;

/**
 * Handles the default help command
 */
public class HelpCommandHandler implements CommandHandler {

	@Override
	public boolean handle(NoEndermanGrief plugin, CommandSender sender, String[] args) {
		if (!sender.hasPermission("noendermangrief.op") &&
			!sender.hasPermission("noendermangrief.admin") &&
			!sender.isOp()) {
			sender.sendMessage("" + plugin.get("neg.message.no_perm"));
			return false;
		}

		sender.sendMessage("[]===============[" + "NoEndermanGrief" + "]===============[]");
		sender.sendMessage(" ");

		if (sender.isOp() || sender.hasPermission("noendermangrief.op") ||
			sender.hasPermission("noendermangrief.admin")) {
			sender.sendMessage(" -<[" + " OP Commands " + "}>-");
			sender.sendMessage(" /NEG update - " + plugin.get("neg.command.update"));
			sender.sendMessage(" /NEG reload - " + plugin.get("neg.command.reload"));

			if (sender.isOp() || sender.hasPermission("noendermangrief.toggledebug") ||
				!(sender instanceof Player) || sender.hasPermission("noendermangrief.admin")) {
				sender.sendMessage(" /NEG toggledebug - " + plugin.get("neg.message.debuguse"));
			}
		}

		if (sender.hasPermission("noendermangrief.admin") || !(sender instanceof Player)) {
			sender.sendMessage(" -<[" + " Admin Commands " + "}>-");
			if (sender instanceof Player) {
				sender.sendMessage(" /NEG BOOK - " + plugin.get("neg.command.book"));
			}
			sender.sendMessage(" /NEG EG true/false - " + plugin.get("neg.command.endermen"));
			sender.sendMessage(" /NEG SH true/false - " + plugin.get("neg.command.skeleton_horse"));
			sender.sendMessage(" /NEG CG true/false - " + plugin.get("neg.command.creeper"));
			sender.sendMessage(" /NEG WT true/false - " + plugin.get("neg.command.wandering_trader"));
			sender.sendMessage(" /NEG GG true/false - " + plugin.get("neg.command.ghast"));
			sender.sendMessage(" /NEG PG true/false - " + plugin.get("neg.command.phantom"));
			sender.sendMessage(" /NEG PP true/false - " + plugin.get("neg.command.pillager_patrol"));
		}

		sender.sendMessage("[]===============[" + "NoEndermanGrief" + "]===============[]");
		return true;
	}

	@Override
	public String getCommandName() {
		return "help";
	}
}
