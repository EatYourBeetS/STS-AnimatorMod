package eatyourbeets.monsters.UnnamedReign.UnnamedHat;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.monsters.AnimatorMonster;
import eatyourbeets.monsters.SharedMoveset.Move_AttackFrail;
import eatyourbeets.monsters.SharedMoveset.Move_GainStrengthAndBlock;
import eatyourbeets.monsters.SharedMoveset.Move_ShuffleDazed;
import eatyourbeets.monsters.AbstractMonsterData;
import eatyourbeets.monsters.UnnamedReign.Shapes.Crystal.Moveset.Move_SummonEnemy;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.UnnamedReign.TheUnnamedHatPower;
import eatyourbeets.utilities.GameUtilities;

public class TheUnnamed_Hat extends AnimatorMonster
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

        moveset.AddSpecial(new Move_SummonEnemy());

        if (GameUtilities.GetAscensionLevel() >= 7)
        {
            moveset.AddNormal(new Move_GainStrengthAndBlock(4, 11));
        }
        else
        {
            moveset.AddNormal(new Move_GainStrengthAndBlock(3, 9));
        }

        moveset.AddNormal(new Move_ShuffleDazed(1, true));
        moveset.AddNormal(new Move_AttackFrail(4, 1));
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
    protected void SetNextMove(int roll, int historySize, Byte previousMove)
    {
        commonMoveset.GetNextMove(this).SetMove();
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

        public AbstractMove GetNextMove(TheUnnamed_Hat owner)
        {
            if (index < xPos.length)
            {
                Move_SummonEnemy move = owner.moveset.GetMove(Move_SummonEnemy.class);

                move.SetSummon(new TheUnnamed_Hat(this, xPos[index], yPos[index]));

                index += 1;

                return move;
            }

            int offset = PlayerStatistics.getTurnCount() + moveOffset;

            return owner.moveset.rotation.get(offset % owner.moveset.rotation.size());
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

    protected static class Data extends AbstractMonsterData
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

            atlasUrl = "images/monsters/animator/TheUnnamed/Hat.atlas";
            jsonUrl = "images/monsters/animator/TheUnnamed/Hat.json";
            scale = 1.6f;

            SetHB(0,0,100,140, 0, 50);
        }
    }
}
