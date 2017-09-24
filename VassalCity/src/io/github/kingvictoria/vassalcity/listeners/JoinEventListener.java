package io.github.kingvictoria.vassalcity.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import io.github.kingvictoria.vassalcity.main.VassalPlayer;
import net.md_5.bungee.api.ChatColor;

public class JoinEventListener implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		
		if(!VassalPlayer.playerLoggedInBefore(player)){
			new VassalPlayer(player);
			player.sendMessage(ChatColor.YELLOW+"Welcome to VassalCraft "+player.getName()+"!");
		}else{
			player.sendMessage(ChatColor.YELLOW+"Welcome back to VassalCraft "+player.getName()+"!");
		}
	}

}
