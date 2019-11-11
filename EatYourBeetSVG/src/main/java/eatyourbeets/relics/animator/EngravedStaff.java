package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.effects.CallbackEffect;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.RandomizedList;

public class EngravedStaff extends AnimatorRelic
{
    public static final String ID = CreateFullID(EngravedStaff.class.getSimpleName());

    public EngravedStaff()
    {
        super(ID, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        EffectHistory.TryActivateLimited(relicId);
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        EffectHistory.TryActivateLimited(relicId);
        AbstractDungeon.effectsQueue.add(new CallbackEffect(new WaitAction(0.1f), this::OnCompletion, this));
    }

    private void OnCompletion(Object state, AbstractGameAction action)
    {
        if (state == this && action != null)
        {
            RandomizedList<AbstractCard> upgradableCards = new RandomizedList<>();
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
            {
                if (c.rarity == AbstractCard.CardRarity.RARE && c.canUpgrade())
                {
                    upgradableCards.Add(c);
                }
            }

            if (upgradableCards.Count() > 0)
            {
                AbstractCard card1 = upgradableCards.Retrieve(AbstractDungeon.cardRandomRng);
                card1.upgrade();
                AbstractDungeon.player.bottledCardUpgradeCheck(card1);
                AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(card1.makeStatEquivalentCopy(), (float) Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F + 20.0F * Settings.scale, (float) Settings.HEIGHT / 2.0F));
                AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
            }
        }
    }
}