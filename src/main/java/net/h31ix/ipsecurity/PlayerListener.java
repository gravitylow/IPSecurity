package net.h31ix.ipsecurity;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener 
{
    private IPSecurity plugin;
    
    public PlayerListener(IPSecurity plugin)
    {
        this.plugin = plugin;
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerLogin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        String name = player.getName();
        InetAddress address = null;
        try
        {
            address = InetAddress.getByName(player.getAddress().getAddress().toString().replace("/", ""));
        	
            String pip = null;
            
            if(address.getHostName() == null)
            {
            	pip = player.getAddress().getAddress().toString().replace("/", "");
            } else
            {
            	pip = address.getHostName().replace("/", "");
            }
            
            if(player.isOp())
            {
                player.setOp(false);
            }
            List<String> ips = plugin.getIps(name);
            
            if(ips != null)
            {
            	List<String> expectedIps = new ArrayList<String>();
            	
            	for(String ip: ips)
            	{
            		if(!ip.equalsIgnoreCase("none"))
            		{
            			if(!pip.startsWith(ip)) 
                        {
            				expectedIps.add(ip); 
                            continue;
                        }
            			
            			if(plugin.isOp(name))
            			{
            				player.setOp(true);
            			}
            			
            			return;
            		}
            		else
            		{
            			error(player, ip, pip, 2);
            			return;
            		}
            	}
            	
            	// set as the actual address in the case of hostnames
            	pip = player.getAddress().getAddress().toString().replace("/", "");
            	
            	//second attempt using the true ip
            	
            	for(String ip: ips) 
            	{
            		if(!ip.equalsIgnoreCase("none")) 
            		{
            			if(!pip.startsWith(ip)) 
            			{
            				expectedIps.add(ip); 
                            continue;
                        }
            			
            			if(plugin.isOp(name)) 
            			{
            				player.setOp(true);
            			}
            			
            			return;
            		}
            		
            		else 
            		{
            			error(player, ip, pip, 2);
            			return;
            		}
            	}
            }  
        } catch (UnknownHostException e)
        {
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        if(player.isOp())
        {
            player.setOp(false);
        }
    }
    
    private void error(Player player, String expected, String given, int type)
    {
        if(plugin.ban())
        {
            String ip = player.getAddress().getAddress().toString();
            plugin.getServer().banIP(ip);
        }
        player.kickPlayer(plugin.getReason());
        
        if(plugin.alert())
        {
        	plugin.getLogger().info("********************************");
        	if(type == 1)
        	{
        		plugin.getLogger().info("ALERT: "+player.getName()+" logged in with ip: "+given);
        		plugin.getLogger().info("But ip: "+expected+" was expected.");
        	}
        	else if (type == 2)
        	{
        		plugin.getLogger().info("ALERT: "+player.getName()+" tried to log in, but is not allowed.");
        	}
        	plugin.getLogger().info("********************************");
        }
    }
}
