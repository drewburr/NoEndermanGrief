package com.github.drewburr.mobgriefcontrol.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import com.github.drewburr.mobgriefcontrol.MobGriefControl;
import com.github.drewburr.mobgriefcontrol.common.PluginLibrary;
import com.github.drewburr.mobgriefcontrol.common.error.Report;

public class EndCrystalListener implements Listener {
	private final MobGriefControl plugin;

	public EndCrystalListener(MobGriefControl plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		try {
			if (event.getEntity().getType() == EntityType.ENDER_CRYSTAL) {
				if(!plugin.getConfig().getBoolean("do_endcrystal_explode", true)){
					event.blockList().clear();
				}
				MobGriefControl.LOGGER.debug("End Crystal attempted to explode at " + event.getLocation());
			}
		} catch (Exception exception) {
			MobGriefControl.reporter.reportDetailed(plugin, Report.newBuilder(PluginLibrary.ERROR_HANDLING_ENDCRYSTAL_GRIEF).error(exception));
		}
	}
}
