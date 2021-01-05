package com.github.srwaggon.roguelike.command;

import com.github.srwaggon.roguelike.worldgen.WorldEditor1_12;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import greymerk.roguelike.command.CommandSender;
import greymerk.roguelike.command.MessageType;
import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.WorldEditor;

public class CommandSender1_12 implements CommandSender {

  private ICommandSender commandSender;

  public CommandSender1_12(ICommandSender iCommandSender) {
    this.commandSender = iCommandSender;
  }

  @Override
  public void sendMessage(String message, MessageType type) {
    String formattedMessage = type.apply(message);
    TextComponentString text = new TextComponentString(formattedMessage);
    commandSender.sendMessage(text);
  }

  @Override
  public void give(ItemStack item) {
    Entity player = commandSender.getCommandSenderEntity();
    EntityItem drop = player.entityDropItem(item, 0);
    drop.setNoPickupDelay();
  }

  @Override
  public WorldEditor createWorldEditor() {
    return new WorldEditor1_12(commandSender.getEntityWorld());
  }

  @Override
  public Coord getPos() {
    BlockPos bp = commandSender.getPosition();
    return new Coord(bp.getX(), bp.getY(), bp.getZ());
  }
}
