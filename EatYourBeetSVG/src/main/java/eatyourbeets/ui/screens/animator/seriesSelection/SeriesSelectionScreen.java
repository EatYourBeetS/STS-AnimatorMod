package eatyourbeets.ui.screens.animator.seriesSelection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.interfaces.csharp.FuncT1;
import eatyourbeets.relics.animator.PurgingStone_Cards;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.AdvancedHitbox;
import eatyourbeets.ui.controls.*;
import eatyourbeets.ui.screens.AbstractScreen;
import eatyourbeets.utilities.RandomizedList;
import eatyourbeets.utilities.Testing;

import java.util.ArrayList;

public class SeriesSelectionScreen extends AbstractScreen
{
    private static final Random rng = new Random();
    private static boolean isBetaToggled = false;

    private final GUI_CardGrid cardGrid;
    private final GUI_Button deselectAll;
    private final GUI_Button selectRandom75;
    private final GUI_Button selectRandom100;
    private final GUI_Button selectAll;
    private final GUI_Button confirm;
    private final GUI_Toggle toggleBeta;
    private final GUI_TextBox selectionInfo;
    private final GUI_TextBox selectionAmount;
    private final GUI_Image purgingStoneImage;

    private final SeriesSelectionBuilder repository = new SeriesSelectionBuilder();
    private final ArrayList<AbstractCard> betaCards = new ArrayList<>();
    private final ArrayList<AbstractCard> promotedCards = new ArrayList<>();
    private final CardGroup cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    private int totalCardsCache = 0;

    public SeriesSelectionScreen()
    {
        final FuncT1<Float,Float> getY = (delta) -> GetHeight(0.78f) - GetHeight(0.08f * delta);

        final float buttonHeight = GetHeight(0.07f);
        final float buttonWidth = GetWidth(0.18f);
        final float xPos = GetWidth(0.82f);

        cardGrid = new GUI_CardGrid();

        toggleBeta = new GUI_Toggle(new AdvancedHitbox(xPos, getY.Invoke(0f), buttonWidth, buttonHeight * 0.8f))
        .SetText("Show Beta series.")
        .SetOnToggle(this::ToggleBetaSeries)
        .SetTexture(GR.Common.Textures.Panel, Color.DARK_GRAY);

        deselectAll = CreateHexagonalButton(xPos, getY.Invoke(1f), buttonWidth, buttonHeight)
        .SetText("Deselect All")
        .SetOnClick(this::DeselectAll)
        .SetColor(Color.FIREBRICK);

        selectRandom75 = CreateHexagonalButton(xPos, getY.Invoke(2f), buttonWidth, buttonHeight)
        .SetText("Random (75 cards)")
        .SetOnClick(() -> SelectRandom(75))
        .SetColor(Color.SKY);

        selectRandom100 = CreateHexagonalButton(xPos, getY.Invoke(3f), buttonWidth, buttonHeight)
        .SetText("Random (100 cards)")
        .SetOnClick(() -> SelectRandom(100))
        .SetColor(Color.SKY);

        selectAll = CreateHexagonalButton(xPos, getY.Invoke(4f), buttonWidth, buttonHeight)
        .SetText("Select All")
        .SetOnClick(this::SelectAll)
        .SetColor(Color.ROYAL);

        selectionAmount = new GUI_TextBox(GR.Common.Textures.Panel, new Hitbox(xPos, getY.Invoke(4.8f), buttonWidth, buttonHeight*0.8f))
        .SetColors(Color.DARK_GRAY, Settings.GOLD_COLOR)
        .SetAlignment(0.5f, true)
        .SetFont(FontHelper.charDescFont); //FontHelper.textAboveEnemyFont);

        float selectionAmountSize = selectionAmount.hb.height;
        purgingStoneImage = new GUI_Relic(new PurgingStone_Cards(), new AdvancedHitbox(selectionAmount.hb.x + (selectionAmountSize * 0.25f),
        selectionAmount.hb.y, selectionAmountSize, selectionAmountSize));

        final String message = "Select #ySeries for a total of #b75 or more cards to proceed. Obtain an additional starting #yRelic if you select at least #b100 cards.";
        selectionInfo = new GUI_TextBox(GR.Common.Textures.Panel, new Hitbox(xPos, getY.Invoke(7f), buttonWidth, buttonHeight*2.5f))
        .SetColors(Color.DARK_GRAY, Settings.CREAM_COLOR)
        .SetFont(FontHelper.tipBodyFont)
        .SetText(message);

        confirm = CreateHexagonalButton(xPos, getY.Invoke(8f), buttonWidth, buttonHeight*1.1f)
        .SetText("Proceed")
        .SetOnClick(() -> selectionAmount.SetColors(Color.DARK_GRAY, Testing.GetRandomColor()))
        .SetColor(Color.FOREST);
    }

