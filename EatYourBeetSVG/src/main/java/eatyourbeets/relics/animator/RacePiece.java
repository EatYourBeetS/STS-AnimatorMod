package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorRuntimeLoadout;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.WeightedList;

import java.util.ArrayList;
import java.util.HashSet;

public class RacePiece extends AnimatorRelic
{
    public static final String ID = CreateFullID(RacePiece.class);

    private boolean awaitingInput;
    private CardSeries series;
    private int toSelect;

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
    public boolean canSpawn()
    {
        if (super.canSpawn())
        {
            HashSet<CardSeries> synergies = CardSeries.GetAllSynergies(player.masterDeck.group);
            for (AnimatorRuntimeLoadout loadout : GR.Animator.Dungeon.Loadouts)
            {
                if (synergies.contains(CardSeries.GetByID(loadout.ID)))
                {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void update()
    {
        super.update();

        if (awaitingInput)
        {
            if (series == null)
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
        CardGroup group = player.masterDeck.getPurgeableCards();

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
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : player.masterDeck.group)
        {
            AnimatorCard card = JUtils.SafeCast(c, AnimatorCard.class);
            if (card != null && card.series != null && card.color != AbstractCard.CardColor.COLORLESS)
            {
                group.addToTop(card);
            }
        }

        series = null;
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
            CardSeries.AddCards(series, CardLibrary.getAllCards(), cards);

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

            float displayCount = 0f;
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards)
            {
                AbstractCard reward = rewards.Retrieve(rng, false);
                if (reward != null)
                {
                    reward = reward.makeCopy();
                    if (c.upgraded)
                    {
                        reward.upgrade();
                    }

                    c.untip();
                    c.unhover();
                    player.masterDeck.removeCard(c);

                    GameEffects.TopLevelList.Add(new ShowCardAndObtainEffect(reward, (float) Settings.WIDTH / 3f + displayCount, (float)Settings.HEIGHT / 2f, false));
                    displayCount += (float)Settings.WIDTH / 6f;
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
            series = null;

            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AnimatorCard card = JUtils.SafeCast(c, AnimatorCard.class);
            if (card != null)
            {
                series = card.series;
            }

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            awaitingInput = series != null;
        }
    }
}