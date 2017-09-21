package io.github.kingvictoria.vassalcity.serialization;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class SerializableLocation implements Serializable{
	
	private static final long serialVersionUID = 9188832058293876405L;
	
	String world;
	int x,y,z;
	
	public SerializableLocation(Location loc){
		this.world = loc.getWorld().getName();
		this.x = loc.getBlockX();
		this.y = loc.getBlockY();
		this.z = loc.getBlockZ();
	}
	
	public Location getLocation(){
		return new Location(getWorld(), x, y, z);
	}
	
	public World getWorld() {
		return Bukkit.getWorld(world);
	}

	public void setWorld(String world) {
		this.world = world;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}


}
