package eatyourbeets.ui.animator.seriesSelection;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorRuntimeLoadout;
import eatyourbeets.ui.controls.GUI_CardGrid;

import java.util.ArrayDeque;

public class AnimatorSeriesSelectEffect extends EYBEffect
{
    private final ArrayDeque<AbstractCard> toAdd = new ArrayDeque<>(20);
    private final AnimatorLoadoutsContainer container;
    private final AnimatorSeriesSelectScreen screen;
    private final GUI_CardGrid grid;

    public AnimatorSeriesSelectEffect(AnimatorSeriesSelectScreen screen)
    {
        super(Settings.ACTION_DUR_FAST, true);

        this.screen = screen;
        this.container = screen.container;
        this.grid = screen.cardGrid;
        this.grid.Clear();
    }

    @Override
    protected void FirstUpdate()
    {
        SetInteractable(false);

        container.CreateCards();

        for (AnimatorRuntimeLoadout c : container.cardsMap.values())
        {
            if (c.promoted)
            {
                if (GR.Animator.Data.SelectedLoadout == c.Loadout)
                {
                    container.promotedCards.add(c.card);
                }
                else
                {
                    container.promotedCards.add(0, c.card);
                }
            }
            else
            {
                if (c.Loadout.IsBeta)
                {
                    container.betaCards.add(c.card);
                }
                else
                {
                    container.allCards.add(c.card);
                }

                c.card.targetTransparency = 0.66f;
            }

            c.card.transparency = 0.01f;
        }

        for (AbstractCard c : container.promotedCards)
        {
            container.allCards.add(0, c);
        }

        if (AnimatorSeriesSelectScreen.isBetaToggled)
        {
            container.allCards.addAll(container.betaCards);
        }

        toAdd.addAll(container.allCards);
    }

    @Override
    protected void UpdateInternal()
    {
        tickDuration();

        if (isDone)
        {
            if (toAdd.size() > 0)
            {
                grid.AddCard(toAdd.pop());

                if (grid.cards.size() <= 3)
                {
                    CardCrawlGame.sound.playA("CARD_SELECT", grid.cards.size() * 0.1f);
                    duration = 0.2f;
                }
                else
                {
                    duration = 0.05f;
                }

                isDone = false;
            }
            else
            {
                SetInteractable(true);

                for (AbstractCard c : container.promotedCards)
                {
                    screen.Select(c);
                }

                screen.toggleBeta.isActive = container.betaCards.size() > 0;
            }
        }
    }

    protected void SetInteractable(boolean value)
    {
        screen.selectAll.SetInteractable(value);
        screen.deselectAll.SetInteractable(value);
        screen.selectRandom75.SetInteractable(value);
        screen.selectRandom100.SetInteractable(value);
        screen.toggleBeta.SetInteractable(value);
    }
}
