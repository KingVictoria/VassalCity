package io.github.kingvictoria.vassalcity.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import io.github.kingvictoria.vassalcity.city.City;
import io.github.kingvictoria.vassalcity.main.VassalCity;
import io.github.kingvictoria.vassalcity.main.VassalPlayer;
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
				if(args.length == 1){
					for(City city: VassalCity.getInstance().cities)
						if(city.getId() == VassalPlayer.getPlayer(player).getActiveCityId())
							if(claim(player, city)){
								player.sendMessage(ChatColor.YELLOW+"This chunk has been successfully claimed for "+ChatColor.LIGHT_PURPLE+city.getName());
								return true;
							}
				}else{
					// TODO implement multi-word name compatability for claims
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
			
			// NEW CITY
			if(args[0].equalsIgnoreCase("n") || args[0].equalsIgnoreCase("new")){
				if(args.length == 1){
					player.sendMessage(ChatColor.YELLOW+"USAGE: vc n <name...>");
					return true;
				}
				
				String name = "";
				name += args[1];
				for(int i = 2; i < args.length; i++)
					name += " "+args[i];
				
				if(!newCity(player, name, player.getLocation())){
					player.sendMessage(ChatColor.YELLOW+"USAGE: vc n <name...>");
					player.sendMessage(ChatColor.YELLOW+"Must be in an "+ChatColor.LIGHT_PURPLE+"Unclaimed Chunk"+ChatColor.YELLOW+" and name it");
					return true;
				}
				
				
				player.sendMessage(ChatColor.YELLOW+"The city of "+ChatColor.LIGHT_PURPLE+name+ChatColor.YELLOW+" has been founded here!");
				return true;
			}
			
			// GUI (gui/g)
			
			//not yet
			
			// INVITE (invite/i <city> <player>)
			if(args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("i")){
				if(args.length != 2){
					player.sendMessage(ChatColor.YELLOW+"USAGE: vc i <player>");
					return true;
				}
				
				
			}
			
			// ABANDON (abandon/ab <city>)
			// RANK (rank/r <add/mod/rem/info> (rank))
			// MAIN (main <city>)
			// LIST CITIES (listcities/lc)
			// LIST RANKS (listranks/lr <city>)
			// REMOVE CLAIM (removeclaim/rc (ChunkX ChunkZ))
			// MOVE SIGIL (movesigil/ms <city> (x y z))
			// LIST MEMBERS (listmembers/ms <city>)
			// LIST ACTIVES (listactives/la <city>)
			// CITY INFO (cityinfo/ci <city>)
			// SET NAME (setname/sn <city> <name...>)
			// SET MESSAGE (setmessage/sm <city> <message...>)
			// SET ACTIVE (vc setactive/sa <city>)
			// ACCEPT (accept/ac <city>)
			// LIST INVITES (vc listinvites/li)
			// SET ACTIVE (vc setactive/sa <city...>
			if(args[0].equalsIgnoreCase("setactive") || args[0].equalsIgnoreCase("sa")){
				if(args.length < 2){
					player.sendMessage(ChatColor.YELLOW+"USAGE: vc sa <city>");
					return true;
				}else{
					String cityName = "";
					cityName += args[1];
					for(int i = 2; i < args.length; i++)  //TODO fix claiming (again)
						cityName += " "+args[i];
					
					for(City city: VassalCity.getInstance().cities)
						if(VassalPlayer.getPlayer(player).isInCity(city) && city.getName().equals(cityName)){
							VassalPlayer.getPlayer(player).setActiveCity(city);
							player.sendMessage(ChatColor.YELLOW+"Active city set to: "+ChatColor.LIGHT_PURPLE+city.getName());
							return true;
						}
					
					player.sendMessage(ChatColor.LIGHT_PURPLE+cityName+ChatColor.YELLOW+" is not a valid city!");
					return true;
				}
					
			}
			
		}
		
		
		return false;
	}
	
	private boolean invitePlayer(Player sender, Player recipient, City city){
		
		
		return false;
	}
	
	private boolean newCity(Player player, String name, Location loc){
		for(City city: VassalCity.getInstance().cities)
			if(city.getName().equalsIgnoreCase(name))
				return false;
		
		for(ChunkCoordinate coord: VassalCity.getInstance().cityClaims.keySet())
			if(coord.equals(loc))
				return false;
		
		VassalCity.getInstance().cities.add(new City(player, name, loc));
		
		return true;
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
		for(City city: VassalCity.getInstance().cities)
			for(VassalPlayer member: city.getMembers())
				if(member.getPlayer().equals(player) && member.getActiveCityId() == city.getId()){
					boolean nextTo = false;
					for(ChunkCoordinate coord: VassalCity.getInstance().cityClaims.keySet()){
						if(coord.equals(player.getLocation()))
							return false;
						if(coord.nextTo(player.getLocation()) && VassalCity.getInstance().cityClaims.get(coord).equals(city))
							nextTo = true;
					}

					if(nextTo){
						VassalCity.getInstance().cityClaims.put(new ChunkCoordinate(player.getLocation()), city);
						player.sendMessage(ChatColor.YELLOW+"Chunk at ("+player.getLocation().getChunk().getX()+", "+player.getLocation().getChunk().getZ()
								+") claimed for "+city.getName());
						return true;
					}else{
						return false;
					}

				}

		return false;
	}
	
	private boolean claim(Player player, City city){
		for(VassalPlayer member: city.getMembers())
			if(member.getPlayer().getUniqueId().equals(player.getUniqueId())){
				boolean nextTo = false;
				for(ChunkCoordinate coord: VassalCity.getInstance().cityClaims.keySet()){
					if(coord.equals(player.getLocation())){
						player.sendMessage(ChatColor.YELLOW+"That chunk has already been claimed by "+VassalCity.getInstance().cityClaims.get(coord).getName());
						return true;
					}
					if(coord.nextTo(player.getLocation()) && VassalCity.getInstance().cityClaims.get(coord).equals(city))
						nextTo = true;
				}
				
				if(nextTo){
					VassalCity.getInstance().cityClaims.put(new ChunkCoordinate(player.getLocation()), city);
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
		for(VassalPlayer member: city.getMembers())
			if(member.getUUID().equals(player.getUniqueId())){
				boolean nextTo = false;
				for(ChunkCoordinate coord: VassalCity.getInstance().cityClaims.keySet()){
					if(coord.equals(loc))
						return false;
					if(coord.nextTo(loc) && VassalCity.getInstance().cityClaims.get(coord).equals(city))
						nextTo = true;
				}
				
				if(nextTo){
					VassalCity.getInstance().cityClaims.put(loc, city);
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
			player.sendMessage(ChatColor.YELLOW+"vc new/n <name...>"+ChatColor.WHITE+"- opens prompt to make a new city");
			player.sendMessage(ChatColor.YELLOW+"vc gui/g "+ChatColor.WHITE+"- opens fancy gui that makes everything easier");
			player.sendMessage(ChatColor.YELLOW+"vc invite/i <city> <player> "+ChatColor.WHITE+"- invites a person to a city");
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
			player.sendMessage(ChatColor.YELLOW+"vc movesigil/ms <city>"+ChatColor.WHITE+"- moves the city center to your location");
			player.sendMessage(ChatColor.YELLOW+"vc listmembers/lm <city> (x y z)"+ChatColor.WHITE+"- lists all the members of a city");
			player.sendMessage(ChatColor.YELLOW+"vc listactives/la <city> "+ChatColor.WHITE+"- lists all the active members of a city");
			player.sendMessage(ChatColor.YELLOW+"vc cityinfo/ci <city> "+ChatColor.WHITE+"- gets all the info about a given city");
			player.sendMessage(ChatColor.YELLOW+"vc setname/sn <city> <name...> "+ChatColor.WHITE+"- changes the name of a city");
			player.sendMessage(ChatColor.YELLOW+"vc setmessage/sm <city> <message...> "+ChatColor.WHITE+"- changes the entrance message of a city");
			return;
		}
		if(pg == 4){
			player.sendMessage(ChatColor.BLUE+"- - - - - ----------==Pg.04==---------- - - - - -");
			player.sendMessage(ChatColor.YELLOW+"vc accept/ac <city> "+ChatColor.WHITE+"- accepts an invitation to a city");
			player.sendMessage(ChatColor.YELLOW+"vc listinvites/li "+ChatColor.WHITE+"- lists all invites to cities");
			player.sendMessage(ChatColor.YELLOW+"vc setactive/sa <city> "+ChatColor.WHITE+"- sets your active city");
			return;
		}
		
		player.sendMessage(ChatColor.YELLOW+"There are no more pages!");
	}

}
