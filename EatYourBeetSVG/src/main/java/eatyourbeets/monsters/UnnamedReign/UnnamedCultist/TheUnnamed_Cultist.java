package eatyourbeets.monsters.UnnamedReign.UnnamedCultist;

import com.megacrit.cardcrawl.localization.MonsterStrings;
import eatyourbeets.monsters.AnimatorMonster;
import eatyourbeets.monsters.AbstractMonsterData;
import eatyourbeets.resources.animator.AnimatorResources;
import eatyourbeets.utilities.GameUtilities;

public abstract class TheUnnamed_Cultist extends AnimatorMonster
{
    public static final String ID = CreateFullID(TheUnnamed_Cultist.class.getSimpleName());
    public static final MonsterStrings STRINGS = AnimatorResources.GetMonsterStrings(ID);

    public TheUnnamed_Cultist(float x, float y)
    {
        super(new Data(ID), EnemyType.NORMAL, x, y);

        data.SetIdleAnimation(this, 1);
    }

    protected static class Data extends AbstractMonsterData
    {
        public Data(String id)
        {
            super(id);

            scale = 2;
            jsonUrl = "images/monsters/animator/TheUnnamed/TheUnnamedCultist.json";
            atlasUrl = "images/monsters/animator/TheUnnamed/TheUnnamedCultist.atlas";
            imgUrl = null;

            if (GameUtilities.GetAscensionLevel() > 7)
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
