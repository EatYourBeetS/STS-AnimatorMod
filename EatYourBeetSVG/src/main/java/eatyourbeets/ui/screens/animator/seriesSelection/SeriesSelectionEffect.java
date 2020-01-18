package eatyourbeets.ui.screens.animator.seriesSelection;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorRuntimeLoadout;
import eatyourbeets.ui.controls.GUI_CardGrid;

import java.util.ArrayDeque;

public class SeriesSelectionEffect extends EYBEffect
{
    private final ArrayDeque<AbstractCard> toAdd = new ArrayDeque<>(20);
    private final SeriesSelectionProvider repository;
    private final SeriesSelectionScreen screen;
    private final GUI_CardGrid grid;

    public SeriesSelectionEffect(SeriesSelectionScreen screen)
    {
        super(Settings.ACTION_DUR_FAST, true);

        this.screen = screen;
        this.repository = screen.repository;
        this.grid = screen.cardGrid;
        this.grid.Clear();
    }

    @Override
    protected void FirstUpdate()
    {
        SetInteractable(false);

        repository.CreateCards();

        for (AnimatorRuntimeLoadout c : repository.cardsMap.values())
        {
            if (c.promoted)
            {
                if (GR.Animator.Data.SelectedLoadout == c.Loadout)
                {
                    repository.promotedCards.add(c.card);
                }
                else
                {
                    repository.promotedCards.add(0, c.card);
                }
            }
            else
            {
                if (c.Loadout.IsBeta)
                {
                    repository.betaCards.add(c.card);
                }
                else
                {
                    repository.allCards.add(c.card);
                }

                c.card.targetTransparency = 0.66f;
            }

            c.card.transparency = 0.01f;
        }

        for (AbstractCard c : repository.promotedCards)
        {
            repository.allCards.add(0, c);
        }

        if (SeriesSelectionScreen.isBetaToggled)
        {
            repository.allCards.addAll(repository.betaCards);
        }

        toAdd.addAll(repository.allCards);
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

                for (AbstractCard c : repository.promotedCards)
                {
                    screen.Select(c);
                }
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
