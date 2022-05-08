package eatyourbeets.ui.animator.seriesSelection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import eatyourbeets.cards.base.CardAffinityComparator;
import eatyourbeets.cards.base.CardSeriesComparator;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.effects.card.ShowCardPileEffect;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.relics.animator.PlotArmor;
import eatyourbeets.relics.animator.RollingCubes;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorStrings;
import eatyourbeets.resources.animator.misc.AnimatorRuntimeLoadout;
import eatyourbeets.ui.AbstractScreen;
import eatyourbeets.ui.controls.*;
import eatyourbeets.ui.hitboxes.AdvancedHitbox;
import eatyourbeets.utilities.*;

import java.util.Collection;

public class AnimatorSeriesSelectScreen extends AbstractScreen
{
    protected static final int MINIMUM_CARDS = 80;
    protected static final int BONUS_RELIC_THRESHOLD = 8;
    protected static final Random rng = new Random();
    protected ShowCardPileEffect previewCardsEffect;
    protected int totalCardsCache = 0;

    public final AnimatorLoadoutsContainer container = new AnimatorLoadoutsContainer();
    public final AnimatorAscensionEditor ascensionEditor;
    public final GUI_CardGrid cardGrid;
    public final GUI_Label startingDeck;
    public final GUI_Button deselectAll;
    public final GUI_Button selectRandomMinimum;
    public final GUI_Button selectRandomForPurgingStone;
    public final GUI_Button selectAll;
    public final GUI_Button previewCards;
    public final GUI_Button confirm;
    public final GUI_Toggle toggleBeta;
    public final GUI_Toggle togglePlotArmor;
    public final GUI_TextBox selectionInfo;
    public final GUI_TextBox selectionAmount;
    public final GUI_TextBox previewCardsInfo;
    public final GUI_Relic bonusRelicImage;

