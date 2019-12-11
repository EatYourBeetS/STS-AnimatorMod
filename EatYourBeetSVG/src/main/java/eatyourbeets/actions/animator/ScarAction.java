package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.actions.basic.RefreshHandLayoutAction;
import eatyourbeets.utilities.GameActions;

public class ScarAction extends AbstractGameAction
{
    private final AbstractPlayer player;

    public ScarAction(AbstractPlayer player)
    {
        this.player = player;
    }

    public void update()
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
                AbstractDungeon.effectsQueue.add(new UpgradeShineEffect((float) Settings.WIDTH / 4.0F, (float) Settings.HEIGHT / 2.0F));
                AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(toUpgrade.makeStatEquivalentCopy(), (float) Settings.WIDTH / 4.0F, (float) Settings.HEIGHT / 2.0F));
            }
        }

        GameActions.Bottom.Add(new RefreshHandLayoutAction());

        this.isDone = true;
    }
}