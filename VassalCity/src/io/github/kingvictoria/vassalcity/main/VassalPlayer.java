package io.github.kingvictoria.vassalcity.main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import io.github.kingvictoria.vassalcity.city.City;
import io.github.kingvictoria.vassalcity.city.Rank;

public class VassalPlayer implements Serializable {

	private static final long serialVersionUID = 803169312515180316L;

	private static ArrayList<VassalPlayer> players = new ArrayList<VassalPlayer>();
	
	private int id;
	private UUID uuid;
	// Integer represents the city id, ArrayList<String> represents the permissions
	private HashMap<Integer, ArrayList<String>> cityPerms = new HashMap<Integer, ArrayList<String>>();
	// Integer represents the city id, ArrayList<Integer> represents the rank id
	private HashMap<Integer, ArrayList<Integer>> cityRanks = new HashMap<Integer, ArrayList<Integer>>();
	
	// ints correspond to ids of cities
	private int activeCity;
	private int knightLocation;
	private int workerLocation;
	
	public VassalPlayer(Player player){
		uuid = player.getUniqueId();
		players.add(this);
		
		id = 0;
		for(VassalPlayer vp: players)
			if(vp.getId() == id)
				id++;
	}
	
	public static boolean playerLoggedInBefore(Player player){
		for(VassalPlayer vp: players)
			if(vp.getUUID().equals(player.getUniqueId()))
				return true;
		
		return false;
	}
	
	public static VassalPlayer getPlayer(Player player){
		for(VassalPlayer vp: players)
			if(player.getUniqueId().equals(vp.getUUID()))
				return vp;
		
		return null;
	}
	
	public static VassalPlayer getPlayer(OfflinePlayer player){
		for(VassalPlayer vp: players)
			if(player.getUniqueId().equals(vp.getUUID()))
				return vp;
		
		return null;
	}
	
	public static VassalPlayer getPlayer(UUID id){
		for(VassalPlayer vp: players)
			if(vp.getUUID().equals(id))
				return vp;
		
		return null;
	}
	
	public static VassalPlayer getPlayer(int id){
		for(VassalPlayer vp: players)
			if(vp.getId() == id)
				return vp;
		
		return null;
	}
	
	public static ArrayList<VassalPlayer> getPlayers(){
		return players;
	}
	
	public static ArrayList<VassalPlayer> getPlayersInCity(City city){
		ArrayList<VassalPlayer> cityPlayers = new ArrayList<VassalPlayer>();
		
		players:
		for(VassalPlayer player: players)
			for(City key: player.getCities())
				if(key.getId() == city.getId()){
					cityPlayers.add(player);
					continue players;
				}
		
		return cityPlayers;
	}
	
	public static boolean completelyRemoveRank(Rank rank){
		for(VassalPlayer player: players)
			for(Integer integer: player.getRankIds(rank.getCity()))
				if(rank.getId() == integer.intValue()){
					player.cityRanks.get(rank.getCityId()).remove(rank.getId());
					VassalCity.getInstance().ranks.remove(rank);
					return true;
				}
		
		return false;
	}
	
	public static void completelyRemoveRanks(ArrayList<Rank> ranks){
		for(Rank rank: ranks)
			completelyRemoveRank(rank);
	}
	
	public static void completelyRemoveCity(City city){
		for(VassalPlayer player: players)
			player.removeCity(city);

		VassalCity.getInstance().cities.remove(city);
	}
	
	public Player getPlayer(){
		return Bukkit.getPlayer(uuid);
	}
	
	public OfflinePlayer getOfflinePlayer(){
		return Bukkit.getOfflinePlayer(uuid);
	}
	
	public UUID getUUID(){
		return uuid;
	}
	
	public int getId(){
		return id;
	}
	
	public String getName(){
		return getPlayer().getName();
	}
	
	public ArrayList<Integer> getCityIds(){
		ArrayList<Integer> cityIds = new ArrayList<Integer>();
		
		for(Integer key: VassalCity.getInstance().citizens.keySet())
			for(Integer integer: VassalCity.getInstance().citizens.get(key))
				if(integer.intValue() == id)
					cityIds.add(key.intValue());
		
		return cityIds;
	}
	
