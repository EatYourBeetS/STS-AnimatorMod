package eatyourbeets.ui.animator.seriesSelection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import eatyourbeets.cards.animator.basic.Defend_Star;
import eatyourbeets.cards.animator.basic.ImprovedDefend;
import eatyourbeets.cards.animator.basic.ImprovedStrike;
import eatyourbeets.cards.animator.basic.Strike_Star;
import eatyourbeets.cards.animator.special.RandomAttack;
import eatyourbeets.cards.animator.special.RandomSkill;
import eatyourbeets.cards.base.CardAffinityComparator;
import eatyourbeets.cards.base.CardSeriesComparator;
import eatyourbeets.cards.base.EYBCardBase;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.card.ShowCardPileEffect;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorStrings;
import eatyourbeets.resources.animator.misc.AnimatorRuntimeLoadout;
import eatyourbeets.ui.AbstractScreen;
import eatyourbeets.ui.controls.*;
import eatyourbeets.ui.hitboxes.AdvancedHitbox;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;
import java.util.Collection;

public class AnimatorSeriesSelectScreen extends AbstractScreen
{
    protected static final int MINIMUM_CARDS = 150;
    protected static final Random rng = new Random();
    protected ShowCardPileEffect previewCardsEffect;
    protected int totalCardsCache = 0;

    public final AnimatorLoadoutsContainer container = new AnimatorLoadoutsContainer();
    public final GUI_CardGrid cardGrid;
    public final GUI_Label startingDeck;
    public final GUI_Button deselectAll;
    public final GUI_Button selectRandomMinimum;
    public final GUI_Button selectAll;
    public final GUI_Button previewCards;
    public final GUI_Button confirm;
    public final GUI_Toggle upgradeToggle;
    public final GUI_Toggle toggleBeta;
    public final GUI_TextBox selectionInfo;
    public final GUI_TextBox selectionAmount;
    public final GUI_TextBox previewCardsInfo;

