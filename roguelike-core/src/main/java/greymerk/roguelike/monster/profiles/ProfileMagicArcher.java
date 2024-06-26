package greymerk.roguelike.monster.profiles;

import com.github.fnar.minecraft.Difficulty;
import com.github.fnar.minecraft.item.Arrow;
import com.github.fnar.util.Color;

import java.util.Random;

import greymerk.roguelike.monster.MonsterProfile;
import greymerk.roguelike.monster.Mob;

public class ProfileMagicArcher implements MonsterProfile {

  @Override
  public Mob apply(Mob mob, int level, int difficulty, Random random) {
    mob.equipBow(random, level, Difficulty.fromInt(difficulty));
    mob.equipArrows(Arrow.newRandomHarmful(random));
    mob.equipArmor(random, level, Color.DEEP_VIOLET, difficulty);
    return mob;
  }

}
