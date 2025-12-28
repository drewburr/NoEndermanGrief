package com.github.drewburr.mobgriefcontrol.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Ghast;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import com.github.drewburr.mobgriefcontrol.MobGriefControl;
import com.github.drewburr.mobgriefcontrol.common.PluginLibrary;
import com.github.drewburr.mobgriefcontrol.common.error.Report;

public class GhastListener implements Listener {
	private final MobGriefControl plugin;

	public GhastListener(MobGriefControl plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		try {
			if ((event.getEntity().getType() == EntityType.FIREBALL) && (((Fireball) event.getEntity()).getShooter() instanceof Ghast)) {
				if(!plugin.getConfig().getBoolean("do_ghast_grief", true)){
					Entity fireball = event.getEntity();
					((Fireball) fireball).setIsIncendiary(false);
					((Fireball) fireball).setYield(0F);
					event.setCancelled(true);
				}
				MobGriefControl.LOGGER.debug("" + plugin.get("mobgriefcontrol.entity.ghast.explode") + event.getLocation().getBlockX() + ", " + event.getLocation().getBlockZ());
				return;
			}
		} catch (Exception exception) {
			plugin.reporter.reportDetailed(plugin, Report.newBuilder(PluginLibrary.ERROR_HANDLING_GHAST_GRIEF).error(exception));
		}
	}
}
