package io.github.kingvictoria.vassalcity.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import io.github.kingvictoria.vassalcity.city.City;
import io.github.kingvictoria.vassalcity.city.Member;
import io.github.kingvictoria.vassalcity.main.Main;
import io.github.kingvictoria.vassalcity.serialization.ChunkCoordinate;

public class CommandVassalCity implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player){
			Player player = (Player) sender;
			
			if(args.length < 1){
				player.sendMessage(ChatColor.YELLOW+"/vc help");
				return true;
			}
			
			// MAP
			if(args[0].equalsIgnoreCase("map") || args[0].equalsIgnoreCase("m")){
				if(args.length == 1){
					if(map(player)){
						player.sendMessage(ChatColor.YELLOW+"A map has been placed in your inventory");
						return true;
					}else{
						player.sendMessage(ChatColor.YELLOW+"You must have an empty map in your hand!");
						return true;
					}
				}
				player.sendMessage(ChatColor.YELLOW+"USAGE: vc m");
			}
			
			// CLAIM
			if(args[0].equalsIgnoreCase("claim") || args[0].equalsIgnoreCase("c")){
				if(args.length == 1)
					if(claim(player)){
						return true;
					}else{
						player.sendMessage(ChatColor.YELLOW+"You must be an active member of a city claiming an unclaimed chunk next to that city!");
						return true;
					}
				
				if(args.length == 2)
					try{
						for(City city: Main.getInstance().cities)
							if(city.getName().equalsIgnoreCase(args[1]))
								if(claim(player, city)){
									return true;
								}else{
									player.sendMessage(ChatColor.YELLOW+"You must be an active member of a city claiming an unclaimed chunk next to that city!");
									return true;
								}
					}catch(Exception e){
						player.sendMessage(ChatColor.YELLOW+"USAGE: vc c (city) (chunkX chunkZ)");
						return true;
					}
				
				if(args.length == 4){
					try{
						for(City city: Main.getInstance().cities)
							if(city.getName().equalsIgnoreCase(args[1]))
								if(claim(player, city, new ChunkCoordinate(new Integer(args[2]).intValue(), new Integer(args[3]).intValue()))){
									return true;
								}else{
									player.sendMessage(ChatColor.YELLOW+"You must be an active member of a city claiming an unclaimed chunk next to that city!");
									return true;
								}
					}catch(Exception e){
						player.sendMessage(ChatColor.YELLOW+"USAGE: vc c (city) (chunkX chunkZ)");
						return true;
					}
				}
			}
			
			// HELP
			if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")){
				if(args.length == 2){
					try{
					help(player, new Integer(args[1]).intValue());
					}catch (Exception e){
						help(player, 0);
					}
					return true;
				}else if(args.length == 1){
					help(player, 1);
				}
				
