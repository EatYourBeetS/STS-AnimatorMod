package pinacolada.relics.pcl;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.relics.PCLRelic;
import pinacolada.utilities.PCLJUtils;

public class ShinigamisFerry extends PCLRelic
{
    public static final String ID = CreateFullID(ShinigamisFerry.class);

    public ShinigamisFerry()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.MAGICAL);
        this.counter = 1;
        fixDescription();
    }

    @Override
    public void onTrigger()
    {
        super.onTrigger();

        this.counter--;
        this.flash();
        fixDescription();
    }

    @Override
    public void instantObtain()
    {
        if (player.hasRelic(ID))
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
        if (player.hasRelic(ID))
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
        if (player.hasRelic(ID))
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
        ShinigamisFerry ferry = PCLJUtils.SafeCast(player.getRelic(ID), ShinigamisFerry.class);
        if (ferry != null)
        {
            ferry.counter++;
            ferry.flash();
            ferry.fixDescription();
        }
    }

    private void fixDescription()
    {
        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PCLCardTooltip(this.name, this.description));
        this.initializeTips();
    }

    @Override
    public String getUpdatedDescription()
    {
        if (CardCrawlGame.isInARun()) {
            return PCLJUtils.Format(DESCRIPTIONS[0], counter);
        } else {
            return PCLJUtils.Format(DESCRIPTIONS[0], 1);
        }
    }
}