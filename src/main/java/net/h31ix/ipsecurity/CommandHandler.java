package net.h31ix.ipsecurity;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHandler implements CommandExecutor
{
    private IPSecurity plugin;
           
    public CommandHandler(IPSecurity plugin) {
        this.plugin = plugin;
    }
    
    public boolean onCommand(CommandSender cs, Command cmd, String alias, String[] args) {
        if(cmd.getName().equalsIgnoreCase("ips")) {
            if(cs.hasPermission("ipsecurity.admin") || cs.isOp()) {
                if(args.length == 0) {
                    sendHelp(cs);
                    return true;
                }
                else {
                    if(args.length == 1) {
                        if(args[0].equalsIgnoreCase("help")) {
                            sendHelp(cs);
                            return true;
                        }
                        if(args[0].equalsIgnoreCase("reload")) {
                            plugin.loadConfiguration();
                            cs.sendMessage(ChatColor.BLUE+"Configuration reloaded.");
                            return true;
                        }                        
                    }
                    else if(args.length == 2) {
                        if(args[0].equalsIgnoreCase("list")) { 
                            List<String> ips = plugin.getIps(args[1]);
                            if(ips != null && !ips.isEmpty()) {
                                cs.sendMessage(ChatColor.BLUE+"IPs for 'Player: "+ChatColor.WHITE+args[1]+ChatColor.BLUE+"':");
                                
                                for(int i=0;i<ips.size();i++) {
                                    cs.sendMessage((i+1) + ") "+ChatColor.GOLD+ips.get(i));
                                }
                            }
                        }                        
                    }
                    else if(args.length == 3) {
                        if(args[0].equalsIgnoreCase("remove")) {  	
                            if(plugin.getIps(args[1]) != null) {
                            	if(plugin.getIps(args[1]).isEmpty()) {
                                    cs.sendMessage(ChatColor.RED+"IPs for "+ChatColor.RED+"'Player: "+ChatColor.WHITE+args[1]+ChatColor.RED+"' not found in the configuration.");
                                    return true;
                            	}
                            	
                            	if(args[2].equalsIgnoreCase("all")) {
                                    plugin.remove(args[1], args[2]);
                                    plugin.loadConfiguration();
                                    cs.sendMessage(ChatColor.BLUE +"IP: * removed from "+ChatColor.BLUE+"'Player: "+ChatColor.WHITE+args[1]+ChatColor.BLUE+"' in the configuration.");
                                    return true;
                            	}
                            	
                            	if(!plugin.getIps(args[1]).contains(args[2]))  {
                                    cs.sendMessage(ChatColor.RED+"IP: "+args[2]+" for "+ChatColor.RED+"'Player: "+ChatColor.WHITE+args[1]+ChatColor.RED+"' not found in the configuration.");
                                    return true;
                            	}
                            	
                                plugin.remove(args[1], args[2]);
                                plugin.loadConfiguration();
                                cs.sendMessage(ChatColor.BLUE +"IP: "+args[2]+" from "+ChatColor.BLUE+"'Player: "+ChatColor.WHITE+args[1]+ChatColor.BLUE+"' in the configuration.");
                                return true;
                            }
                            else {
                                cs.sendMessage(ChatColor.RED+"IPs for "+ChatColor.RED+"'Player: "+ChatColor.WHITE+args[1]+ChatColor.RED+"' not found in the configuration.");
                                return true;
                            }
                        }
                    }
                    else if(args.length == 4) {
                        if(args[0].equalsIgnoreCase("add")) {
                            if(isBoolean(args[3])) {
                                plugin.add(args[1], args[2], Boolean.parseBoolean(args[3]));
                                plugin.loadConfiguration();
                                cs.sendMessage(ChatColor.BLUE+"IP: "+args[2]+ " added to "+ChatColor.BLUE+"'Player: "+ChatColor.WHITE+args[1]+ChatColor.BLUE+"' in the configuration");
                                return true;
                            }
                            else {
                                cs.sendMessage(ChatColor.RED+"True/False value expected for op setting, String given.");
                                return true;
                            }
                        }
                        else {
                            sendHelp(cs);
                            return true;
                        }
                    }  
                    else {
                        sendHelp(cs);
                        return true;
                    }
                }
            }
            else {
                cs.sendMessage(ChatColor.RED+"Sorry, but you do not have permission to execute that command.");
                return true;
            }
        }
        return true;
    } 
    
    private boolean isBoolean(String s) {
        try {
            Boolean.parseBoolean(s);
            return true;
        }
        catch(Exception ex) {
            return false;
        }
    }
    
    private void sendHelp(CommandSender cs) {
        cs.sendMessage(ChatColor.GOLD+"Usage:");
        cs.sendMessage(ChatColor.GOLD+"/ips reload "+ChatColor.WHITE+"| Reload the config.");
        cs.sendMessage(ChatColor.GOLD+"/ips add [user] [ip/none] [op(true/false)] "+ChatColor.WHITE+"| Add a user or ip to the config.");
        cs.sendMessage(ChatColor.GOLD+"/ips remove [user] [ip/all] "+ChatColor.WHITE+"| Remove a ip from a user in the config.");
        cs.sendMessage(ChatColor.GOLD+"/ips list [user] "+ChatColor.WHITE+"| List allowed IPs for a player.");
        cs.sendMessage(ChatColor.GOLD+"/ips help "+ChatColor.WHITE+"| Access this page.");        
    }
}
