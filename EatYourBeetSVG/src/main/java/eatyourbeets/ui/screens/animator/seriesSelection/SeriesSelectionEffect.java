package eatyourbeets.ui.screens.animator.seriesSelection;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.controls.GUI_CardGrid;

import java.util.ArrayList;

public class SeriesSelectionEffect extends EYBEffect
{
    private final ArrayList<AbstractCard> toAdd = new ArrayList<>();
    private final SeriesSelectionProvider repository;
    private final SeriesSelectionScreen screen;
    private final GUI_CardGrid grid;

    public SeriesSelectionEffect(SeriesSelectionScreen screen)
    {
        super(1, Settings.ACTION_DUR_FAST, true);

        this.screen = screen;
        this.repository = screen.repository;
        this.grid = screen.cardGrid;
        this.grid.cards.clear();
        this.grid.Refresh();
    }

    @Override
    protected void FirstUpdate()
    {
        SetInteractable(false);

        repository.CreateCards();

        for (SeriesSelectionItem c : repository.cardsMap.values())
        {
            if (c.promoted)
            {
                if (GR.Animator.Database.SelectedLoadout == c.loadout)
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
                if (c.loadout.IsBeta)
                {
                    repository.betaCards.add(c.card);
                }
                else
                {
                    repository.allCards.add(c.card);
                }

                c.card.targetTransparency = 0.66f;
            }
        }

        for (AbstractCard c : repository.promotedCards)
        {
            repository.allCards.add(0, c);
            screen.Select(c);
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
                grid.cards.add(toAdd.remove(0));

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
