package com.github.drewburr.mobgriefcontrol.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.github.drewburr.mobgriefcontrol.MobGriefControl;
import com.github.drewburr.mobgriefcontrol.common.PluginLibrary;
import com.github.drewburr.mobgriefcontrol.common.error.Report;

public class SpawnListener implements Listener {
	private final MobGriefControl plugin;

	public SpawnListener(MobGriefControl plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		Entity entity = event.getEntity();

		// Skeleton Horse spawn prevention
		try {
			if (entity instanceof SkeletonHorse) {
				if(!plugin.getConfig().getBoolean("spawn_skeleton_horse", true)){
					MobGriefControl.LOGGER.debug("" + plugin.get("mobgriefcontrol.entity.skeleton_horse") + event.getLocation());
					event.setCancelled(true);
				}
			}
		} catch (Exception exception) {
			plugin.reporter.reportDetailed(plugin, Report.newBuilder(PluginLibrary.ERROR_HANDLING_SKELETON_HORSE_GRIEF).error(exception));
		}

		// Wandering Trader spawn prevention
		try {
			if (entity instanceof WanderingTrader) {
				if(!plugin.getConfig().getBoolean("spawn_wandering_trader", true)){
					MobGriefControl.LOGGER.debug("" + plugin.get("mobgriefcontrol.entity.wandering_trader") + event.getLocation());
					event.setCancelled(true);
				}
			}
		} catch (Exception exception) {
			plugin.reporter.reportDetailed(plugin, Report.newBuilder(PluginLibrary.ERROR_HANDLING_WANDERING_TRADER_GRIEF).error(exception));
		}

		// Phantom spawn prevention
		try {
			if (entity instanceof Phantom) {
				if(!plugin.getConfig().getBoolean("spawn_phantom", true)){
					MobGriefControl.LOGGER.debug("" + plugin.get("mobgriefcontrol.entity.phantom") + event.getLocation());
					event.setCancelled(true);
				}
			}
		} catch (Exception exception) {
			plugin.reporter.reportDetailed(plugin, Report.newBuilder(PluginLibrary.ERROR_HANDLING_PHANTOM_GRIEF).error(exception));
		}

		// Pillager Patrol spawn prevention
		try {
			if(event.getSpawnReason() == SpawnReason.PATROL) {
				if(!plugin.getConfig().getBoolean("spawn_pillager_patrol", true)){
					MobGriefControl.LOGGER.debug("" + plugin.get("mobgriefcontrol.entity.pillager_patrol") + event.getLocation());
					event.setCancelled(true);
				}
			}
		} catch (Exception exception) {
			plugin.reporter.reportDetailed(plugin, Report.newBuilder(PluginLibrary.ERROR_HANDLING_PILLAGER_PATROL_GRIEF).error(exception));
		}
	}
}
