package eatyourbeets.ui;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.characters.UnnamedCharacter;
import eatyourbeets.interfaces.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.interfaces.OnVoidTurnStartSubscriber;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.JavaUtilities;
import patches.EnergyPanelPatches;

public class Void extends CardGroup implements OnStartOfTurnPostDrawSubscriber
{
    private final VoidEnergyOrb energyOrb = new VoidEnergyOrb(this);

    public Void()
    {
        super(CardGroupType.UNSPECIFIED);
    }

    public void Initialize(boolean firstTime)
    {
        if (!firstTime || AbstractDungeon.player instanceof UnnamedCharacter)
        {
            EnergyPanelPatches.SetOrb(energyOrb);

            PlayerStatistics.onStartOfTurnPostDraw.Subscribe(this);
        }
        else
        {
            EnergyPanelPatches.SetOrb(null);
        }
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        energyOrb.refill();

        for (AbstractCard c : group)
        {
            OnVoidTurnStartSubscriber card = JavaUtilities.SafeCast(c, OnVoidTurnStartSubscriber.class);
            if (card != null)
            {
                card.OnVoidTurnStart();
            }
        }
    }

    public boolean CanUse(AbstractCard card)
    {
        UnnamedCard c = JavaUtilities.SafeCast(card, UnnamedCard.class);
        if (c != null)
        {
            return CanUse(c);
        }

        return true;
    }

    public void UseMastery(AbstractCard card)
    {
        UnnamedCard c = JavaUtilities.SafeCast(card, UnnamedCard.class);
        if (c != null)
        {
            UseMastery(c);
        }
    }

    public boolean CanUse(UnnamedCard card)
    {
        return card.masteryCost <= energyOrb.currentEnergy;
    }

    public void UseMastery(UnnamedCard card)
    {
        energyOrb.useEnergy(card.masteryCost);
    }
}
