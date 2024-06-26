package com.github.fnar.minecraft.item;

import java.util.Random;

public class Record extends RldBaseItem {

  private Song song;

  public static Record newRecord() {
    return new Record();
  }

  @Override
  public ItemType getItemType() {
    return ItemType.RECORD;
  }

  public Record withSong(Song song) {
    this.song = song;
    return this;
  }

  public Song getSong() {
    return song;
  }

  public enum Song {

    THIRTEEN,
    CAT,
    BLOCKS,
    CHIRP,
    FAR,
    MALL,
    MELLOHI,
    STAL,
    STRAD,
    WARD,
    ELEVEN,
    WAIT;

    public static Song chooseRandom(Random random) {
      return Song.values()[random.nextInt(Song.values().length)];
    }

    public Record asItem() {
      return Record.newRecord().withSong(this);
    }
  }
}
