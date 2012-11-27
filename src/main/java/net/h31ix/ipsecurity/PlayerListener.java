package net.h31ix.ipsecurity;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
    
    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        String name = player.getName();
        InetAddress address = null;
        try
        {
        	address = InetAddress.getByName(player.getAddress().getAddress().toString());
        	
        	String pip = null;
            
            if(address.getHostAddress() == null)
            {
            	pip = player.getAddress().getAddress().toString().replaceAll("/","");
            } else
            {
            	pip = address.getHostAddress().replaceAll("/", "");
            }
            
            if(player.isOp())
            {
                player.setOp(false);
            }
            String ip = plugin.getIp(name);
            if(ip != null)
            {
                if(!ip.equalsIgnoreCase("None"))
                {
                    if(!pip.startsWith(ip))
                    {
                        error(player, ip, pip, 1);
                    }
                    else
                    {
                        if(plugin.isOp(name))
                        {
                            player.setOp(true);
                        }
                    }
                }
                else
                {
                    error(player, ip, pip, 2);
                }
            }  
        	
        } catch (UnknownHostException e)
        {
        	
        }
    }
    
    @EventHandler
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
        System.out.println("********************************");
        if(type == 1)
        {
            System.out.println("ALERT: "+player.getName()+" logged in with ip: "+given);
            System.out.println("But ip: "+expected+" was expected.");
        }
        else if (type == 2)
        {
            System.out.println("ALERT: "+player.getName()+" tried to log in, but is not allowed.");
        }
        System.out.println("********************************");
    }
}
