package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class CrumblingOrb extends AnimatorRelic
{
    public static final String ID = CreateFullID(CrumblingOrb.class);

    public CrumblingOrb()
    {
        super(ID, RelicTier.BOSS, LandingSound.MAGICAL);
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

        boolean basic = true;
        RandomizedList<AbstractCard> randomList = new RandomizedList<>();
        for (AbstractCard card : player.masterDeck.group)
        {
            if (!GameUtilities.IsCurseOrStatus(card))
            {
                if (basic || card.rarity != AbstractCard.CardRarity.BASIC)
                {
                    randomList.Add(card);
                    basic = false;
                }
            }
        }

        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        while (group.size() < 3 && randomList.Size() > 0)
        {
            group.addToBottom(randomList.Retrieve(rng, true));
        }

        GameActions.Top.SelectFromPile(name, 2, group)
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

                    GameEffects.TopLevelQueue.Add(new ShowCardAndObtainEffect(replacement, (float) Settings.WIDTH / 3f + x_offset, (float) Settings.HEIGHT / 2f, false));
                    GameActions.Top.ReplaceCard(c.uuid, replacement);
                    x_offset += (float) Settings.WIDTH / 6f;
                }
            }
        });
    }
}