    public AnimatorSeriesSelectScreen()
    {
        final AnimatorStrings.SeriesSelection textboxStrings = GR.Animator.Strings.SeriesSelection;
        final AnimatorStrings.SeriesSelectionButtons buttonStrings = GR.Animator.Strings.SeriesSelectionButtons;
        final Texture panelTexture = GR.Common.Images.Panel.Texture();
        final FuncT1<Float, Float> getY = (delta) -> ScreenH(0.9f) - ScreenH(0.08f * delta);
        final float buttonHeight = ScreenH(0.07f);
        final float buttonWidth = ScreenW(0.18f);
        final float xPos = ScreenW(0.82f);

        cardGrid = new GUI_CardGrid(0.41f)
        .SetOnCardClick(this::OnCardClicked)
        .SetOnCardRightClick(this::OnCardRightClicked)
        .ShowScrollbar(false);

        startingDeck = new GUI_Label(null, new AdvancedHitbox(ScreenW(0.18f), ScreenH(0.05f))
        .SetPosition(ScreenW(0.08f), ScreenH(0.97f)))
        .SetFont(EYBFontHelper.CardDescriptionFont_Normal, 0.9f)
        .SetColor(Settings.CREAM_COLOR);

        ascensionEditor = new AnimatorAscensionEditor(ScreenW(0.0455f), ScreenH(0.8055f));

        toggleBeta = new GUI_Toggle(new Hitbox(xPos, getY.Invoke(0f), buttonWidth, buttonHeight * 0.8f))
        .SetText(buttonStrings.ShowBetaSeries)
        .SetOnToggle(this::ToggleBetaSeries)
        .SetBackground(panelTexture, Colors.DarkGray(0.75f));

        togglePlotArmor = new GUI_Toggle(new Hitbox(xPos, getY.Invoke(0.9f), buttonWidth, buttonHeight * 0.8f))
        .SetText(buttonStrings.PlotArmor)
        .SetTooltip(new EYBCardTooltip(buttonStrings.PlotArmor, textboxStrings.PlotArmorInfo), true)
        .SetBackground(panelTexture, Colors.DarkGray(0.75f));

        deselectAll = CreateHexagonalButton(xPos, getY.Invoke(1.8f), buttonWidth, buttonHeight)
        .SetText(buttonStrings.DeselectAll)
        .SetOnClick(this::DeselectAll)
        .SetColor(Color.FIREBRICK);

        selectRandomMinimum = CreateHexagonalButton(xPos, getY.Invoke(2.8f), buttonWidth, buttonHeight)
        .SetText(buttonStrings.SelectRandomCards(MINIMUM_CARDS))
        .SetOnClick(() -> SelectRandomCards(MINIMUM_CARDS))
        .SetColor(Color.SKY);

        selectRandomForPurgingStone = CreateHexagonalButton(xPos, getY.Invoke(3.8f), buttonWidth, buttonHeight)
        .SetText(buttonStrings.SelectRandomSeries(BONUS_RELIC_THRESHOLD))
        .SetOnClick(() -> SelectRandomSeries(BONUS_RELIC_THRESHOLD))
        .SetColor(Color.SKY);

        selectAll = CreateHexagonalButton(xPos, getY.Invoke(4.8f), buttonWidth, buttonHeight)
        .SetText(buttonStrings.SelectAll)
        .SetOnClick(this::SelectAll)
        .SetColor(Color.ROYAL);

        selectionAmount = new GUI_TextBox(panelTexture, new Hitbox(xPos, getY.Invoke(5.5f), buttonWidth, buttonHeight * 0.8f))
        .SetColors(Color.DARK_GRAY, Settings.GOLD_COLOR)
        .SetAlignment(0.5f, 0.5f)
        .SetFont(FontHelper.charDescFont, 1); //FontHelper.textAboveEnemyFont);

        final float selectionAmountSize = selectionAmount.hb.height;
        bonusRelicImage = new GUI_Relic(new RollingCubes(), new Hitbox(selectionAmount.hb.x + (selectionAmountSize * 0.2f),
        selectionAmount.hb.y, selectionAmountSize, selectionAmountSize));

        selectionInfo = new GUI_TextBox(panelTexture, new Hitbox(xPos, getY.Invoke(7f), buttonWidth, buttonHeight * 1.8f))
        .SetText(textboxStrings.CardPoolSizeRequirement(MINIMUM_CARDS, BONUS_RELIC_THRESHOLD))
        .SetColors(Color.DARK_GRAY, Settings.CREAM_COLOR)
        .SetFont(FontHelper.tipBodyFont, 1);

        previewCardsInfo = new GUI_TextBox(panelTexture, new Hitbox(xPos, getY.Invoke(8f), buttonWidth, buttonHeight * 1.2f))
        .SetText(textboxStrings.RightClickToPreview)
        .SetAlignment(0.75f, 0.1f, true)
        .SetColors(Color.DARK_GRAY, Settings.CREAM_COLOR)
        .SetFont(FontHelper.tipBodyFont, 1);

        previewCards = CreateHexagonalButton(xPos, getY.Invoke(9f), buttonWidth, buttonHeight)
        .SetText(buttonStrings.ShowCardPool)
        .SetOnClick(() -> PreviewCardPool(null))
        .SetColor(Color.LIGHT_GRAY);

        confirm = CreateHexagonalButton(xPos, getY.Invoke(10f), buttonWidth, buttonHeight * 1.1f)
        .SetText(buttonStrings.Proceed)
        .SetOnClick(this::Proceed)
        .SetColor(Color.FOREST);
    }

    public void Open(boolean firstTime)
    {
        super.Open();

        if (firstTime)
        {
            if (AbstractDungeon.actNum > 1)
            {
                GR.Common.Dungeon.SetCheating();
            }

            ascensionEditor.Open();
            toggleBeta.isActive = false;
            bonusRelicImage.isActive = false;
            togglePlotArmor.isActive = true;
            UpdateStartingDeckText();
            GameEffects.TopLevelList.Add(new AnimatorSeriesSelectEffect(this));
        }
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        GR.UI.CardAffinities.TryRender(sb);

        cardGrid.TryRender(sb);

        toggleBeta.TryRender(sb);
        togglePlotArmor.Render(sb);

        startingDeck.TryRender(sb);
        ascensionEditor.TryRender(sb);
        deselectAll.Render(sb);
        selectRandomMinimum.Render(sb);
        selectRandomForPurgingStone.Render(sb);
        selectAll.Render(sb);
        previewCards.Render(sb);
        confirm.Render(sb);

        selectionInfo.Render(sb);
        selectionAmount.Render(sb);
        previewCardsInfo.Render(sb);

        bonusRelicImage.TryRender(sb);

        if (previewCardsEffect != null)
        {
            previewCardsEffect.render(sb);
        }
    }

