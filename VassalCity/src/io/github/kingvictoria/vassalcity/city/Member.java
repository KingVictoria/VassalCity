package io.github.kingvictoria.vassalcity.city;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Member implements Serializable {

	private static final long serialVersionUID = 3899683937379985055L;
	
	private UUID playerUUID;
	private boolean active;
	private City city;
	
	public Member(Player player, City city){
		this.playerUUID = player.getUniqueId();
		this.city = city;
		active = true;
	}
	
	public City getCity(){
		return city;
	}
	
	public Player getPlayer(){
		return Bukkit.getPlayer(playerUUID);
	}
	
	public boolean getActive(){
		return active;
	}
}
