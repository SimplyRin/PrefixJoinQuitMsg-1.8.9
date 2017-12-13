package net.simplyrin.prefixjoinquitmsg;

import club.sk1er.mods.publicmod.Multithreading;
import club.sk1er.mods.publicmod.Sk1erMod;
import net.md_5.bungee.api.ChatColor;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;

@Mod(modid = PrefixJoinQuitMsg.MODID, version = PrefixJoinQuitMsg.VERSION)
public class PrefixJoinQuitMsg {

	public static final String MODID = "PrefixJoinQuitMsg";
	public static final String VERSION = "1.0";

	public static boolean isHypixel = false;

	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onLogin(FMLNetworkEvent.ClientConnectedToServerEvent event) {
		String address = event.manager.getRemoteAddress().toString().toLowerCase();

		if(address.contains("hypixel.net")) {
			isHypixel = true;
		} else {
			isHypixel = false;
		}
	}

	@SubscribeEvent
	public void onChat(ClientChatReceivedEvent event) {
		String[] args = ChatColor.stripColor(event.message.getFormattedText()).split(" ");

		if(!isHypixel) {
			return;
		}

		try {
			if(args == null) {
				return;
			}
		} catch (Exception e) {
			return;
		}

		try {
			if(args[0].isEmpty()) {
				return;
			}
		} catch (Exception e) {
			return;
		}

		try {
			if(args[1].isEmpty()) {
				return;
			}
		} catch (Exception e) {
			return;
		}

		if(args[1].equals("joined.")) {
			String name = args[0];
			event.setCanceled(true);

			System.out.println(name + " joined.");
			post(name, TYPE.JOIN);
		}

		if(args[1].equals("left.")) {
			String name = args[0];
			event.setCanceled(true);

			System.out.println(name + " left.");
			post(name, TYPE.LEFT);
		}
	}

	@SubscribeEvent
	public void onConnect(ClientConnectedToServerEvent event) {
		Multithreading.runAsync(() -> {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if(!isHypixel) {
				return;
			}

			String result = Sk1erMod.rawWithAgent("https://api.simplyrin.net/Forge-Mods/PrefixJoinQuitMsg/" + VERSION);

			if(!result.contains(",") || result == null) {
				ChatHandler.send(getPrefix() + "&cサーバーへの接続に失敗したか、間違ったバージョンを使用している可能性があります。");
				return;
			}

			Boolean update = Boolean.valueOf(result.split(",")[0]);
			String version = ChatColor.translateAlternateColorCodes('&', result.split(",")[1]);
			String msg = ChatColor.translateAlternateColorCodes('&', result.split(",")[2]);

			if(!update) {
				return;
			}

			ChatHandler.send(getPrefix() + "§b§m-------------------------");
			ChatHandler.send(getPrefix() + "§b新しいバージョンが使用できます。");
			ChatHandler.send(getPrefix() + " ");
			ChatHandler.send(getPrefix() + "§bバージョン: " + version);
			ChatHandler.send(getPrefix() + "§bメッセージ: " + msg);
			ChatHandler.send(getPrefix() + " ");
			ChatHandler.send(getPrefix() + "§b§m-------------------------");
		});
	}

	private static void post(String name, TYPE type) {
		Multithreading.runAsync(() -> {
			String result = Sk1erMod.rawWithAgent("http://hypixel.chattriggers.com/stats/playerinfo/" + name);

			if(result == null) {
				return;
			}

			if(!result.contains("Rank: ")) {
				return;
			}

			String prefix = result.split("Rank: ")[1].split(",")[0];

			if(type.equals(TYPE.JOIN)) {
				ChatHandler.send("§7[§a+§7] §r" + prefix + " " + name + " §ejoined.");
			}

			if(type.equals(TYPE.LEFT)) {
				ChatHandler.send("§7[§c-§7] §r" + prefix + " " + name + " §eleft.");
			}
		});
	}

	public static String getPrefix() {
		return "§7[§cPrefixJoinQuitMsg§7] §r";
	}

	public static enum TYPE {
		JOIN, LEFT
	}

}