	public ArrayList<City> getCities(){
		ArrayList<City> cities = new ArrayList<City>();
		
		for(Integer key: VassalCity.getInstance().citizens.keySet())
			for(Integer integer: VassalCity.getInstance().citizens.get(key))
				if(integer.intValue() == id)
					for(City city: VassalCity.getInstance().cities)
						if(city.getId() == key.intValue())
							cities.add(city);
		
		return cities;
	}
	
	public ArrayList<City> getCitiesWithPerms(){
		ArrayList<City> cities = new ArrayList<City>();
		
		for(Integer key: cityPerms.keySet())
			for(City city: VassalCity.getInstance().cities)
				if(key.intValue() == city.getId())
					cities.add(city);
		
		return cities;
	}
	
	public ArrayList<City> getCitiesWithRanks(){
		ArrayList<City> cities = new ArrayList<City>();
		
		for(Integer key: cityRanks.keySet())
			for(City city: VassalCity.getInstance().cities)
				if(key.intValue() == city.getId())
					cities.add(city);
		
		return cities;
	}
	
	public ArrayList<String> getPerms(City city){
		ArrayList<String> perms = new ArrayList<String>();
		
		for(Integer integer: cityPerms.keySet())
			if(integer.intValue() == city.getId())
				for(String perm: cityPerms.get(integer))
					perms.add(perm);
		
		for(Integer integer: cityRanks.keySet())
			if(integer.intValue() == city.getId())
				for(Rank rank: VassalCity.getInstance().ranks)
					for(Integer rid: cityRanks.get(integer))
						if(rank.getId() == rid.intValue())
							for(String perm: rank.getPerms())
								perms.add(perm);
				
		return perms;
	}
	
	public ArrayList<String> getPerms(Integer id){
		ArrayList<String> perms = new ArrayList<String>();
		
		for(City city: VassalCity.getInstance().cities)
			if(city.getId() == id.intValue())
				perms = getPerms(city);
		
		return perms;
	}
	
	public ArrayList<String> getPerms(int id){
		ArrayList<String> perms = new ArrayList<String>();
		
		for(City city: VassalCity.getInstance().cities)
			if(city.getId() == id)
				perms = getPerms(city);
		
		return perms;
	}
	
	public ArrayList<Rank> getRanks(City city){
		ArrayList<Rank> ranks = new ArrayList<Rank>();
		
		for(Rank rank: VassalCity.getInstance().ranks)
			if(rank.getCityId() == city.getId())
				ranks.add(rank);
		
		return ranks;
	}
	
	public ArrayList<Integer> getRankIds(City city){
		ArrayList<Integer> ranks = new ArrayList<Integer>();
		
		for(Rank rank: getRanks(city))
			ranks.add(new Integer(rank.getId()));
		
		return ranks;
	}
	
	public int getActiveCityId(){
		return activeCity;
	}
	
	public int getKnightLocationId(){
		return knightLocation;
	}
	
	public int getWorkerLocationId(){
		return workerLocation;
	}
	
	
	public void setActiveCity(City city){
		if(isInCity(city))
			activeCity = city.getId();
	}
	
	public void setKnightLocation(City city){
		knightLocation = city.getId();
	}
	
	public void setWorkerLocation(City city){
		workerLocation = city.getId();
	}
	
	public boolean addPerm(String perm, City city){
		if(getCities().contains(city) && !getCitiesWithPerms().contains(city)){
			ArrayList<String> perms = new ArrayList<String>();
			perms.add(perm);
			cityPerms.put(new Integer(city.getId()), perms);
			return true;
		}
		
		
		for(Integer key: cityPerms.keySet())
			if(key.intValue() == city.getId()){
				for(String p: cityPerms.get(key))
					if(perm.equals(p))
						return false;
				
				cityPerms.get(key).add(perm);
				return true;
			}
		
		return false;
	}
	
	public void addPerms(ArrayList<String> perms, City city){
		for(String perm: perms)
			addPerm(perm, city);
	}
	
