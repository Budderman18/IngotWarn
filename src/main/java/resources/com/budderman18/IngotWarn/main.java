
package com.budderman18.IngotWarn;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class enables and disables the plugin
 * It also imports commands and handles events
 */
public class main extends JavaPlugin implements Listener { 
    //used if the given file isnt in another folder
    final String ROOT = "";
    ConsoleCommandSender sender = getServer().getConsoleSender();
    //retrive plugin instance
    private static main plugin;
    
    public static main getInstance() {
        return plugin;
    }
    

    /*
    * This method creates files if needed
    */
    private void createFiles() {
        File configf = new File(getDataFolder(), "config.yml");
        if (!configf.exists()) {
            configf.getParentFile().mkdirs();
            saveResource("config.yml", false);
         }

        FileConfiguration config = new YamlConfiguration();
        try {
            config.load(configf);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        File playerdataf = new File(getDataFolder(), "playerdata.yml");
        if (!playerdataf.exists()) {
            playerdataf.getParentFile().mkdirs();
            saveResource("playerdata.yml", false);
         }

        FileConfiguration pd = new YamlConfiguration();
        try {
            pd.load(playerdataf);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        File languagef = new File(getDataFolder(), "language.yml");
        if (!languagef.exists()) {
            languagef.getParentFile().mkdirs();
            saveResource("language.yml", false);
         }

        FileConfiguration language = new YamlConfiguration();
        try {
            language.load(languagef);
        } 
        catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    } 
    /*
    * Enables the plugin.
    * Checks if MC version isn't the latest.
    * If its not, warn the player about lacking support
    * Checks if server is running offline mode
    * If it is, disable the plugin
    * Also checks for dependencies and loads commands
    */
    @Override
    public void onEnable() {
        //creates files if needed
        createFiles();
        getServer().getPluginManager().enablePlugin(this);
        plugin = this;
        FileUpdater getdata = new FileUpdater();
        //import files
        FileConfiguration config = getdata.getCustomData(plugin,"config",ROOT);
        FileConfiguration language = getdata.getCustomData(plugin,"language",ROOT);
        FileConfiguration pd = getdata.getCustomData(plugin,"playerdata",ROOT);
        //language updater variables
        String outdatedConfigMessage = ChatColor.translateAlternateColorCodes('&', "&4OUTDATED CONFIG!!!! &bConverting to newest format...");
        String outdatedLanguageMessage = ChatColor.translateAlternateColorCodes('&', "&4OUTDATED LANGUAGE!!!! &bConverting to newest format...");
        String outdatedPlayerDataMessage = ChatColor.translateAlternateColorCodes('&', "&4OUTDATED PLAYERDATA!!!! &bConverting to newest format...");
        if (language.getString("Outdated-Config-Message") != null) {
            outdatedConfigMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Outdated-Config-Message"));
        }
        if (language.getString("Outdated-Language-Message") != null) {
            outdatedLanguageMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Outdated-Language-Message"));
        }
        if (language.getString("Outdated-Player-Data-Message") != null) {
            outdatedPlayerDataMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Outdated-Player-Data-Message"));
        } 
        //check for versions
        FileUpdater update = new FileUpdater();
        if (!"1.1".equals(config.getString("version"))) {
            sender.sendMessage(outdatedConfigMessage);
            update.updateConfig();
        }
        if (!"1.1".equals(language.getString("version"))) {
            sender.sendMessage(outdatedLanguageMessage);
            update.updateLanguage();
        }
        if (!"1.1".equals(pd.getString("version"))) {
            sender.sendMessage(outdatedPlayerDataMessage);
            update.updatePlayerData();
        }
        //language variables)
        String prefixMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Prefix-Message")); 
        String unsupportedVersionAMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Unsupported-VersionA-Message")); 
        String unsupportedVersionBMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Unsupported-VersionB-Message")); 
        String unsupportedVersionCMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Unsupported-VersionC-Message")); 
        String unsecureServerAMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Unsecure-ServerA-Message")); 
        String unsecureServerBMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Unsecure-ServerB-Message")); 
        String unsecureServerCMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Unsecure-ServerC-Message")); 
        String pluginEnabledMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Plugin-Enabled-Message")); 
        //check for correct version
        if (!(Bukkit.getVersion().contains("1.19"))) {
            sender.sendMessage(prefixMessage + unsupportedVersionAMessage);
            sender.sendMessage(prefixMessage + unsupportedVersionBMessage);
            sender.sendMessage(prefixMessage + unsupportedVersionCMessage); 
        }
        //check for online mode
        if (!(getServer().getOnlineMode())) {
            sender.sendMessage(prefixMessage + unsecureServerAMessage);
            sender.sendMessage(prefixMessage + unsecureServerBMessage);
            sender.sendMessage(prefixMessage + unsecureServerCMessage);
            getServer().getPluginManager().disablePlugin(this);
        }
        //commands
        this.getCommand("warn").setExecutor(new Warn());
        this.getCommand("checkwarns").setExecutor(new CheckWarn());
        this.getCommand("adminwarn").setExecutor(new AdminWarn());
        //events
        getServer().getPluginManager().registerEvents(this,this);
        sender.sendMessage(prefixMessage + pluginEnabledMessage);
    }
    /*
    * This method creates playerdata when someone new joins
    * In the future, it'll alert players where werent online when warned
    */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void createUserData(PlayerJoinEvent event) throws IOException {
        FileUpdater getdata = new FileUpdater();
        plugin = this;
        //import files
        File playerdataf = new File("plugins/IngotWarn","playerdata.yml");
        FileConfiguration config = getdata.getCustomData(plugin,"config",ROOT);
        FileConfiguration language = getdata.getCustomData(plugin,"language",ROOT);
        FileConfiguration pd = getdata.getCustomData(plugin,"playerdata",ROOT);
        //language
        String prefixMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Prefix-Message")); 
        String newPlayerMessage = ChatColor.translateAlternateColorCodes('&', language.getString("New-Player-Message")); 
        String playerExistsMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Player-Exists-Message")); 
        //converts username back into a string
        Player username = event.getPlayer();
        String usernameString = username.getName();
        boolean exists = false;
        boolean checkable = true;
        String tempusername = null;
        //check if joined user has data
        if (pd.getString(usernameString) == null) {
            //tell console
            sender.sendMessage(prefixMessage + newPlayerMessage);
            //check if joined player's UUID already is in the data
            for (String key : pd.getKeys(false)) {
                try {
                    pd.getString(key + ".UUID").equals(username.getUniqueId().toString());
                }
                catch (NullPointerException ex) {
                    checkable = false;
                }
                if (checkable == true) {
                    if (pd.getString(key + ".UUID").equals(username.getUniqueId().toString())) {
                        tempusername = key;
                        //skip fresh generation
                        exists = true;
                    }
                }
            }
            //create section
            pd.createSection(usernameString);
            if (exists == true) {
                //tell console
                sender.sendMessage(prefixMessage + playerExistsMessage);
                //copy over old data to new data
                byte maxWarns = (byte) Integer.parseInt(config.getString("Max-Warns"));
                pd.set(usernameString + ".UUID", pd.getString(tempusername + ".UUID"));
                for (byte i = 1; i < maxWarns + 1; i++) {
                    if (pd.get(tempusername + ".Warn" + i) != null) {
                        pd.set(usernameString + ".Warn" + i, pd.getString(tempusername + ".Warn" + i));
                    } else {
                        i = maxWarns;
                    }
                }
            }
            //check if no data exists
            if (exists == false) {
                //generate UUID section
                UUID uuid = username.getUniqueId();
                pd.set(usernameString + ".UUID", uuid.toString());
            }
            //saves file 
            pd.save(playerdataf);
        }
    }
    /*
    * This method checks if the joined player was notified for all their warns
    * Useful for offline warns
    */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void warnOfflinePlayers(PlayerJoinEvent event) throws IOException {
        plugin = this;
        FileUpdater getdata = new FileUpdater();
        //import files
        File playerdataf = new File("plugins/IngotWarn","playerdata.yml");
        FileConfiguration config = getdata.getCustomData(plugin,"config",ROOT);
        FileConfiguration language = getdata.getCustomData(plugin,"language",ROOT);
        FileConfiguration pd = getdata.getCustomData(plugin,"playerdata",ROOT);
        //language
        String warnedMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Warned-Player-Message"));
        byte maxWarns = (byte) Integer.parseInt(config.getString("Max-Warns"));
        String usernameString = event.getPlayer().getName();
        for (byte i=1; i < maxWarns + 1; i++) {
            if ((pd.get(usernameString + ".Warn" + i) != null && pd.getBoolean(usernameString + ".Warn" + i + ".isNotified") == false) && pd.getString(usernameString) != null) {
                Player target = event.getPlayer();
                String warnReason = pd.getString(usernameString + ".Warn" + i + ".Message");
                target.sendMessage(warnedMessage + warnReason);
                pd.set(usernameString + ".Warn" + i + ".isNotified", "true");
                //saves file
                pd.save(playerdataf);
            }
        }
    }
    /*
    * Disables the plugin.
    */
    @Override
    public void onDisable() {
        plugin = this;
        FileUpdater getdata = new FileUpdater();
        //import files
        File configf = new File("plugins/IngotWarn","config.yml");
        File languagef = new File("plugins/IngotWarn","language.yml");
        File playerdataf = new File("plugins/IngotWarn","playerdata.yml");
        FileConfiguration config = getdata.getCustomData(plugin, "config", ROOT);
        FileConfiguration language = getdata.getCustomData(plugin, "language", ROOT);
        FileConfiguration pd = getdata.getCustomData(plugin, "playerdata", ROOT);
        //language
        String prefixMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Prefix-Message")); 
        String pluginDisabledMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Plugin-Disabled-Message")); 
        //saves files
        try {
            config.save(configf);
        } 
        catch (IOException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            language.save(languagef);
        } 
        catch (IOException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            pd.save(playerdataf);
        } 
        catch (IOException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        //disables plugin
        getServer().getPluginManager().disablePlugin(this);
        sender.sendMessage(prefixMessage + pluginDisabledMessage);
    }
}
