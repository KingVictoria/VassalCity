package io.github.kingvictoria.vassalcity.commands;


import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import io.github.kingvictoria.vassalcity.main.VassalCity;
import io.github.kingvictoria.vassalcity.serialization.ChunkCoordinate;

public class LocalMap extends MapRenderer {
	
	private Chunk lastChunk;
	private byte[][] lastMap;
	
	public LocalMap(){
		super(true);
	}

	@Override
	public void render(MapView view, MapCanvas canvas, Player player) {
		
		byte[][] map = new byte[9][9];
		Chunk chunk = player.getLocation().getChunk();
		
		for(int x = 0; x < 9; x++){
			for(int z = 0; z < 9; z++){
				for(ChunkCoordinate coord: VassalCity.getInstance().cityClaims.keySet())
					if(coord.equals(chunk.getX()-4+x, chunk.getZ()-4+z))
						map[x][z] = VassalCity.getInstance().cityClaims.get(coord).getColor();
				
				if(map[x][z] == 0)
					map[x][z] = (byte) 255;
			}
		}
		
		// Checks to see if any data has changed. If not, it will not update the map (and thus prevent lag).
		if(lastChunk != null && lastMap != null)
			if(lastChunk.equals(chunk) && lastMap.equals(map))
				return;
		
		lastChunk = player.getLocation().getChunk();
		lastMap = map;
		
		for(int x = 143; x > -1; x--)
			for(int z = 143; z > -1; z--){
				if(z % 16 == 0 || z % 16 == 15 || x % 16 == 0 || x % 16 == 15){
					canvas.setPixel(x-8, z-8, (byte) (67) );
				}else{ 
					canvas.setPixel(x-8, z-8, map[x/16][z/16]);
				}
				for(int i = 63; i < 66; i++)
					for(int ii = 63; ii < 66; ii++){
						if((x+1) == i && (z+1) == ii){
							canvas.setPixel(x, z, (byte) 67);
							switch(getDirection(player)) {
							case "n": canvas.setPixel(62, 63, (byte) 58);
							break;
							case "ne": canvas.setPixel(62, 62, (byte) 58);
							break;
							case "e": canvas.setPixel(63, 62, (byte) 58);
							break;
							case "se": canvas.setPixel(64, 62, (byte) 58);
							break;
							case "s": canvas.setPixel(64, 63, (byte) 58);
							break;
							case "sw": canvas.setPixel(64, 64, (byte) 58);
							break;
							case "w": canvas.setPixel(63, 64, (byte) 58);
							break;
							case "nw": canvas.setPixel(62, 64, (byte) 58);
							break;
							default: canvas.setPixel(64, 63, (byte) 58);
							break;
							}
						}
					}
			}

	}
	
	private String getDirection(Player player) {
		double rotation = (player.getLocation().getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
         if (0 <= rotation && rotation < 22.5) {
            return "n";
        } else if (22.5 <= rotation && rotation < 67.5) {
            return "ne";
        } else if (67.5 <= rotation && rotation < 112.5) {
            return "e";
        } else if (112.5 <= rotation && rotation < 157.5) {
            return "se";
        } else if (157.5 <= rotation && rotation < 202.5) {
            return "s";
        } else if (202.5 <= rotation && rotation < 247.5) {
            return "sw";
        } else if (247.5 <= rotation && rotation < 292.5) {
            return "w";
        } else if (292.5 <= rotation && rotation < 337.5) {
            return "nw";
        } else if (337.5 <= rotation && rotation < 360.0) {
            return "n";
        } else {
            return null;
        }
	}

}
