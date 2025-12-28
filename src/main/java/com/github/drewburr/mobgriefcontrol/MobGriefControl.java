package com.github.drewburr.mobgriefcontrol;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import com.github.drewburr.mobgriefcontrol.common.PluginLibrary;
import com.github.drewburr.mobgriefcontrol.listeners.CreeperListener;
import com.github.drewburr.mobgriefcontrol.listeners.EndermanListener;
import com.github.drewburr.mobgriefcontrol.listeners.GhastListener;
import com.github.drewburr.mobgriefcontrol.listeners.WitherListener;
import com.github.drewburr.mobgriefcontrol.listeners.DragonListener;
import com.github.drewburr.mobgriefcontrol.listeners.ZombieListener;
import com.github.drewburr.mobgriefcontrol.listeners.VillagerListener;
import com.github.drewburr.mobgriefcontrol.listeners.SheepListener;
import com.github.drewburr.mobgriefcontrol.listeners.RabbitListener;
import com.github.drewburr.mobgriefcontrol.listeners.FoxListener;
import com.github.drewburr.mobgriefcontrol.listeners.SnowGolemListener;
import com.github.drewburr.mobgriefcontrol.listeners.SilverfishListener;
import com.github.drewburr.mobgriefcontrol.listeners.RavagerListener;
import com.github.drewburr.mobgriefcontrol.listeners.EndCrystalListener;
import com.github.drewburr.mobgriefcontrol.common.PluginLogger;
import com.github.drewburr.mobgriefcontrol.common.error.DetailedErrorReporter;
import com.github.drewburr.mobgriefcontrol.common.error.Report;
import com.github.drewburr.mobgriefcontrol.i18n.Translator;
import com.github.drewburr.mobgriefcontrol.util.PluginUtils;
import com.github.drewburr.mobgriefcontrol.commands.CommandManager;

@SuppressWarnings("unused")
public class MobGriefControl extends JavaPlugin implements Listener{
	/** Languages: čeština (cs_CZ), Deutsch (de_DE), English (en_US), Español (es_ES), Español (es_MX), Français (fr_FR), Italiano (it_IT), Magyar (hu_HU), 日本語 (ja_JP), 한국어 (ko_KR), Lolcat (lol_US), Melayu (my_MY), Nederlands (nl_NL), Polski (pl_PL), Português (pt_BR), Русский (ru_RU), Svenska (sv_SV), Türkçe (tr_TR), 中文(简体) (zh_CN), 中文(繁體) (zh_TW) */
	//public final static Logger logger = Logger.getLogger("Minecraft");
	public static String THIS_NAME;
	public static String THIS_VERSION;
	public static String daLang;
	public static boolean cancelbroadcast;
	public static boolean debug;
	File langFile;
	public FileConfiguration lang;
	YamlConfiguration oldconfig = new YamlConfiguration();
	String configVersion = "2.0.0";
	//String langVersion = "2.0.0";
	String pluginName = THIS_NAME;
	Translator lang2;
	public String jarfilename = this.getFile().getAbsoluteFile().toString();
	public static DetailedErrorReporter reporter;
	public static PluginLogger LOGGER;
	private CommandManager commandManager;

