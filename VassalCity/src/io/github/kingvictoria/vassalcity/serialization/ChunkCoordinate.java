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
			for(int z = -1; z <= 1; z++)
				if(this.x+x == coord.getX() && this.z+z == coord.getZ() && !(z == 0 && x == 0))
					return true;
		
		return false;
	}
	
	public boolean nextTo(Chunk chunk){
		for(int x = -1; x <= 1; x++)
			for(int z = -1; z <= 1; z++)
				if(this.x+x == chunk.getX() && this.z+z == chunk.getZ() && !(z == 0 && x == 0))
					return true;
		
		return false;
	}
	
	public boolean nextTo(Location loc){
		for(int x = -1; x <= 1; x++)
			for(int z = -1; z <= 1; z++)
				if(this.x+x == loc.getChunk().getX() && this.z+z == loc.getChunk().getZ() && !(z == 0 && x == 0))
					return true;
		
		return false;
	}
	
	public boolean nextTo(int xx, int zz){
		for(int x = -1; x <= 1; x++)
			for(int z = -1; z <= 1; z++)
				if(this.x+x == xx && this.z+z == zz && !(z == 0 && x == 0))
					return true;
		
		return false;
	}

	
}
