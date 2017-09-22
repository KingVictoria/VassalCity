package io.github.kingvictoria.vassalcity.city;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import io.github.kingvictoria.vassalcity.main.VassalCity;
import io.github.kingvictoria.vassalcity.serialization.ChunkCoordinate;
import io.github.kingvictoria.vassalcity.serialization.SerializableLocation;
import io.github.kingvictoria.vassalcore.main.VassalPlayer;


/**
 * A group that holds physical territory and has a political structure
 * 
 * @author KingVictoria
 *
 */
public class City implements Serializable {

	private static final long serialVersionUID = 3643463171455939146L;
	
	private int id;
	private byte color;
	private SerializableLocation center; // SigilPoint (City Center) 
	private String name;
	private String entranceMessage;
	private UUID owner;
	private ArrayList<UUID> members = new ArrayList<UUID>();
	
	/**
	 * Creates an instance of type City.
	 * 
	 * @param player The founder of the city
	 * @param name   The name of the city
	 * @param loc    The location of the city center (Sigil)
	 */
	public City(Player player, String name, Location loc){
		center = new SerializableLocation(loc);
		color = (byte) (center.getX()*center.getY()*center.getZ());
		this.name = name;
		owner = player.getUniqueId();
		members.add(owner);
		Rank r = new Rank(this, "owner");
		// VassalPlayer.getPlayer(owner).addRank TODO add ranks to VassalPlayer
		VassalCity.getInstance().cityClaims.put(new ChunkCoordinate(center.getWorld().getChunkAt(center.getLocation()).getX(), center.getWorld().getChunkAt(center.getLocation()).getZ()), this);
		
		// UNIQUE CITY ID
		id = 0;
		for(City city: VassalCity.getInstance().cities)
			if(city.getId() == id)
				id++;
	}
	
	/**
	 * Gives the unique id of the city.
	 * 
	 * @return int id
	 */
	public int getId(){
		return id;
	}
	
	/**
	 * Searches through the members of the city to find all which are active.
	 * 
	 * @return returns number of active members
	 */
	public int getNumActives(){
		int actives = 0;
		for(Member member: members)
			if(member.getActive())
				actives++;
		
		return actives;
	}
	
	/**
	 * Searches through the members of the city and returns an array of the active ones.
	 * 
	 * @return an array of the active members
	 */
	public ArrayList<Member> getActives(){
		ArrayList<Member> actives = new ArrayList<Member>();
		for(Member member: members)
			if(member.getActive())
				actives.add(member);
		
		return actives;
	}
	
	/**
	 * Returns the city center (Sigil) location.
	 * 
	 * @return location of city center
	 */
	public Location getSigilPoint(){
		return center.getLocation();
	}
	
	/**
	 * Returns the name of the city as a String.
	 * 
	 * @return the city's name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Returns the entrance message of the city as a String.
	 * 
	 * @return the city's entrance message
	 */
	public String getEntranceMessage(){
		return entranceMessage;
	}
	
	/**
	 * Returns a reference to the city's owner's Member object.
	 * 
	 * @return the city's owner
	 */
	public Member getOwner(){
		return owner;
	}
	
	/**
	 * Returns a reference to the city's members ArrayList of Member objects.
	 * 
	 * @return the members of the city
	 */
	public ArrayList<Member> getMembers(){
		return members;
	}
	
	/**
	 * Returns a reference to the city's ranks HashMap of Rank objects.
	 * 
	 * @return ranks ArrayList of Strings
	 */
	public HashMap<String, Rank> getRanks(){
		return ranks;
	}
	
	/**
	 * Returns the claims of the city in an ArrayList of ChunkCoordinates.
	 * 
	 * @return city claims
	 */
	public ArrayList<ChunkCoordinate> getClaims(){
		ArrayList<ChunkCoordinate> claims = new ArrayList<ChunkCoordinate>();
		for(ChunkCoordinate coord: VassalCity.getInstance().cityClaims.keySet())
			if(VassalCity.getInstance().cityClaims.get(coord).equals(this))
				claims.add(coord);
		
		return claims;
	}
	
