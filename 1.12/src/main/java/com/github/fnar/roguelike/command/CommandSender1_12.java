package com.github.fnar.roguelike.command;

import com.github.fnar.minecraft.WorldEditor1_12;
import com.github.fnar.minecraft.item.RldItemStack;
import com.github.fnar.minecraft.item.mapper.ItemMapper1_12;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.WorldEditor;

public class CommandSender1_12 implements CommandSender {

  private final ICommandSender commandSender;

  public CommandSender1_12(ICommandSender iCommandSender) {
    this.commandSender = iCommandSender;
  }

  @Override
  public void sendMessage(String message, MessageType type) {
    commandSender.sendMessage(formatMessage(message, type));
  }

  @Override
  public void sendMessage(String message, String details, MessageType type) {
    commandSender.sendMessage(
        formatMessage(message, type)
            .appendText(" ")
            .appendSibling(new TextComponentString(details)));
  }

  private static ITextComponent formatMessage(String message, MessageType type) {
    Style style = new Style().setColor(TextFormattingMapper1_12.map(type.getTextFormat().getCodeChar()));
    return new TextComponentTranslation(message).setStyle(style);
  }

  @Override
  public void give(RldItemStack item) {
    Entity player = commandSender.getCommandSenderEntity();
    ItemStack mappedItem = new ItemMapper1_12().map(item);
    EntityItem drop = player.entityDropItem(mappedItem, 0);
    drop.setNoPickupDelay();
  }

  @Override
  public WorldEditor createWorldEditor() {
    return new WorldEditor1_12(commandSender.getEntityWorld());
  }

  @Override
  public Coord getCoord() {
    BlockPos blockPos = commandSender.getPosition();
    return new Coord(blockPos.getX(), blockPos.getY(), blockPos.getZ());
  }

}
