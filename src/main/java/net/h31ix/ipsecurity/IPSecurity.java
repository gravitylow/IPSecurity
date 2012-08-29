package net.h31ix.ipsecurity;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class IPSecurity extends JavaPlugin
{
    private FileConfiguration config;
    private File configFile;
    private boolean ban;
    private String kickReason;
    
    @Override
    public void onDisable() 
    {
    }

    @Override
    public void onEnable() 
    {
        loadConfiguration();
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getCommand("ips").setExecutor(new CommandHandler(this));
    }
    
    public String getIp(String player)
    {      
        return config.getString("players."+player.toLowerCase()+".ip");
    }
    
    public boolean isOp(String player)
    {
        return config.getBoolean("players."+player.toLowerCase()+".op");
    }
    
    public boolean ban()
    {
        return ban;
    }
    
    public String getReason()
    {
        return kickReason;
    }
    
    public void remove(String player)
    {
        config.set("players."+player.toLowerCase(), null);
        saveConfiguration();
    }
    
    public void add(String player, String ip, boolean op)
    {
        config.set("players."+player.toLowerCase()+".ip", ip);
        config.set("players."+player.toLowerCase()+".op", op);
        saveConfiguration();
    }
    
    private void saveConfiguration()
    {
        try 
        {
            config.save(configFile);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(IPSecurity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void loadConfiguration()
    {
        configFile = new File(this.getDataFolder()+"/config.yml");
        if(!configFile.exists())
        {
            this.saveDefaultConfig();
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        ban = config.getBoolean("settings.ban-on-wrong-ip");
        kickReason = config.getString("settings.kick-reason");        
    }
}

