package eatyourbeets.actions;

import com.megacrit.cardcrawl.actions.utility.ShowCardAndPoofAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.animator.Scar;
import eatyourbeets.powers.PlayerStatistics;

import java.util.UUID;

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
        CardGroup ugradableCards = player.masterDeck.getUpgradableCards();
        if (ugradableCards.size() > 0)
        {
            AbstractCard toUpgrade = ugradableCards.getRandomCard(true);
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

        this.isDone = true;
    }
}