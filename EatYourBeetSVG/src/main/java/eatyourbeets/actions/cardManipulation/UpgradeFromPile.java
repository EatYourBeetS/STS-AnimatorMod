package eatyourbeets.actions.cardManipulation;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.utility.GenericCardSelection;
import eatyourbeets.effects.vfx.megacritCopy.UpgradeShineEffect2;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class UpgradeFromPile extends GenericCardSelection
{
    protected boolean permanent;
    protected boolean playVFX;

    public UpgradeFromPile(AbstractCard card)
    {
        super(card, null, 1);

        this.canCancel = false;
        this.playVFX = true;
    }

    public UpgradeFromPile(CardGroup group, int amount)
    {
        super(null, group, amount);

        this.canCancel = false;
        this.playVFX = true;
    }

    public UpgradeFromPile ShowVFX(boolean playVFX)
    {
        this.playVFX = playVFX;

        return this;
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

        for (AbstractCard c : GameUtilities.GetAllInBattleInstances(card.uuid))
        {
            if (c != card && c.canUpgrade())
            {
                c.upgrade();
            }
        }

        if (permanent)
        {
            final AbstractCard c = GameUtilities.GetMasterDeckInstance(card.uuid);
            if (c != null)
            {
                if (c != card && c.canUpgrade())
                {
                    c.upgrade();
                }

                player.bottledCardUpgradeCheck(c);
            }
        }

        final float x;
        if (group == null || group.type == CardGroup.CardGroupType.DRAW_PILE)
        {
            x = (Settings.WIDTH * 0.25f) + ((result.size() - 1) * (AbstractCard.IMG_WIDTH * 0.75f));
        }
        else
        {
            x = (Settings.WIDTH * 0.75f) - ((result.size() - 1) * (AbstractCard.IMG_WIDTH * 0.75f));
        }

        if (playVFX)
        {
            GameEffects.TopLevelQueue.Add(new UpgradeShineEffect2(x, Settings.HEIGHT / 2f));
        }

        GameEffects.TopLevelQueue.ShowCardBriefly(card.makeStatEquivalentCopy(), x, Settings.HEIGHT / 2f);
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