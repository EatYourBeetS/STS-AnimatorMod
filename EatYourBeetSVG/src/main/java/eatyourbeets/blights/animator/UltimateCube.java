package eatyourbeets.blights.animator;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.blights.AnimatorBlight;
import eatyourbeets.powers.EYBPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class UltimateCube extends AnimatorBlight
{
    public static final String ID = CreateFullID(UltimateCube.class);
    public static final int LOSE_BLOCK = 6;

    public UltimateCube()
    {
        super(ID, LOSE_BLOCK);

        setCounter(-1);
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        if (GameUtilities.InBattle())
        {
            for (AbstractMonster m : GameUtilities.GetEnemies(true))
            {
                m.powers.add(new Tracker(this, m));
            }

            setCounter(0);
        }
        else
        {
            setCounter(-1);
        }
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        setCounter(-1);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        setCounter(0);
    }

    @Override
    public void setCounter(int counter)
    {
        super.setCounter(counter);

        if (this.pulse = (counter == 0))
        {
            this.flashTimer += 0.01f; // because updateFlash() checks for flashTimer != 0 ...
        }
    }

    @Override
    public void onPlayerEndTurn()
    {
        super.onPlayerEndTurn();

        if (counter == 0)
        {
            AbstractDungeon.player.loseBlock(LOSE_BLOCK);
            GameActions.Top.WaitRealtime(0.25f);
            this.flash();
        }
    }

    @Override
    public void onCreateEnemy(AbstractMonster m)
    {
        super.onCreateEnemy(m);

        m.powers.add(new Tracker(this, m));
    }

    private static class Tracker extends EYBPower implements InvisiblePower
    {
        private UltimateCube blight;

        public Tracker(UltimateCube blight, AbstractCreature owner)
        {
            super(owner, UltimateCube.ID + "Power");

            this.blight = blight;
        }

        @Override
        public void wasHPLost(DamageInfo info, int damageAmount)
        {
            super.wasHPLost(info, damageAmount);

            if (damageAmount > 0 && info.type == DamageInfo.DamageType.NORMAL && GameUtilities.IsPlayer(info.owner))
            {
                blight.setCounter(blight.counter + damageAmount);
            }
        }
    }
}