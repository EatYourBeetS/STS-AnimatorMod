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
import eatyourbeets.resources.GR;
import eatyourbeets.ui.screens.AbstractScreen;
import eatyourbeets.ui.controls.*;
import eatyourbeets.utilities.RandomizedList;
import eatyourbeets.utilities.RenderHelpers;
import eatyourbeets.utilities.Testing;
import eatyourbeets.ui.controls.TextureRenderer;

import java.util.ArrayList;

public class SeriesSelectionScreen extends AbstractScreen
{
    private final GridLayoutControl gridLayoutControl = new GridLayoutControl();
    private final GenericButton deselectAll;
    private final GenericButton selectRandom75;
    private final GenericButton selectRandom100;
    private final GenericButton selectAll;
    private final GenericButton confirm;
    private final ToggleButton toggleBeta;
    private final TextBox infoTextBox;
    private final TextBox selectedAmountTextBox;

    private TextureRenderer purgingStoneRenderer;

    private final Random rng = new Random();
    private final SeriesSelectionBuilder repository = new SeriesSelectionBuilder();
    private final ArrayList<AbstractCard> betaCards = new ArrayList<>();
    private final ArrayList<AbstractCard> promotedCards = new ArrayList<>();
    private final CardGroup cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    private boolean isBetaToggled = false;
    private int totalCardsCache = 0;

    public SeriesSelectionScreen()
    {
        final FuncT1<Float,Float> getY = (delta) -> (Settings.HEIGHT * 0.78f - (Settings.HEIGHT * 0.08f * delta));
        final float xPos = Settings.WIDTH * 0.82f;
        final float buttonWidth = Settings.WIDTH * 0.18f;
        final float buttonHeight = Settings.HEIGHT * 0.07f;

        toggleBeta = new ToggleButton(new AdvancedHitbox(xPos, getY.Invoke(0f), buttonWidth, buttonHeight * 0.8f))
        .SetText("Show Beta series.")
        .SetOnToggle(this::ToggleBetaSeries)
        .SetTexture(GR.Common.Textures.Panel, Color.DARK_GRAY);

        deselectAll = CreateButton(xPos, getY.Invoke(1f), buttonWidth, buttonHeight)
        .SetText("Deselect All")
        .SetOnClick(this::DeselectAll)
        .SetColor(Color.FIREBRICK);

        selectRandom75 = CreateButton(xPos, getY.Invoke(2f), buttonWidth, buttonHeight)
        .SetText("Random (75 cards)")
        .SetOnClick(() -> SelectRandom(75))
        .SetColor(Color.SKY);

        selectRandom100 = CreateButton(xPos, getY.Invoke(3f), buttonWidth, buttonHeight)
        .SetText("Random (100 cards)")
        .SetOnClick(() -> SelectRandom(100))
        .SetColor(Color.SKY);

        selectAll = CreateButton(xPos, getY.Invoke(4f), buttonWidth, buttonHeight)
        .SetText("Select All")
        .SetOnClick(this::SelectAll)
        .SetColor(Color.ROYAL);

        final String message = "Select #ySeries for a total of #b75 or more cards to proceed. Obtain an additional starting #yRelic if you select at least #b100 cards.";
        infoTextBox = new TextBox(GR.Common.Textures.Panel, new Hitbox(xPos, getY.Invoke(6.3f), buttonWidth, buttonHeight*2.5f))
        .SetColors(Color.DARK_GRAY, Settings.CREAM_COLOR)
        .SetFont(FontHelper.tipBodyFont)
        .SetText(message);

        selectedAmountTextBox = new TextBox(GR.Common.Textures.Panel, new Hitbox(xPos, getY.Invoke(7f), buttonWidth, buttonHeight*0.8f))
        .SetColors(Color.DARK_GRAY, Settings.GOLD_COLOR)
        .SetAlignment(0.5f, true)
        .SetFont(FontHelper.charDescFont); //FontHelper.textAboveEnemyFont);

        purgingStoneRenderer = RenderHelpers.ForTexture(GR.GetTexture(GR.GetRelicImage(GR.Animator.CreateID("PurgingStone"))))
        .SetHitbox(new Hitbox(xPos, getY.Invoke(6.5f), buttonHeight*0.8f, buttonHeight*0.8f));

        confirm = CreateButton(xPos, getY.Invoke(8f), buttonWidth, buttonHeight*1.1f)
        .SetText("Proceed")
        .SetOnClick(() -> selectedAmountTextBox.SetColors(Color.DARK_GRAY, Testing.GetRandomColor()))
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

        gridLayoutControl.Open(cardGroup, this::OnCardClicked);

        ToggleBetaSeries(isBetaToggled);
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        gridLayoutControl.Render(sb);

        if (betaCards.size() > 0)
        {
            toggleBeta.Render(sb);
        }

        deselectAll.Render(sb);
        selectRandom75.Render(sb);
        selectRandom100.Render(sb);
        selectAll.Render(sb);
        confirm.Render(sb);

        infoTextBox.Render(sb);
        selectedAmountTextBox.Render(sb);

        if (totalCardsCache >= 100)
        {
            purgingStoneRenderer.Draw(sb);
        }
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

        deselectAll.Update();
        selectRandom75.Update();
        selectRandom100.Update();
        selectAll.Update();
        confirm.Update();

        gridLayoutControl.Update();
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
        selectedAmountTextBox.SetText(totalCards + " cards selected.");
        if (totalCards >= 100)
        {
            confirm.SetInteractable(true);
            selectedAmountTextBox.SetFontColor(Color.GREEN);
        }
        else if (totalCards >= 75)
        {
            confirm.SetInteractable(true);
            selectedAmountTextBox.SetFontColor(Color.LIME);
        }
        else
        {
            confirm.SetInteractable(false);
            selectedAmountTextBox.SetFontColor(Color.GRAY);
        }
    }
}
