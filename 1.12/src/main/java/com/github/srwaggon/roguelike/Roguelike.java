package com.github.srwaggon.roguelike;

import com.github.srwaggon.roguelike.command.CommandRoguelike;
import com.github.srwaggon.roguelike.worldgen.DungeonGenerator;

import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import greymerk.roguelike.CommonProxy;
import greymerk.roguelike.EntityJoinWorld;

@Mod(modid = "roguelike", name = "Roguelike Dungeons -- Fnar Edition", version = Roguelike.version, acceptableRemoteVersions = "*")
public class Roguelike {

  public static final String version = "2.3.1";
  public static final String date = "2021/2/01";
  // The instance of your mod that Forge uses.
  @Instance("roguelike")
  public static Roguelike instance;
  // Says where the client and server 'proxy' code is loaded.
  @SidedProxy(clientSide = "greymerk.roguelike.ClientProxy", serverSide = "greymerk.roguelike.CommonProxy")
  public static CommonProxy proxy;
  public static DungeonGenerator worldGen = new DungeonGenerator();

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    GameRegistry.registerWorldGenerator(worldGen, 0);
  }

  @EventHandler
  public void modInit(FMLInitializationEvent event) {
    MinecraftForge.EVENT_BUS.register(new EntityJoinWorld());
  }

  @EventHandler
  public void serverStart(FMLServerStartingEvent event) {
    MinecraftServer server = event.getServer();
    ICommandManager command = server.getCommandManager();
    ServerCommandManager serverCommand = ((ServerCommandManager) command);
    serverCommand.registerCommand(new CommandRoguelike());
  }
}
