package pinacolada.ui.seriesSelection;

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
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import eatyourbeets.interfaces.delegates.ActionT0;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.interfaces.delegates.FuncT2;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.base.CardAffinityComparator;
import pinacolada.cards.base.CardSeriesComparator;
import pinacolada.effects.card.ShowCardPileEffect;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLHotkeys;
import pinacolada.resources.pcl.PCLStrings;
import pinacolada.resources.pcl.misc.PCLRuntimeLoadout;
import pinacolada.ui.AbstractScreen;
import pinacolada.ui.controls.*;
import pinacolada.ui.hitboxes.AdvancedHitbox;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.Collection;

import static pinacolada.ui.seriesSelection.PCLLoadoutsContainer.MINIMUM_SERIES;

public class PCLSeriesSelectScreen extends AbstractScreen
{
    public enum ContextOption
    {
        Deselect(GR.PCL.Strings.SeriesSelection.RemoveFromPool, (screen, card) -> {
            screen.RemoveFromPool(card);
            return null;
        }),
        Select(GR.PCL.Strings.SeriesSelection.AddToPool, (screen, card) -> {
            screen.AddToPool(card);
            return null;
        }),
        ToggleExpansion(GR.PCL.Strings.SeriesSelectionButtons.EnableExpansion, (screen, card) -> {
            screen.ToggleExpansion(card);
            return null;
        }),
        ViewCards(GR.PCL.Strings.SeriesSelection.ViewPool, (screen, card) -> {
            if (screen.previewCardsEffect == null) {
                screen.PreviewCardPool(card);
            }
            return null;
        });

        public final String name;
        public final FuncT2<Void, PCLSeriesSelectScreen, AbstractCard> onSelect;

        ContextOption(String name, FuncT2<Void, PCLSeriesSelectScreen, AbstractCard> onSelect) {
            this.name = name;
            this.onSelect = onSelect;
        }
    }

    protected static final Random rng = new Random();
    protected static final PCLStrings.SeriesSelectionButtons buttonStrings = GR.PCL.Strings.SeriesSelectionButtons;
    protected AbstractCard selectedCard;
    protected ActionT0 onClose;
    protected ShowCardPileEffect previewCardsEffect;
    protected CharacterOption characterOption;
    protected int totalCardsCache = 0;
    public boolean isScreenDisabled;

    public final PCLLoadoutsContainer container = new PCLLoadoutsContainer();
    public final GUI_Image background_image;
    public final GUI_CardGrid cardGrid;
    public final GUI_Label startingDeck;
    public final GUI_Button massSelectSeriesButton;
    public final GUI_Button massExpansionButton;
    public final GUI_Button previewCards;
    public final GUI_Button selectRandom;
    public final GUI_Button cancel;
    public final GUI_Button confirm;
    public final GUI_Button seriesCountLeft;
    public final GUI_Button seriesCountRight;
    public final GUI_Toggle upgradeToggle;
    public final GUI_Toggle toggleBeta;
    public final GUI_TextBox seriesAmount;
    public final GUI_TextBox cardsAmount;
    public final GUI_TextBox previewCardsInfo;
    public final GUI_Dropdown<Integer> seriesCountDropdown;
    public final GUI_ContextMenu<ContextOption> contextMenu;