				return false;
			}
			
			// UTTERLY TEMPORARY
			if(args[0].equalsIgnoreCase("n")){
				String name = "";
				for(int i = 1; i < args.length-1; i++)
					name += args[i];
				
				for(ChunkCoordinate coord: Main.getInstance().cityClaims.keySet())
					if(coord.equals(player.getLocation())){
						player.sendMessage(ChatColor.YELLOW+"That chunk is already claimed by "+Main.getInstance().cityClaims.get(coord).getName());
						return true;
					}
				
				Main.getInstance().cities.add(new City(player, name, player.getLocation()));
				player.sendMessage(ChatColor.YELLOW+"The city of "+ChatColor.LIGHT_PURPLE+name+ChatColor.YELLOW+" has been founded here!");
				return true;
			}
		}
		
		
		return false;
	}
	
	@SuppressWarnings("deprecation")
	private boolean map(Player player){
		if(!player.getInventory().getItemInMainHand().getType().equals(Material.EMPTY_MAP))
			return false;
		
		player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount()-1);
		
		MapView mapview = Bukkit.createMap(player.getWorld());
		for(MapRenderer r: mapview.getRenderers())
			mapview.removeRenderer(r);
		mapview.addRenderer(new LocalMap());
		
		ItemStack map = new ItemStack(Material.MAP, 1, mapview.getId());
		player.getInventory().addItem(map);
		return true;
	}
	
	private boolean claim(Player player){
		for(City city: Main.getInstance().cities)
			for(Member member: city.getMembers())
				if(member.getPlayer().equals(player) && member.getActive()){
					boolean nextTo = false;
					for(ChunkCoordinate coord: Main.getInstance().cityClaims.keySet()){
						if(coord.equals(player.getLocation()))
							return false;
						if(coord.nextTo(player.getLocation()) && Main.getInstance().cityClaims.get(coord).equals(city))
							nextTo = true;
					}

					if(nextTo){
						Main.getInstance().cityClaims.put(new ChunkCoordinate(player.getLocation()), city);
						player.sendMessage(ChatColor.YELLOW+"Chunk at ("+player.getLocation().getChunk().getX()+", "+player.getLocation().getChunk().getZ()
								+") claimed for "+city.getName());
						return true;
					}else{
						player.sendMessage(ChatColor.YELLOW+"The claim must be next to your city claims!");
						return false;
					}

				}

		return false;
	}
	
	private boolean claim(Player player, City city){
		for(Member member: city.getMembers())
			if(member.getPlayer().equals(player)){
				boolean nextTo = false;
				for(ChunkCoordinate coord: Main.getInstance().cityClaims.keySet()){
					if(coord.equals(player.getLocation())){
						player.sendMessage(ChatColor.YELLOW+"That chunk has already been claimed by "+Main.getInstance().cityClaims.get(coord).getName());
						return true;
					}
					if(coord.nextTo(player.getLocation()) && Main.getInstance().cityClaims.get(coord).equals(city))
						nextTo = true;
				}
				
				if(nextTo){
					Main.getInstance().cityClaims.put(new ChunkCoordinate(player.getLocation()), city);
					player.sendMessage(ChatColor.YELLOW+"Chunk at ("+player.getLocation().getChunk().getX()+", "+player.getLocation().getChunk().getZ()
							+") claimed for "+city.getName());
					return true;
				}else{
					player.sendMessage(ChatColor.YELLOW+"The claim must be next to your city claims!");
					return false;
				}
					
			}
				
		return false;
	}
	
	private boolean claim(Player player, City city, ChunkCoordinate loc){
		for(Member member: city.getMembers())
			if(member.getPlayer().equals(player)){
				boolean nextTo = false;
				for(ChunkCoordinate coord: Main.getInstance().cityClaims.keySet()){
					if(coord.equals(loc))
						return false;
					if(coord.nextTo(loc) && Main.getInstance().cityClaims.get(coord).equals(city))
						nextTo = true;
				}
				
				if(nextTo){
					Main.getInstance().cityClaims.put(loc, city);
					player.sendMessage(ChatColor.YELLOW+"Chunk at ("+player.getLocation().getChunk().getX()+", "+player.getLocation().getChunk().getZ()
							+") claimed for "+city.getName());
					return true;
				}else{
					player.sendMessage(ChatColor.YELLOW+"The claim must be next to your city claims!");
					return false;
				}
					
			}
				
		return false;
	}
	
	private void help(Player player, int pg){
		if(pg == 0){
			player.sendMessage(ChatColor.YELLOW+"/vc help <pg>");
			return;
		}
		if(pg == 1){
			player.sendMessage(ChatColor.BLUE+"- - - - - ----------==Pg.01==---------- - - - - -");
			player.sendMessage(ChatColor.YELLOW+"vc help/? <pg> "+ChatColor.WHITE+"- pulls up help info");
			player.sendMessage(ChatColor.YELLOW+"vc claim/c (city) (ChunkX ChunkZ) "+ChatColor.WHITE+"- claims a chunk for your main city");
			player.sendMessage(ChatColor.YELLOW+"vc map/m "+ChatColor.WHITE+"- displays a map of nearby city claims");
			player.sendMessage(ChatColor.YELLOW+"vc new/n "+ChatColor.WHITE+"- opens prompt to make a new city");
			player.sendMessage(ChatColor.YELLOW+"vc gui/g "+ChatColor.WHITE+"- opens fancy gui that makes everything easier");
			player.sendMessage(ChatColor.YELLOW+"vc add/a <city> <player> "+ChatColor.WHITE+"- adds a person to a city");
			return;
		}
		if(pg == 2){
			player.sendMessage(ChatColor.BLUE+"- - - - - ----------==Pg.02==---------- - - - - -");
			player.sendMessage(ChatColor.YELLOW+"vc abandon/ab <city> "+ChatColor.WHITE+"- opens prompt to abandon city");
			player.sendMessage(ChatColor.YELLOW+"vc rank/r <add/mod/rem/info> (rank) "+ChatColor.WHITE+"- rank Add/Modify/Remove/Information");
			player.sendMessage(ChatColor.YELLOW+"vc main <city> "+ChatColor.WHITE+"- sets your main city");
			player.sendMessage(ChatColor.YELLOW+"vc listcities/lc "+ChatColor.WHITE+"- lists all cities");
			player.sendMessage(ChatColor.YELLOW+"vc listranks/lr <city> "+ChatColor.WHITE+"- lists all ranks in a city");
			player.sendMessage(ChatColor.YELLOW+"vc removeclaim/rc "+ChatColor.WHITE+"- removes a chunk from your main city");
			return;
		}
		if(pg == 3){
			player.sendMessage(ChatColor.BLUE+"- - - - - ----------==Pg.03==---------- - - - - -");
			player.sendMessage(ChatColor.YELLOW+"vc movesigil/ms "+ChatColor.WHITE+"- moves the city center to your location");
			player.sendMessage(ChatColor.YELLOW+"vc listmembers/lm <city> "+ChatColor.WHITE+"- lists all the members of a city");
			player.sendMessage(ChatColor.YELLOW+"vc listactives/la <city> "+ChatColor.WHITE+"- lists all the active members of a city");
			player.sendMessage(ChatColor.YELLOW+"vc cityinfo/ci <city> "+ChatColor.WHITE+"- gets all the info about a given city");
			player.sendMessage(ChatColor.YELLOW+"vc setname/sn <city> <name> "+ChatColor.WHITE+"- changes the name of a city");
			player.sendMessage(ChatColor.YELLOW+"vc setmessage/sm "+ChatColor.WHITE+"- <city> <message> changes the entrance message of a city");
			return;
		}
		
		player.sendMessage(ChatColor.YELLOW+"There are no more pages!");
	}

}
