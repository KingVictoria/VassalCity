package io.github.kingvictoria.vassalcity.city;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.bukkit.conversations.Conversable;
import io.github.kingvictoria.vassalcity.main.VassalPlayer;
import net.md_5.bungee.api.ChatColor;

public class Permissions {
	/*
	 * Permissions:
	 * 
	 * CLAIMING: add_claim, remove_claim
	 * RANKS: add_rank, remove_rank, give_rank_[rank], take_rank_[rank], manage_rank_permissions, set_rank_dec
	 * GEN. ADMIN: abandon, set_message, move_sigil, set_name, invite_player, add_perm_, remove_perm_
	 * GENERAL: set_active
	 */
	
	// All
	public static final String all = "*";
	// Claiming
	public static final String add_claim = "add_claim";
	public static final String remove_claim = "remove_claim";
	// Ranks
	public static final String add_rank = "add_rank";
	public static final String remove_rank = "remove_rank";
	public static final String give_rank_ = "give_rank_";
	public static final String take_rank_ = "take_rank_";
	public static final String manage_rank_permissions = "manage_rank_permissions";
	public static final String set_rank_dec = "set_rank_desc";
	// General Administrative
	public static final String abandon = "abandon";
	public static final String set_message = "set_message";
	public static final String move_sigil = "move_sigil";
	public static final String set_name = "set_name";
	public static final String invite_player = "invite_player";
	public static final String add_perm_ = "add_perm_"; // very special
	public static final String remove_perm_ = "remove_perm_"; // very special
	// General
	public static final String set_active = "set_active";

	public static boolean contains(VassalPlayer player, City city, String perm){
		if(perm.charAt(perm.length() - 1) == '_'){
			System.out.println("[VassalCity] Permission "+perm+"called by "+player.getPlayer().getName()+" requires tag.");
			return false;
		}
		
		return player.getPerms(city).contains(perm) || player.getPerms(city).contains(all);
	}
	
	public static boolean contains(VassalPlayer player, City city, String perm, String tag){
		return player.getPerms(city).contains(perm+tag) || player.getPerms(city).contains(all);
	}
	
	public static ArrayList<String> getPerms(){
		ArrayList<String> perms = new ArrayList<String>();
		
		Field[] fields=Permissions.class.getDeclaredFields(); // get all declared fields 
		for(Field field:fields){
		   if(field.getType().equals(String.class)){ // if it is a String field
		     perms.add(field.getName());
		   }
		}
		
		return perms;
	}
	
	public static void printPerms(Conversable con, City city){
		con.sendRawMessage(ChatColor.YELLOW+all);
		con.sendRawMessage(ChatColor.YELLOW+add_claim);
		con.sendRawMessage(ChatColor.YELLOW+remove_claim);
		con.sendRawMessage(ChatColor.YELLOW+add_rank);
		con.sendRawMessage(ChatColor.YELLOW+remove_rank);
		for(Rank rank: city.getRanks())
			con.sendRawMessage(ChatColor.YELLOW+give_rank_+rank.getName());
		for(Rank rank: city.getRanks())
			con.sendRawMessage(ChatColor.YELLOW+take_rank_+rank.getName());
		con.sendRawMessage(ChatColor.YELLOW+manage_rank_permissions);
		con.sendRawMessage(ChatColor.YELLOW+set_rank_dec);
		con.sendRawMessage(ChatColor.YELLOW+abandon);
		con.sendRawMessage(ChatColor.YELLOW+set_message);
		con.sendRawMessage(ChatColor.YELLOW+move_sigil);
		con.sendRawMessage(ChatColor.YELLOW+set_name);
		con.sendRawMessage(ChatColor.YELLOW+invite_player);
		for(String perm: getPerms())
			if(!perm.equals(add_perm_) || !perm.equals(remove_perm_) || !perm.equals(take_rank_) || !perm.equals(give_rank_)){
				con.sendRawMessage(ChatColor.YELLOW+add_perm_+perm);
			}else if(perm.equals(take_rank_)){
				for(Rank rank: city.getRanks())
					con.sendRawMessage(ChatColor.YELLOW+add_perm_+give_rank_+rank.getName());
			}else if(perm.equals(give_rank_)){
				for(Rank rank: city.getRanks())
					con.sendRawMessage(ChatColor.YELLOW+add_perm_+take_rank_+rank.getName());
			}
		for(String perm: getPerms())
			if(!perm.equals(add_perm_) || !perm.equals(remove_perm_) || !perm.equals(take_rank_) || !perm.equals(give_rank_)){
				con.sendRawMessage(ChatColor.YELLOW+remove_perm_+perm);
			}else if(perm.equals(take_rank_)){
				for(Rank rank: city.getRanks())
					con.sendRawMessage(ChatColor.YELLOW+remove_perm_+give_rank_+rank.getName());
			}else if(perm.equals(give_rank_)){
				for(Rank rank: city.getRanks())
					con.sendRawMessage(ChatColor.YELLOW+remove_perm_+take_rank_+rank.getName());
			}
		con.sendRawMessage(ChatColor.YELLOW+set_active);
	}
}
