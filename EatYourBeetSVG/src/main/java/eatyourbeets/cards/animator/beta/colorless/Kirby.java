package eatyourbeets.cards.animator.beta.colorless;

import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.EYBEffectWithCallback;
import eatyourbeets.interfaces.listeners.OnAddToDeckListener;
import eatyourbeets.interfaces.subscribers.OnApplyPowerSubscriber;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.*;

import java.util.ArrayList;

import static eatyourbeets.resources.GR.Enums.CardTags.LOYAL;

public class Kirby extends AnimatorCard implements CustomSavable<ArrayList<String>>,
                                                   OnAddToDeckListener,
                                                   OnApplyPowerSubscriber,
                                                   StartupCard
{
    public static final EYBCardData DATA = Register(Kirby.class).SetSkill(-2, CardRarity.RARE).SetColor(CardColor.COLORLESS).SetMaxCopies(2).SetSeries(CardSeries.HoshiNoKirby).SetMaxCopies(1);
    protected static final int COPIED_CARDS = 2;
    protected static final RotatingList<EYBCardPreview> previews = new RotatingList<>();
    protected static final ArrayList<String> inheritedCardIDs = new ArrayList<>(COPIED_CARDS);
    protected final ArrayList<AnimatorCard> inheritedCards = new ArrayList<>(COPIED_CARDS);
    protected boolean hasAttackOrSkill = false;


    public Kirby()
    {
        super(DATA);

        SetAffinity_Star(2);
        SetUnique(true, true);
        hasAttackOrSkill = false;
    }

    @Override
    public void OnApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        if (target == player) {
            for (AnimatorCard card: inheritedCards) {
                card.applyPowers();
            }
        }
    }

    @Override
    protected void OnUpgrade()
    {
        this.cost = -2;
        this.exhaust = false;
        this.isEthereal = false;
        for (AnimatorCard card : inheritedCards) {
            card.upgrade();
            updateTags(card);
        }
    }

    @Override
    public EYBCardPreview GetCardPreview()
    {
        if (previews.Count() == 0) {
            for (AnimatorCard card: inheritedCards) {
                previews.Add((new EYBCardPreview(card, false)));
            }
        }
        if (previews.Count() > 0) {
            EYBCardPreview currentPreview;
            if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_RIGHT))
            {
                currentPreview = previews.Next(true);
            }
            else
            {
                currentPreview = previews.Current();
            }

            currentPreview.isMultiPreview = true;
            return currentPreview;
        }
        return null;
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);
        for (AnimatorCard card : inheritedCards) {
            card.Refresh(enemy);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        for (AnimatorCard card : inheritedCards) {
            card.OnUse(p,m,isSynergizing);
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        for (AnimatorCard card : inheritedCards) {
            card.OnLateUse(p,m,isSynergizing);
        }
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        for (AnimatorCard card : inheritedCards) {
            if (card instanceof StartupCard) {
                ((StartupCard) card).atBattleStartPreDraw();
            }
        }
        return false;
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        for (AnimatorCard card : inheritedCards) {
            card.triggerOnEndOfTurnForPlayingCard();
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        for (AnimatorCard card : inheritedCards) {
            card.triggerOnExhaust();
        }
    }

    @Override
    public void triggerOnManualDiscard()
    {
        for (AnimatorCard card : inheritedCards) {
            card.triggerOnManualDiscard();
        }
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);
        if (inheritedCards.size() == 0) {
            if (inheritedCardIDs.size() == 0) {
                OnAddToDeck();
            }
            else {
                for (String id : inheritedCardIDs) {
                    AbstractCard card = CardLibrary.getCard(id);
                    if (card instanceof AnimatorCard) {
                        for (int i = 0; i < this.timesUpgraded; i++) {
                            card.upgrade();
                        }

                        AddInheritedCard((AnimatorCard) card, false);
                    }
                }
            }
        }
        for (AnimatorCard card : inheritedCards) {
            card.triggerWhenCreated(startOfBattle);
        }
        refreshDescription();
    }

    @Override
    public void triggerWhenDrawn()
    {
        for (AnimatorCard card : inheritedCards) {
            card.triggerWhenDrawn();
        }
    }

    @Override
    public ArrayList<String> onSave()
    {
        ArrayList<String> ids = new ArrayList<>();
        for (AnimatorCard card : inheritedCards) {
            ids.add(card.cardID);
        }
        return ids;
    }

    @Override
    public void onLoad(ArrayList<String> cardIDs)
    {
        inheritedCardIDs.clear();
        inheritedCards.clear();
        previews.Clear();
        for (String id: cardIDs) {
            AbstractCard card = CardLibrary.getCard(id);
            if (card instanceof AnimatorCard) {
                for (int i = 0; i < this.timesUpgraded; i++) {
                    card.upgrade();
                }

                AddInheritedCard((AnimatorCard) card, true);
            }
        }
        refreshDescription();
    }

    @Override
    public boolean OnAddToDeck()
    {
        GameEffects.Queue.Add(new KirbyEffect(COPIED_CARDS));
        return true;
    }

    public void AddInheritedCard(AnimatorCard card, boolean isAddingID) {
        if (isAddingID) {
            inheritedCardIDs.add(card.cardID);
        }
        inheritedCards.add(card);
        previews.Add((new EYBCardPreview(card, false)));

        if (card.type == CardType.ATTACK) {
            if (this.type == CardType.POWER) {
                SetPurge(true);
            }
            hasAttackOrSkill = true;
            this.type = CardType.ATTACK;
        }
        else if (card.type == CardType.POWER) {
            if (hasAttackOrSkill) {
                SetPurge(true);
            }
            else if (this.type == CardType.SKILL) {
                this.type = CardType.POWER;
            }
        }
        else if (card.type == CardType.SKILL) {
            if (this.type == CardType.POWER) {
                SetPurge(true);
                this.type = CardType.SKILL;
            }
            hasAttackOrSkill = true;
        }


        updateTags(card);

    }

    private void updateTags(AbstractCard card) {
        if (this.cost == -2 || card.cost == -1) {
            this.cost = this.costForTurn = card.cost;
        }
        else if (card.cost > 0 && this.cost > -1) {
            this.cost = this.costForTurn = this.cost + card.cost;
        }

        if (card.isInnate) {
            SetInnate(true);
        }
        if (card.isEthereal) {
            SetEthereal(true);
        }
        if (card.selfRetain) {
            SetRetain(true);
            SetRetainOnce(false);
        }
        else if (card.retain && !this.selfRetain) {
            SetRetainOnce(true);
        }
        if (card.hasTag(LOYAL)) {
            SetLoyal(true);
        }
        if (card.hasTag(HASTE_INFINITE)) {
            SetPermanentHaste(true);
        }
        else if (card.hasTag(HASTE) && !this.hasTag(HASTE_INFINITE)) {
            SetHaste(true);
        }
        if (card.hasTag(GR.Enums.CardTags.PURGE))
        {
            SetPurge(true);
        }
        else if (card.exhaust || card.exhaustOnUseOnce) {
            SetExhaust(true);
        }
        if (AfterLifeMod.IsAdded(card) && !card.hasTag(GR.Enums.CardTags.PURGE)) {
            AfterLifeMod.Add(this);
        }
        if (card.hasTag(AUTOPLAY)) {
            SetAutoplay(true);
        }
    }

    private void refreshDescription() {
        if (inheritedCards.size() >= 2) {
            cardText.OverrideDescription(JUtils.Format(cardData.Strings.EXTENDED_DESCRIPTION[1], inheritedCards.get(0).name, inheritedCards.get(1).name), true);
        }
        else if (inheritedCards.size() == 1) {
            cardText.OverrideDescription(JUtils.Format(cardData.Strings.EXTENDED_DESCRIPTION[2], inheritedCards.get(0).name), true);
        }
        else {
            cardText.OverrideDescription(null, true);
        }
    }


    protected class KirbyEffect extends EYBEffectWithCallback<Object>
    {
        private static final int GROUP_SIZE = 3;
        private final RandomizedList<AbstractCard> cards = new RandomizedList<>();
        private final String purgeMessage;
        private final Color screenColor;
        private int cardsToRemove;

        public KirbyEffect(int remove)
        {
            super(0.75f, true);

            this.purgeMessage = cardData.Strings.EXTENDED_DESCRIPTION[0];
            this.cardsToRemove = remove;
            this.screenColor = AbstractDungeon.fadeColor.cpy();
            this.screenColor.a = 0f;
            AbstractDungeon.overlayMenu.proceedButton.hide();
        }

        @Override
        protected void FirstUpdate()
        {
            super.FirstUpdate();

            if (cardsToRemove > 0)
            {
                OpenPanel_Remove();
            }
            else
            {
                Complete();
            }
        }

        @Override
        protected void UpdateInternal(float deltaTime)
        {
            if (cardsToRemove > 0)
            {
                if (AbstractDungeon.gridSelectScreen.selectedCards.size() == cardsToRemove)
                {
                    for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards)
                    {
                        AbstractDungeon.player.masterDeck.removeCard(card);
                        AnimatorCard newCard = (AnimatorCard) card.makeStatEquivalentCopy();
                        AddInheritedCard((AnimatorCard) card, true);
                    }

                    AbstractDungeon.gridSelectScreen.selectedCards.clear();
                    refreshDescription();
                    cardsToRemove = 0;
                }
            }
            else if (TickDuration(deltaTime))
            {
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                Complete(this);
            }
        }

        @Override
        public void render(SpriteBatch sb)
        {
            sb.setColor(this.screenColor);
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0f, 0f, (float) Settings.WIDTH, (float) Settings.HEIGHT);
            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GRID)
            {
                AbstractDungeon.gridSelectScreen.render(sb);
            }
        }

        public void OpenPanel_Remove()
        {
            CardGroup cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : player.masterDeck.getPurgeableCards().group)
            {
                if (c instanceof AnimatorCard && c.cost > -2 && !GameUtilities.IsCurseOrStatus(c) && !(c instanceof AnimatorCard_UltraRare) && !(c instanceof Kirby))
                {
                    cardGroup.group.add(c);
                }
            }
            if (cardGroup.size() < cardsToRemove)
            {
                Complete(this);
                return;
            }

            if (AbstractDungeon.isScreenUp)
            {
                AbstractDungeon.dynamicBanner.hide();
                AbstractDungeon.overlayMenu.cancelButton.hide();
                AbstractDungeon.previousScreen = AbstractDungeon.screen;
            }

            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
            AbstractDungeon.gridSelectScreen.open(cardGroup, cardsToRemove, purgeMessage, false, false, false, true);
        }
    }


}