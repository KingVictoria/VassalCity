package io.github.kingvictoria.vassalcity.city;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import io.github.kingvictoria.vassalcity.main.VassalCity;

/**
 * A city rank
 * 
 * @author KingVitoria
 *
 */
public class Rank implements Serializable {
	
	private static final long serialVersionUID = 803169377515180316L;
	
	// Integer represents the city id, ArrayList<String> represents the permissions
	private HashMap<Integer, ArrayList<String>> cityPerms = new HashMap<Integer, ArrayList<String>>();
	private int cityId;
	private int id; // unique rank id
	private String name;
	
	public Rank(City city, String name){
		cityId = city.getId();
		this.name = name;
		for(Rank rank: VassalCity.getInstance().ranks)
			if(rank.name.equalsIgnoreCase(name))
				this.name += "fail_double_rank"+((int)(Math.random() * 10000)); // Ensures the rank has a unique name just in case I need that hook
		
		id = 0;
		for(Rank rank: VassalCity.getInstance().ranks)
			if(rank.getId() == id)
				id++;
		
		VassalCity.getInstance().ranks.add(this);
	}
	
	public City getCity(){
		for(City city: VassalCity.getInstance().cities)
			if(city.getId() == cityId)
				return city;
		
		return null;
	}
	
	public int getCityId(){
		return cityId;
	}
	
	public int getId(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public ArrayList<String> getPerms(City city){
		for(Integer integer: cityPerms.keySet())
			if(integer.intValue() == city.getId())
				return cityPerms.get(integer);
		
		return null;
	}
	
	public ArrayList<String> getPerms(Integer integer){
		for(Integer key: cityPerms.keySet())
			if(integer.intValue() == key.intValue())
				return cityPerms.get(key);
		
		return null;
	}
	
	public ArrayList<String> getPerms(int id){
		for(Integer integer: cityPerms.keySet())
			if(integer.intValue() == id)
				return cityPerms.get(integer);
		
		return null;
	}
	
	public boolean setName(String name){
		for(Rank rank: VassalCity.getInstance().ranks)
			if(rank.getName().equalsIgnoreCase(name))
				return false;
		
		this.name = name;
		return true;
	}
	
	public boolean addPerm(String perm, City city){
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
	
	public boolean addPerms(ArrayList<String> perms, City city){
		for(Integer key: cityPerms.keySet())
			if(key.intValue() == city.getId()){
				for(String p: cityPerms.get(key))
					for(String per: perms)
						if(p.equals(per))
							return false;
				
				for(String perm: perms)
					cityPerms.get(key).add(perm);
				return true;
			}
		
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
	
	public boolean removePerms(ArrayList<String> perms, City city){
		ArrayList<String> backup = new ArrayList<String>();
		int count = 0;
		for(Integer key: cityPerms.keySet())
			if(key.intValue() == city.getId()){
				for(String cityperm: cityPerms.get(key))
					for(String perm: perms)
						if(perm.equalsIgnoreCase(cityperm)){
							backup.add(cityperm);
							cityPerms.remove(cityperm);
							count++;
						}
				
				if(count == perms.size() && count != 0){
					return true;
				}else{
					for(String s: backup)
						cityPerms.get(key).add(s);
					
					return false;
				}
			}
		
		return false;
	}
	
	

}
