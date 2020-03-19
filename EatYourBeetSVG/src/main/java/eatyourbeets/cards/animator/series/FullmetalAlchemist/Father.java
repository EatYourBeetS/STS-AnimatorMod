package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PhilosopherStone;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnAddedToDeckSubscriber;
import eatyourbeets.interfaces.subscribers.OnAddingToCardReward;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Father extends AnimatorCard implements OnAddedToDeckSubscriber, OnAddingToCardReward
{
    private static final AbstractRelic relic = new PhilosopherStone();
    private static final EYBCardTooltip tooltip = new EYBCardTooltip(relic.name, relic.description);

    public static final EYBCardData DATA = Register(Father.class).SetSkill(4, CardRarity.RARE, EYBCardTarget.None).SetMaxCopies(1);

    public Father()
    {
        super(DATA);

        Initialize(0, 0, 0, 50);
        SetUpgrade(0, 0, 0, -8);
        SetCostUpgrade(-1);

        SetHealing(true);
        SetPurge(true);
        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void initializeDescription()
    {
        super.initializeDescription();

        if (cardText != null)
        {
            tooltips.add(tooltip);
        }
    }

    @Override
    public void OnAddedToDeck()
    {
        GR.Animator.Dungeon.BannedCards.add(cardData.ID);
        AbstractDungeon.rareCardPool.removeCard(cardID);
        AbstractDungeon.srcRareCardPool.removeCard(cardID);
    }

    @Override
    public boolean ShouldCancel(RewardItem rewardItem)
    {
        OnAddedToDeck();

        return AbstractDungeon.actNum < 4 && AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(PhilosopherStone.ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (!p.hasRelic(relic.relicId))
        {
            p.decreaseMaxHealth((int)Math.ceil(p.maxHealth * (secondaryValue / 100f)));
            GameActions.Bottom.VFX(new OfferingEffect(), 0.5F);
            GameActions.Bottom.Callback(() -> GameEffects.Queue.SpawnRelic(relic.makeCopy(), current_x, current_y));
            AbstractDungeon.bossRelicPool.remove(relic.relicId);

            p.energy.energy += 1;
        }

        //noinspection StatementWithEmptyBody
        while (p.masterDeck.removeCard(cardID));

        for (AbstractCard card : GameUtilities.GetAllInBattleCopies(cardID))
        {
            GameActions.Bottom.Purge(card);
        }
    }
}