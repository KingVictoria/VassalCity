package io.github.kingvictoria.vassalcity.city;

import java.io.Serializable;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.map.MapPalette;

import io.github.kingvictoria.vassalcity.main.VassalCity;
import io.github.kingvictoria.vassalcity.main.VassalPlayer;
import io.github.kingvictoria.vassalcity.serialization.ChunkCoordinate;
import io.github.kingvictoria.vassalcity.serialization.SerializableLocation;


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
	private int owner;
	
	/**
	 * Creates an instance of type City.
	 * 
	 * @param player The founder of the city
	 * @param name   The name of the city
	 * @param loc    The location of the city center (Sigil)
	 */
	@SuppressWarnings("deprecation")
	public City(Player player, String name, Location loc){
		this.center = new SerializableLocation(loc);
		this.color = MapPalette.matchColor((int)(255*Math.random()), (int)(255*Math.random()), (int)(255*Math.random()));
		this.name = name;
		
		owner = VassalPlayer.getPlayer(player).getId();
		VassalCity.getInstance().cityClaims.put(new ChunkCoordinate(center.getWorld().getChunkAt(center.getLocation()).getX(), center.getWorld().getChunkAt(center.getLocation()).getZ()), this);
		
		// UNIQUE CITY ID
		id = 0;
		for(City city: VassalCity.getInstance().cities)
			if(city.getId() == id)
				id++;
		
		for(City city: VassalCity.getInstance().cities)
			if(city.getName().equalsIgnoreCase(name))
				this.name += id+"name_already_in_use";
		

		VassalPlayer.getPlayer(owner).addCity(this);
		VassalPlayer.getPlayer(owner).addRank(new Rank(this, "Owner"));
	}
	
	/**
	 * Gets a city by its name
	 * 
	 * @param name the name being searched for
	 * @return     the city
	 */
	public static City getCity(String name){
		for(City city: VassalCity.getInstance().cities)
			if(city.getName().equalsIgnoreCase(name))
				return city;
		
		return null;
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
		for(VassalPlayer player: getMembers())
			if(player.getActiveCityId() == id)
				actives++;
		
		return actives;
	}
	
	public int getNumWorkers(){
		int workers = 0;
		for(VassalPlayer player: getMembers())
			if(player.getWorkerLocationId() == id)
				workers++;
		
		return workers;
	}
	
	public int getNumKnights(){
		int knights = 0;
		for(VassalPlayer player: getMembers())
			if(player.getKnightLocationId() == id)
				knights++;
		
		return knights;
	}
	
	/**
	 * Searches through the members of the city and returns an array of the active ones.
	 * 
	 * @return an array of the active members
	 */
	public ArrayList<VassalPlayer> getActives(){
		ArrayList<VassalPlayer> actives = new ArrayList<VassalPlayer>();
		for(VassalPlayer player: getMembers())
			if(player.getActiveCityId() == id)
				actives.add(player);
		
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
	 * Returns a reference to the city's owner VassalPlayer object.
	 * 
	 * @return the city's owner
	 */
	public VassalPlayer getOwner(){
		return VassalPlayer.getPlayer(id);
	}
	
	/**
	 * Returns a reference to the city's members ArrayList of Player objects.
	 * 
	 * @return the members of the city
	 */
	public ArrayList<VassalPlayer> getMembers(){
		return VassalPlayer.getPlayersInCity(this);
	}
	
	/**
	 * Returns a reference to the city's ranks HashMap of Rank objects.
	 * 
	 * @return ranks ArrayList of Strings
	 */
	public ArrayList<Rank> getRanks(){
		ArrayList<Rank> ranks = new ArrayList<Rank>();
		
		for(Rank rank: VassalCity.getInstance().ranks)
			if(rank.getCityId() == id)
				ranks.add(rank);
		
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
	public boolean setOwner(VassalPlayer player){
		if(isMember(player) && player.getId() != owner){
			VassalPlayer.getPlayer(owner).removeRank(Rank.getRank("Owner", id));
			owner = player.getId();
			VassalPlayer.getPlayer(owner).addRank(Rank.getRank("Owner", id));
			return true;
		}
		return false;
	}
	
	/**
	 * Adds a member with ranks to the city so long as they are not already a member and the ranks are legitimate
	 * 
	 * @param player the player to be added
	 * @param ranks  an ArrayList of String objects indicating the ranks to be added
	 * @return       false if either the player is already a member
	 */
	public boolean addMember(VassalPlayer player, ArrayList<String> ranks){
		if(isMember(player))
			return false;
		
		player.addCity(this);
		
		for(String s: ranks)
			if(getRanks().contains(Rank.getRank(s, id)))
				player.addRank(Rank.getRank(s, id));
		
		return true;
	}
	
	/**
	 * Checks to see if a player is a member of the city.
	 * 
	 * @param player  Vassalplayer check
	 * @return        true if is member of city
	 */
	public boolean isMember(VassalPlayer player){
		if(player.getCities().contains(this))
			return true;
		
		return false;
	}
	
	/**
	 * Removes a member from the city so long as they are a member and not the owner.
	 * 
	 * @param member the member to be removed
	 * @return       false if either they are not a member of the city or are the owner
	 */
	public boolean removeMember(VassalPlayer player){
		if(!getMembers().contains(player))
			return false;
		
		if(player.getRanks(this).contains(Rank.getRank("Owner", id)))
			return false;
		
		player.removeCity(this);
		return true;
	}
	
	/**
	 * Adds a rank to the city so long as it does not already exist.
	 * 
	 * @param rank the rank to be added indicated as a String object
	 * @return     false if the rank already exists
	 */
	public boolean addRank(String rank){
		if(Rank.getRank(rank, id) != null)
			return false;
		
		new Rank(this, rank);
		
		return true;
	}
	
	/**
	 * Removes a rank and strips everyone who has it of it so long as it exists.
	 * 
	 * @param rank the rank to be removed
	 * @return     false if the rank does not exist
	 */
	public boolean removeRank(String rank){
		if(Rank.getRank(rank, id) != null){
			VassalPlayer.completelyRemoveRank(Rank.getRank(rank, id));
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes the city
	 */
	public void abandon(){
		VassalPlayer.completelyRemoveCity(this);
		VassalPlayer.completelyRemoveRanks(Rank.getRanks(this));
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
