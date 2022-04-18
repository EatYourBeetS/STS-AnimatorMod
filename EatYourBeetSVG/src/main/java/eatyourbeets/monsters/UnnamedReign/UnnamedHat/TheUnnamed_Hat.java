package eatyourbeets.monsters.UnnamedReign.UnnamedHat;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import eatyourbeets.cards.animator.status.Status_Dazed;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.monsters.EYBMonster;
import eatyourbeets.monsters.EYBMonsterData;
import eatyourbeets.monsters.SharedMoveset.special.EYBMove_SummonEnemy;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.monsters.TheUnnamedHatPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class TheUnnamed_Hat extends EYBMonster
{
    public static final String ID = CreateFullID(TheUnnamed_Hat.class);

    private boolean first = false;
    private final CommonMoveset commonMoveset;

    public TheUnnamed_Hat(float x, float y)
    {
        this(null, x, y);
    }

    public TheUnnamed_Hat(CommonMoveset commonMoveset, float x, float y)
    {
        super(new Data(ID), EnemyType.NORMAL, x, y);

        this.data.SetIdleAnimation(this, 1);

        if (commonMoveset == null)
        {
            this.commonMoveset = new CommonMoveset();
            this.first = true;
        }
        else
        {
            this.commonMoveset = commonMoveset;
        }

        moveset.Special.Add(new EYBMove_SummonEnemy());

        //Rotation:
        moveset.Normal.DefendBuff(9, PowerHelper.Strength, 3)
        .SetBlockBonus(7, 2)
        .SetMiscBonus(7, 1);

        moveset.Normal.ShuffleCard(new Status_Dazed(), 1)
        .SkipAnimation(true);

        moveset.Normal.AttackDebuff(4, PowerHelper.Frail, 1)
        .SetDamageBonus(17, 1);
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        GameActions.Bottom.StackPower(new TheUnnamedHatPower(this, 2));
        GameActions.Bottom.StackPower(new ArtifactPower(this, 3));

        if (first)
        {
            GameActions.Bottom.GainBlock(this, 33);
            GameActions.Bottom.Talk(this, data.strings.DIALOG[0], 2, 2);
        }
        else
        {
            GameActions.Bottom.Talk(this, data.strings.DIALOG[0], 1, 1);
        }
    }

    @Override
    protected void SetNextMove(int roll, int historySize)
    {
        commonMoveset.GetNextMove(this).Select(false);
    }

    protected static class CommonMoveset
    {
        protected static final int[] xPos = new int[7];
        protected static final int[] yPos = new int[7];

        protected int moveOffset;
        protected int index = 0;

        public CommonMoveset()
        {
            moveOffset = AbstractDungeon.aiRng.random(100);
        }

        public EYBAbstractMove GetNextMove(TheUnnamed_Hat owner)
        {
            if (index < xPos.length)
            {
                EYBMove_SummonEnemy move = owner.moveset.GetMove(EYBMove_SummonEnemy.class);

                move.SetSummon(new TheUnnamed_Hat(this, xPos[index], yPos[index]));

                index += 1;

                return move;
            }

            return owner.GetRotation().get((CombatStats.TurnCount(true) + moveOffset) % owner.GetRotation().size());
        }

        static
        {
            for (int i = 0; i < 7; i++)
            {
                xPos[i] = 160 - (130 * i);
                yPos[i] = (i % 2 == 0) ? 36 : -38;
            }
        }
    }

    protected static class Data extends EYBMonsterData
    {
        public Data(String id)
        {
            super(id);

            if (GameUtilities.GetAscensionLevel() > 7)
            {
                maxHealth = 74;
            }
            else
            {
                maxHealth = 66;
            }

            atlasUrl = "images/animator/monsters/TheUnnamed/Hat.atlas";
            jsonUrl = "images/animator/monsters/TheUnnamed/Hat.json";
            scale = 1.6f;

            SetHB(0,0,100,140, 0, 50);
        }
    }
}
