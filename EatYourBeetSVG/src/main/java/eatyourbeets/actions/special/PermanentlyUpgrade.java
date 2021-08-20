package eatyourbeets.actions.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import eatyourbeets.actions.utility.GenericCardSelection;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class PermanentlyUpgrade extends GenericCardSelection
{
    public PermanentlyUpgrade(AbstractCard card)
    {
        super(card, null, 1);

        this.canCancel = false;
    }

    public PermanentlyUpgrade(CardGroup group, int amount)
    {
        super(null, group, amount);

        this.canCancel = false;
    }

    @Override
    protected void SelectCard(AbstractCard card)
    {
        super.SelectCard(card);

        for (AbstractCard c : GameUtilities.GetAllInBattleInstances(card.uuid))
        {
            if (c.canUpgrade())
            {
                c.upgrade();
                c.flash();
            }
        }

        final AbstractCard permanentCard = GameUtilities.GetMasterDeckInstance(card.uuid);
        if (permanentCard != null)
        {
            permanentCard.upgrade();
            player.bottledCardUpgradeCheck(card);

            final float x = (Settings.WIDTH / 4f) + ((result.size() - 1) * (AbstractCard.IMG_WIDTH * 0.75f));
            GameEffects.TopLevelQueue.Add(new UpgradeShineEffect(x, Settings.HEIGHT / 2f));
            GameEffects.TopLevelQueue.ShowCardBriefly(card.makeStatEquivalentCopy(), x, Settings.HEIGHT / 2f);
        }
    }

    @Override
    protected boolean CanSelect(AbstractCard card)
    {
        return card.canUpgrade() && super.CanSelect(card);
    }

    @Override
    protected void Complete(ArrayList<AbstractCard> result)
    {
        GameUtilities.RefreshHandLayout(false);

        super.Complete(result);
    }
}