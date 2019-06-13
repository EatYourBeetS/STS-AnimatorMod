package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.actions.common.RefreshHandLayoutAction;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.animator.Scar;

public class ScarAction extends AnimatorAction
{
    private final AbstractPlayer player;
    private final Scar scar;

    public ScarAction(AbstractPlayer player, Scar scar)
    {
        this.player = player;
        this.scar = scar;
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

        scar.purgeOnUse = true;
        GameActionsHelper.AddToBottom(new RefreshHandLayoutAction());

        this.isDone = true;
    }
}