    public PCLSeriesSelectScreen()
    {
        final PCLStrings.SeriesSelection textboxStrings = GR.PCL.Strings.SeriesSelection;

        final Texture panelTexture = GR.PCL.Images.Panel.Texture();
        final FuncT1<Float, Float> getY = (delta) -> ScreenH(0.95f) - ScreenH(0.08f * delta);
        final float buttonHeight = ScreenH(0.07f);
        final float buttonWidth = ScreenW(0.18f);
        final float xPos = ScreenW(0.82f);

        background_image = new GUI_Image(GR.PCL.Images.FullSquare.Texture(), new Hitbox(ScreenW(1), ScreenH(1)))
                .SetPosition(ScreenW(0.5f), ScreenH(0.5f))
                .SetColor(0, 0, 0, 0.85f);

        cardGrid = new GUI_CardGrid(0.41f, false)
        .SetOnCardClick(this::OnCardClicked)
        .SetOnCardRightClick(this::OnCardRightClicked)
        .ShowScrollbar(false);

        startingDeck = new GUI_Label(null, new AdvancedHitbox(ScreenW(0.18f), ScreenH(0.05f))
        .SetPosition(ScreenW(0.08f), ScreenH(0.97f)))
        .SetFont(EYBFontHelper.CardDescriptionFont_Normal, 0.9f)
        .SetColor(Settings.CREAM_COLOR);

        upgradeToggle = new GUI_Toggle(new Hitbox(xPos, getY.Invoke(0.5f), buttonWidth, buttonHeight * 0.8f))
                .SetBackground(panelTexture, Color.DARK_GRAY)
                .SetText(SingleCardViewPopup.TEXT[6])
                .SetOnToggle(this::ToggleViewUpgrades);

        previewCardsInfo = new GUI_TextBox(panelTexture, new Hitbox(xPos, getY.Invoke(1.2f), buttonWidth, buttonHeight * 2f))
                .SetText(PCLJUtils.Format(textboxStrings.RightClickToPreview, MINIMUM_SERIES))
                .SetAlignment(0.75f, 0.1f, true)
                .SetColors(Color.DARK_GRAY, Settings.CREAM_COLOR)
                .SetFont(FontHelper.tipBodyFont, 1);

        seriesAmount = new GUI_TextBox(panelTexture, new Hitbox(xPos, getY.Invoke(2.0f), buttonWidth, buttonHeight * 0.8f))
                .SetColors(Color.DARK_GRAY, Settings.GOLD_COLOR)
                .SetAlignment(0.5f, 0.5f)
                .SetFont(FontHelper.charDescFont, 1);

        cardsAmount = new GUI_TextBox(panelTexture, new Hitbox(xPos, getY.Invoke(2.8f), buttonWidth, buttonHeight * 0.8f))
                .SetColors(Color.DARK_GRAY, Settings.GOLD_COLOR)
                .SetFontColor(Color.WHITE)
                .SetAlignment(0.5f, 0.5f)
                .SetFont(FontHelper.charDescFont, 1);

        selectRandom = CreateHexagonalButton(xPos, getY.Invoke(4f), buttonWidth, buttonHeight)
                .SetText(buttonStrings.SelectRandom)
                .SetOnClick(this::SelectRandom)
                .SetColor(Color.SKY);

        previewCards = CreateHexagonalButton(xPos, getY.Invoke(5f), buttonWidth, buttonHeight)
                .SetText(buttonStrings.ShowCardPool)
                .SetOnClick(() -> PreviewCardPool(null))
                .SetColor(Color.LIGHT_GRAY);

        massSelectSeriesButton = CreateHexagonalButton(xPos, getY.Invoke(6f), buttonWidth, buttonHeight)
                .SetText(buttonStrings.SelectAll)
                .SetOnClick(this::SelectAll)
                .SetColor(Color.ROYAL);

        massExpansionButton = CreateHexagonalButton(xPos, getY.Invoke(7f), buttonWidth, buttonHeight)
        .SetText(buttonStrings.AllExpansionEnable)
        .SetOnClick(this::SelectAllExpansions)
        .SetColor(Color.ROYAL);

        seriesCountDropdown = new GUI_Dropdown<Integer>(new AdvancedHitbox(ScreenW(0.875f),getY.Invoke(8f),buttonWidth,buttonHeight * 0.5f))
                .SetFontForButton(EYBFontHelper.CardTitleFont_Small, 1f)
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 1f, Settings.GOLD_COLOR, textboxStrings.PoolSizeHeader)
                .SetOnOpenOrClose(isOpen -> {
                    isScreenDisabled = isOpen;
                })
                .SetOnChange(value -> {
                    if (value.size() > 0) {
                        container.CurrentSeriesLimit = value.get(0);
                    }
                })
                .SetCanAutosizeButton(true);

        seriesCountLeft = new GUI_Button(ImageMaster.CF_LEFT_ARROW, new AdvancedHitbox(seriesCountDropdown.hb.cX - ScreenW(0.023f), seriesCountDropdown.hb.y, Scale(48), Scale(48)))
                .SetText("")
                .SetOnClick(() -> {
                    int val = container.CurrentSeriesLimit - 1;
                    if (val >= MINIMUM_SERIES) {
                        container.CurrentSeriesLimit = val;
                        seriesCountDropdown.SetSelection(val, false);
                    }
                });

