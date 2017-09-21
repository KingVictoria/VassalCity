package io.github.kingvictoria.vassalcity.main;


import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.plugin.java.JavaPlugin;

import io.github.kingvictoria.vassalcity.city.City;
import io.github.kingvictoria.vassalcity.commands.CommandVassalCity;
import io.github.kingvictoria.vassalcity.serialization.ChunkCoordinate;

public class Main extends JavaPlugin {
	
	public ArrayList<City> cities;
	public HashMap<ChunkCoordinate, City> cityClaims;
	
	private static Main instance;
	private static FileHandler fh;
	
	public static Main getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;
		
		// Load from files
		fh = new FileHandler();
		cities = fh.getCities();
		cityClaims = fh.getCityClaims();
		
		// Test Commands
		getCommand("vassalcity").setExecutor(new CommandVassalCity());
		getCommand("vc").setExecutor(new CommandVassalCity());
	}
	
	@Override
	public void onDisable() {
		fh.close();
	}
	
	

}
