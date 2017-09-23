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
	
	private ArrayList<String> perms;
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
	
	public ArrayList<String> getPerms(){
		return perms;
	}
	
	public boolean setName(String name){
		for(Rank rank: VassalCity.getInstance().ranks)
			if(rank.getName().equalsIgnoreCase(name))
				return false;
		
		this.name = name;
		return true;
	}
	
	public boolean addPerm(String perm){
		for(String key: perms)
			if(key.equalsIgnoreCase(perm))
				return false;
		
		perms.add(perm);
		return true;
	}
	
	public boolean addPerms(ArrayList<String> perms){
		for(String key: perms)
			for(String perm: this.perms)
			if(key.equalsIgnoreCase(perm))
				return false;
				
		
		for(String perm: perms)
			this.perms.add(perm);
		return true;
	}
	
	public boolean removePerm(String perm){
		for(String key: perms)
			if(key.equalsIgnoreCase(perm)){
				perms.remove(key);
				return true;
			}
		
		return false;
	}
	
	public boolean removePerms(ArrayList<String> perms){
		int counter = 0;
		for(String perm: perms)
			for(String key: this.perms)
				if(perm.equalsIgnoreCase(key))
					counter++;
		
		if(counter == perms.size())
			for(String perm: perms)
				for(String key: this.perms)
					if(perm.equalsIgnoreCase(key)){
						this.perms.remove(key);
						return true;
					}
		
		return false;
	}
	
	

}
