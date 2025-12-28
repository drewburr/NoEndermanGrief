package com.github.drewburr.mobgriefcontrol.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;

import com.github.drewburr.mobgriefcontrol.MobGriefControl;
import com.github.drewburr.mobgriefcontrol.common.PluginLibrary;
import com.github.drewburr.mobgriefcontrol.common.error.Report;

public class SnowGolemListener implements Listener {
	private final MobGriefControl plugin;

	public SnowGolemListener(MobGriefControl plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onEntityBlockForm(EntityBlockFormEvent event) {
		try {
			// Check if the entity is a snow golem and it's forming snow
			if (event.getEntity() != null &&
				event.getEntity().getType() == org.bukkit.entity.EntityType.SNOWMAN &&
				event.getNewState().getType() == Material.SNOW) {

				if (!plugin.getConfig().getBoolean("do_snowgolem_snow_trail", true)) {
					event.setCancelled(true);
				}
				MobGriefControl.LOGGER
						.debug("Snow Golem attempted to create snow at " + event.getBlock().getLocation());
			}
		} catch (Exception exception) {
			MobGriefControl.reporter.reportDetailed(plugin,
					Report.newBuilder(PluginLibrary.ERROR_HANDLING_SNOWGOLEM_GRIEF).error(exception));
		}
	}
}