        seriesCountRight = new GUI_Button(ImageMaster.CF_RIGHT_ARROW, new AdvancedHitbox(seriesCountDropdown.hb.cX + ScreenW(0.05f), seriesCountDropdown.hb.y, Scale(48), Scale(48)))
                .SetText("")
                .SetOnClick(() -> {
                    int val = container.CurrentSeriesLimit + 1;
                    if (val <= container.allCards.size()) {
                        container.CurrentSeriesLimit = val;
                        seriesCountDropdown.SetSelection(val, false);
                    }
                });


        toggleBeta = new GUI_Toggle(new Hitbox(xPos, getY.Invoke(8f), buttonWidth, buttonHeight * 0.8f))
                .SetText(buttonStrings.ShowBetaSeries)
                .SetOnToggle(this::ToggleBetaSeries)
                .SetBackground(panelTexture, Color.DARK_GRAY);

        cancel = CreateHexagonalButton(xPos, getY.Invoke(10f), buttonWidth, buttonHeight * 1.1f)
                .SetText(buttonStrings.Cancel)
                .SetOnClick(this::Cancel)
                .SetColor(Color.FIREBRICK);

        confirm = CreateHexagonalButton(xPos, getY.Invoke(11f), buttonWidth, buttonHeight * 1.1f)
        .SetText(buttonStrings.Save)
        .SetOnClick(this::Proceed)
        .SetColor(Color.FOREST);

