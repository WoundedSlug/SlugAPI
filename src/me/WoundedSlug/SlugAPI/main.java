package me.WoundedSlug.SlugAPI;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class main extends JavaPlugin implements Listener{
	
	static String worldName = "world";
	
	protected static Pattern chatColorPattern = Pattern.compile("(?i)&([0-9A-F])");
	protected static Pattern chatMagicPattern = Pattern.compile("(?i)&([K])");
	protected static Pattern chatBoldPattern = Pattern.compile("(?i)&([L])");
	protected static Pattern chatStrikethroughPattern = Pattern.compile("(?i)&([M])");
	protected static Pattern chatUnderlinePattern = Pattern.compile("(?i)&([N])");
	protected static Pattern chatItalicPattern = Pattern.compile("(?i)&([O])");
	protected static Pattern chatResetPattern = Pattern.compile("(?i)&([R])");
	public final static String MESSAGE_FORMAT = "%prefix%player%suffix &8&l» &f%message";

	protected String optionDisplayname = "display-name-format";
	protected String displayNameFormat = "%prefix%player%suffix";
	
	
	
	public static main plugin;
    
	static HashMap<String, Integer> players = new HashMap<String, Integer>();

	
	@Override
	public void onEnable(){
		World w = Bukkit.getServer().createWorld(new WorldCreator(worldName));
		w.setSpawnFlags(false, false);
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		plugin = this;
		for(Entity e :  Bukkit.getServer().getWorld(worldName).getEntities()){
			e.remove();
		}
		PluginManager pm = getServer().getPluginManager();
	    //pm.registerEvents(new JoinListener(), this);
	    
	    //getCommand("tag").setExecutor(new COMMAND_tag());
	    
	    //loadConfig();
	    /*new PermissionManager();
	    
	    new BukkitRunnable()
	    {
	      public void run()
	      {
	        new PermissionManager();
	      }
	    }.runTaskLater(plugin, 100L);
	    if (Data.enableAutoRefresh) {
	      new AutoRefresh().runTaskTimer(plugin, 0L, 20 * Data.AutoRefreshDelay.intValue());
	    }*/

		new BukkitRunnable(){
			public void run(){
				for(Player p : Bukkit.getOnlinePlayers()){
					for(Player p2 : Bukkit.getOnlinePlayers()){
						p.showPlayer(p2);
					}
				}
			}
		}.runTaskLater(plugin,  40L);
		
	}
	
	@Override
	public void onDisable(){
		
	}


	
	@EventHandler (priority = EventPriority.HIGH)
	public void onPlayerDamage(EntityDamageEvent event){
		if(event.getEntity().getLocation().getWorld().getName().equalsIgnoreCase(worldName))
			event.setCancelled(true);
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
    public void onFoodLevelChange(FoodLevelChangeEvent event)
    {
		event.setFoodLevel(20);
    }

	
	@EventHandler (priority = EventPriority.HIGH)
	public void onPlayerDamage2(EntityDamageByEntityEvent event){
		if(event.getEntityType() == EntityType.PLAYER){
			//Players
			Player hit = (Player) event.getEntity();
			if(hit.getLocation().getWorld().getName().equalsIgnoreCase(worldName)){
				event.setCancelled(true);
			}
		}
	}
	
	 @EventHandler (ignoreCancelled = true, priority = EventPriority.LOW)
	    public void onWeatherChange(WeatherChangeEvent event) {
	        if (event.toWeatherState()) { //Rain is trying to turn on
	        		event.setCancelled(true);
	        }

	    }
	
	@EventHandler (ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onEntitySpawn(CreatureSpawnEvent event){
		if(event.getLocation().getWorld().getName().equalsIgnoreCase(worldName)){
			event.setCancelled(true);
			//event.getEntity().remove();
		}
	}
	
	@EventHandler (priority = EventPriority.HIGH)
	public void onBlockBreak(BlockBreakEvent event){
		if(event.getPlayer().getWorld().getName().equalsIgnoreCase(worldName))
			event.setCancelled(true);
	}
	
	@EventHandler (priority = EventPriority.HIGH)
	public void onBlockPlace(BlockPlaceEvent event){
		if(event.getPlayer().getWorld().getName().equalsIgnoreCase(worldName))
			event.setCancelled(true);
	}
	
	
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent event){
		if(!event.getFrom().getName().equalsIgnoreCase(worldName) && event.getPlayer().getWorld().getName().equalsIgnoreCase(worldName)){
			//Initial World Join
			event.getPlayer().setAllowFlight(false);
			event.getPlayer().setGameMode(GameMode.SURVIVAL);
			event.getPlayer().getInventory().clear();
			event.getPlayer().getInventory().setHelmet(null);
			event.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			
			
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		event.getPlayer().teleport(Bukkit.getWorld(worldName).getSpawnLocation());
		event.getPlayer().setAllowFlight(false);
		event.getPlayer().setGameMode(GameMode.SURVIVAL);
		event.getPlayer().getInventory().clear();
		final String uuid = event.getPlayer().getUniqueId().toString();
		final String name = event.getPlayer().getName();
		Player p = event.getPlayer();
		
		final int hasSlug;
		if(p.hasPermission("slug.slug"))
			hasSlug = 1;
		else
			hasSlug = 0;
		final int hasBuilder;
		if(p.hasPermission("builder.builder"))
			hasBuilder = 1;
		else
			hasBuilder = 0;
		final int hasDev;
		if(p.hasPermission("dev.dev"))
			hasDev = 1;
		else
			hasDev = 0;
		final int hasOwner;
		if(p.hasPermission("owner.owner"))
			hasOwner = 1;
		else
			hasOwner = 0;
		final int hasAdmin;
		if(p.hasPermission("admin.admin"))
			hasAdmin = 1;
		else
			hasAdmin = 0;
		
		players.put(uuid, 0);
		new BukkitRunnable()
	    {
	      public void run()
	      {
	    	  if(event.getPlayer().isValid() && event.getPlayer().isOnline()){
		    	   try {
		    		   TeamHandler modRem = new TeamHandler(name, PermissionsEx.getPermissionManager().getUser(event.getPlayer()).getPrefix(worldName).replace("&", "§"), "", new ArrayList<String>(), 1);
		    		   TeamHandler mod = new TeamHandler(name, PermissionsEx.getPermissionManager().getUser(event.getPlayer()).getPrefix(worldName).replace("&", "§"), "", new ArrayList<String>(), 0);
		    		   TeamHandler mod1 = new TeamHandler(name, Arrays.asList(name), 3);
		    		   for(Player p1 : Bukkit.getOnlinePlayers()){
		    			   modRem.sendToPlayer(p1);
		    		   }
		    		   new BukkitRunnable(){
	    			   public void run(){
							for(Player p1 : Bukkit.getOnlinePlayers()){
								try{
									mod.sendToPlayer(p1);
									mod1.sendToPlayer(p1);
								}catch(Exception e){
									
								}
							}
	    			   }
		    		   }.runTaskLater(plugin, 1L);
				  } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException
				  		| NoSuchFieldException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				  }
		    	   
		    	   for(Player p1 : Bukkit.getOnlinePlayers()){
		    		   try{
		    			   
		    			   String name1 = p1.getName();
		    			   TeamHandler modRem = new TeamHandler(name1, PermissionsEx.getPermissionManager().getUser(p1).getPrefix(worldName).replace("&", "§"), "", new ArrayList<String>(), 1);
			    		   TeamHandler mod2 = new TeamHandler(name1, PermissionsEx.getPermissionManager().getUser(p1).getPrefix(worldName).replace("&", "§"), "", new ArrayList<String>(), 0);
			    		   TeamHandler mod3 = new TeamHandler(name1, Arrays.asList(name1), 3);
			    		   modRem.sendToPlayer(event.getPlayer());
			    		   if(!name1.equalsIgnoreCase(name)){
				    		   new BukkitRunnable(){
				    			   public void run(){
				    				   try{
				    					   if(event.getPlayer().isValid() && event.getPlayer().isOnline()){
						    				   mod2.sendToPlayer(event.getPlayer());
								    		   mod3.sendToPlayer(event.getPlayer());
				    					   }
				    				   }catch(Exception e){
				    					   
				    				   }
				    			   }
				    		   }.runTaskLater(plugin, 1L);
			    		   }
		    		   }catch(Exception e1){
		    			   e1.printStackTrace();
		    		   }
		    	   }
	    	  }
	    	 
	    			  
	      }
	    }.runTaskLater(this, 35L);
	    
	    Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, new BukkitRunnable(){
	    	public void run(){
	    		Connection c = null;
	    		MySQL my = null;
	    		try{
	    			my = (MySQL) MySQL.class.getDeclaredConstructor(new Class[]{Plugin.class, String.class, String.class, String.class, String.class, String.class}).newInstance(plugin, "*ip*", "*port*", "*database*", "*username*", "*password*");
	    			try {
	    				c = my.openConnection();
	    			} catch (ClassNotFoundException e1) {
	    				// TODO Auto-generated catch block
	    				e1.printStackTrace();
	    			} catch (SQLException e1) {
	    				// TODO Auto-generated catch block
	    				e1.printStackTrace();
	    			}
		    		Statement statement = c.createStatement();
		    		ResultSet res = statement.executeQuery("SELECT * FROM points WHERE uuid = '" + uuid+ "';");
		    		if(!res.next()){
		    			players.put(uuid, 0);
		    			statement.executeUpdate("INSERT INTO points (uuid, points, name, slug, builder, dev, owner) VALUES ('" + uuid + "', '0', '" + name +"', '0', '0', '0', '0');");
		    		}else if(res.getString("uuid") == null){
		    			players.put(uuid, 0);
		    			statement.executeUpdate("INSERT INTO points (uuid, points, name, slug, builder, dev, owner) VALUES ('" + uuid + "', '0', '" + name + "', '0', '0', '0', '0');");
		    		}else{
		    			int pts = res.getInt("points");
		    			players.put(uuid, pts);
		    			if(res.getString("name") == null){
		    				statement.executeUpdate("UPDATE points SET name='" + name + "' WHERE uuid='" + uuid +"';");
		    			}
		    			statement = c.createStatement();
			    		res = statement.executeQuery("SELECT * FROM points WHERE uuid = '" + uuid+ "';");
		    			if(res.next() && res.getInt("slug") != hasSlug){
		    				statement.executeUpdate("UPDATE points SET slug='" + hasSlug + "' WHERE uuid='" + uuid +"';");
		    			}
		    			statement = c.createStatement();
			    		res = statement.executeQuery("SELECT * FROM points WHERE uuid = '" + uuid+ "';");
		    			if(res.next() && res.getInt("admin") != hasAdmin){
		    				statement.executeUpdate("UPDATE points SET admin='" + hasAdmin + "' WHERE uuid='" + uuid +"';");
		    			}
		    			statement = c.createStatement();
			    		res = statement.executeQuery("SELECT * FROM points WHERE uuid = '" + uuid+ "';");
		    			if(res.next() && res.getInt("owner") != hasOwner){
		    				statement.executeUpdate("UPDATE points SET owner='" + hasOwner + "' WHERE uuid='" + uuid +"';");
		    			}
		    			statement = c.createStatement();
			    		res = statement.executeQuery("SELECT * FROM points WHERE uuid = '" + uuid+ "';");
		    			if(res.next() && res.getInt("dev") != hasDev){
		    				statement.executeUpdate("UPDATE points SET dev='" + hasDev + "' WHERE uuid='" + uuid +"';");
		    			}
		    			statement = c.createStatement();
			    		res = statement.executeQuery("SELECT * FROM points WHERE uuid = '" + uuid+ "';");
		    			if(res.next() && res.getInt("builder") != hasBuilder){
		    				statement.executeUpdate("UPDATE points SET builder='" + hasBuilder + "' WHERE uuid='" + uuid +"';");
		    			}
		    			
		    		}
		    		
		    		statement.close();
		    		my.closeConnection();
		    		c.close();
	    		}catch(Exception e){
	    			System.out.println(e.getMessage());
	    			try{
	    				my.closeConnection();
	    				c.close();
	    			}catch(Exception e2){
	    				
	    			}
	    		}
	    	}
	    }, 1);
	    

		
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		players.remove(e.getPlayer().getUniqueId().toString());
		String name = e.getPlayer().getName();
		try {
			TeamHandler mod = new TeamHandler(name, PermissionsEx.getPermissionManager().getUser(e.getPlayer()).getPrefix(worldName).replace("&", "§"), "", new ArrayList<String>(), 1);
			for(Player p1 : Bukkit.getOnlinePlayers()){
				mod.sendToPlayer(p1);
			}
		  } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException
		  		| NoSuchFieldException | InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		  }
	}
	
	
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		if (event.isCancelled()) {
			return;
		}

		Player player = event.getPlayer();

		String worldName = player.getWorld().getName();
		
		PermissionUser user = PermissionsEx.getPermissionManager().getUser(player);
		if (user == null) {
			return;
		}
		
		String message = MESSAGE_FORMAT;
		String chatMessage = event.getMessage();
		
		message = this.translateColorCodes(message);
		
		
		chatMessage = this.translateColorCodes(chatMessage, user, worldName);
		if(player.hasPermission("mod.mod")){
			chatMessage = this.translateColorCodes(chatMessage);
		}


		message = message.replace("%message", "%2$s").replace("%displayname", "%1$s");
		message = this.replacePlayerPlaceholders(player, message);
		message = this.replaceTime(message);

		event.setFormat(message);
		event.setMessage(chatMessage);

	}
	
	protected void updateDisplayNames() {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			updateDisplayName(player);
		}
	}

	protected void updateDisplayName(Player player) {
		PermissionUser user = PermissionsEx.getPermissionManager().getUser(player);
		if (user == null) {
			return;
		}

		String worldName = player.getWorld().getName();
		player.setDisplayName(this.translateColorCodes(this.replacePlayerPlaceholders(player, user.getOption(this.optionDisplayname, worldName, this.displayNameFormat))));
	}

	protected String replacePlayerPlaceholders(Player player, String format) {
		PermissionUser user = PermissionsEx.getPermissionManager().getUser(player);
		String worldName = player.getWorld().getName();
		return format.replace("%prefix", this.translateColorCodes(user.getPrefix(worldName))).replace("%suffix", this.translateColorCodes(user.getSuffix(worldName))).replace("%world", worldName).replace("%player", player.getDisplayName()).replace("%group", user.getGroupsNames()[0]);
	}

	protected List<Player> getLocalRecipients(Player sender, String message, double range) {
		Location playerLocation = sender.getLocation();
		List<Player> recipients = new LinkedList<Player>();
		double squaredDistance = Math.pow(range, 2);
		PermissionManager manager = PermissionsEx.getPermissionManager();
		for (Player recipient : Bukkit.getServer().getOnlinePlayers()) {
			// Recipient are not from same world
			if (!recipient.getWorld().equals(sender.getWorld())) {
				continue;
			}

			if (playerLocation.distanceSquared(recipient.getLocation()) > squaredDistance && !manager.has(sender, "chatmanager.override.ranged")) {
				continue;
			}

			recipients.add(recipient);
		}
		return recipients;
	}

	protected String replaceTime(String message) {
		Calendar calendar = Calendar.getInstance();

		if (message.contains("%h")) {
			message = message.replace("%h", String.format("%02d", calendar.get(Calendar.HOUR)));
		}

		if (message.contains("%H")) {
			message = message.replace("%H", String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)));
		}

		if (message.contains("%g")) {
			message = message.replace("%g", Integer.toString(calendar.get(Calendar.HOUR)));
		}

		if (message.contains("%G")) {
			message = message.replace("%G", Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)));
		}

		if (message.contains("%i")) {
			message = message.replace("%i", String.format("%02d", calendar.get(Calendar.MINUTE)));
		}

		if (message.contains("%s")) {
			message = message.replace("%s", String.format("%02d", calendar.get(Calendar.SECOND)));
		}

		if (message.contains("%a")) {
			message = message.replace("%a", (calendar.get(Calendar.AM_PM) == 0) ? "am" : "pm");
		}

		if (message.contains("%A")) {
			message = message.replace("%A", (calendar.get(Calendar.AM_PM) == 0) ? "AM" : "PM");
		}

		return message;
	}
	
	

	protected static String translateColorCodes(String string) {
		if (string == null) {
			return "";
		}

		String newstring = string;
		newstring = chatColorPattern.matcher(newstring).replaceAll("\u00A7$1");
		newstring = chatMagicPattern.matcher(newstring).replaceAll("\u00A7$1");
		newstring = chatBoldPattern.matcher(newstring).replaceAll("\u00A7$1");
		newstring = chatStrikethroughPattern.matcher(newstring).replaceAll("\u00A7$1");
		newstring = chatUnderlinePattern.matcher(newstring).replaceAll("\u00A7$1");
		newstring = chatItalicPattern.matcher(newstring).replaceAll("\u00A7$1");
		newstring = chatResetPattern.matcher(newstring).replaceAll("\u00A7$1");
		return newstring;
	}

	protected String translateColorCodes(String string, PermissionUser user, String worldName) {
		if (string == null) {
			return "";
		}

		String newstring = string;
		if(user.has("mod.mod")){
			string = translateColorCodes(string);
		}
		newstring = chatResetPattern.matcher(newstring).replaceAll("\u00A7$1");
		return newstring;
	}
	
	
	

	public boolean onCommand (CommandSender sender, Command command, String commandLabel, String[] args){
		

		
		if(sender.hasPermission("owner.owner")){
			if(args.length > 0){
				boolean doesExist = false;
				String uuid = "";
				for(Player pe : Bukkit.getOnlinePlayers()){
					if(pe.getName().equalsIgnoreCase(args[0])){
						doesExist = true;
						uuid = pe.getUniqueId().toString();
					}
				}
				if(doesExist){
					addPoints(uuid, 5);
					sender.sendMessage(ChatColor.RED + "Player now has " + getPoints(uuid) + " points!");
				}else{
					sender.sendMessage(ChatColor.RED + "Could not find player \"" + args[0] + "\"");
				}
			}else{
				sender.sendMessage(ChatColor.RED + "Please provide a player's name");
			}
		}else{
			sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
		}
		
		
		return true;
		
	}
	
	//API STUFF
	public static String getPrefix(Player player){
		PermissionUser user = PermissionsEx.getPermissionManager().getUser(player);
		String worldName = player.getWorld().getName();
		if(user.getPrefix(worldName).length() > 4)
			return translateColorCodes(user.getPrefix(worldName));
		return ChatColor.GRAY + "[DEFAULT]";
	}
	
	public static String getRealPrefix(Player player){
		PermissionUser user = PermissionsEx.getPermissionManager().getUser(player);
		String worldName = player.getWorld().getName();
		return translateColorCodes(user.getPrefix(worldName));
	}
	
	
	public static int getPoints(String uuid){
		if(players.get(uuid) == null)
			return 0;
		return players.get(uuid);
	}
	
	@SuppressWarnings("deprecation")
	public static void addPoints(final String uuid, final int points){
		if(players.get(uuid) != null){
			final int newPts = players.get(uuid) + points;
			players.put(uuid, newPts);
			if(points > 0)
				Bukkit.getPlayer(UUID.fromString(uuid)).sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC + "+" + points + " points!");
			else
				Bukkit.getPlayer(UUID.fromString(uuid)).sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC + "" + points + " points!");
		}
		
		Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, new BukkitRunnable(){
			public void run(){
				Connection c = null;
	    		MySQL my = null;
	    		try{
	    			my = (MySQL) MySQL.class.getDeclaredConstructor(new Class[]{Plugin.class, String.class, String.class, String.class, String.class, String.class}).newInstance(plugin, "198.245.63.162", "3306", "slugPoints", "slug", "F3y1fds89z");
	    			try {
	    				c = my.openConnection();
	    			} catch (ClassNotFoundException e1) {
	    				// TODO Auto-generated catch block
	    				e1.printStackTrace();
	    			} catch (SQLException e1) {
	    				// TODO Auto-generated catch block
	    				e1.printStackTrace();
	    			}
					Statement statement = c.createStatement();
		    		ResultSet res = statement.executeQuery("SELECT * FROM points WHERE uuid = '" + uuid+ "';");
		    		if(res.next()){
		    			if(points > 0)
		    				statement.executeUpdate("UPDATE points SET points=points+" + points + " WHERE uuid='" + uuid +"';");
		    			else
		    				statement.executeUpdate("UPDATE points SET points=points" + points + " WHERE uuid='" + uuid +"';");
		    		}
		    		statement.close();
		    		my.closeConnection();
		    		c.close();
				}catch(Exception e){
					System.out.println(e.getMessage());
					try {
						my.closeConnection();
						c.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}, 5);
		
	}


}
