package eatyourbeets.relics.animator.beta;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.JavaUtilities;

public class ShinigamisFerry extends AnimatorRelic
{
    public static final String ID = CreateFullID(ShinigamisFerry.class);

    public ShinigamisFerry()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.MAGICAL);
        this.counter = 1;
    }

    @Override
    public void onTrigger()
    {
        super.onTrigger();

        this.counter--;
        this.flash();
        updateDescription(null);
    }

    @Override
    public void instantObtain()
    {
        if (AbstractDungeon.player.hasRelic(ID))
        {
            increment();
        }
        else
        {
            super.instantObtain();
        }
    }

    @Override
    public void instantObtain(AbstractPlayer p, int slot, boolean callOnEquip)
    {
        if (AbstractDungeon.player.hasRelic(ID))
        {
            increment();

            isDone = true;
            isObtained = true;
            discarded = true;
        }
        else
        {
            super.instantObtain(p, slot, callOnEquip);
        }
    }

    @Override
    public void obtain()
    {
        if (AbstractDungeon.player.hasRelic(ID))
        {
            increment();
        }
        else
        {
            super.obtain();
        }
    }

    private void increment()
    {
        ShinigamisFerry ferry = JavaUtilities.SafeCast(AbstractDungeon.player.getRelic(ID), ShinigamisFerry.class);
        if (ferry != null)
        {
            ferry.counter++;
            ferry.flash();
            ferry.description = ferry.getUpdatedDescription();
        }
    }

    @Override
    public String getUpdatedDescription()
    {
        return JavaUtilities.Format(DESCRIPTIONS[0], counter);
    }
}