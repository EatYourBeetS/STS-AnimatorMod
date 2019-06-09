package eatyourbeets.monsters.UnnamedReign.Cultist;

import eatyourbeets.monsters.AnimatorMonster;
import eatyourbeets.monsters.UnnamedReign.AbstractMonsterData;
import eatyourbeets.powers.PlayerStatistics;

public abstract class TheUnnamed_Cultist extends AnimatorMonster
{
    public static final String ID = "Animator_TheUnnamedCultist";
    public static final String NAME = "Cultist";

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
            jsonUrl = "images/monsters/Animator_TheUnnamed/TheUnnamedCultist.json";
            atlasUrl = "images/monsters/Animator_TheUnnamed/TheUnnamedCultist.atlas";
            imgUrl = null;

            if (PlayerStatistics.GetAscensionLevel() > 7)
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
