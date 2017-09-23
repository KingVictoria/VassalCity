package io.github.kingvictoria.vassalcity.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import io.github.kingvictoria.vassalcity.city.City;
import io.github.kingvictoria.vassalcity.city.Rank;
import io.github.kingvictoria.vassalcity.serialization.ChunkCoordinate;

public class FileHandler {
	
	private ArrayList<City> cities;
	private ArrayList<Rank> ranks;
	private HashMap<ChunkCoordinate, City> cityClaims;
	private HashMap<Integer, ArrayList<Integer>> citizens;
	private VassalCity vassalCity;
	
	public FileHandler() {
		vassalCity = VassalCity.getInstance();
		
		File dir = vassalCity.getDataFolder();
		
		if(!dir.exists())
			if(!dir.mkdir())
				System.out.println("["+vassalCity.getName()+"] Could not create data directory");
		
		loadCities();
		if(cities.size() > 0)
			System.out.println("[VassalCity] Loaded Cities: "+cities.toString());
		
		loadCitizens();
		if(citizens.size() > 0)
			System.out.println("[VassalCity] Loaded Cities: "+citizens.toString());
					
		
		loadRanks();
		if(ranks.size() > 0)
			System.out.println("[VassalCity] Loaded Cities: "+ranks.toString());
		
		loadCityClaims();
		if(cityClaims.size() > 0){
			System.out.println("[VassalCity] Number of city claims: "+cityClaims.size());
			System.out.println("[VassalCity] Loaded City Claims: "+cityClaims.toString());
		}
	}
	
	public void close(){
		save(cities, new File(vassalCity.getDataFolder(), "cities.dat"));
		if(cities.size() > 0)
			System.out.println("[VassalCity] Saving Cities: "+cities.toString());
		
		save(ranks, new File(vassalCity.getDataFolder(), "ranks.dat"));
		if(ranks.size() > 0)
			System.out.println("[VassalCity] Saving Ranks: "+ranks.toString());
		
		save(citizens, new File(vassalCity.getDataFolder(), "citizens.dat"));
		if(citizens.size() > 0)
			System.out.println("[VassalCity] Saving Citizens: "+citizens.toString());
		
		HashMap<ChunkCoordinate, String> saveCityClaims = new HashMap<ChunkCoordinate, String>();
		for(ChunkCoordinate key: cityClaims.keySet()) //Changes all those cities into strings that represent them (avoids doubling of city objects among other stupidity)
			saveCityClaims.put(key, cityClaims.get(key).getName());
		save(saveCityClaims, new File(vassalCity.getDataFolder(), "cityclaims.dat"));
		if(cityClaims.size() > 0){
			System.out.println("[VassalCity] Number of city claims: "+saveCityClaims.size());
			System.out.println("[VassalCity] Saving City Claims: "+cityClaims.toString());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void loadCities() {
		cities = (ArrayList<City>) load(new File(vassalCity.getDataFolder(), "cities.dat"));
		
		if(cities == null)
			cities = new ArrayList<City>();
	}
	
	@SuppressWarnings("unchecked")
	private void loadCityClaims() {
		cityClaims = new HashMap<ChunkCoordinate, City>();
		HashMap<ChunkCoordinate, String> loadCityClaims = (HashMap<ChunkCoordinate, String>) load(new File(vassalCity.getDataFolder(), "cityclaims.dat"));
		
		if (loadCityClaims != null) // Changes all those strings to references to the city objects
			for(ChunkCoordinate key: loadCityClaims.keySet())
				for(City city: cities)
					if(loadCityClaims.get(key).equals(city.getName()))
						cityClaims.put(key, city);
	}
	
	@SuppressWarnings("unchecked")
	private void loadRanks() {
		ranks = (ArrayList<Rank>) load(new File(vassalCity.getDataFolder(), "ranks.dat"));
		
		if(ranks == null)
			ranks = new ArrayList<Rank>();
	}
	
	@SuppressWarnings("unchecked")
	private void loadCitizens() {
		citizens = (HashMap<Integer, ArrayList<Integer>>) load(new File(vassalCity.getDataFolder(), "citizens.dat"));
		
		if(citizens == null)
			citizens = new HashMap<Integer, ArrayList<Integer>>();
	}
	
	public ArrayList<City> getCities(){
		return cities;
	}
	
	public ArrayList<Rank> getRanks(){
		return ranks;
	}
	
	public HashMap<Integer, ArrayList<Integer>> getCitizens(){
		return citizens;
	}
	
	public HashMap<ChunkCoordinate, City> getCityClaims(){
		return cityClaims;
	}

	private void save(Object obj, File file) {
		try {
			if(!file.exists())
				file.createNewFile();
			
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(obj);
			oos.flush();
			oos.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private Object load(File file) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			Object result = ois.readObject();
			ois.close();
			return result;
		} catch(Exception e) {
			return null;
		}
	}
	
}
