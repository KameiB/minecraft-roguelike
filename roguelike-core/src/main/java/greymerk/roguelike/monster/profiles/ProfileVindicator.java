package greymerk.roguelike.monster.profiles;

import com.github.fnar.roguelike.loot.special.tools.SpecialAxe;

import java.util.Random;

import greymerk.roguelike.monster.MonsterProfile;
import greymerk.roguelike.monster.Mob;

public class ProfileVindicator implements MonsterProfile {

  @Override
  public Mob apply(Mob mob, int level, int difficulty, Random random) {
    mob.equipMainhand(new SpecialAxe(random, level).complete());
    return mob;
  }

}