        contextMenu = new GUI_ContextMenu<ContextOption>(new AdvancedHitbox(0,0,0,0), o -> o.name)
                .SetOnOpenOrClose(isOpen -> {
                    isScreenDisabled = isOpen;
                })
            .SetOnChange(options -> {
                for (ContextOption o: options) {
                    o.onSelect.Invoke(this, selectedCard);
                }
            })
            .SetCanAutosizeButton(true);
    }

    public void Open(CharacterOption characterOption, ActionT0 onClose)
    {
        super.Open();
        this.onClose = onClose;
        this.characterOption = characterOption;

        upgradeToggle.isActive = false;
        toggleBeta.isActive = false;
        upgradeToggle.Toggle(false);

        container.CreateCards();
        cardGrid.AddCards(container.allCards);
        UpdateStartingDeckText();

        GR.UI.CardAffinities.SetActive(true);
        GR.UI.CardAffinities.Open(container.GetAllCardsInPool(), false, null, true);

        seriesCountDropdown.SetItems(PCLJUtils.RangeArray(PCLLoadoutsContainer.MINIMUM_SERIES, GR.PCL.Data.GetEveryLoadout().size()));
        seriesCountDropdown.SetSelection(GR.PCL.Config.SeriesSize.Get(), false);
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        background_image.Render(sb);

        cardGrid.TryRender(sb);

        upgradeToggle.TryRender(sb);
        toggleBeta.TryRender(sb);

        startingDeck.TryRender(sb);
        selectRandom.TryRender(sb);
        massSelectSeriesButton.TryRender(sb);
        massExpansionButton.TryRender(sb);
        previewCards.Render(sb);
        cancel.Render(sb);
        confirm.Render(sb);

        seriesAmount.Render(sb);
        cardsAmount.Render(sb);
        previewCardsInfo.Render(sb);
        seriesCountDropdown.TryRender(sb);
        if (container.CurrentSeriesLimit > MINIMUM_SERIES) {
            seriesCountLeft.TryRender(sb);
        }
        if (container.CurrentSeriesLimit < container.allCards.size()) {
            seriesCountRight.TryRender(sb);
        }
        if (previewCardsEffect != null)
        {
            previewCardsEffect.render(sb);
        }
        else {
            GR.UI.CardAffinities.TryRender(sb);
        }

        contextMenu.TryRender(sb);
    }

    @Override
    public void Update()
    {
        background_image.Update();
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

        toggleBeta.SetToggle(GR.PCL.Config.DisplayBetaSeries.Get()).TryUpdate();

        startingDeck.TryUpdate();

        if (!isScreenDisabled) {
            selectRandom.TryUpdate();
            massSelectSeriesButton.TryUpdate();
            massExpansionButton.TryUpdate();
            previewCards.Update();
            cancel.Update();
            confirm.Update();
            cardGrid.TryUpdate();
        }
        upgradeToggle.SetToggle(SingleCardViewPopup.isViewingUpgrade).Update();

        if (container.CurrentSeriesLimit > MINIMUM_SERIES) {
            seriesCountLeft.TryUpdate();
        }
        if (container.CurrentSeriesLimit < container.allCards.size()) {
            seriesCountRight.TryUpdate();
        }
        seriesCountDropdown.TryUpdate();
        contextMenu.TryUpdate();
    }

    protected void OpenLoadoutEditor()
    {
        PCLRuntimeLoadout current = container.Find(container.currentSeriesCard);
        if (characterOption != null && current != null) {
            GR.UI.LoadoutEditor.Open(current.Loadout, characterOption, () -> {});
        }
    }

    protected void OnCardClicked(AbstractCard card)
    {
        if (!isScreenDisabled) {
            PCLRuntimeLoadout c = container.Find(card);
            if (PCLHotkeys.cycle.isJustPressed() && c.canEnableExpansion) {
                CardCrawlGame.sound.play("CARD_SELECT");
                ToggleExpansion(card);
            }
            else
            {
                if (!container.currentCards.contains(card)) {
                    container.AddToPool(card);
                }
                ChooseSeries(card);
            }
        }
    }

    public void OnCardRightClicked(AbstractCard card)
    {
        selectedCard = card;
        ArrayList<ContextOption> list = new ArrayList<>();
        list.add(ContextOption.ViewCards);
        if (container.currentCards.contains(card) && container.currentSeriesCard != card) {
            list.add(ContextOption.Deselect);
        }
        else if (!container.currentCards.contains(card)) {
            list.add(ContextOption.Select);
        }
        PCLRuntimeLoadout c = container.Find(card);
        if (c.canEnableExpansion) {
            list.add(ContextOption.ToggleExpansion);
        }

        contextMenu.SetPosition(InputHelper.mX, InputHelper.mY);
        contextMenu.SetItems(list);
        contextMenu.OpenOrCloseMenu();
    }

    public void SelectRandom()
    {
        final RandomizedList<AbstractCard> toSelect = new RandomizedList<>(container.currentCards);
        if (toSelect.Size() > 0) {
            ChooseSeries(toSelect.Retrieve(rng));
        }
    }

    public void DeselectAll()
    {
        for (AbstractCard c : container.allCards)
        {
            if (c != container.currentSeriesCard) {
                RemoveFromPool(c);
            }
        }
    }

    public void SelectAll()
    {
        for (AbstractCard c : container.allCards)
        {
            AddToPool(c);
        }
    }

    public CardGroup GetCardPool(PCLRuntimeLoadout loadout) {
        final CardGroup cards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        if (loadout != null) {
            final Collection<AbstractCard> cardsSource = loadout.GetCardPoolInPlay().values();
            for (AbstractCard c : cardsSource)
            {
                AbstractCard nc = c.makeStatEquivalentCopy();
                if (SingleCardViewPopup.isViewingUpgrade) nc.upgrade();
                cards.group.add(nc);
            }
        }
        else {
            for (AbstractCard cs : container.currentCards)
            {
                final Collection<AbstractCard> cardsSource = container.Find(cs).GetCardPoolInPlay().values();
                for (AbstractCard c : cardsSource)
                {
                    AbstractCard nc = c.makeStatEquivalentCopy();
                    if (SingleCardViewPopup.isViewingUpgrade) nc.upgrade();
                    cards.group.add(nc);
                }
            }
        }
        cards.sortAlphabetically(true);
        cards.group.sort(new CardSeriesComparator());
        cards.sortByRarity(true);
        return cards;
    }

    public void PreviewCardPool(AbstractCard source)
    {
        if (container.TotalCardsInPool > 0) {
            PCLRuntimeLoadout loadout = null;
            if (source != null) {
                source.unhover();
                loadout = container.Find(source);
            }
            final CardGroup cards = GetCardPool(loadout);
            PreviewCards(cards, loadout);
        }
    }

    public void PreviewCards(CardGroup cards, PCLRuntimeLoadout loadout)
    {
        previewCardsEffect = new ShowCardPileEffect(cards)
        .SetStartingPosition(InputHelper.mX, InputHelper.mY);
        previewCardsEffect.SetLoadout(loadout, () -> {
           previewCardsEffect.Refresh(GetCardPool(loadout));
        });
        PCLGameEffects.Manual.Add(previewCardsEffect);
    }

    public void ToggleExpansion(AbstractCard card)
    {
        if (container.ToggleExpansion(card) && totalCardsCache != container.TotalCardsInPool) {
            totalCardsCache = container.TotalCardsInPool;
            TotalCardsChanged(totalCardsCache);
        }
    }

    public void ToggleExpansion(AbstractCard card, boolean value)
    {
        if (container.ToggleExpansion(card, value) && totalCardsCache != container.TotalCardsInPool) {
            totalCardsCache = container.TotalCardsInPool;
            TotalCardsChanged(totalCardsCache);
        }
    }

    public void DeselectAllExpansions()
    {
        for (AbstractCard card : container.allCards)
        {
            ToggleExpansion(card, false);
        }
    }

    public void SelectAllExpansions()
    {
        for (AbstractCard card : container.allCards)
        {
            ToggleExpansion(card, true);
        }
    }

    public void ChooseSeries(AbstractCard card)
    {
        if (container.SelectCard(card)) {
            UpdateStartingDeckText();
        }
    }

    public void RemoveFromPool(AbstractCard card)
    {
        container.RemoveFromPool(card);
    }

    public void AddToPool(AbstractCard card)
    {
        container.AddToPool(card);
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
                RemoveFromPool(card);
                cardGrid.cards.remove(card);
                container.allCards.remove(card);
            }
        }

        GR.PCL.Config.DisplayBetaSeries.Set(value, true);
        UpdateStartingDeckText();
    }

    public void Cancel()
    {
        SingleCardViewPopup.isViewingUpgrade = false;
        cardGrid.Clear();
        AbstractDungeon.closeCurrentScreen();
    }

    public void Proceed()
    {
        SingleCardViewPopup.isViewingUpgrade = false;
        cardGrid.Clear();
        container.CommitChanges();
        if (onClose != null)
        {
            onClose.Invoke();
        }
        AbstractDungeon.closeCurrentScreen();
    }

    protected void TotalCardsChanged(int totalCards)
    {
        if (GR.UI.CardAffinities.isActive)
        {
            GR.UI.CardAffinities.Open(container.GetAllCardsInPool(), true, c ->
            {
                CardGroup group = PCLGameUtilities.CreateCardGroup(c.AffinityGroup.GetCards());
                if (group.size() > 0 && previewCardsEffect == null)
                {
                    group.sortByRarity(true);
                    group.sortAlphabetically(true);
                    group.group.sort(new CardSeriesComparator());
                    group.group.sort(new CardAffinityComparator(c.Type));

                    PreviewCards(group, null);
                }
            }, true);
        }

        seriesAmount.SetText(GR.PCL.Strings.SeriesSelection.SeriesSelected(container.currentCards.size()));
        cardsAmount.SetText(GR.PCL.Strings.SeriesSelection.CardsSelected(totalCards));

        if (container.currentCards.size() >= MINIMUM_SERIES)
        {
            confirm.SetInteractable(true);
            seriesAmount.SetFontColor(Color.GREEN);
        }
        else
        {
            confirm.SetInteractable(false);
            seriesAmount.SetFontColor(Color.GRAY);
        }

        if (container.currentCards.size() < container.cardsMap.size()) {
            massSelectSeriesButton
                    .SetText(buttonStrings.SelectAll)
                    .SetOnClick(this::SelectAll)
                    .SetColor(Color.ROYAL);
        }
        else {
            massSelectSeriesButton
                    .SetText(buttonStrings.DeselectAll)
                    .SetOnClick(this::DeselectAll)
                    .SetColor(Color.FIREBRICK);
        }

        if (container.expandedCards.size() == 0) {
            massExpansionButton
                    .SetText(buttonStrings.AllExpansionEnable)
                    .SetOnClick(this::SelectAllExpansions)
                    .SetColor(Color.ROYAL);
        }
        else {
            massExpansionButton
                    .SetText(buttonStrings.AllExpansionDisable)
                    .SetOnClick(this::DeselectAllExpansions)
                    .SetColor(Color.FIREBRICK);
        }
    }

    protected void UpdateStartingDeckText()
    {
        String text = "Starting Series: NL #y" + ((container.currentSeriesCard != null) ? container.currentSeriesCard.name.replace(" ", " #y").replace("+","") : "");
        if (GR.PCL.Config.DisplayBetaSeries.Get() && GR.PCL.Data.BetaLoadouts.size() > 0)
        {
            text += " NL Beta: Ascension and NL Trophies disabled.";
        }
        startingDeck.SetText(text);
    }

    private void ToggleViewUpgrades(boolean value)
    {
        SingleCardViewPopup.isViewingUpgrade = value;
    }

}
