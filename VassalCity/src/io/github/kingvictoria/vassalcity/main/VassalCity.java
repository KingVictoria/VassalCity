package io.github.kingvictoria.vassalcity.main;


import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.plugin.java.JavaPlugin;

import io.github.kingvictoria.vassalcity.city.City;
import io.github.kingvictoria.vassalcity.city.Rank;
import io.github.kingvictoria.vassalcity.commands.CommandVassalCity;
import io.github.kingvictoria.vassalcity.listeners.JoinEventListener;
import io.github.kingvictoria.vassalcity.serialization.ChunkCoordinate;

public class VassalCity extends JavaPlugin {
	
	public ArrayList<City> cities;
	public ArrayList<Rank> ranks;
	public HashMap<ChunkCoordinate, City> cityClaims;
	// Integer represents cities and ArrayList<Integer> represents VassalPlayers
	public HashMap<Integer, ArrayList<Integer>> citizens;
	
	private FileHandler fh;
	private static VassalCity instance;
	
	public static VassalCity getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;
		
		fh = new FileHandler();
		
		cities = fh.getCities();
		ranks = fh.getRanks();
		cityClaims = fh.getCityClaims();
		citizens = fh.getCitizens();
		
		// Event Listener
		getServer().getPluginManager().registerEvents(new JoinEventListener(), this);
		
		// Test Commands
		getCommand("vassalcity").setExecutor(new CommandVassalCity());
		getCommand("vc").setExecutor(new CommandVassalCity());
	}
	
	@Override
	public void onDisable() {
		fh.close();
	}

}