	/**
	 * Chances the location of the city's center so long as the new location is within the city boundaries
	 * 
	 * @param loc the new location
	 * @return    false if the new location is not within the claims
	 */
	public boolean setLocation(Location loc){
		if(isInClaims(loc)){
			center = new SerializableLocation(loc);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Changes the name of the city.
	 * 
	 * @param name  the new name of the city
	 */
	public void setName(String name){
		this.name = name;
	}
	
	/**
	 * Changes the entrance message of the city.
	 * 
	 * @param entranceMessage  the new entrance message
	 */
	public void setEntranceMessage(String entranceMessage){
		this.entranceMessage = entranceMessage;
	}
	
	/**
	 * Changes the owner if the new member is one of the city and is not already the owner.
	 * 
	 * @param member the new owner
	 * @return       false if the member is not of the city or is already the owner
	 */
	public boolean setOwner(Member member){
		if(members.contains(member) && !ranks.get("Owner").getRanked().contains(member)){
			ranks.get("Owner").getRanked().remove(owner);
			owner = member;
			ranks.get("Owner").getRanked().add(owner);
			return true;
		}
		return false;
	}
	
	/**
	 * Adds a member with ranks to the city so long as they are not already a member and the ranks are legitimate
	 * 
	 * @param player the player to be added
	 * @param ranks  an ArrayList of String objects indicating the ranks to be added
	 * @return       false if either the player is already a member or a given rank does not exist
	 */
	public boolean addMember(Player player, ArrayList<String> ranks){
		for(Member member: members)
			if(member.getPlayer().equals(player))
				return false;
		
		int reals = 0;
		for(String key: this.ranks.keySet())
			for(String rank: ranks)
				if(key == rank)
					reals++;
		if(reals != ranks.size())
			return false;
			
		
		Member member = new Member(player, this);
		members.add(member);
		for(String key: this.ranks.keySet())
			for(String rank: ranks)
				if(key == rank)
					this.ranks.get(key).addRanked(member);
			
		return true;
	}
	
	/**
	 * Removes a member from the city so long as they are a member and not the owner.
	 * 
	 * @param member the member to be removed
	 * @return       false if either they are not a member of the city or are the owner
	 */
	public boolean removeMember(Member member){
		if(!members.contains(member))
			return false;
		
		if(ranks.get("Owner").getRanked().contains(member))
			return false;
		
		members.remove(member);
		return true;
	}
	
	/**
	 * Adds a rank to the city so long as it does not already exist.
	 * 
	 * @param rank the rank to be added indicated as a String object
	 * @return     false if the rank already exists
	 */
	public boolean addRank(String rank){
		for(String key: ranks.keySet())
			if(key == rank)
				return false;
		
		ranks.put(rank, new Rank(this));
		return true;
	}
	
	/**
	 * Removes a rank and strips everyone who has it of it so long as it exists.
	 * 
	 * @param rank the rank to be removed
	 * @return     false if the rank does not exist
	 */
	public boolean removeRank(String rank){
		for(String key: ranks.keySet())
			if(key == rank){
				ranks.remove(rank);
				return true;
			}
		
		return false;
	}
	
	/**
	 * Removes the city
	 */
	public void abandon(){
		members.clear();
		VassalCity.getInstance().cities.remove(this);
		for(ChunkCoordinate key: VassalCity.getInstance().cityClaims.keySet()){
			if(VassalCity.getInstance().cityClaims.get(key).equals(this))
				VassalCity.getInstance().cityClaims.remove(key);
		}
	}
	
	/**
	 * Checks to see whether a location is within the claims of the city.
	 * 
	 * @param loc the location
	 * @return    false if the location is not within the city's claims
	 */
	public boolean isInClaims(Location loc){
		for(ChunkCoordinate key: VassalCity.getInstance().cityClaims.keySet())
			if(VassalCity.getInstance().cityClaims.get(key).equals(this) 
					&& loc.getChunk().getX() == key.getX() && loc.getChunk().getZ() == key.getZ() && center.getWorld().equals(loc.getWorld()))
				return true;
		
		return false;
	}
	
	public String toString(){
		return name;
	}
	
	public byte getColor(){
		return color;
	}
	
	/**
	 * Sets the color of the city on the map
	 * 
	 * @param color  is a byte correspondent to the Map Color
	 */
	public void setColor(byte color){
		this.color = color;
	}
	
}
