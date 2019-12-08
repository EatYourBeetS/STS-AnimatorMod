package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.interfaces.OnCallbackSubscriber;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.utilities.JavaUtilities;

public class Buoy extends AnimatorRelic implements OnCallbackSubscriber
{
    public static final String ID = CreateFullID(Buoy.class.getSimpleName());

    private static final int HP_THRESHOLD = 30;
    private static final int BLOCK_AMOUNT = 4;

    public Buoy()
    {
        super(ID, RelicTier.COMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return JavaUtilities.Format(DESCRIPTIONS[0], HP_THRESHOLD, BLOCK_AMOUNT);
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        counter = -1;
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        counter = -1;
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        stopPulse();
    }

    @Override
    public void atTurnStartPostDraw()
    {
        super.atTurnStartPostDraw();

        UpdateThreshold();
    }

    @Override
    public void onLoseHp(int damageAmount)
    {
        super.onLoseHp(damageAmount);

        UpdateThreshold();
    }

    @Override
    public int onPlayerHeal(int healAmount)
    {
        UpdateThreshold();

        return super.onPlayerHeal(healAmount);
    }

    @Override
    public void onPlayerEndTurn()
    {
        super.onPlayerEndTurn();

        if (AbstractDungeon.player.currentHealth < counter)
        {
            GameActionsHelper.GainBlock(AbstractDungeon.player, BLOCK_AMOUNT);
            this.flash();
        }
    }

    private void UpdateThreshold()
    {
        GameActionsHelper.DelayedAction(this);
    }

    @Override
    public void OnCallback(Object state, AbstractGameAction action)
    {
        if (state == this && action != null)
        {
            AbstractPlayer p = AbstractDungeon.player;

            counter = (int) Math.ceil(AbstractDungeon.player.maxHealth * (HP_THRESHOLD / 100f));
            if (p.currentHealth < counter)
            {
                this.beginLongPulse();
            }
            else
            {
                this.stopPulse();
            }
        }
    }
}