	public boolean addRank(Rank rank){
		City city = null;
		for(City city_: VassalCity.getInstance().cities)
			if(rank.getCityId() == city_.getId())
				city = city_;
		
		if(city == null)
			return false;
		
		if(getCities().contains(city) && !getCitiesWithPerms().contains(city)){
			ArrayList<Integer> ranks = new ArrayList<Integer>();
			ranks.add(new Integer(rank.getId()));
			cityRanks.put(new Integer(rank.getId()), ranks);
			return true;
		}
		
		for(Integer key: cityRanks.keySet())
			if(key == rank.getCityId()){
				for(Integer integer: cityRanks.get(key))
					if(rank.getId() == integer.intValue())
						return false;
				
				cityRanks.get(key).add(new Integer(rank.getId()));
				return true;
			}
		
		return false;
	}
	
	public boolean addRank(Integer rank){
		for(Rank r: VassalCity.getInstance().ranks)
			if(r.getId() == rank.intValue())
				return addRank(r);
		
		return false;
	}
	
	public boolean addRank(int rank){
		for(Rank r: VassalCity.getInstance().ranks)
			if(r.getId() == rank)
				return addRank(r);
		
		return false;
	}
	
	public boolean isInCity(City city){
		if(getCities().size() == 0)
			return false;
		
		for(City c: getCities()){
			if(c.equals(city)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isInCity(int id){
		for(City c: VassalCity.getInstance().cities)
			if(c.getId() == id)
				return isInCity(c);
		
		return false;
	}
	
	public boolean isInCity(Integer id){
		for(City c: VassalCity.getInstance().cities)
			if(c.getId() == id.intValue())
				return isInCity(c);
		
		return false;
	}
	
	public boolean addCity(City city){
		if(getCities().contains(city))
			return false;
		
		for(Integer key: VassalCity.getInstance().citizens.keySet())
			if(key.intValue() == city.getId()){
				VassalCity.getInstance().citizens.get(key).add(new Integer(id));
				return true;
			}
			
		VassalCity.getInstance().citizens.put(new Integer(city.getId()), new ArrayList<Integer>());
		return addCity(city);
	}
	
	public boolean addCity(int id){
		for(City city: VassalCity.getInstance().cities)
			if(city.getId() == id)
				return addCity(city);
		
		return false;
	}
	
	public boolean addCity(Integer id){
		for(City city: VassalCity.getInstance().cities)
			if(city.getId() == id.intValue())
				return addCity(city);
		
		return false;
	}
	
	public boolean removeCity(City city){
		if(getCities().contains(city)){
			for(Integer key: cityPerms.keySet())
				if(key.intValue() == city.getId())
					cityPerms.remove(key);
			
			for(Integer key: cityRanks.keySet())
				if(key.intValue() == city.getId())
					cityRanks.remove(key);
			
			for(Integer key: VassalCity.getInstance().citizens.keySet())
				if(key.intValue() == city.getId())
					VassalCity.getInstance().citizens.remove(key);
			
			return true;
		}
		
		return false;
	}
	
	public boolean removeCity(int id){
		for(City city: VassalCity.getInstance().cities)
			if(city.getId() == id)
				return removeCity(city);
		
		return false;
	}
	
	public boolean removeCity(Integer id){
		for(City city: VassalCity.getInstance().cities)
			if(city.getId() == id.intValue())
				return removeCity(city);
		
		return false;
	}
	
	public boolean removePerm(String perm, City city){
		for(Integer key: cityPerms.keySet())
			if(city.getId() == key.intValue())
				for(String s: cityPerms.get(key))
					if(s.equalsIgnoreCase(perm)){
						cityPerms.get(key).remove(s);
						return true;
					}
		
		return false;
	}
	
	public void removePerms(ArrayList<String> perms, City city){
		for(String perm: perms)
			removePerm(perm, city);
	}
	
	public boolean removeRank(Rank rank){
		for(Integer integer: cityRanks.keySet())
			if(integer.intValue() == rank.getCityId())
				for(Integer key: cityRanks.get(integer))
					if(key.intValue() == rank.getId()){
						cityRanks.get(integer).remove(key);
						return true;
					}
		
		return false;
	}
	
	public boolean removeRank(Integer rank_){
		for(Rank rank: VassalCity.getInstance().ranks)
			if(rank.getId() == rank_.intValue())
				return removeRank(rank);
		
		return false;
	}
	
	public boolean removeRank(int rank_){
		for(Rank rank: VassalCity.getInstance().ranks)
			if(rank.getId() == rank_)
				return removeRank(rank);
		
		return false;
	}

}
