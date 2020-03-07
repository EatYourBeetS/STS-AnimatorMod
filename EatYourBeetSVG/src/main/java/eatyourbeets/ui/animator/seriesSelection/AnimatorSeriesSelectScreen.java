package eatyourbeets.ui.animator.seriesSelection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.relics.animator.PurgingStone;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorRuntimeLoadout;
import eatyourbeets.ui.AbstractScreen;
import eatyourbeets.ui.controls.*;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.RandomizedList;

public class AnimatorSeriesSelectScreen extends AbstractScreen
{
    protected static final Random rng = new Random();
    protected int totalCardsCache = 0;

    public final AnimatorLoadoutsContainer container = new AnimatorLoadoutsContainer();
    public final GUI_CardGrid cardGrid;
    public final GUI_Button deselectAll;
    public final GUI_Button selectRandom75;
    public final GUI_Button selectRandom100;
    public final GUI_Button selectAll;
    public final GUI_Button confirm;
    public final GUI_Toggle toggleBeta;
    public final GUI_TextBox selectionInfo;
    public final GUI_TextBox selectionAmount;
    public final GUI_Image purgingStoneImage;

    public AnimatorSeriesSelectScreen()
    {
        final Texture panelTexture = GR.Common.Images.Panel.Texture();
        final FuncT1<Float,Float> getY = (delta) -> ScreenH(0.78f) - ScreenH(0.08f * delta);
        final float buttonHeight = ScreenH(0.07f);
        final float buttonWidth = ScreenW(0.18f);
        final float xPos = ScreenW(0.82f);

        cardGrid = new GUI_CardGrid()
        .SetOnCardClick(this::OnCardClicked);

        toggleBeta = new GUI_Toggle(new Hitbox(xPos, getY.Invoke(0f), buttonWidth, buttonHeight * 0.8f))
        .SetText("Show Beta series.")
        .SetOnToggle(this::ToggleBetaSeries)
        .SetBackground(panelTexture, Color.DARK_GRAY);

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

        selectionAmount = new GUI_TextBox(panelTexture, new Hitbox(xPos, getY.Invoke(4.8f), buttonWidth, buttonHeight*0.8f))
        .SetColors(Color.DARK_GRAY, Settings.GOLD_COLOR)
        .SetAlignment(0.5f, 0.5f)
        .SetFont(FontHelper.charDescFont, 1); //FontHelper.textAboveEnemyFont);

        final float selectionAmountSize = selectionAmount.hb.height;
        purgingStoneImage = new GUI_Relic(new PurgingStone(), new Hitbox(selectionAmount.hb.x + (selectionAmountSize * 0.25f),
        selectionAmount.hb.y, selectionAmountSize, selectionAmountSize));

        selectionInfo = new GUI_TextBox(panelTexture, new Hitbox(xPos, getY.Invoke(7f), buttonWidth, buttonHeight*2.5f))
        .SetText(GR.Animator.Strings.SeriesSelection.PurgingStoneRequirement)
        .SetColors(Color.DARK_GRAY, Settings.CREAM_COLOR)
        .SetFont(FontHelper.tipBodyFont, 1);

        confirm = CreateHexagonalButton(xPos, getY.Invoke(8f), buttonWidth, buttonHeight*1.1f)
        .SetText("Proceed")
        .SetOnClick(this::Proceed)
        .SetColor(Color.FOREST);
    }

    public void Open(boolean firstTime)
    {
        super.Open();

        if (firstTime)
        {
            toggleBeta.isActive = false;
            purgingStoneImage.isActive = false;
            GameEffects.TopLevelList.Add(new AnimatorSeriesSelectEffect(this));
        }
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        cardGrid.TryRender(sb);

        toggleBeta.TryRender(sb);

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
        if (totalCardsCache != container.TotalCardsInPool)
        {
            totalCardsCache = container.TotalCardsInPool;
            TotalCardsChanged(totalCardsCache);
        }

        toggleBeta.SetToggle(GR.Animator.Config.GetDisplayBetaSeries()).TryUpdate();

        purgingStoneImage.TryUpdate();

        deselectAll.Update();
        selectRandom75.Update();
        selectRandom100.Update();
        selectAll.Update();
        confirm.Update();

        cardGrid.TryUpdate();
    }

    protected void OnCardClicked(AbstractCard card)
    {
        AnimatorRuntimeLoadout c = container.Find(card);
        if (c.promoted)
        {
            CardCrawlGame.sound.play("CARD_REJECT");
        }
        else if (container.selectedCards.contains(card))
        {
            Deselect(card);
        }
        else
        {
            Select(card);
            CardCrawlGame.sound.play("CARD_SELECT");
        }
    }

    public void SelectRandom(int minimum)
    {
        RandomizedList<AbstractCard> toSelect = new RandomizedList<>();
        for (AbstractCard c : container.allCards)
        {
            Deselect(c);
            toSelect.Add(c);
        }

        while (container.TotalCardsInPool < minimum)
        {
            Select(toSelect.Retrieve(rng));
        }
    }

    public void DeselectAll()
    {
        for (AbstractCard c : container.allCards)
        {
            Deselect(c);
        }
    }

    public void SelectAll()
    {
        for (AbstractCard c : container.allCards)
        {
            Select(c);
        }
    }

    public void Deselect(AbstractCard card)
    {
        if (container.Deselect(card))
        {
            card.targetTransparency = 0.66f;
            card.stopGlowing();
        }
    }

    public void Select(AbstractCard card)
    {
        if (container.Select(card))
        {
            card.targetTransparency = 1f;
            card.beginGlowing();
        }
    }

    public void ToggleBetaSeries(boolean value)
    {
        GR.Animator.Config.SetDisplayBetaSeries(value, true);

        if (value)
        {
            for (AbstractCard card : container.betaCards)
            {
                cardGrid.cards.add(card);
                container.allCards.add(card);
                card.transparency = 0.01f;
            }
        }
        else
        {
            for (AbstractCard card : container.betaCards)
            {
                Deselect(card);
                cardGrid.cards.remove(card);
                container.allCards.remove(card);
            }
        }
    }

    public void Proceed()
    {
        //TODO: Check card pool
        if (purgingStoneImage.isActive)
        {
            GameEffects.TopLevelQueue.SpawnRelic(new PurgingStone(), purgingStoneImage.hb.cX, purgingStoneImage.hb.cY);
        }

        cardGrid.Clear();
        container.CommitChanges();
        AbstractDungeon.closeCurrentScreen();
    }

    protected void TotalCardsChanged(int totalCards)
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
