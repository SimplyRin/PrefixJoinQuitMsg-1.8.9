package net.simplyrin.prefixjoinquitmsg;

import java.util.HashMap;

import club.sk1er.mods.publicmod.JsonHolder;
import club.sk1er.mods.publicmod.Multithreading;
import club.sk1er.mods.publicmod.Sk1erMod;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
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
	public static final String VERSION = "1.4";

	public boolean isHypixel = false;

	private HashMap<String, String> cache = new HashMap<String, String>();

	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onLogin(FMLNetworkEvent.ClientConnectedToServerEvent event) {
		String address = event.manager.getRemoteAddress().toString().toLowerCase();
		isHypixel = address.contains("hypixel.net");
	}

	@SubscribeEvent
	public void onChat(ClientChatReceivedEvent event) {
		String[] args = ChatColor.stripColor(event.message.getFormattedText()).split(" ");

		if(!this.isHypixel) {
			return;
		}

		if(args.length > 1) {
			if(args[1].equals("joined.")) {
				String name = args[0];
				event.setCanceled(true);

				System.out.println(name + " joined.");
				this.post(name, TYPE.JOIN);
			}

			if(args[1].equals("left.")) {
				String name = args[0];
				event.setCanceled(true);

				System.out.println(name + " left.");
				this.post(name, TYPE.LEFT);
			}
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

			if(!this.isHypixel) {
				return;
			}

			String result = Sk1erMod.rawWithAgent("https://api.simplyrin.net/Forge-Mods/PrefixJoinQuitMsg/" + VERSION + ".txt");

			if(!result.contains(",") || result == null) {
				this.sendMessage(this.getPrefix() + "&cサーバーへの接続に失敗したか、間違ったバージョンを使用している可能性があります。");
				return;
			}

			Boolean update = Boolean.valueOf(result.split(",")[0]);
			String version = ChatColor.translateAlternateColorCodes('&', result.split(",")[1]);
			String msg = ChatColor.translateAlternateColorCodes('&', result.split(",")[2]);

			if(!update) {
				return;
			}

			this.sendMessage(getPrefix() + "§b§m-------------------------");
			this.sendMessage(getPrefix() + "§b新しいバージョンが使用できます。");
			this.sendMessage(getPrefix() + " ");
			this.sendMessage(getPrefix() + "§bバージョン: " + version);
			this.sendMessage(getPrefix() + "§bメッセージ: " + msg);
			this.sendMessage(getPrefix() + " ");
			this.sendMessage(getPrefix() + "§b§m-------------------------");
		});
	}

	private void post(String name, TYPE type) {
		if(this.cache.get(name) != null) {
			if(type.equals(TYPE.JOIN)) {
				this.sendMessage("§7[§a+§7] §r" + this.cache.get(name) + " " + name + " §ejoined.");
			}

			if(type.equals(TYPE.LEFT)) {
				this.sendMessage("§7[§c-§7] §r" + this.cache.get(name) + " " + name + " §eleft.");
			}
			return;
		}

		Multithreading.runAsync(() -> {
			String result = Sk1erMod.rawWithAgent("https://api.simplyrin.net/Hypixel-API/prefix.php?name=" + name);
			JsonHolder jsonHolder = new JsonHolder(result);

			if(!jsonHolder.has("success")) {
				return;
			}

			if(!jsonHolder.optBoolean("success")) {
				return;
			}

			String prefix = jsonHolder.optString("prefix");

			this.cache.put(name, prefix);

			if(type.equals(TYPE.JOIN)) {
				this.sendMessage("§7[§a+§7] §r" + prefix + " " + name + " §ejoined.");
			}

			if(type.equals(TYPE.LEFT)) {
				this.sendMessage("§7[§c-§7] §r" + prefix + " " + name + " §eleft.");
			}
		});
	}

	public String getPrefix() {
		return "§7[§cPrefixJoinQuitMsg§7] §r";
	}

	public enum TYPE {
		JOIN, LEFT
	}

	public void sendMessage(String message) {
		message = message.replaceAll("&", "\u00a7");
		message = message.replaceAll("§", "\u00a7");

		Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(message));
	}

}
