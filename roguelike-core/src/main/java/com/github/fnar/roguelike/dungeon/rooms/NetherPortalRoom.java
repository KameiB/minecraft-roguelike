package com.github.fnar.roguelike.dungeon.rooms;

import com.google.common.collect.Lists;

import com.github.fnar.minecraft.block.SingleBlockBrush;
import com.github.fnar.minecraft.block.normal.StairsBlock;
import com.github.fnar.roguelike.worldgen.generatables.NetherPortal;
import com.github.fnar.roguelike.worldgen.generatables.Pillar;

import java.util.List;
import java.util.stream.Stream;

import greymerk.roguelike.dungeon.base.BaseRoom;
import greymerk.roguelike.dungeon.rooms.RoomSetting;
import greymerk.roguelike.dungeon.settings.LevelSettings;
import greymerk.roguelike.treasure.loot.ChestType;
import greymerk.roguelike.worldgen.BlockBrush;
import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.Direction;
import greymerk.roguelike.worldgen.WorldEditor;
import greymerk.roguelike.worldgen.shapes.RectHollow;
import greymerk.roguelike.worldgen.shapes.RectSolid;

public class NetherPortalRoom extends BaseRoom {

  public NetherPortalRoom(RoomSetting roomsSetting, LevelSettings levelSettings, WorldEditor worldEditor) {
    super(roomsSetting, levelSettings, worldEditor);
    this.size = 10;
    this.height = 8;
  }

  @Override
  public BaseRoom generate(Coord origin, List<Direction> entrances) {
    super.generate(origin, entrances);

    Direction front = getEntrance(entrances);

    generateCatwalks(origin);
    theFloorIsLava(origin);
    createPathFromEachEntranceToTheCenterOverTheLiquid(origin, front);
    generateNetherPortalWithPlatform(origin, front);
    generateChestInCorner(origin, front);

    return null;
  }

  @Override
  protected void generateWalls(Coord at) {
    walls().fill(worldEditor, RectHollow.newRect(
        at.copy().north(getWallDist()).west(getWallDist()).up(getCeilingHeight()),
        at.copy().south(getWallDist()).east(getWallDist()).down(3)
    ));
  }

  @Override
  protected void generateFloor(Coord at) {
    floors().fill(worldEditor, RectSolid.newRect(
        at.copy().north(3).west(3).down(),
        at.copy().south(3).east(3).down(2)
    ));
  }

  private void generateCatwalks(Coord origin) {
    StairsBlock stair = stairs();

    for (Direction side : Direction.cardinals()) {
      Coord catwalkOrigin = origin.copy().translate(side, getWallDist() - 1);
      floors().fill(worldEditor, RectSolid.newRect(
          catwalkOrigin.copy().translate(side.left(), getWallDist()),
          catwalkOrigin.copy().translate(side.right(), getWallDist()).translate(side.back()).down(2)
      ));

      SingleBlockBrush.AIR.fill(worldEditor, RectSolid.newRect(
          catwalkOrigin.copy().translate(side.left(), 2),
          catwalkOrigin.copy().translate(side.right(), 2).translate(side.back())
      ));

      for (Direction orthogonal : side.orthogonals()) {
        Coord place = catwalkOrigin.copy().translate(orthogonal, 2);
        stair.setUpsideDown(false).setFacing(orthogonal.reverse());
        stair.stroke(worldEditor, place);
        stair.stroke(worldEditor, place.translate(side.back()));
      }
    }
  }

  private void createPathFromEachEntranceToTheCenterOverTheLiquid(Coord origin, Direction front) {
    Direction walkwayDirection = front.reverse();
    floors().fill(worldEditor, RectSolid.newRect(
        origin.copy().translate(walkwayDirection.left()).down(),
        origin.copy().translate(walkwayDirection.right()).translate(walkwayDirection, getWallDist()).down(2)
    ));
  }

  private void generateNetherPortalWithPlatform(Coord origin, Direction front) {
    int portalHeight = 7;
    int portalWidth = 5;
    Coord portalBase = origin.copy().down(2);

    // encasing
    BlockBrush pillar = pillars();
    pillar.fill(worldEditor, RectSolid.newRect(
        portalBase.copy().translate(front).translate(front.left(), 3),
        portalBase.copy().translate(front.back()).translate(front.right(), 3).up(portalHeight)
    ));

    // portal platform
    pillar.fill(worldEditor, RectSolid.newRect(
        portalBase.copy().translate(front.left(), 2).translate(front, 2).up(),
        portalBase.copy().translate(front.right(), 2).translate(front.back(), 2)
    ));

    StairsBlock stairsBrush = stairs();
    Stream.of(front, front.reverse())
        .forEach(side -> {
          Coord platformStairs = portalBase.copy().translate(side, 2).up();
          stairsBrush.setUpsideDown(false);
          stairsBrush.setFacing(side.reverse());
          stairsBrush.stroke(worldEditor, platformStairs);
          stairsBrush.stroke(worldEditor, platformStairs.copy().translate(front.left()));
          stairsBrush.stroke(worldEditor, platformStairs.copy().translate(front.right()));
        });

    // nether portal, atop the portal platform
    new NetherPortal(worldEditor).generate(portalBase, front, portalWidth, portalHeight);

    // 2x cheeky spawners beneath of portal
    for (Direction orthogonal : front.orthogonals()) {
      generateSpawner(portalBase.copy().down().translate(orthogonal));
    }
  }

  @Override
  protected void generateDecorations(Coord origin) {
    List<Coord> pillarCoords = Lists.newArrayList();
    for (Direction c : Direction.cardinals()) {
      Direction l = c.left();
      Direction r = c.right();
      pillarCoords.add(origin.copy().translate(l, 3).translate(c, 8));
      pillarCoords.add(origin.copy().translate(r, 3).translate(c, 8));
      pillarCoords.add(origin.copy().translate(l, 8).translate(c, 8));
    }
    Pillar pillar = Pillar.newPillar(worldEditor)
        .withHeight(getCeilingHeight())
        .withStairBrush(stairs())
        .withPillarBrush(secondaryPillars());
    pillarCoords.forEach(pillar::generate);
  }

  private void generateChestInCorner(Coord origin, Direction front) {
    if (random().nextInt(3) == 0) {
      return;
    }
    int distanceFromOrigin = getWallDist() - 2;
    Coord cursor = origin.copy()
        .up()
        .translate(front.reverse(), distanceFromOrigin)
        .translate(front.reverse().left(), distanceFromOrigin);
    generateChest(cursor, front, ChestType.UNCOMMON_TREASURES);
  }

}