    public AnimatorSeriesSelectScreen()
    {
        final AnimatorStrings.SeriesSelection textboxStrings = GR.Animator.Strings.SeriesSelection;
        final AnimatorStrings.SeriesSelectionButtons buttonStrings = GR.Animator.Strings.SeriesSelectionButtons;
        final Texture panelTexture = GR.Common.Images.Panel.Texture();
        final FuncT1<Float, Float> getY = (delta) -> ScreenH(0.95f) - ScreenH(0.08f * delta);
        final float buttonHeight = ScreenH(0.07f);
        final float buttonWidth = ScreenW(0.18f);
        final float xPos = ScreenW(0.82f);

        cardGrid = new GUI_CardGrid(0.41f, false)
        .SetOnCardClick(this::OnCardClicked)
        .SetOnCardRightClick(this::OnCardRightClicked)
        .ShowScrollbar(false);

        startingDeck = new GUI_Label(null, new AdvancedHitbox(ScreenW(0.18f), ScreenH(0.05f))
        .SetPosition(ScreenW(0.08f), ScreenH(0.97f)))
        .SetFont(EYBFontHelper.CardDescriptionFont_Normal, 0.9f)
        .SetColor(Settings.CREAM_COLOR);

        upgradeToggle = new GUI_Toggle(new Hitbox(xPos, getY.Invoke(0.2f), buttonWidth, buttonHeight * 0.8f))
                .SetBackground(panelTexture, Color.DARK_GRAY)
                .SetText(SingleCardViewPopup.TEXT[6])
                .SetOnToggle(this::ToggleViewUpgrades);

        toggleBeta = new GUI_Toggle(new Hitbox(xPos, getY.Invoke(1f), buttonWidth, buttonHeight * 0.8f))
        .SetText(buttonStrings.ShowBetaSeries)
        .SetOnToggle(this::ToggleBetaSeries)
        .SetBackground(panelTexture, Color.DARK_GRAY);

        deselectAll = CreateHexagonalButton(xPos, getY.Invoke(2f), buttonWidth, buttonHeight)
        .SetText(buttonStrings.DeselectAll)
        .SetOnClick(this::DeselectAll)
        .SetColor(Color.FIREBRICK);

        selectRandomMinimum = CreateHexagonalButton(xPos, getY.Invoke(2f), buttonWidth, buttonHeight)
        .SetText(buttonStrings.SelectRandom(MINIMUM_CARDS))
        .SetOnClick(() -> SelectRandom(MINIMUM_CARDS))
        .SetColor(Color.SKY);

        selectAll = CreateHexagonalButton(xPos, getY.Invoke(5f), buttonWidth, buttonHeight)
        .SetText(buttonStrings.SelectAll)
        .SetOnClick(this::SelectAll)
        .SetColor(Color.ROYAL);

        selectionAmount = new GUI_TextBox(panelTexture, new Hitbox(xPos, getY.Invoke(5.8f), buttonWidth, buttonHeight * 0.8f))
        .SetColors(Color.DARK_GRAY, Settings.GOLD_COLOR)
        .SetAlignment(0.5f, 0.5f)
        .SetFont(FontHelper.charDescFont, 1); //FontHelper.textAboveEnemyFont);

        selectionInfo = new GUI_TextBox(panelTexture, new Hitbox(xPos, getY.Invoke(8f), buttonWidth, buttonHeight * 2.5f))
        .SetText(textboxStrings.PurgingStoneRequirement)
        .SetColors(Color.DARK_GRAY, Settings.CREAM_COLOR)
        .SetFont(FontHelper.tipBodyFont, 1);

        previewCardsInfo = new GUI_TextBox(panelTexture, new Hitbox(xPos, getY.Invoke(9f), buttonWidth, buttonHeight * 1.2f))
        .SetText(textboxStrings.RightClickToPreview)
        .SetAlignment(0.75f, 0.1f, true)
        .SetColors(Color.DARK_GRAY, Settings.CREAM_COLOR)
        .SetFont(FontHelper.tipBodyFont, 1);

        previewCards = CreateHexagonalButton(xPos, getY.Invoke(10f), buttonWidth, buttonHeight)
        .SetText(buttonStrings.ShowCardPool)
        .SetOnClick(() -> PreviewCardPool(null))
        .SetColor(Color.LIGHT_GRAY);

        confirm = CreateHexagonalButton(xPos, getY.Invoke(11f), buttonWidth, buttonHeight * 1.1f)
        .SetText(buttonStrings.Proceed)
        .SetOnClick(this::Proceed)
        .SetColor(Color.FOREST);
    }

