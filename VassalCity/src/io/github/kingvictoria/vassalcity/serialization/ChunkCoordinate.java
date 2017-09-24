package io.github.kingvictoria.vassalcity.serialization;

import java.io.Serializable;

import org.bukkit.Chunk;
import org.bukkit.Location;

public class ChunkCoordinate implements Serializable {

	private static final long serialVersionUID = 8370989818762315731L;
	
	int x, z;
	
	public ChunkCoordinate(int x, int z){
		this.x = x;
		this.z = z;
	}
	
	public ChunkCoordinate(Location loc){
		this.x = loc.getChunk().getX();
		this.z = loc.getChunk().getZ();
	}
	
	public ChunkCoordinate(Integer x, Integer z){
		this.x = x.intValue();
		this.z = z.intValue();
	}
	
	public ChunkCoordinate(Chunk chunk){
		this.x = chunk.getX();
		this.z = chunk.getZ();
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}
	
	public String toString() {
		return "("+x+", "+z+")";
	}
	
	public boolean equals(ChunkCoordinate coord){
		if(coord.getX() == x && coord.getZ() == z)
			return true;
		
		return false;
	}
	
	public boolean equals(Chunk chunk){
		if(chunk.getX() == x && chunk.getZ() == z)
			return true;
		
		return false;
	}
	
	public boolean equals(Location loc){
		if(loc.getChunk().getX() == x && loc.getChunk().getZ() == z)
			return true;
		
		return false;
	}
	
	public boolean equals(int x, int z){
		if(this.x == x)
			if(this.z == z)
				return true;
		
		return false;
	}
	
	public boolean nextTo(ChunkCoordinate coord){
		for(int x = -1; x <= 1; x++)
			if(this.x+x == coord.getX() && this.z == coord.getZ() && !(z == 0 && x == 0))
				return true;
		
		for(int z = -1; z <= 1; z++)
			if(this.x == coord.getX() && this.z+z == coord.getZ() && !(z == 0 && x == 0))
				return true;
		
		return false;
	}
	
	public boolean nextTo(Chunk chunk){
		return nextTo(new ChunkCoordinate(chunk));
	}
	
	public boolean nextTo(Location loc){
		return nextTo(new ChunkCoordinate(loc));
	}
	
	public boolean nextTo(int x, int z){
		return nextTo(new ChunkCoordinate(x, z));
	}

	
}