	@Override // TODO: onEnable
	public void onEnable(){
		long startTime = System.currentTimeMillis();
		LOGGER = new PluginLogger(this);
		reporter = new DetailedErrorReporter(this);
		debug = getConfig().getBoolean("debug", false);
		daLang = getConfig().getString("lang", "en_US");
		lang2 = new Translator(daLang, getDataFolder().toString());
		THIS_NAME = this.getPluginMeta().getName();
		THIS_VERSION = this.getPluginMeta().getVersion();
		if(!getConfig().getBoolean("console.longpluginname", true)) {
			pluginName = "mobgriefcontrol";
		}else {
			pluginName = THIS_NAME;
		}

		LOGGER = new PluginLogger(this);

		LOGGER.log("**************************************");
		LOGGER.log(" v" + THIS_VERSION + " Loading...");
		LOGGER.log("Server Version: " + getServer().getVersion().toString());

		/** DEV check **/
		File jarfile = this.getFile().getAbsoluteFile();
		if(jarfile.toString().contains("-DEV")){
			debug = true;
			LOGGER.log("jarfile contains DEV, debug set to true.");
		}else {
			LOGGER.log("This is not a DEV version.");
		}

		LOGGER.log("Checking lang files...");

		LOGGER.log("Checking config file...");
		/**  Check for config */
		try{
			if(!getDataFolder().exists()){
				LOGGER.log("Data Folder doesn't exist");
				LOGGER.log("Creating Data Folder");
				getDataFolder().mkdirs();
				LOGGER.log("Data Folder Created at " + getDataFolder());
			}
			File  file = new File(getDataFolder(), "config.yml");
			LOGGER.log("" + file);
			if(!file.exists()){
				LOGGER.log("config.yml not found, creating!");
				saveResource("config.yml", true);
			}
		}catch(Exception exception){
			reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_CANNOT_CHECK_CONFIG).error(exception));
		}
		LOGGER.log("Checking config file version...");
		String checkconfigversion = getConfig().getString("version", "1.0.0");
		LOGGER.log("Config file version=" + checkconfigversion + " expected=" + configVersion);
		if(checkconfigversion != null){
			if(!checkconfigversion.equalsIgnoreCase(configVersion)){
				try {
				copyFile_Java7(getDataFolder() + "" + File.separatorChar + "config.yml",getDataFolder() + "" + File.separatorChar + "old_config.yml");
				} catch (Exception exception) {
					reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_CANNOT_COPY_FILE).error(exception));
				}
				try {
					oldconfig.load(new File(getDataFolder(), "config.yml"));
				} catch (Exception exception) {
					LOGGER.warn("Could not load config.yml");
					reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_CANNOT_LOAD_CONFIG).error(exception));
				}
				saveResource("config.yml", true);
				try {
					getConfig().load(new File(getDataFolder(), "config.yml"));
				} catch (Exception exception) {
					LOGGER.warn("Could not load config.yml");
					reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_CANNOT_LOAD_CONFIG).error(exception));
				}
				try {
					oldconfig.load(new File(getDataFolder(), "old_config.yml"));
				} catch (Exception exception) {
					reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_CANNOT_LOAD_CONFIG).error(exception));
				}
				getConfig().set("debug", oldconfig.get("debug", false));
				getConfig().set("lang", oldconfig.get("lang", "en_US"));
				getConfig().set("console.longpluginname", oldconfig.get("console.longpluginname", true));
				// Migrate old keys to new descriptive names
				getConfig().set("do_enderman_pickup", oldconfig.get("do_enderman_grief", true));
				getConfig().set("do_creeper_explode", oldconfig.get("do_creeper_grief", true));
				getConfig().set("do_ghast_explode", oldconfig.get("do_ghast_grief", true));
				getConfig().set("do_wither_explode", oldconfig.get("do_wither_grief", true));
				getConfig().set("do_dragon_destroy", oldconfig.get("do_dragon_grief", true));
				getConfig().set("do_zombie_break_doors", oldconfig.get("do_zombie_door_break", true));
				getConfig().set("do_villager_farm", oldconfig.get("do_villager_farming", true));
				getConfig().set("do_sheep_eat_grass", oldconfig.get("do_sheep_eat_grass", true));
				getConfig().set("do_rabbit_eat_crops", oldconfig.get("do_rabbit_eat_crops", true));
				getConfig().set("do_fox_pickup_items", oldconfig.get("do_fox_pickup", true));
				getConfig().set("do_snowgolem_snow_trail", oldconfig.get("do_snowgolem_trail", true));
				getConfig().set("do_silverfish_infest_blocks", oldconfig.get("do_silverfish_blocks", true));
				getConfig().set("do_ravager_destroy_crops", oldconfig.get("do_ravager_grief", true));
				getConfig().set("do_endcrystal_explode", oldconfig.get("do_endcrystal_grief", true));
				try {
					getConfig().save(new File(getDataFolder(), "config.yml"));
				} catch (Exception exception) {
					LOGGER.warn("Could not save old settings to config.yml");
					reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_CANNOT_SAVE_CONFIG).error(exception));
				}
				LOGGER.log("config.yml has been updated");
			}
		}
		/** end config check */
		try {
getConfig().load(new File(getDataFolder(), "config.yml"));
			} catch (Exception exception) {
				LOGGER.warn("Could not load config.yml");
			reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_CANNOT_LOAD_CONFIG).error(exception));
		}

		if(debug){
			LOGGER.debug("Config.yml dump");
			LOGGER.debug("debug=" + getConfig().getBoolean("debug"));
			LOGGER.debug("lang=" + getConfig().getString("lang"));

			LOGGER.debug("console.longpluginname=" + getConfig().getBoolean("console.longpluginname"));
			LOGGER.debug("do_enderman_pickup=" + getConfig().getBoolean("do_enderman_pickup"));
			LOGGER.debug("do_creeper_explode=" + getConfig().getBoolean("do_creeper_explode"));
			LOGGER.debug("do_ghast_explode=" + getConfig().getBoolean("do_ghast_explode"));
			LOGGER.debug("do_wither_explode=" + getConfig().getBoolean("do_wither_explode"));
			LOGGER.debug("do_dragon_destroy=" + getConfig().getBoolean("do_dragon_destroy"));
			LOGGER.debug("do_zombie_break_doors=" + getConfig().getBoolean("do_zombie_break_doors"));
			LOGGER.debug("do_villager_farm=" + getConfig().getBoolean("do_villager_farm"));
			LOGGER.debug("do_sheep_eat_grass=" + getConfig().getBoolean("do_sheep_eat_grass"));
			LOGGER.debug("do_rabbit_eat_crops=" + getConfig().getBoolean("do_rabbit_eat_crops"));
			LOGGER.debug("do_fox_pickup_items=" + getConfig().getBoolean("do_fox_pickup_items"));
			LOGGER.debug("do_snowgolem_snow_trail=" + getConfig().getBoolean("do_snowgolem_snow_trail"));
			LOGGER.debug("do_silverfish_infest_blocks=" + getConfig().getBoolean("do_silverfish_infest_blocks"));
			LOGGER.debug("do_ravager_destroy_crops=" + getConfig().getBoolean("do_ravager_destroy_crops"));
			LOGGER.debug("do_endcrystal_explode=" + getConfig().getBoolean("do_endcrystal_explode"));
		}

		// Register mob-specific listeners
		getServer().getPluginManager().registerEvents(new EndermanListener(this), this);
		getServer().getPluginManager().registerEvents(new CreeperListener(this), this);
		getServer().getPluginManager().registerEvents(new GhastListener(this), this);
		getServer().getPluginManager().registerEvents(new WitherListener(this), this);
		getServer().getPluginManager().registerEvents(new DragonListener(this), this);
		getServer().getPluginManager().registerEvents(new ZombieListener(this), this);
		getServer().getPluginManager().registerEvents(new VillagerListener(this), this);
		getServer().getPluginManager().registerEvents(new SheepListener(this), this);
		getServer().getPluginManager().registerEvents(new RabbitListener(this), this);
		getServer().getPluginManager().registerEvents(new FoxListener(this), this);
		getServer().getPluginManager().registerEvents(new SnowGolemListener(this), this);
		getServer().getPluginManager().registerEvents(new SilverfishListener(this), this);
		getServer().getPluginManager().registerEvents(new RavagerListener(this), this);
		getServer().getPluginManager().registerEvents(new EndCrystalListener(this), this);

		// Initialize command manager
		commandManager = new CommandManager();

		consoleInfo("ENABLED - Loading took " + LoadTime(startTime));

	}

	@Override // TODO: onDisable
	public void onDisable(){
		//saveConfig();
		consoleInfo("DISABLED");
	}

	public void consoleInfo(String state) {
		loading(" v" + THIS_VERSION + " is " + state);
	}

	public void loading(String string) {
		LOGGER.log(string);
	}

	// Enderman event handler moved to EndermanListener

	// Creeper and Ghast explosion handlers moved to CreeperListener and GhastListener

	// Player join event handler moved to UpdateListener

	// Creature spawn event handlers moved to SpawnListener

	@SuppressWarnings("static-access")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		try {
			if (cmd.getName().equalsIgnoreCase("mobgriefcontrol")) {
				return commandManager.handleCommand(this, sender, args);
			}
		} catch (Exception exception) {
			reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.UNHANDLED_COMMAND_ERROR).error(exception));
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		try {
			if (command.getName().equalsIgnoreCase("mobgriefcontrol")) {
				return commandManager.handleTabComplete(sender, args);
			}
		} catch (Exception exception) {
			reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_TAB_COMPLETE_ERROR).error(exception));
		}
		return null;
	}
	public boolean makeBoolean(String args){
		if(args.contains("true")){
			return true;
		}
		else if(args.contains("false")){
			return false;
		}
		return false;
	}

	public void copyFile_Java7(String origin, String destination) throws IOException {
		try {
			Path FROM = Paths.get(origin);
			Path TO = Paths.get(destination);
			//overwrite the destination file if it exists, and copy
			// the file attributes, including the rwx permissions
			CopyOption[] options = new CopyOption[]{
					StandardCopyOption.REPLACE_EXISTING,
					StandardCopyOption.COPY_ATTRIBUTES
			};
			Files.copy(FROM, TO, options);
		} catch (Exception exception) {
			reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_CANNOT_COPY_FILE).error(exception));
		}
	}

	@Override
	public void saveConfig(){
		try {
			getConfig().save(new File(getDataFolder(), "config.yml"));
		} catch (Exception exception) {
			LOGGER.warn("Could not save old settings to config.yml");
			reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_CANNOT_SAVE_CONFIG).error(exception));
		}
	}

	public boolean isCorrectVersion(){
		// NMS version checking removed - always return true for Paper API compatibility
		return true;
	}

	public boolean saveConfig(boolean update, boolean Debug, boolean Console, boolean Longname, boolean Ender, boolean Ghast, boolean Creeper, String Lang) {
		//	debug	daLang	colorful_console
		debug = Debug;
		daLang = Lang;
		if(!Longname) {
			pluginName = "mobgriefcontrol";
		}else {
			pluginName = THIS_NAME;
		}
		getConfig().set("debug", Debug);
		getConfig().set("lang", Lang);
		getConfig().set("console.longpluginname", Longname);
		getConfig().set("do_enderman_grief", Ender);
		getConfig().set("do_creeper_grief", Creeper);
		getConfig().set("do_ghast_grief", Ghast);
		try {
			getConfig().save(new File(getDataFolder(), "config.yml"));
		} catch (IOException e) {
			LOGGER.warn("Could not save settings to config.yml");
			e.printStackTrace();
			return false;
		}
		LOGGER.log("config.yml has been updated");
		return true;
	}

	public String LoadTime(long startTime) {
		long elapsedTime = System.currentTimeMillis() - startTime;
		long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) % 60;
		long milliseconds = elapsedTime % 1000;

		if (minutes > 0) {
			return String.format("%d min %d s %d ms.", minutes, seconds, milliseconds);
		} else if (seconds > 0) {
			return String.format("%d s %d ms.", seconds, milliseconds);
		} else {
			return String.format("%d ms.", elapsedTime);
		}
	}

	@SuppressWarnings("static-access")
	public String get(String key, String... defaultValue) {
		return lang2.get(key, defaultValue);
	}

	public String getjarfilename() {
		return jarfilename;
	}

	public boolean getDebug() {
		return debug;
	}

}