    public void Open(boolean firstTime)
    {
        super.Open();

        if (firstTime)
        {
            upgradeToggle.isActive = false;
            toggleBeta.isActive = false;
            upgradeToggle.Toggle(false);
            UpdateStartingDeckText();
            GameEffects.TopLevelList.Add(new AnimatorSeriesSelectEffect(this));
        }
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        GR.UI.CardAffinities.TryRender(sb);

        cardGrid.TryRender(sb);

        upgradeToggle.TryRender(sb);
        toggleBeta.TryRender(sb);

        startingDeck.TryRender(sb);
        deselectAll.Render(sb);
        selectRandomMinimum.Render(sb);
        selectAll.Render(sb);
        previewCards.Render(sb);
        confirm.Render(sb);

        selectionInfo.Render(sb);
        selectionAmount.Render(sb);
        previewCardsInfo.Render(sb);

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

        startingDeck.TryUpdate();
        deselectAll.Update();
        selectRandomMinimum.Update();
        selectAll.Update();
        previewCards.Update();
        confirm.Update();

        cardGrid.TryUpdate();
        upgradeToggle.SetToggle(SingleCardViewPopup.isViewingUpgrade).Update();
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

    public void SelectRandom(int minimum)
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
                AbstractCard nc = c.makeStatEquivalentCopy();
                if (SingleCardViewPopup.isViewingUpgrade) nc.upgrade();
                cards.group.add(nc);
            }
        }
        else
        {
            for (AbstractCard cs : container.selectedCards)
            {
                final Collection<AbstractCard> cardsSource = container.Find(cs).Cards.values();
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
        SingleCardViewPopup.isViewingUpgrade = false;
        cardGrid.Clear();
        container.CommitChanges();

        //Where generic starting cards are transformed
        TransformGenericStartingCards();

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

    protected void TransformGenericStartingCards()
    {
        AbstractPlayer player = AbstractDungeon.player;

        ArrayList<AbstractCard> masterDeck = player.masterDeck.group;

        int size = masterDeck.size();

        for (int i=0; i<size; i++) {

            AbstractCard card = masterDeck.get(i);

            if (card.cardID.equals(Strike_Star.DATA.ID))
            {
                RandomizedList<EYBCardData> improved = new RandomizedList<>();
                improved.AddAll(ImprovedStrike.GetCards());

                if (improved.Size() > 0) {
                    AbstractCard newCard = improved.Retrieve(EYBCardBase.rng).CreateNewInstance(false);

                    if (newCard != null) {
                        GameEffects.TopLevelList.ShowAndObtain(newCard);
                    }
                }

                player.masterDeck.removeCard(card.cardID);
                size = masterDeck.size();
                i--;

                continue;
            }
            else if (card.cardID.equals(Defend_Star.DATA.ID))
            {
                RandomizedList<EYBCardData> improved = new RandomizedList<>();
                improved.AddAll(ImprovedDefend.GetCards());

                if (improved.Size() > 0) {
                    AbstractCard newCard = improved.Retrieve(EYBCardBase.rng).CreateNewInstance(false);

                    if (newCard != null) {
                        GameEffects.TopLevelList.ShowAndObtain(newCard);
                    }
                }

                player.masterDeck.removeCard(card.cardID);
                size = masterDeck.size();
                i--;

                continue;
            }

            else if (card.cardID.equals(RandomAttack.DATA.ID))
            {
                RandomizedList<AbstractCard> starterAttacks = new RandomizedList<>();
                ArrayList<AbstractCard> starterCards = GR.Animator.Data.SelectedLoadout.GetStarterCards();

                for (AbstractCard starter : starterCards)
                {
                    if (starter.type == AbstractCard.CardType.ATTACK)
                    {
                        starterAttacks.Add(starter.makeCopy());
                    }
                }

                if (starterAttacks.Size() > 0) {
                    AbstractCard newCard = starterAttacks.Retrieve(EYBCardBase.rng);

                    if (newCard != null) {
                        GameEffects.TopLevelList.ShowAndObtain(newCard);
                    }
                }

                player.masterDeck.removeCard(card.cardID);
                size = masterDeck.size();
                i--;

                continue;
            }

            else if (card.cardID.equals(RandomSkill.DATA.ID))
            {
                RandomizedList<AbstractCard> starterSkills = new RandomizedList<>();
                ArrayList<AbstractCard> starterCards = GR.Animator.Data.SelectedLoadout.GetStarterCards();

                for (AbstractCard starter : starterCards)
                {
                    if (starter.type == AbstractCard.CardType.SKILL)
                    {
                        starterSkills.Add(starter.makeCopy());
                    }
                }

                if (starterSkills.Size() > 0) {
                    AbstractCard newCard = starterSkills.Retrieve(EYBCardBase.rng);

                    if (newCard != null) {
                        GameEffects.TopLevelList.ShowAndObtain(newCard);
                    }
                }

                player.masterDeck.removeCard(card.cardID);
                size = masterDeck.size();

                continue;
            }

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

    private void ToggleViewUpgrades(boolean value)
    {
        SingleCardViewPopup.isViewingUpgrade = value;
    }
}
