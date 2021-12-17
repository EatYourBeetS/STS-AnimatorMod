package pinacolada.actions.cardManipulation;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import pinacolada.actions.utility.GenericCardSelection;
import pinacolada.effects.vfx.megacritCopy.UpgradeShineEffect2;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;

public class UpgradeFromPile extends GenericCardSelection
{
    protected boolean permanent;

    public UpgradeFromPile(AbstractCard card)
    {
        super(card, null, 1);

        this.canCancel = false;
    }

    public UpgradeFromPile(CardGroup group, int amount)
    {
        super(null, group, amount);

        this.canCancel = false;
    }

    public UpgradeFromPile UpgradePermanently(boolean permanent)
    {
        this.permanent = permanent;

        return this;
    }

    @Override
    protected void SelectCard(AbstractCard card)
    {
        super.SelectCard(card);

        if (card.canUpgrade())
        {
            card.upgrade();
        }

        for (AbstractCard c : PCLGameUtilities.GetAllInBattleInstances(card.uuid))
        {
            if (c != card && c.canUpgrade())
            {
                c.upgrade();
            }
        }

        if (permanent)
        {
            final AbstractCard c = PCLGameUtilities.GetMasterDeckInstance(card.uuid);
            if (c != null)
            {
                if (c != card && c.canUpgrade())
                {
                    c.upgrade();
                }

                player.bottledCardUpgradeCheck(c);
            }
        }

        final float x = (Settings.WIDTH / 4f) + ((result.size() - 1) * (AbstractCard.IMG_WIDTH * 0.75f));
        PCLGameEffects.TopLevelQueue.Add(new UpgradeShineEffect2(x, Settings.HEIGHT / 2f));
        PCLGameEffects.TopLevelQueue.ShowCardBriefly(card.makeStatEquivalentCopy(), x, Settings.HEIGHT / 2f);
    }

    @Override
    protected boolean CanSelect(AbstractCard card)
    {
        return card.canUpgrade() && super.CanSelect(card);
    }

    @Override
    protected void Complete(ArrayList<AbstractCard> result)
    {
        PCLGameUtilities.RefreshHandLayout(false);

        super.Complete(result);
    }
}