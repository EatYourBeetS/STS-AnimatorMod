package eatyourbeets.monsters.UnnamedReign.UnnamedCultist;

import com.megacrit.cardcrawl.localization.MonsterStrings;
import eatyourbeets.monsters.EYBMonster;
import eatyourbeets.monsters.EYBMonsterData;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;

public abstract class TheUnnamed_Cultist extends EYBMonster
{
    public static final String ID = CreateFullID(TheUnnamed_Cultist.class);
    public static final MonsterStrings STRINGS = GR.GetMonsterStrings(ID);

    public TheUnnamed_Cultist(float x, float y)
    {
        super(new Data(ID), EnemyType.NORMAL, x, y);

        data.SetIdleAnimation(this, 1);
    }

    protected static class Data extends EYBMonsterData
    {
        public Data(String id)
        {
            super(id);

            scale = 2;
            jsonUrl = "images/animator/monsters/TheUnnamed/TheUnnamedCultist.json";
            atlasUrl = "images/animator/monsters/TheUnnamed/TheUnnamedCultist.atlas";
            imgUrl = null;

            if (GameUtilities.GetAscensionLevel() >= 8)
            {
                maxHealth = 222;
            }
            else
            {
                maxHealth = 198;
            }

            SetHB(0, -30, 140, 210, 0, 80);
        }
    }
}
