package eatyourbeets.monsters.UnnamedReign.Shapes.Crystal;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.SlowPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.SharedMoveset.*;
import eatyourbeets.monsters.UnnamedReign.Shapes.Crystal.Moveset.Move_SummonEnemy;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterShape;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.AntiArtifactSlowPower;
import eatyourbeets.powers.UnnamedReign.UltimateCrystalPower;

public class UltimateCrystal extends Crystal
{
    public static final String ID = CreateFullID(MonsterShape.Crystal, MonsterElement.Ultimate, MonsterTier.Ultimate);
    public static final String NAME = "Ultimate Crystal";

    private boolean alreadySummoned = false;
    private int sequence = 0;

    public UltimateCrystal()
    {
        super(MonsterElement.Ultimate, MonsterTier.Ultimate, 0, 0);

        int level = AbstractDungeon.ascensionLevel;

        moveset.AddSpecial(new Move_SummonEnemy());

        moveset.AddNormal(new Move_AttackDefend(1, 8));
        moveset.AddNormal(new Move_ShuffleDazed(3));
        moveset.AddNormal(new Move_GainArtifact(3));
    }

    @Override
    protected void ExecuteNextMove()
    {
        if (moveset.GetMove(nextMove) instanceof Move_SummonEnemy)
        {
            alreadySummoned = true;
        }

        super.ExecuteNextMove();
    }

    @Override
    protected void SetNextMove(int roll, int historySize, Byte previousMove)
    {
        if (!alreadySummoned && currentHealth < maxHealth / 2)
        {
            Move_SummonEnemy move = moveset.GetMove(Move_SummonEnemy.class);
            if (move.CanUse(previousMove))
            {
                MonsterElement element = MonsterElement.GetRandomizedList().Retrieve(AbstractDungeon.miscRng);
                move.SetSummon(Crystal.CreateEnemy(MonsterTier.Normal, element, -240, 0));
                move.SetMove();
                return;
            }
        }

        super.SetNextMove(sequence % 100, historySize, previousMove);
        sequence += 1;
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        GameActionsHelper.ApplyPower(this, this, new UltimateCrystalPower(this));
        GameActionsHelper.ApplyPower(this, this, new AntiArtifactSlowPower(this, 1), 1);

        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("ANIMATOR_THE_ULTIMATE_CRYSTAL.ogg");
    }

    @Override
    public void die()
    {
        super.die();

        AbstractDungeon.scene.fadeInAmbiance();
        CardCrawlGame.music.fadeOutTempBGM();
    }
}