    @Override
    public void Update()
    {
        GR.UI.CardAffinities.TryUpdate();

        if (previewCardsEffect != null)
        {
            previewCardsEffect.update();

            if (previewCardsEffect.isDone)
            {
                previewCardsEffect = null;
            }
            else
            {
                return;
            }
        }

        if (totalCardsCache != container.TotalCardsInPool)
        {
            totalCardsCache = container.TotalCardsInPool;
            TotalCardsChanged(totalCardsCache);
        }

        toggleBeta.SetToggle(GR.Animator.Config.DisplayBetaSeries.Get()).TryUpdate();
        togglePlotArmor.Update();

        bonusRelicImage.TryUpdate();

        ascensionEditor.TryUpdate();
        startingDeck.TryUpdate();
        deselectAll.Update();
        selectRandomMinimum.Update();
        selectRandomForPurgingStone.Update();
        selectAll.Update();
        previewCards.Update();
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

    public void OnCardRightClicked(AbstractCard card)
    {
        if (previewCardsEffect == null)
        {
            PreviewCardPool(card);
        }
    }

    public void SelectRandomCards(int minimum)
    {
        final RandomizedList<AbstractCard> toSelect = new RandomizedList<>();
        for (AbstractCard c : container.allCards)
        {
            Deselect(c);
            toSelect.Add(c);
        }

        while (toSelect.Size() > 0 && container.TotalCardsInPool < minimum)
        {
            Select(toSelect.Retrieve(rng));
        }
    }

    public void SelectRandomSeries(int minimum)
    {
        final RandomizedList<AbstractCard> toSelect = new RandomizedList<>();
        for (AbstractCard c : container.allCards)
        {
            Deselect(c);
            toSelect.Add(c);
        }

        while (toSelect.Size() > 0 && container.selectedCards.size() < minimum)
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

    public void PreviewCardPool(AbstractCard source)
    {
        final CardGroup cards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        if (source != null)
        {
            source.unhover();

            final Collection<AbstractCard> cardsSource = container.Find(source).Cards.values();
            for (AbstractCard c : cardsSource)
            {
                cards.group.add(c.makeStatEquivalentCopy());
            }
        }
        else
        {
            for (AbstractCard cs : container.selectedCards)
            {
                final Collection<AbstractCard> cardsSource = container.Find(cs).Cards.values();
                for (AbstractCard c : cardsSource)
                {
                    cards.group.add(c.makeStatEquivalentCopy());
                }
            }
        }

        cards.sortAlphabetically(true);
        cards.group.sort(new CardSeriesComparator());
        cards.sortByRarity(true);

        PreviewCards(cards);
    }

    public void PreviewCards(CardGroup cards)
    {
        previewCardsEffect = new ShowCardPileEffect(cards)
        .SetStartingPosition(InputHelper.mX, InputHelper.mY);
        GameEffects.Manual.Add(previewCardsEffect);
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

        GR.Animator.Config.DisplayBetaSeries.Set(value, true);
        UpdateStartingDeckText();
    }

    public void Proceed()
    {
        //TODO: Check card pool
        if (bonusRelicImage.isActive)
        {
            GameEffects.TopLevelQueue.SpawnRelic(new RollingCubes(), bonusRelicImage.hb.cX, bonusRelicImage.hb.cY);
        }
        if (togglePlotArmor.toggled)
        {
            GameEffects.TopLevelQueue.SpawnRelic(new PlotArmor(), togglePlotArmor.hb.cX, togglePlotArmor.hb.cY);
            GR.Common.Dungeon.SetCheating();
        }

        SingleCardViewPopup.isViewingUpgrade = false;
        cardGrid.Clear();
        container.CommitChanges();
        AbstractDungeon.closeCurrentScreen();
    }

    protected void TotalCardsChanged(int totalCards)
    {
        if (GR.UI.CardAffinities.isActive)
        {
            GR.UI.CardAffinities.Open(container.GetAllCardsInPool(), true, c ->
            {
                CardGroup group = GameUtilities.CreateCardGroup(c.AffinityGroup.GetCards());
                if (group.size() > 0 && previewCardsEffect == null)
                {
                    group.sortByRarity(true);
                    group.sortAlphabetically(true);
                    group.group.sort(new CardSeriesComparator());
                    group.group.sort(new CardAffinityComparator(c.Type));

                    PreviewCards(group);
                }
            });
        }

        selectionAmount.SetText(totalCards + " cards selected.");
        bonusRelicImage.SetActive(container.selectedCards.size() >= BONUS_RELIC_THRESHOLD);

        if (totalCards >= MINIMUM_CARDS)
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

    protected void UpdateStartingDeckText()
    {
        String text = "Starting Series: NL #y" + GR.Animator.Data.SelectedLoadout.Name.replace(" ", " #y");
        if (GR.Animator.Config.DisplayBetaSeries.Get() && GR.Animator.Data.BetaLoadouts.size() > 0)
        {
            text += " NL Beta: Ascension and NL Trophies disabled.";
        }
        startingDeck.SetText(text);
    }
}
