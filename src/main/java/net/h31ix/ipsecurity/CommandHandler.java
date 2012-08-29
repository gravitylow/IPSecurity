package net.h31ix.ipsecurity;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHandler implements CommandExecutor
{
    private IPSecurity plugin;
           
    public CommandHandler(IPSecurity plugin)
    {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String alias, String[] args)
    {
        if(cmd.getName().equalsIgnoreCase("ips"))
        {
            if(cs.hasPermission("ipsecurity.admin") || cs.isOp())
            {
                if(args.length == 0)
                {
                    sendHelp(cs);
                }
                else
                {
                    if(args.length == 1)
                    {
                        if(args[0].equalsIgnoreCase("help"))
                        {
                            sendHelp(cs);
                        }
                        if(args[0].equalsIgnoreCase("reload"))
                        {
                            plugin.loadConfiguration();
                            cs.sendMessage(ChatColor.BLUE+"Configuration reloaded.");
                        }                        
                    }
                    else if(args.length == 2)
                    {
                        if(args[0].equalsIgnoreCase("remove"))
                        {
                            if(plugin.getIp(args[1]) != null)
                            {
                                plugin.remove(args[1]);
                                plugin.loadConfiguration();
                                cs.sendMessage(ChatColor.BLUE+"Player: '"+ChatColor.WHITE+args[1]+ChatColor.BLUE+"' removed from the configuration.");
                            }
                            else
                            {
                                cs.sendMessage(ChatColor.RED+"Player: '"+ChatColor.WHITE+args[1]+ChatColor.RED+"' not found in the configuration.");
                            }
                        }
                        else
                        {
                            sendHelp(cs);
                        }
                    }
                    else if(args.length == 4)
                    {
                        if(args[0].equalsIgnoreCase("add"))
                        {
                            if(isBoolean(args[3]))
                            {
                                plugin.add(args[1],args[2],Boolean.parseBoolean(args[3]));
                                plugin.loadConfiguration();
                                cs.sendMessage(ChatColor.BLUE+"Player: '"+ChatColor.WHITE+args[1]+ChatColor.BLUE+"' added to the configuration");
                            }
                            else
                            {
                                cs.sendMessage(ChatColor.RED+"True/False value expected for op setting, String given.");
                            }
                        }
                        else
                        {
                            sendHelp(cs);
                        }
                    }  
                    else
                    {
                        sendHelp(cs);
                    }
                }
            }
            else
            {
                cs.sendMessage(ChatColor.RED+"Sorry, but you do not have permission to execute that command.");
            }
        }
        return true;
    } 
    
    private boolean isBoolean(String s)
    {
        try 
        {
            Boolean.parseBoolean(s);
            return true;
        }
        catch(Exception ex)
        {
            return false;
        }
    }
    
    private void sendHelp(CommandSender cs)
    {
        cs.sendMessage(ChatColor.GOLD+"Usage:");
        cs.sendMessage(ChatColor.GOLD+"/ips reload "+ChatColor.WHITE+"| Reload the config.");
        cs.sendMessage(ChatColor.GOLD+"/ips add [user] [ip/none] [op(true/false)] "+ChatColor.WHITE+"| Add a user to the config.");
        cs.sendMessage(ChatColor.GOLD+"/ips remove [user] "+ChatColor.WHITE+"| Remove a user from the config.");
        cs.sendMessage(ChatColor.GOLD+"/ips help "+ChatColor.WHITE+"| Access this page.");        
    }
}