    public void Open(boolean firstTime)
    {
        super.Open();

        betaCards.clear();
        promotedCards.clear();
        cardGroup.clear();

        for (SeriesSelectionCard c : repository.CreateCards())
        {
            if (c.promoted)
            {
                if (GR.Animator.Database.SelectedLoadout == c.loadout)
                {
                    promotedCards.add(c.card);
                }
                else
                {
                    promotedCards.add(0, c.card);
                }
            }
            else
            {
                if (c.loadout.IsBeta)
                {
                    betaCards.add(c.card);
                }
                else
                {
                    cardGroup.addToBottom(c.card);
                }

                Deselect(c.card);
            }
        }

        for (AbstractCard c : promotedCards)
        {
            cardGroup.addToBottom(c);
        }

        for (AbstractCard c : betaCards)
        {
            cardGroup.addToTop(c);
        }

        cardGrid.Open(cardGroup, this::OnCardClicked);

        ToggleBetaSeries(isBetaToggled);
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        cardGrid.Render(sb);

        if (betaCards.size() > 0)
        {
            toggleBeta.Render(sb);
        }

        deselectAll.Render(sb);
        selectRandom75.Render(sb);
        selectRandom100.Render(sb);
        selectAll.Render(sb);
        confirm.Render(sb);

        selectionInfo.Render(sb);
        selectionAmount.Render(sb);

        purgingStoneImage.TryRender(sb);
    }

    @Override
    public void Update()
    {
        if (totalCardsCache != repository.TotalCardsInPool)
        {
            totalCardsCache = repository.TotalCardsInPool;
            TotalCardsChanged(totalCardsCache);
        }

        if (betaCards.size() > 0)
        {
            toggleBeta.SetToggle(isBetaToggled).Update();
        }

        purgingStoneImage.TryUpdate();

        deselectAll.Update();
        selectRandom75.Update();
        selectRandom100.Update();
        selectAll.Update();
        confirm.Update();

        cardGrid.Update();
    }

    private void OnCardClicked(AbstractCard card)
    {
        SeriesSelectionCard c = repository.Find(card);
        if (c.promoted)
        {
            CardCrawlGame.sound.play("CARD_REJECT");
        }
        else if (repository.selectedCards.contains(card))
        {
            Deselect(card);
        }
        else
        {
            Select(card);
            CardCrawlGame.sound.play("CARD_SELECT");
        }
    }

    private void SelectRandom(int minimum)
    {
        RandomizedList<AbstractCard> toSelect = new RandomizedList<>();
        for (AbstractCard c : cardGroup.group)
        {
            Deselect(c);
            toSelect.Add(c);
        }

        while (repository.TotalCardsInPool < minimum)
        {
            Select(toSelect.Retrieve(rng));
        }
    }

    private void DeselectAll()
    {
        for (AbstractCard c : cardGroup.group)
        {
            Deselect(c);
        }
    }

    private void SelectAll()
    {
        for (AbstractCard c : cardGroup.group)
        {
            Select(c);
        }
    }

    private void Deselect(AbstractCard card)
    {
        if (repository.Deselect(card))
        {
            card.stopGlowing();
            card.targetTransparency = 0.66f;
        }
    }

    private void Select(AbstractCard card)
    {
        if (repository.Select(card))
        {
            card.beginGlowing();
            card.targetTransparency = 1f;
        }
    }

    private void ToggleBetaSeries(boolean value)
    {
        if (isBetaToggled = value)
        {
            for (AbstractCard card : betaCards)
            {
                cardGroup.addToTop(card);
                card.transparency = 0.01f;
            }
        }
        else
        {
            for (AbstractCard card : betaCards)
            {
                Deselect(card);
                cardGroup.removeCard(card);
            }
        }
    }

    private void TotalCardsChanged(int totalCards)
    {
        selectionAmount.SetText(totalCards + " cards selected.");
        purgingStoneImage.SetActive(totalCards >= 100);

        if (totalCards >= 75)
        {
            confirm.SetInteractable(true);
            selectionAmount.SetFontColor(Color.GREEN);
        }
        else
        {
            confirm.SetInteractable(false);
            selectionAmount.SetFontColor(Color.GRAY);
        }
    }
}
