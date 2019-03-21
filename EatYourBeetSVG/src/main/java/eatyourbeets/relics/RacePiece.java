package eatyourbeets.relics;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.SoulboundField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.curses.AscendersBane;
import com.megacrit.cardcrawl.cards.curses.Necronomicurse;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import eatyourbeets.Utilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.cards.Synergy;
import eatyourbeets.misc.WeightedList;

import java.util.ArrayList;

public class RacePiece extends AnimatorRelic
{
    public static final String ID = CreateFullID(RacePiece.class.getSimpleName());

    private int toSelect;
    private Synergy synergy;
    private boolean awaitingInput;

    public RacePiece()
    {
        super(ID, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        toSelect = 0;
        OpenSynergySelection();
    }

    @Override
    public void update()
    {
        super.update();

        if (awaitingInput)
        {
            if (synergy == null)
            {
                UpdateSynergy();
            }
            else
            {
                if (toSelect > 0)
                {
                    UpdateTransform();
                }
                else
                {
                    OpenTransformSelection();
                }
            }
        }
    }

    private String GetGridSelectMessage()
    {
        return DESCRIPTIONS[0].replace("#b","").replace("#y", "");
    }

    private void OpenTransformSelection()
    {
        AbstractPlayer p = AbstractDungeon.player;
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : p.masterDeck.group)
        {
            if (!SoulboundField.soulbound.get(c) && !c.cardID.equals(Necronomicurse.ID) && !c.cardID.equals(AscendersBane.ID))
            {
                group.addToTop(c);
            }
        }

        if (group.size() > 0)
        {
            toSelect = Math.min(3, group.size());
            awaitingInput = true;
        }
        else
        {
            awaitingInput = false;
            return;
        }

        if (!AbstractDungeon.isScreenUp)
        {
            AbstractDungeon.gridSelectScreen.open(group, toSelect, GetGridSelectMessage(), false, false, false, false);
        }
        else
        {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
            AbstractDungeon.gridSelectScreen.open(group, toSelect, GetGridSelectMessage(), false, false, false, false);
        }
    }

    private void OpenSynergySelection()
    {
        AbstractPlayer p = AbstractDungeon.player;
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : p.masterDeck.group)
        {
            AnimatorCard card = Utilities.SafeCast(c, AnimatorCard.class);
            if (card != null && card.GetSynergy() != null)
            {
                group.addToTop(card);
            }
        }

        synergy = null;
        if (group.size() > 0)
        {
            awaitingInput = true;
        }
        else
        {
            awaitingInput = false;
            return;
        }

        if (!AbstractDungeon.isScreenUp)
        {
            AbstractDungeon.gridSelectScreen.open(group, 1, GetGridSelectMessage(), false, false, false, false);
        }
        else
        {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
            AbstractDungeon.gridSelectScreen.open(group, 1, GetGridSelectMessage(), false, false, false, false);
        }
    }

    private void UpdateTransform()
    {
        if (AbstractDungeon.gridSelectScreen.selectedCards.size() >= toSelect)
        {
            WeightedList<AnimatorCard> rewards = new WeightedList<>();

            ArrayList<AnimatorCard> cards = new ArrayList<>();
            Synergies.AddCardsWithSynergy(synergy, CardLibrary.getAllCards(), cards);

            for (AnimatorCard c : cards)
            {
                if (c.type != AbstractCard.CardType.CURSE && c.type != AbstractCard.CardType.STATUS)
                {
                    switch (c.rarity)
                    {
                        case COMMON:
                            rewards.Add(c, 45);
                            break;

                        case UNCOMMON:
                            rewards.Add(c, 40);
                            break;

                        case RARE:
                            rewards.Add(c, 15);
                            break;
                    }
                }
            }

            float displayCount = 0.0F;
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards)
            {
                AbstractCard reward = rewards.Retrieve(AbstractDungeon.miscRng, false);
                if (reward != null)
                {
                    reward = reward.makeCopy();
                    if (c.upgraded)
                    {
                        reward.upgrade();
                    }

                    c.untip();
                    c.unhover();
                    AbstractDungeon.player.masterDeck.removeCard(c);

                    //if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.TRANSFORM)
                    //{
                        AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(reward, (float) Settings.WIDTH / 3.0F + displayCount, (float)Settings.HEIGHT / 2.0F, false));
                        displayCount += (float)Settings.WIDTH / 6.0F;
                    //}
                }
            }

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            awaitingInput = false;
        }
    }

    private void UpdateSynergy()
    {
        if (AbstractDungeon.gridSelectScreen.selectedCards.size() > 0)
        {
            synergy = null;

            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AnimatorCard card = Utilities.SafeCast(c, AnimatorCard.class);
            if (card != null)
            {
                synergy = card.GetSynergy();
            }

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            awaitingInput = synergy != null;
        }
    }
}