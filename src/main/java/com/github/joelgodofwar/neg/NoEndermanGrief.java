package com.github.joelgodofwar.neg;

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

import com.github.joelgodofwar.neg.common.PluginLibrary;
import com.github.joelgodofwar.neg.listeners.CreeperListener;
import com.github.joelgodofwar.neg.listeners.EndermanListener;
import com.github.joelgodofwar.neg.listeners.GhastListener;
import com.github.joelgodofwar.neg.listeners.SpawnListener;
import com.github.joelgodofwar.neg.common.PluginLogger;
import com.github.joelgodofwar.neg.common.error.DetailedErrorReporter;
import com.github.joelgodofwar.neg.common.error.Report;
import com.github.joelgodofwar.neg.i18n.Translator;
import com.github.joelgodofwar.neg.util.PluginUtils;

@SuppressWarnings("unused")
public class NoEndermanGrief extends JavaPlugin implements Listener{
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
	String configVersion = "1.0.6";
	//String langVersion = "1.0.6";
	String pluginName = THIS_NAME;
	Translator lang2;
	public String jarfilename = this.getFile().getAbsoluteFile().toString();
	public static DetailedErrorReporter reporter;
	public static PluginLogger LOGGER;

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
			pluginName = "NEG";
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
			File  file = new File(getDataFolder(), "getConfig().yml");
			LOGGER.log("" + file);
			if(!file.exists()){
				LOGGER.log("getConfig().yml not found, creating!");
				saveResource("getConfig().yml", true);
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
					copyFile_Java7(getDataFolder() + "" + File.separatorChar + "getConfig().yml",getDataFolder() + "" + File.separatorChar + "old_getConfig().yml");
				} catch (Exception exception) {
					reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_CANNOT_COPY_FILE).error(exception));
				}
				try {
					oldconfig.load(new File(getDataFolder(), "getConfig().yml"));
				} catch (Exception exception) {
					LOGGER.warn("Could not load getConfig().yml");
					reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_CANNOT_LOAD_CONFIG).error(exception));
				}
				saveResource("getConfig().yml", true);
				try {
					getConfig().load(new File(getDataFolder(), "getConfig().yml"));
				} catch (Exception exception) {
					LOGGER.warn("Could not load getConfig().yml");
					reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_CANNOT_LOAD_CONFIG).error(exception));
				}
				try {
					oldconfig.load(new File(getDataFolder(), "old_getConfig().yml"));
				} catch (Exception exception) {
					reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_CANNOT_LOAD_CONFIG).error(exception));
				}
				getConfig().set("debug", oldconfig.get("debug", false));
				getConfig().set("lang", oldconfig.get("lang", "en_US"));
				getConfig().set("console.longpluginname", oldconfig.get("console.longpluginname", true));
				getConfig().set("enderman_grief", oldconfig.get("enderman_grief", false));
				getConfig().set("skeleton_horse_spawn", oldconfig.get("skeleton_horse_spawn", false));
				getConfig().set("creeper_grief", oldconfig.get("creeper_grief", false));
				getConfig().set("wandering_trader_spawn", oldconfig.get("wandering_trader", false));
				getConfig().set("ghast_grief", oldconfig.get("ghast_grief", false));
				getConfig().set("phantom_spawn", oldconfig.get("phantom_spawn", false));
				getConfig().set("pillager_patrol_spawn", oldconfig.get("pillager_patrol_spawn", false));
				try {
					getConfig().save(new File(getDataFolder(), "getConfig().yml"));
				} catch (Exception exception) {
					LOGGER.warn("Could not save old settings to getConfig().yml");
					reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_CANNOT_SAVE_CONFIG).error(exception));
				}
				LOGGER.log("getConfig().yml has been updated");
			}
		}
		/** end config check */
		try {
			getConfig().load(new File(getDataFolder(), "getConfig().yml"));
		} catch (Exception exception) {
			LOGGER.warn("Could not load getConfig().yml");
			reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_CANNOT_LOAD_CONFIG).error(exception));
		}

		if(debug){
			LOGGER.debug("Config.yml dump");
			LOGGER.debug("debug=" + getConfig().getBoolean("debug"));
			LOGGER.debug("lang=" + getConfig().getString("lang"));

			LOGGER.debug("console.longpluginname=" + getConfig().getBoolean("console.longpluginname"));
			LOGGER.debug("enderman_grief=" + getConfig().getBoolean("enderman_grief"));
			LOGGER.debug("skeleton_horse_spawn=" + getConfig().getBoolean("skeleton_horse_spawn"));
			LOGGER.debug("creeper_grief=" + getConfig().getBoolean("creeper_grief"));
			LOGGER.debug("wandering_trader_spawn=" + getConfig().getBoolean("wandering_trader_spawn"));
			LOGGER.debug("ghast_grief=" + getConfig().getBoolean("ghast_grief"));
			LOGGER.debug("phantom_spawn=" + getConfig().getBoolean("phantom_spawn"));
			LOGGER.debug("pillager_patrol_spawn=" + getConfig().getBoolean("pillager_patrol_spawn"));
		}

		// Register mob-specific listeners
		getServer().getPluginManager().registerEvents(new EndermanListener(this), this);
		getServer().getPluginManager().registerEvents(new CreeperListener(this), this);
		getServer().getPluginManager().registerEvents(new GhastListener(this), this);
		getServer().getPluginManager().registerEvents(new SpawnListener(this), this);
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
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){ // TODO: Commands
		try {
			//Player p = (Player)sender;
			if (cmd.getName().equalsIgnoreCase("NEG")){
				if (args.length == 0)
				{
					/** Check if sender has permission */
					if ( sender.hasPermission("noendermangrief.op") || sender.hasPermission("noendermangrief.admin") || sender.isOp() ) {
						/** Command code */
						sender.sendMessage("[]===============[" + "NoEndermanGrief" + "]===============[]");
						sender.sendMessage(" ");
						if( sender.isOp()||sender.hasPermission("noendermangrief.op") || sender.hasPermission("noendermangrief.admin") ){
							sender.sendMessage(" -<[" + " OP Commands " + "}>-");
							sender.sendMessage(" /NEG update - " + get("neg.command.update"));//Check for update.");
							sender.sendMessage(" /NEG reload - " + get("neg.command.reload") );//Reload config file.");
							if( sender.isOp() || sender.hasPermission("noendermangrief.toggledebug") ||
									!(sender instanceof Player) || sender.hasPermission("noendermangrief.admin") ){
								sender.sendMessage(" /NEG toggledebug - " + get("neg.message.debuguse") );
							}
						}
						if( sender.hasPermission("noendermangrief.admin") || !(sender instanceof Player) ){
							sender.sendMessage(" -<[" + " Admin Commands " + "}>-");
							if( sender instanceof Player ) {
								sender.sendMessage(" /NEG BOOK - " + get("neg.command.book"));
							}
							sender.sendMessage(" /NEG EG true/false - " + get("neg.command.endermen"));
							sender.sendMessage(" /NEG SH true/false - " + get("neg.command.skeleton_horse"));
							sender.sendMessage(" /NEG CG true/false - " + get("neg.command.creeper"));
							sender.sendMessage(" /NEG WT true/false - " + get("neg.command.wandering_trader"));
							sender.sendMessage(" /NEG GG true/false - " + get("neg.command.ghast"));
							sender.sendMessage(" /NEG PG true/false - " + get("neg.command.phantom"));
							sender.sendMessage(" /NEG PP true/false - " + get("neg.command.pillager_patrol"));
						}
						sender.sendMessage("[]===============[" + "NoEndermanGrief" + "]===============[]");
						return true;
					}else {
						sender.sendMessage("" + get("neg.message.no_perm"));
						return false;
					}
				}
				if(args[0].equalsIgnoreCase("dumpinfo") || args[0].equalsIgnoreCase("di")){
					if( sender.isOp() || sender.hasPermission("noendermangrief.op")
							|| sender.hasPermission("noendermangrief.admin") || !(sender instanceof Player)) {
						Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
						//StringBuilder messageBuilder = new StringBuilder();

						LOGGER.log("" + "Please copy from this line until the second line of dashes.");
						LOGGER.log("" + "------------------------------------------------------------");
						LOGGER.log("Config.yml dump");
						LOGGER.log("auto_update_check=" + getConfig().getBoolean("auto_update_check"));
						LOGGER.log("debug=" + getConfig().getBoolean("debug"));
						LOGGER.log("lang=" + getConfig().getString("lang"));

						LOGGER.log("console.longpluginname=" + getConfig().getBoolean("console.longpluginname"));
						LOGGER.log("enderman_grief=" + getConfig().getBoolean("enderman_grief"));
						LOGGER.log("skeleton_horse_spawn=" + getConfig().getBoolean("skeleton_horse_spawn"));
						LOGGER.log("creeper_grief=" + getConfig().getBoolean("creeper_grief"));
						LOGGER.log("wandering_trader_spawn=" + getConfig().getBoolean("wandering_trader_spawn"));
						LOGGER.log("ghast_grief=" + getConfig().getBoolean("ghast_grief"));
						LOGGER.log("phantom_spawn=" + getConfig().getBoolean("phantom_spawn"));
						LOGGER.log("pillager_patrol_spawn=" + getConfig().getBoolean("pillager_patrol_spawn"));
						LOGGER.log("");
						LOGGER.log("This server is running " + Bukkit.getName() + " version " + Bukkit.getVersion() + " (Implementing API version " + Bukkit.getBukkitVersion() + ")");
						LOGGER.log("");
						LOGGER.log("Plugins dump");
						PluginUtils.loadPluginJarNames();
						for (Plugin plugin : plugins) {
							Map<String, Object> info = PluginUtils.getInfo(plugin);
							String pluginName = (String) info.get("Name");
							String pluginVersion = (String) info.get("Version");

							LOGGER.log(String.format("[%s] v%s", pluginName, pluginVersion));
							LOGGER.log(String.format("  FileName: %s", info.get("FileName")));
							LOGGER.log(String.format("  Main: %s", info.get("Main")));
							LOGGER.log(String.format("  Enabled: %b, API-Version: %s", info.get("Enabled"), info.get("API-Version")));
							LOGGER.log(String.format("  Description: %s", info.get("Description")));
							LOGGER.log(String.format("  Authors: %s", info.get("Authors")));
							LOGGER.log(String.format("  Website: %s", info.get("Website")));
							LOGGER.log(String.format("  Depends: %s", info.get("Depends")));
							LOGGER.log(String.format("  SoftDepends: %s", info.get("SoftDepends")));
							LOGGER.log(String.format("  Commands: %s", info.get("Commands")));
							LOGGER.log(String.format("  Permissions: %s", info.get("Permissions")));
							LOGGER.log(String.format("  Default Permissions: %s", info.get("Default Permissions")));
							LOGGER.log(String.format("  Load: %s", info.get("Load")));
							LOGGER.log(String.format("  LoadBefore: %s", info.get("LoadBefore")));
							LOGGER.log(String.format("  Provides: %s", info.get("Provides")));
							LOGGER.log("");
						}
						LOGGER.log("" + "------------------------------------------------------------");
						LOGGER.log("" + "This is the end of the debug dump.");
						//sender.sendMessage(messageBuilder.toString());
						return true;
					}
				}
				if(args[0].equalsIgnoreCase("config")){
					if(!(sender instanceof Player)) {
						sender.sendMessage("This command can not be sent by console");
						return false;
					}
					if( (sender instanceof Player) && sender.hasPermission("noendermangrief.admin") ){
						Player player = (Player) sender;
					Inventory gui = Bukkit.createInventory(player, 6*9, Component.text("Configurations"));
						ItemStack btnSpace = new ItemStack(Material.AIR);
						ItemStack btnUpdate = new ItemStack(Material.WRITABLE_BOOK);
						//ItemStack btnUpdateTrue = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
						ItemStack btnUpdateTrue = new ItemStack(getConfig().getBoolean("auto_update_check") ? Material.LIME_STAINED_GLASS_PANE : Material.GRAY_STAINED_GLASS_PANE);
						ItemStack btnUpdateFalse = new ItemStack(getConfig().getBoolean("auto_update_check") ? Material.GRAY_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE);

						ItemStack btnLang = new ItemStack(Material.WRITABLE_BOOK);
						ItemStack btnLangCZ = new ItemStack(getConfig().getString("lang").equalsIgnoreCase("cs_CZ") ? Material.LIME_WOOL : Material.LIGHT_GRAY_WOOL);
						ItemStack btnLangDE = new ItemStack(getConfig().getString("lang").equalsIgnoreCase("de_DE") ? Material.LIME_WOOL : Material.LIGHT_GRAY_WOOL);
						ItemStack btnLangEN = new ItemStack(getConfig().getString("lang").equalsIgnoreCase("en_US") ? Material.LIME_WOOL : Material.LIGHT_GRAY_WOOL);
						ItemStack btnLangFR = new ItemStack(getConfig().getString("lang").equalsIgnoreCase("fr_FR") ? Material.LIME_WOOL : Material.LIGHT_GRAY_WOOL);
						ItemStack btnLangLOL = new ItemStack(getConfig().getString("lang").equalsIgnoreCase("lol_US") ? Material.LIME_WOOL : Material.LIGHT_GRAY_WOOL);
						ItemStack btnLangNL = new ItemStack(getConfig().getString("lang").equalsIgnoreCase("nl_NL") ? Material.LIME_WOOL : Material.LIGHT_GRAY_WOOL);
						ItemStack btnLangBR = new ItemStack(getConfig().getString("lang").equalsIgnoreCase("pt_BR") ? Material.LIME_WOOL : Material.LIGHT_GRAY_WOOL);

						ItemStack btnDebug =  new ItemStack(Material.WRITABLE_BOOK);
						ItemStack btnDebugTrue =  new ItemStack(getConfig().getBoolean("debug") ? Material.LIME_STAINED_GLASS_PANE : Material.GRAY_STAINED_GLASS_PANE);
						ItemStack btnDebugFalse =  new ItemStack(getConfig().getBoolean("debug") ? Material.GRAY_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE);


						ItemStack btnLongname = new ItemStack(Material.WRITABLE_BOOK);
						ItemStack btnLongnameTrue =  new ItemStack(getConfig().getBoolean("console.longpluginname") ? Material.LIME_STAINED_GLASS_PANE : Material.GRAY_STAINED_GLASS_PANE);
						ItemStack btnLongnameFalse =  new ItemStack(getConfig().getBoolean("console.longpluginname") ? Material.GRAY_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE);


						ItemStack btnTrader = new ItemStack(Material.WRITABLE_BOOK);
						ItemStack btnTraderTrue =  new ItemStack(getConfig().getBoolean("wandering_trader_spawn") ? Material.LIME_STAINED_GLASS_PANE : Material.GRAY_STAINED_GLASS_PANE);
						ItemStack btnTraderFalse =  new ItemStack(getConfig().getBoolean("wandering_trader_spawn") ? Material.GRAY_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE);

						ItemStack btnPillager = new ItemStack(Material.WRITABLE_BOOK);
						ItemStack btnPillagerTrue =  new ItemStack(getConfig().getBoolean("pillager_patrol_spawn") ? Material.LIME_STAINED_GLASS_PANE : Material.GRAY_STAINED_GLASS_PANE);
						ItemStack btnPillagerFalse =  new ItemStack(getConfig().getBoolean("pillager_patrol_spawn") ? Material.GRAY_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE);

						ItemStack btnEnder = new ItemStack(Material.WRITABLE_BOOK);
						ItemStack btnEnderTrue =  new ItemStack(getConfig().getBoolean("enderman_grief") ? Material.LIME_STAINED_GLASS_PANE : Material.GRAY_STAINED_GLASS_PANE);
						ItemStack btnEnderFalse =  new ItemStack(getConfig().getBoolean("enderman_grief") ? Material.GRAY_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE);

						ItemStack btnGhast = new ItemStack(Material.WRITABLE_BOOK);
						ItemStack btnGhastTrue =  new ItemStack(getConfig().getBoolean("ghast_grief") ? Material.LIME_STAINED_GLASS_PANE : Material.GRAY_STAINED_GLASS_PANE);
						ItemStack btnGhastFalse =  new ItemStack(getConfig().getBoolean("ghast_grief") ? Material.GRAY_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE);

						ItemStack btnHorse = new ItemStack(Material.WRITABLE_BOOK);
						ItemStack btnHorseTrue =  new ItemStack(getConfig().getBoolean("skeleton_horse_spawn") ? Material.LIME_STAINED_GLASS_PANE : Material.GRAY_STAINED_GLASS_PANE);
						ItemStack btnHorseFalse =  new ItemStack(getConfig().getBoolean("skeleton_horse_spawn") ? Material.GRAY_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE);

						ItemStack btnPhantom = new ItemStack(Material.WRITABLE_BOOK);
						ItemStack btnPhantomTrue =  new ItemStack(getConfig().getBoolean("phantom_spawn") ? Material.LIME_STAINED_GLASS_PANE : Material.GRAY_STAINED_GLASS_PANE);
						ItemStack btnPhantomFalse =  new ItemStack(getConfig().getBoolean("phantom_spawn") ? Material.GRAY_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE);

						ItemStack btnCreeper = new ItemStack(Material.WRITABLE_BOOK);
						ItemStack btnCreeperTrue =  new ItemStack(getConfig().getBoolean("creeper_grief") ? Material.LIME_STAINED_GLASS_PANE : Material.GRAY_STAINED_GLASS_PANE);
						ItemStack btnCreeperFalse =  new ItemStack(getConfig().getBoolean("creeper_grief") ? Material.GRAY_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE);

						ItemStack btnSave = new ItemStack(Material.SLIME_BLOCK);
						ItemStack btnCancel =  new ItemStack(Material.BARRIER);


						//Edit the items
						ItemMeta update_meta = btnUpdate.getItemMeta();
					update_meta.displayName(Component.text("auto_update_check"));
					update_meta.lore(List.of(
						Component.text("Should this plugin "),
						Component.text("automatically check"),
						Component.text(" for updates?")
					));
					btnUpdate.setItemMeta(update_meta);

					ItemMeta updatetrue_meta = btnUpdateTrue.getItemMeta();
					updatetrue_meta.displayName(Component.text("Set auto_update_check to True"));
					updatetrue_meta.lore(List.of(
						Component.text("Will check for updates.")
					));
					btnUpdateTrue.setItemMeta(updatetrue_meta);

					ItemMeta updatefalse_meta = btnUpdateTrue.getItemMeta();
					updatefalse_meta.displayName(Component.text("Set auto_update_check to False"));
					updatefalse_meta.lore(List.of(
						Component.text("Will not check for updates.")
					));


						ItemMeta lang_meta = btnLang.getItemMeta();
					lang_meta.displayName(Component.text("language"));
					lang_meta.lore(List.of(
						Component.text("Select your preferred language")
					));
					btnLang.setItemMeta(lang_meta);

					ItemMeta langCZ_meta = btnLangCZ.getItemMeta();
					langCZ_meta.displayName(Component.text("čeština (cs-CZ)"));
					langCZ_meta.lore(List.of(
						Component.text("Jako jazyk vyberte češtinu.")
					));
					btnLangCZ.setItemMeta(langCZ_meta);

					ItemMeta langDE_meta = btnLangDE.getItemMeta();
					langDE_meta.displayName(Component.text("Deutsche (de_DE)"));
					langDE_meta.lore(List.of(
						Component.text("Wählen Sie Deutsch als Sprache aus.")
					));
					btnLangDE.setItemMeta(langDE_meta);

					ItemMeta langEN_meta = btnLangEN.getItemMeta();
					langEN_meta.displayName(Component.text("English (en_US)"));
					langEN_meta.lore(List.of(
						Component.text("Select English as your language.")
					));
					btnLangEN.setItemMeta(langEN_meta);

					ItemMeta langFR_meta = btnLangFR.getItemMeta();
					langFR_meta.displayName(Component.text("Français (fr_FR)"));
					langFR_meta.lore(List.of(
						Component.text("Sélectionnez Français comme langue.")
					));
					btnLangFR.setItemMeta(langFR_meta);

					ItemMeta langLOL_meta = btnLangLOL.getItemMeta();
					langLOL_meta.displayName(Component.text("LoL Cat (lol_US)"));
					langLOL_meta.lore(List.of(
						Component.text("Select lulz kat az ur language.")
					));
					btnLangLOL.setItemMeta(langLOL_meta);

					ItemMeta langNL_meta = btnLangNL.getItemMeta();
					langNL_meta.displayName(Component.text("Nederlands (nl_NL)"));
					langNL_meta.lore(List.of(
						Component.text("Selecteer Nederlands als je taal.")
					));
					btnLangNL.setItemMeta(langNL_meta);

					ItemMeta langBR_meta = btnLangBR.getItemMeta();
					langBR_meta.displayName(Component.text("Português (pt_BR)"));
					langBR_meta.lore(List.of(
						Component.text("Selecione Português como seu idioma.")
					));



						ItemMeta debug_meta = btnDebug.getItemMeta();
					debug_meta.displayName(Component.text("debug"));
					debug_meta.lore(List.of(
						Component.text("Set to true before."),
						Component.text("sending a log about"),
						Component.text("an issue."),
						Component.text(" "),
						Component.text("Logs trace data"),
						Component.text("required to pinpoint"),
						Component.text("where errors are.")
					));
					btnDebug.setItemMeta(debug_meta);

					ItemMeta debugtrue_meta = btnUpdateTrue.getItemMeta();
					debugtrue_meta.displayName(Component.text("Set debug to True"));
					debugtrue_meta.lore(List.of(
						Component.text("Will log debug information.")
					));
					btnDebugTrue.setItemMeta(debugtrue_meta);

					ItemMeta debugfalse_meta = btnUpdateTrue.getItemMeta();
					debugfalse_meta.displayName(Component.text("Set debug to False"));
					debugfalse_meta.lore(List.of(
						Component.text("Will not log debug information.")
					));
					btnDebugFalse.setItemMeta(debugfalse_meta);

					ItemMeta longname_meta = btnLongname.getItemMeta();
					longname_meta.displayName(Component.text("console.longpluginname"));
					longname_meta.lore(List.of(
						Component.text("Logs use NoEndermanGrief"),
						Component.text("or NEG.")
					));
					btnLongname.setItemMeta(longname_meta);

					ItemMeta longnametrue_meta = btnLongnameTrue.getItemMeta();
					longnametrue_meta.displayName(Component.text("Set longname.colorful_longname to True"));
					longnametrue_meta.lore(List.of(
						Component.text("Will have colorful text in longname.")
					));
					btnLongnameTrue.setItemMeta(longnametrue_meta);

					ItemMeta longnamefalse_meta = btnLongnameFalse.getItemMeta();
					longnamefalse_meta.displayName(Component.text("Set longname.colorful_longname to False"));
					longnamefalse_meta.lore(List.of(
						Component.text("Will not have colorful text in longname.")
					));
					btnLongnameFalse.setItemMeta(longnamefalse_meta);

					ItemMeta trader_meta = btnTrader.getItemMeta();
					trader_meta.displayName(Component.text("wandering_trader_spawn"));
					trader_meta.lore(List.of(
						Component.text("Set if Wandering"),
						Component.text("Traders should spawn."),
						Component.text(" "),
						Component.text("false = no spawn")
					));
					btnTrader.setItemMeta(trader_meta);

					ItemMeta tradertrue_meta = btnTraderTrue.getItemMeta();
					tradertrue_meta.displayName(Component.text("Set wandering_trader_spawn to True"));
					tradertrue_meta.lore(List.of(
						Component.text("Wandering Traders will spawn.")
					));
					btnTraderTrue.setItemMeta(tradertrue_meta);

					ItemMeta traderfalse_meta = btnTraderFalse.getItemMeta();
					traderfalse_meta.displayName(Component.text("Set wandering_trader_spawn to False"));
					traderfalse_meta.lore(List.of(
						Component.text("Wandering Traders will NOT spawn.")
					));
					btnTraderFalse.setItemMeta(traderfalse_meta);

					ItemMeta pillager_meta = btnPillager.getItemMeta();
					pillager_meta.displayName(Component.text("pillager_patrol_spawn"));
					pillager_meta.lore(List.of(
						Component.text("Set if Pillager"),
						Component.text("Patrols should spawn."),
						Component.text(" "),
						Component.text("false = no spawn")
					));
					btnPillager.setItemMeta(pillager_meta);

					ItemMeta pillagertrue_meta = btnPillagerTrue.getItemMeta();
					pillagertrue_meta.displayName(Component.text("Set pillager_patrol_spawn to True"));
					pillagertrue_meta.lore(List.of(
						Component.text("Pillagers will spawn.")
					));
					btnPillagerTrue.setItemMeta(pillagertrue_meta);

					ItemMeta pillagerfalse_meta = btnPillagerFalse.getItemMeta();
					pillagerfalse_meta.displayName(Component.text("Set pillager_patrol_spawn to False"));
					pillagerfalse_meta.lore(List.of(
						Component.text("Pillagers will NOT spawn.")
					));
					btnPillagerFalse.setItemMeta(pillagerfalse_meta);

					ItemMeta ender_meta = btnEnder.getItemMeta();
					ender_meta.displayName(Component.text("enderman_grief"));
					ender_meta.lore(List.of(
						Component.text("Set if Endermen can"),
						Component.text("pick up blocks."),
						Component.text(" "),
						Component.text("false = no pickup")
					));
					btnEnder.setItemMeta(ender_meta);

					ItemMeta endertrue_meta = btnEnderTrue.getItemMeta();
					endertrue_meta.displayName(Component.text("Set enderman_grief to True"));
					endertrue_meta.lore(List.of(
						Component.text("Endermen will pickup blocks.")
					));
					btnEnderTrue.setItemMeta(endertrue_meta);

					ItemMeta enderfalse_meta = btnEnderFalse.getItemMeta();
					enderfalse_meta.displayName(Component.text("Set enderman_grief to False"));
					enderfalse_meta.lore(List.of(
						Component.text("Endermen will NOT pickup blocks.")
					));
					btnEnderFalse.setItemMeta(enderfalse_meta);

					ItemMeta ghast_meta = btnGhast.getItemMeta();
					ghast_meta.displayName(Component.text("ghast_grief"));
					ghast_meta.lore(List.of(
						Component.text("Set whether Ghast"),
						Component.text("fireball explosions"),
						Component.text("can destroy blocks."),
						Component.text(" "),
						Component.text("false = no grief")
					));
					btnGhast.setItemMeta(ghast_meta);

					ItemMeta ghasttrue_meta = btnGhastTrue.getItemMeta();
					ghasttrue_meta.displayName(Component.text("Set ghast_grief to True"));
					ghasttrue_meta.lore(List.of(
						Component.text("Ghast fireballs will destroy blocks.")
					));
					btnGhastTrue.setItemMeta(ghasttrue_meta);

					ItemMeta ghastfalse_meta = btnGhastFalse.getItemMeta();
					ghastfalse_meta.displayName(Component.text("Set ghast_grief to False"));
					ghastfalse_meta.lore(List.of(
						Component.text("Ghast fireballs will NOT"),
						Component.text("destroy blocks.")
					));
					btnGhastFalse.setItemMeta(ghastfalse_meta);

					ItemMeta horse_meta = btnHorse.getItemMeta();
					horse_meta.displayName(Component.text("skeleton_horse_spawn"));
					horse_meta.lore(List.of(
						Component.text("Set whether Ghast"),
						Component.text("fireball explosions"),
						Component.text("can destroy blocks."),
						Component.text(" "),
						Component.text("false = no grief")
					));
					btnHorse.setItemMeta(horse_meta);

					ItemMeta horsetrue_meta = btnHorseTrue.getItemMeta();
					horsetrue_meta.displayName(Component.text("Set skeleton_horse_spawn to True"));
					horsetrue_meta.lore(List.of(
						Component.text("Skeleton Horses will spawn.")
					));
					btnHorseTrue.setItemMeta(horsetrue_meta);

					ItemMeta horsefalse_meta = btnHorseFalse.getItemMeta();
					horsefalse_meta.displayName(Component.text("Set skeleton_horse_spawn to False"));
					horsefalse_meta.lore(List.of(
						Component.text("Skeleton Horses will NOT spawn.")
					));
					btnHorseFalse.setItemMeta(horsefalse_meta);

					ItemMeta phantom_meta = btnPhantom.getItemMeta();
					phantom_meta.displayName(Component.text("phantom_spawn"));
					phantom_meta.lore(List.of(
						Component.text("Set whether Ghast"),
						Component.text("fireball explosions"),
						Component.text("can destroy blocks."),
						Component.text(" "),
						Component.text("false = no grief")
					));
					btnPhantom.setItemMeta(phantom_meta);

					ItemMeta phantomtrue_meta = btnPhantomTrue.getItemMeta();
					phantomtrue_meta.displayName(Component.text("Set phantom_spawn to True"));
					phantomtrue_meta.lore(List.of(
						Component.text("Phantoms will spawn.")
					));
					btnPhantomTrue.setItemMeta(phantomtrue_meta);

					ItemMeta phantomfalse_meta = btnPhantomFalse.getItemMeta();
					phantomfalse_meta.displayName(Component.text("Set phantom_spawn to False"));
					phantomfalse_meta.lore(List.of(
						Component.text("Phantoms will NOT spawn.")
					));
					btnPhantomFalse.setItemMeta(phantomfalse_meta);

					ItemMeta creeper_meta = btnCreeper.getItemMeta();
					creeper_meta.displayName(Component.text("creeper_grief"));
					creeper_meta.lore(List.of(
						Component.text("Set if Creeper"),
						Component.text("explosions can"),
						Component.text("destroy blocks."),
						Component.text(" "),
						Component.text("false = no grief")
					));
					btnCreeper.setItemMeta(creeper_meta);

					ItemMeta creepertrue_meta = btnCreeperTrue.getItemMeta();
					creepertrue_meta.displayName(Component.text("Set creeper_grief to True"));
					creepertrue_meta.lore(List.of(
						Component.text("Creeper Explosions will destroy blocks.")
					));
					btnCreeperTrue.setItemMeta(creepertrue_meta);

					ItemMeta creeperfalse_meta = btnCreeperFalse.getItemMeta();
					creeperfalse_meta.displayName(Component.text("Set creeper_grief to False"));
					creeperfalse_meta.lore(List.of(
						Component.text("Creeper Explosions will NOT destroy blocks.")
					));
					btnCreeperFalse.setItemMeta(creeperfalse_meta);

					ItemMeta save_meta = btnSave.getItemMeta();
					save_meta.displayName(Component.text("Set configs and save."));
					save_meta.lore(List.of(
						Component.text("Your current changes"),
						Component.text("will be set and saved"),
						Component.text("to getConfig().yml")
					));
					btnSave.setItemMeta(save_meta);

					ItemMeta cancel_meta = btnCancel.getItemMeta();
					cancel_meta.displayName(Component.text("Cancel"));
					cancel_meta.lore(List.of(
						Component.text("Cancel without setting"),
						Component.text("or saving."),
						Component.text("Your current changes"),
						Component.text("will be lost.")
					));



						//

						//Put the items in the inventory
						ItemStack[] menu_items = {
								btnUpdate, btnUpdateTrue, btnUpdateFalse, btnLang, btnLangCZ, btnLangDE, btnLangEN, btnLangFR, btnSpace,
								btnDebug, btnDebugTrue, btnDebugFalse, btnSpace, btnLangLOL, btnLangNL, btnLangBR, btnSpace, btnSpace,
								btnEnder, btnEnderTrue, btnEnderFalse, btnGhast, btnGhastTrue, btnGhastFalse, btnSpace, btnSpace, btnSpace,
								btnHorse, btnHorseTrue, btnHorseFalse, btnPhantom, btnPhantomTrue, btnPhantomFalse, btnSpace, btnSpace, btnSpace,
								btnCreeper, btnCreeperTrue, btnCreeperFalse, btnLongname, btnLongnameTrue, btnLongnameFalse, btnSpace, btnSave, btnCancel};
						gui.setContents(menu_items);
						player.openInventory(gui);
						return true;
					}else if(!sender.hasPermission("noendermangrief.admin")){
						sender.sendMessage(THIS_NAME + " " + get("neg.message.noperm"));
						return false;
					}
				}

				// BOOK command removed - GUI features no longer supported
				if(args[0].equalsIgnoreCase("toggledebug")||args[0].equalsIgnoreCase("td")){
					if( sender.isOp() || sender.hasPermission("noendermangrief.toggledebug") || !(sender instanceof Player) ||
							sender.hasPermission("noendermangrief.op") || sender.hasPermission("noendermangrief.admin") ){
						debug = !debug;
						sender.sendMessage(THIS_NAME + " " +
								get("neg.message.debugtrue").toString().replace("<boolean>", get("neg.message.boolean." + String.valueOf(debug).toLowerCase()) ));
						return true;
					}else{
						sender.sendMessage(THIS_NAME + " " + get("neg.message.noperm"));
						return false;
					}
				}

				if(args[0].equalsIgnoreCase("reload")){
					/** Check if player has permission */
					if ( sender.hasPermission("noendermangrief.op") || sender.isOp() || sender.hasPermission("noendermangrief.admin") || !(sender instanceof Player) ) {
						/** Command code */
						oldconfig = new YamlConfiguration();
						LOGGER.log("Checking config file version...");
						try {
							oldconfig.load(new File(getDataFolder() + "" + File.separatorChar + "getConfig().yml"));
						} catch (Exception exception) {
							LOGGER.warn("Could not load getConfig().yml");
							reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_CANNOT_CHECK_CONFIG).error(exception));
						}
						String checkconfigversion = oldconfig.getString("version", "1.0.0");
						if(checkconfigversion != null){
							if(!checkconfigversion.equalsIgnoreCase(configVersion)){
								try {
									copyFile_Java7(getDataFolder() + "" + File.separatorChar + "getConfig().yml",getDataFolder() + "" + File.separatorChar + "old_getConfig().yml");
								} catch (Exception exception) {
									reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_CANNOT_CHECK_CONFIG).error(exception));
								}
								saveResource("getConfig().yml", true);

								try {
									getConfig().load(new File(getDataFolder(), "getConfig().yml"));
								} catch (Exception exception) {
									LOGGER.warn("Could not load getConfig().yml");
									reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_CANNOT_CHECK_CONFIG).error(exception));
								}
								try {
									oldconfig.load(new File(getDataFolder(), "old_getConfig().yml"));
								} catch (Exception exception) {
									LOGGER.warn("Could not load old_getConfig().yml");
									reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_CANNOT_CHECK_CONFIG).error(exception));
								}
								getConfig().set("auto_update_check", oldconfig.get("auto_update_check", true));
								getConfig().set("debug", oldconfig.get("debug", false));
								getConfig().set("lang", oldconfig.get("lang", "en_US"));
								getConfig().set("console.longpluginname", oldconfig.get("console.longpluginname", true));
								getConfig().set("enderman_grief", oldconfig.get("enderman_grief", false));
								getConfig().set("skeleton_horse_spawn", oldconfig.get("skeleton_horse_spawn", false));
								getConfig().set("creeper_grief", oldconfig.get("creeper_grief", false));
								getConfig().set("wandering_trader_spawn", oldconfig.get("wandering_trader", false));
								getConfig().set("ghast_grief", oldconfig.get("ghast_grief", false));
								getConfig().set("phantom_spawn", oldconfig.get("phantom_spawn", false));
								getConfig().set("pillager_patrol_spawn", oldconfig.get("pillager_patrol_spawn", false));

								try {
									getConfig().save(new File(getDataFolder(), "getConfig().yml"));
								} catch (Exception exception) {
									LOGGER.warn("Could not save old settings to getConfig().yml");
									reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_CANNOT_CHECK_CONFIG).error(exception));
								}

								LOGGER.log("getConfig().yml Updated! old config saved as old_getConfig().yml");
								LOGGER.log("chance_getConfig().yml saved.");
							}else{
								try {
									getConfig().load(new File(getDataFolder(), "getConfig().yml"));
								} catch (Exception exception) {
									LOGGER.warn("Could not load getConfig().yml");
									reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_CANNOT_CHECK_CONFIG).error(exception));
								}
							}
							oldconfig = null;
						}
						LOGGER.log("Loading config file...");
						try {
							getConfig().load(new File(getDataFolder(), "getConfig().yml"));
						} catch (Exception exception) {
							reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_CANNOT_CHECK_CONFIG).error(exception));
						}
						try {
							getConfig().load(new File(getDataFolder(), "getConfig().yml"));
						} catch (Exception exception) {
							LOGGER.warn("Could not load getConfig().yml");
							reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_CANNOT_CHECK_CONFIG).error(exception));
						}

						debug = getConfig().getBoolean("debug", false);
						daLang = getConfig().getString("lang", "en_US");
						reloadConfig();

						sender.sendMessage(THIS_NAME + " has been " + "reloaded");
						return true;
					}else{
						sender.sendMessage("" + get("neg.message.no_perm"));
						return false;
					}
				}
				if( args[0].equalsIgnoreCase("eg") || args[0].equalsIgnoreCase("endermangrief") ){
					if(args.length <= 1){
						sender.sendMessage(THIS_NAME + " " + get("neg.entity.enderman.current" +
								"").toString().replace("[setting]", "" + getConfig().getBoolean("enderman_grief", false)));
						return true;
					}
					/** Check if player has permission */
					if ( !(sender instanceof Player) || sender.hasPermission("noendermangrief.admin") ) {
						/** Command code */
						if(!args[1].equalsIgnoreCase("true") & !args[1].equalsIgnoreCase("false")){
							sender.sendMessage(THIS_NAME + " " + get("neg.var.boolean") + ": /neg eg True/False");
							return false;
						}else if(args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false")){
							getConfig().set("enderman_grief", Boolean.parseBoolean(args[1]));
							saveConfig();

							sender.sendMessage(THIS_NAME + " " + get("neg.entity.enderman.set") + " " + args[1]);
							if(args[1].equalsIgnoreCase("false")){
								sender.sendMessage(THIS_NAME + " " + get("neg.entity.enderman.wont") );
							}else if(args[1].equalsIgnoreCase("true")){
								sender.sendMessage(THIS_NAME + " " + get("neg.entity.enderman.will") );
							}
							try {
								getConfig().load(new File(getDataFolder(), "getConfig().yml"));
							} catch (Exception exception) {
								LOGGER.warn("Could not load getConfig().yml");
								reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_CANNOT_LOAD_CONFIG).error(exception));
							}
							return true;
						}
					}else {
						sender.sendMessage("" + get("neg.message.noperm"));
						return false;
					}

				}
				if( args[0].equalsIgnoreCase("sh") || args[0].equalsIgnoreCase("skeletonhorse") ){
					if(args.length <= 1){
						sender.sendMessage(THIS_NAME + " " + get("neg.entity.skeleton_horse.current" +
								"").toString().replace("[setting]", "" + getConfig().getBoolean("skeleton_horse_spawn", false)));
						return true;
					}
					/** Check if player has permission */
					if ( !(sender instanceof Player) || sender.hasPermission("noendermangrief.admin") ) {
						/** Command code */
						if(!args[1].equalsIgnoreCase("true") & !args[1].equalsIgnoreCase("false")){
							sender.sendMessage(THIS_NAME + " " + get("neg.var.boolean") + ": /neg sh True/False");
							return false;
						}else if(args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false")){
							getConfig().set("skeleton_horse_spawn", Boolean.parseBoolean(args[1]));
							saveConfig();

							sender.sendMessage(THIS_NAME + " " + get("neg.entity.skeleton_horse.set") + " " + args[1]);
							if(args[1].equalsIgnoreCase("false")){
								sender.sendMessage(THIS_NAME + " " + get("neg.entity.skeleton_horse.wont") );
							}else if(args[1].equalsIgnoreCase("true")){
								sender.sendMessage(THIS_NAME + " " + get("neg.entity.skeleton_horse.will") );
							}
							try {
								getConfig().load(new File(getDataFolder(), "getConfig().yml"));
							} catch (Exception exception) {
								LOGGER.warn("Could not load getConfig().yml");
								reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_CANNOT_LOAD_CONFIG).error(exception));
							}
							return true;
						}
					}else {
						sender.sendMessage("" + get("neg.message.noperm"));
						return false;
					}
				}
				if( args[0].equalsIgnoreCase("wt") || args[0].equalsIgnoreCase("wanderingtrader") ){
					if(args.length <= 1){
						sender.sendMessage(THIS_NAME + " " + get("neg.entity.wandering_trader.current" +
								"").toString().replace("[setting]", "" + getConfig().getBoolean("wandering_trader_spawn", false)));
						return true;
					}
					LOGGER.log("args.length=" + args.length);
					/** Check if player has permission */
					if ( !(sender instanceof Player) || sender.hasPermission("noendermangrief.admin") ) {
						/** Command code */
						if(!args[1].equalsIgnoreCase("true") & !args[1].equalsIgnoreCase("false")){
							sender.sendMessage(THIS_NAME + " " + get("neg.var.boolean") + ": /neg sh True/False");
							return false;
						}else if(args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false")){
							getConfig().set("wandering_trader_spawn", Boolean.parseBoolean(args[1]));
							saveConfig();

							sender.sendMessage(THIS_NAME + " " + get("neg.entity.wandering_trader.set") + " " + args[1]);
							if(args[1].equalsIgnoreCase("false")){
								sender.sendMessage(THIS_NAME + " " + get("neg.entity.wandering_trader.wont") );
							}else if(args[1].equalsIgnoreCase("true")){
								sender.sendMessage(THIS_NAME + " " + get("neg.entity.wandering_trader.will") );
							}
							try {
								getConfig().load(new File(getDataFolder(), "getConfig().yml"));
							} catch (Exception exception) {
								LOGGER.warn("Could not load getConfig().yml");
								reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_CANNOT_LOAD_CONFIG).error(exception));
							}
							return true;
						}
					}else {
						sender.sendMessage("" + get("neg.message.noperm"));
						return false;
					}
				}
				if( args[0].equalsIgnoreCase("cg") || args[0].equalsIgnoreCase("creepergrief") ){
					if(args.length <= 1){
						sender.sendMessage(THIS_NAME + " " + get("neg.entity.creeper.current" +
								"").toString().replace("[setting]", "" + getConfig().getBoolean("creeper_grief", false)));
						return true;
					}
					/** Check if player has permission */
					if ( !(sender instanceof Player) || sender.hasPermission("noendermangrief.admin") ) {
						/** Command code */
						if(!args[1].equalsIgnoreCase("true") & !args[1].equalsIgnoreCase("false")){
							sender.sendMessage(THIS_NAME + " " + get("neg.var.boolean") + ": /neg eg True/False");
							return false;
						}else if(args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false")){
							getConfig().set("creeper_grief", Boolean.parseBoolean(args[1]));
							saveConfig();

							sender.sendMessage(THIS_NAME + " " + get("neg.entity.creeper.set") + " " + args[1]);
							if(args[1].equalsIgnoreCase("false")){
								sender.sendMessage(THIS_NAME + " " + get("neg.entity.creeper.wont") );
							}else if(args[1].equalsIgnoreCase("true")){
								sender.sendMessage(THIS_NAME + " " + get("neg.entity.creeper.will") );
							}
							try {
								getConfig().load(new File(getDataFolder(), "getConfig().yml"));
							} catch (Exception exception) {
								LOGGER.warn("Could not load getConfig().yml");
								reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_CANNOT_LOAD_CONFIG).error(exception));
							}
							return true;
						}
					}else {
						sender.sendMessage("" + get("neg.message.noperm"));
						return false;
					}
				}
				// Ghast Grief
				if( args[0].equalsIgnoreCase("gg") || args[0].equalsIgnoreCase("ghastgrief") ){
					if(args.length <= 1){
						sender.sendMessage(THIS_NAME + " " + get("neg.entity.ghast.current")
						.toString().replace("[setting]", "" + getConfig().getBoolean("ghast_grief", false)));
						return true;
					}
					/** Check if player has permission */
					if ( !(sender instanceof Player) || sender.hasPermission("noendermangrief.admin") ) {
						/** Command code */
						if(!args[1].equalsIgnoreCase("true") & !args[1].equalsIgnoreCase("false")){
							sender.sendMessage(THIS_NAME + " " + get("neg.var.boolean") + ": /neg eg True/False");
							return false;
						}else if(args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false")){
							getConfig().set("ghast_grief", Boolean.parseBoolean(args[1]));
							saveConfig();

							sender.sendMessage(THIS_NAME + " " + get("neg.entity.ghast.set") + " " + args[1]);
							if(args[1].equalsIgnoreCase("false")){
								sender.sendMessage(THIS_NAME + " " + get("neg.entity.ghast.wont") );
							}else if(args[1].equalsIgnoreCase("true")){
								sender.sendMessage(THIS_NAME + " " + get("neg.entity.ghast.will") );
							}
							try {
								getConfig().load(new File(getDataFolder(), "getConfig().yml"));
							} catch (Exception exception) {
								LOGGER.warn("Could not load getConfig().yml");
								reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_CANNOT_LOAD_CONFIG).error(exception));
							}
							return true;
						}
					}else {
						sender.sendMessage("" + get("neg.message.noperm"));
						return false;
					}
				}
				if( args[0].equalsIgnoreCase("pg") || args[0].equalsIgnoreCase("phantomgrief") ){ // Phantom Grief
					if(args.length <= 1){
						sender.sendMessage(THIS_NAME + " " + get("neg.entity.phantom.current")
						.toString().replace("[setting]", "" + getConfig().getBoolean("phantom_spawn", false)));
						return true;
					}
					/** Check if player has permission */
					if( !(sender instanceof Player) || sender.hasPermission("noendermangrief.admin") ) {
						/** Command code */
						if(!args[1].equalsIgnoreCase("true") & !args[1].equalsIgnoreCase("false")){
							sender.sendMessage(THIS_NAME + " " + get("neg.var.boolean") + ": /neg eg True/False");
							return false;
						}else if(args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false")){
							getConfig().set("phantom_spawn", Boolean.parseBoolean(args[1]));
							saveConfig();

							sender.sendMessage(THIS_NAME + " " + get("neg.entity.phantom.set") + " " + args[1]);
							if(args[1].equalsIgnoreCase("false")){
								sender.sendMessage(THIS_NAME + " " + get("neg.entity.phantom.wont") );
							}else if(args[1].equalsIgnoreCase("true")){
								sender.sendMessage(THIS_NAME + " " + get("neg.entity.phantom.will") );
							}
							try {
								getConfig().load(new File(getDataFolder(), "getConfig().yml"));
							} catch (Exception exception) {
								LOGGER.warn("Could not load getConfig().yml");
								reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_CANNOT_LOAD_CONFIG).error(exception));
							}
							return true;
						}
					}else {
						sender.sendMessage("" + get("neg.message.noperm"));
						return false;
					}
				}
				if( args[0].equalsIgnoreCase("pp") || args[0].equalsIgnoreCase("pillagerpatrol") ){ // Pillager Patrol Grief
					if(args.length <= 1){
						sender.sendMessage(THIS_NAME + " " + get("neg.entity.pillager_patrol.current")
						.toString().replace("[setting]", "" + getConfig().getBoolean("pillager_patrol_spawn", false)));
						return true;
					}
					/** Check if player has permission */
					if( !(sender instanceof Player) || sender.hasPermission("noendermangrief.admin") ) {
						/** Command code */
						if(!args[1].equalsIgnoreCase("true") & !args[1].equalsIgnoreCase("false")){
							sender.sendMessage(THIS_NAME + " " + get("neg.var.boolean") + ": /neg eg True/False");
							return false;
						}else if(args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false")){
							getConfig().set("pillager_patrol_spawn", Boolean.parseBoolean(args[1]));
							saveConfig();

							sender.sendMessage(THIS_NAME + " " + get("neg.entity.pillager_patrol.set") + " " + args[1]);
							if(args[1].equalsIgnoreCase("false")){
								sender.sendMessage(THIS_NAME + " " + get("neg.entity.pillager_patrol.wont") );
							}else if(args[1].equalsIgnoreCase("true")){
								sender.sendMessage(THIS_NAME + " " + get("neg.entity.pillager_patrol.will") );
							}
							try {
								getConfig().load(new File(getDataFolder(), "getConfig().yml"));
							} catch (Exception exception) {
								LOGGER.warn("Could not load getConfig().yml");
								reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_CANNOT_LOAD_CONFIG).error(exception));
							}
							return true;
						}
					}else {
						sender.sendMessage("" + get("neg.message.noperm"));
						return false;
					}
				}
			}
		}catch(Exception exception) {
			reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.UNHANDLED_COMMAND_ERROR).error(exception));
			// ERROR_RUNNING_DRAGON_DEATH_COMMAND "Error running command after dragon death."
		}
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) { // TODO: Tab Complete
		try {
			if (command.getName().equalsIgnoreCase("NEG")) {
				List<String> autoCompletes = new ArrayList<>(); //create a new string list for tab completion
				if (args.length == 1) { // reload, toggledebug, playerheads, customtrader, headfix
					autoCompletes.add("Update");
					autoCompletes.add("Reload");
					autoCompletes.add("ToggleDebug");
					autoCompletes.add("Book");
					autoCompletes.add("EndermanGrief");
					autoCompletes.add("SkeletonHorse");
					autoCompletes.add("CreeperGrief");
					autoCompletes.add("WanderingTrader");
					autoCompletes.add("GhastGrief");
					autoCompletes.add("PhantomGrief");
					autoCompletes.add("PillagerPatrol");
					return autoCompletes; // then return the list
				}
				if(args.length > 1) {
					if( args[0].equalsIgnoreCase("EndermanGrief") || args[0].equalsIgnoreCase("eg") ) {
						autoCompletes.add("true");
						autoCompletes.add("false");
						return autoCompletes; // then return the list
					}
					if( args[0].equalsIgnoreCase("SkeletonHorse") || args[0].equalsIgnoreCase("sh") ) {
						autoCompletes.add("true");
						autoCompletes.add("false");
						return autoCompletes; // then return the list
					}
					if( args[0].equalsIgnoreCase("CreeperGrief") || args[0].equalsIgnoreCase("cg") ) {
						autoCompletes.add("true");
						autoCompletes.add("false");
						return autoCompletes; // then return the list
					}
					if( args[0].equalsIgnoreCase("WanderingTrader") || args[0].equalsIgnoreCase("wt") ) {
						autoCompletes.add("true");
						autoCompletes.add("false");
						return autoCompletes; // then return the list
					}
					if( args[0].equalsIgnoreCase("GhastGrief") || args[0].equalsIgnoreCase("gg") ) {
						autoCompletes.add("true");
						autoCompletes.add("false");
						return autoCompletes; // then return the list
					}
					if( args[0].equalsIgnoreCase("PhantomGrief") || args[0].equalsIgnoreCase("pg") ) {
						autoCompletes.add("true");
						autoCompletes.add("false");
						return autoCompletes; // then return the list
					}
					if( args[0].equalsIgnoreCase("PillagerPatrol") || args[0].equalsIgnoreCase("pp") ) {
						autoCompletes.add("true");
						autoCompletes.add("false");
						return autoCompletes; // then return the list
					}
				}
			}
		}catch(Exception exception) {
			reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_TAB_COMPLETE_ERROR).error(exception));
			// ERROR_RUNNING_DRAGON_DEATH_COMMAND "Error running command after dragon death."
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
			getConfig().save(new File(getDataFolder(), "getConfig().yml"));
		} catch (Exception exception) {
			LOGGER.warn("Could not save old settings to getConfig().yml");
			reporter.reportDetailed(this, Report.newBuilder(PluginLibrary.REPORT_CANNOT_SAVE_CONFIG).error(exception));
		}
	}

	public boolean isCorrectVersion(){
		// NMS version checking removed - always return true for Paper API compatibility
		return true;
	}

	public boolean saveConfig(boolean update, boolean Debug, boolean Console, boolean Longname, boolean Trader, boolean Pillager, boolean Ender, boolean Ghast, boolean Horse,
			boolean Phantom, boolean Creeper, String Lang) {
		//	debug	daLang	colorful_console
		debug = Debug;
		daLang = Lang;
		if(!Longname) {
			pluginName = "NEG";
		}else {
			pluginName = THIS_NAME;
		}
		getConfig().set("debug", Debug);
		getConfig().set("lang", Lang);
		getConfig().set("console.longpluginname", Longname);
		getConfig().set("enderman_grief", Ender);
		getConfig().set("skeleton_horse_spawn", Horse);
		getConfig().set("creeper_grief", Creeper);
		getConfig().set("wandering_trader_spawn", Trader);
		getConfig().set("ghast_grief", Ghast);
		getConfig().set("phantom_spawn", Phantom);
		getConfig().set("pillager_patrol_spawn", Pillager);
		try {
			getConfig().save(new File(getDataFolder(), "getConfig().yml"));
		} catch (IOException e) {
			LOGGER.warn("Could not save settings to getConfig().yml");
			e.printStackTrace();
			return false;
		}
		LOGGER.log("getConfig().yml has been updated");
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
