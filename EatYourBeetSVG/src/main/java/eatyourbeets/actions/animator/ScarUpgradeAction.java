package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class ScarUpgradeAction extends EYBAction
{
    public ScarUpgradeAction()
    {
        super(ActionType.SPECIAL);

        this.canCancel = false;
    }

    @Override
    protected void FirstUpdate()
    {
        CardGroup upgradableCards = player.masterDeck.getUpgradableCards();
        if (upgradableCards.size() > 0)
        {
            AbstractCard toUpgrade = upgradableCards.getRandomCard(true);
            if (toUpgrade != null)
            {
                for (AbstractCard c : GetAllInBattleInstances.get(toUpgrade.uuid))
                {
                    if (c.canUpgrade())
                    {
                        c.upgrade();
                        c.flash();
                    }
                }

                toUpgrade.upgrade();
                player.bottledCardUpgradeCheck(toUpgrade);
                GameEffects.Queue.Add(new UpgradeShineEffect(Settings.WIDTH / 4f, Settings.HEIGHT / 2f));
                GameEffects.Queue.ShowCardBriefly(toUpgrade.makeStatEquivalentCopy(), Settings.WIDTH / 4f, Settings.HEIGHT / 2f);
            }
        }

        GameActions.Bottom.Add(new RefreshHandLayout());
        Complete();
    }
}