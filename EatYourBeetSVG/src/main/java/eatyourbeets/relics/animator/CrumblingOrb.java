package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.*;

public class CrumblingOrb extends AnimatorRelic
{
    public static final String ID = CreateFullID(CrumblingOrb.class);
    public static final int CHOOSE_AMOUNT = 2;
    public static final int CHOOSE_SIZE = 4;

    public CrumblingOrb()
    {
        super(ID, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, CHOOSE_AMOUNT, CHOOSE_SIZE);
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        player.energy.energyMaster += 1;
    }

    @Override
    public void onUnequip()
    {
        super.onUnequip();

        player.energy.energyMaster -= 1;
    }

    @Override
    public void atPreBattle()
    {
        super.atPreBattle();

        if (!GameUtilities.InBossRoom() && !GameUtilities.InEliteRoom())
        {
            return;
        }

        final WeightedList<AbstractCard> list1 = new WeightedList<>();
        final WeightedList<AbstractCard> list2 = new WeightedList<>();
        for (AbstractCard card : player.masterDeck.getPurgeableCards().group)
        {
            boolean alreadyAdded = false;
            for (AbstractCard c : list1.GetInnerList())
            {
                if (c.cardID.equals(card.cardID))
                {
                    alreadyAdded = true;
                    break;
                }
            }

            int weight = 1;
            if (!GameUtilities.IsHindrance(card))
            {
                weight += 2;
            }
            if (card.rarity != AbstractCard.CardRarity.BASIC)
            {
                weight += 1;
            }

            if (alreadyAdded)
            {
                list2.Add(card, weight);
            }
            else
            {
                list1.Add(card, weight);
            }
        }

        final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        while (group.size() < CHOOSE_SIZE && list1.Size() > 0)
        {
            group.group.add(list1.Retrieve(rng, true));
        }
        while (group.size() < CHOOSE_SIZE && list2.Size() > 0)
        {
            group.group.add(list2.Retrieve(rng, true));
        }

        GameActions.Top.SelectFromPile(name, CHOOSE_AMOUNT, group)
        .SetOptions(false, false)
        .CancellableFromPlayer(false)
        .AddCallback(cards ->
        {
            float x_offset = 0;
            for (AbstractCard c : cards)
            {
                AbstractCard replacement = GR.Common.Dungeon.GetRandomRewardCard(cards, true, true);
                if (replacement != null)
                {
                    replacement = replacement.makeCopy();
                    AbstractDungeon.player.masterDeck.removeCard(c);

                    if (c.upgraded)
                    {
                        replacement.upgrade();
                    }

                    GameEffects.TopLevelQueue.ShowAndObtain(replacement, (float) Settings.WIDTH / 3f + x_offset, (float) Settings.HEIGHT / 2f, false);
                    GameActions.Top.ReplaceCard(c.uuid, replacement);
                    x_offset += (float) Settings.WIDTH / 6f;
                }
            }
        });
    }
}