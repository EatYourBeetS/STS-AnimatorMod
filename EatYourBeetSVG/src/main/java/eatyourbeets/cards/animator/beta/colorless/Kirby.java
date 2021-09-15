package eatyourbeets.cards.animator.beta.colorless;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.KirbyAction;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.delegates.ActionT0;
import eatyourbeets.interfaces.listeners.OnAddToDeckListener;
import eatyourbeets.interfaces.subscribers.OnCostChangedSubscriber;
import eatyourbeets.interfaces.subscribers.OnTagChangedSubscriber;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RotatingList;

import java.util.ArrayList;

public class Kirby extends AnimatorCard implements
        OnAddToDeckListener,
        OnCostChangedSubscriber,
        OnTagChangedSubscriber,
        StartupCard {
    public static final EYBCardData DATA = Register(Kirby.class).SetSkill(-2, CardRarity.RARE).SetColor(CardColor.COLORLESS).SetMaxCopies(2).SetSeries(CardSeries.Kirby).SetMaxCopies(1);
    public static final int COPIED_CARDS = 2;
    protected final RotatingList<EYBCardPreview> previews = new RotatingList<>();
    protected final ArrayList<AbstractCard> inheritedCards = new ArrayList<>(COPIED_CARDS);
    protected boolean hasAttackOrSkill;

    public Kirby() {
        super(DATA);

        SetAffinity_Star(2);
        SetObtainableInCombat(false);
        SetUnique(true, true);
        hasAttackOrSkill = false;
        previews.Clear();
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();
        for (AbstractCard card : inheritedCards) {
            card.applyPowers();
        }
    }

    @Override
    public void AddScaling(Affinity affinity, int amount)
    {
        super.AddScaling(affinity, amount);
        for (AbstractCard card : inheritedCards) {
            if (card instanceof EYBCard) {
                ((EYBCard) card).AddScaling(affinity, amount);
            }
        }
    }

    @Override
    protected void OnUpgrade() {
        for (AbstractCard card : inheritedCards) {
            if ((card instanceof EYBCard && ((EYBCard) card).isMultiUpgrade && card.timesUpgraded < this.timesUpgraded)
                    || !card.upgraded) {
                card.upgrade();
            }
        }
        updateProperties();
    }

    @Override
    public void renderUpgradePreview(SpriteBatch sb) {
        EYBCard upgrade = cardData.tempCard;
        if (upgrade == null || upgrade.uuid != this.uuid || (upgrade.timesUpgraded != (timesUpgraded + 1))) {
            upgrade = cardData.tempCard = (EYBCard) this.makeSameInstanceOf();
            upgrade.isPreview = true;
            for (AbstractCard iCard : inheritedCards) {
                EYBCard innerCard = (EYBCard) iCard.makeSameInstanceOf();
                innerCard.isPreview = true;
                ((Kirby) upgrade).AddInheritedCard(innerCard);
            }
            ((Kirby) upgrade).updateProperties();

            upgrade.upgrade();
            upgrade.displayUpgrades();
        }

        upgrade.current_x = this.current_x;
        upgrade.current_y = this.current_y;
        upgrade.drawScale = this.drawScale;
        upgrade.render(sb, false);
    }

    @Override
    public EYBCardPreview GetCardPreview() {
        if (previews.Count() == 0) {
            for (AbstractCard card : inheritedCards) {
                previews.Add(GeneratePreviewCard(card));
            }
        }
        if (previews.Count() > 0) {
            EYBCardPreview currentPreview;
            if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_RIGHT)) {
                currentPreview = previews.Next(true);
            } else {
                currentPreview = previews.Current();
            }

            currentPreview.isMultiPreview = true;
            return currentPreview;
        }
        return null;
    }

    @Override
    public void Refresh(AbstractMonster enemy) {
        super.Refresh(enemy);
        for (AbstractCard card : inheritedCards) {
            if (card instanceof AnimatorCard) {
                ((AnimatorCard) card).Refresh(enemy);
            }
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        ArrayList<AbstractCard> played = AbstractDungeon.actionManager.cardsPlayedThisTurn;
        if (played != null && (played.isEmpty() || (played.size() == 1 && played.get(0) == this))) {
            AbstractDungeon.actionManager.cardsPlayedThisTurn.clear();
        }
        for (AbstractCard card : inheritedCards) {
            if (card instanceof AnimatorCard) {
                ((AnimatorCard) card).OnUse(p, m, info);
                ((AnimatorCard) card).OnLateUse(p, m, info);
            } else {
                card.use(p, m);
            }
        }
        if (played != null && !played.isEmpty() && played.get(played.size() - 1) != this) {
            AbstractDungeon.actionManager.cardsPlayedThisTurn.add(this);
        }
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo)
    {
        super.calculateCardDamage(mo);
        for (AbstractCard card : inheritedCards) {
            card.calculateCardDamage(mo);
        }
    }

    @Override
    public boolean atBattleStartPreDraw() {
        for (AbstractCard card : inheritedCards) {
            if (card instanceof StartupCard) {
                ((StartupCard) card).atBattleStartPreDraw();
            }
        }
        return false;
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        for (AbstractCard card : inheritedCards) {
            card.triggerOnEndOfTurnForPlayingCard();
        }
    }

    @Override
    public void triggerOnExhaust() {
        for (AbstractCard card : inheritedCards) {
            card.triggerOnExhaust();
        }
    }

    @Override
    public void triggerOnManualDiscard() {
        for (AbstractCard card : inheritedCards) {
            card.triggerOnManualDiscard();
        }
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle) {
        super.triggerWhenCreated(startOfBattle);

        if (inheritedCards.size() < COPIED_CARDS) {
            while (inheritedCards.size() < COPIED_CARDS) {
                AddInheritedCard(new MysteryCard());
            }
        }
        for (AbstractCard card : inheritedCards) {
            if (card instanceof AbstractMysteryCard) {
                AbstractCard newCard = ((AbstractMysteryCard) card).CreateObscuredCard();
                ReplaceInheritedCard(card, newCard);
                if (newCard instanceof AnimatorCard) {
                    ((AnimatorCard) newCard).triggerWhenCreated(startOfBattle);
                }
            }
            else if (card instanceof AnimatorCard) {
                ((AnimatorCard) card).triggerWhenCreated(startOfBattle);
            }
            card.isLocked = false;
            card.isSeen = true;
        }
        CombatStats.onCostChanged.Subscribe(this);
        CombatStats.onTagChanged.Subscribe(this);

        updateProperties();
    }

    @Override
    public void triggerWhenDrawn() {
        for (AbstractCard card : inheritedCards) {
            card.triggerWhenDrawn();
        }
        updateProperties();
    }

    @Override
    public boolean cardPlayable(AbstractMonster m) {
        return JUtils.Find(inheritedCards, card -> !card.cardPlayable(m)) == null;
    }

    @Override
    public EYBCardSaveData onSave() {

        ArrayList<String> ids = new ArrayList<>();
        for (AbstractCard card : inheritedCards) {
            ids.add(card.cardID);
        }
        auxiliaryData.additionalData = ids;
        return auxiliaryData;
    }

    @Override
    public void onLoad(EYBCardSaveData data) {
        super.onLoad(data);
        inheritedCards.clear();
        previews.Clear();
        if (data.additionalData != null) {
            for (String id : data.additionalData) {
                AbstractCard card = CardLibrary.getCard(id);
                for (int i = 0; i < this.timesUpgraded; i++) {
                    card.upgrade();
                }

                AddInheritedCard(card);
            }
        }
        refreshDescription();
    }

    public void RemoveInheritedCards() {
        GameEffects.Queue.Callback(() -> {
            for (AbstractCard card : inheritedCards) {
                if (!(card instanceof AbstractMysteryCard)) {
                    GameEffects.TopLevelList.ShowAndObtain(card);
                }
            }
            this.inheritedCards.clear();
            this.previews.Clear();
            updateProperties();
        }).AddCallback((ActionT0) this::OnAddToDeck);
    }

    @Override
    public boolean OnAddToDeck() {
        GameEffects.Queue.Callback(new KirbyAction(this, 1, 1)
                .AddCallback(() ->
                        GameEffects.Queue.Callback(new KirbyAction(this, 1, 2))));
        return true;
    }

    @Override
    public AbstractCard makeCopy() {
        Kirby other = (Kirby) super.makeCopy();
        for (AbstractCard iCard : inheritedCards) {
            other.AddInheritedCard(iCard.makeSameInstanceOf());
        }
        other.updateProperties();

        return other;
    }

    public int GetInheritedCardsSize() {
        return inheritedCards.size();
    }

    public AbstractCard GetCard(int index) {
        return inheritedCards.size() > index ? inheritedCards.get(index) : null;
    }

    public void ReplaceInheritedCard(AbstractCard original, AbstractCard incoming) {
        int index = inheritedCards.indexOf(original);
        if (incoming != null && index >= 0) {
            inheritedCards.set(index, incoming);
        }
        this.previews.Clear();
        updateProperties();
    }

    public void AddInheritedCard(AbstractCard card) {
        for (int i = 0; i < timesUpgraded; i++) {
            card.upgrade();
        }
        inheritedCards.add(card);
        previews.Add(GeneratePreviewCard(card));

        addCardProperties(card);

    }

    protected EYBCardPreview GeneratePreviewCard(AbstractCard card) {
        return (card instanceof EYBCardBase) ? new EYBCardPreview((EYBCardBase) card, false) : new EYBCardPreview(new FakeAbstractCard(card), false);
    }

    protected void addCardProperties(AbstractCard card) {
        if (this.cost == -2 || card.cost == -1) {
            this.cost = this.costForTurn = card.cost;
        } else if (card.cost > 0 && this.cost > -1) {
            this.cost = this.costForTurn = this.cost + card.cost;
        }

        if (card.type == CardType.ATTACK) {
            if (this.type == CardType.POWER) {
                SetPurge(true);
            }
            hasAttackOrSkill = true;
            this.type = CardType.ATTACK;
        } else if (card.type == CardType.POWER) {
            if (hasAttackOrSkill) {
                SetPurge(true);
            } else if (this.type == CardType.SKILL) {
                this.type = CardType.POWER;
            }
        } else if (card.type == CardType.SKILL) {
            if (this.type == CardType.POWER) {
                SetPurge(true);
                this.type = CardType.SKILL;
            }
            hasAttackOrSkill = true;
        }

        if (card.hasTag(DELAYED)) {
            SetDelayed(true);
        } else if (card.isInnate) {
            SetInnate(true);
        }
        if (card.isEthereal) {
            SetEthereal(true);
        }
        if (card.selfRetain) {
            SetRetain(true);
            SetRetainOnce(false);
        } else if (card.retain && !this.selfRetain) {
            SetRetainOnce(true);
        }
        if (card.hasTag(LOYAL)) {
            SetLoyal(true);
        }
        if (card.hasTag(HASTE_INFINITE)) {
            SetPermanentHaste(true);
        } else if (card.hasTag(HASTE) && !this.hasTag(HASTE_INFINITE)) {
            SetHaste(true);
        }
        if (card.hasTag(GR.Enums.CardTags.PURGE)) {
            SetPurge(true);
        } else if (card.exhaust || card.exhaustOnUseOnce) {
            SetExhaust(true);
        }
        if (AfterLifeMod.IsAdded(card) && !card.hasTag(GR.Enums.CardTags.PURGE)) {
            AfterLifeMod.Add(this);
        }
        if (card.hasTag(AUTOPLAY)) {
            SetAutoplay(true);
        }

        if (card.hasTag(HARMONIC)) {
            SetAutoplay(true);
        }
    }

    protected void updateProperties() {
        this.cost = -2;
        this.exhaust = false;
        this.isEthereal = false;
        this.type = CardType.SKILL;
        for (AbstractCard card : inheritedCards) {
            addCardProperties(card);
        }
        refreshDescription();
    }

    public void refreshDescription() {
        if (inheritedCards.size() > 0) {
            cardText.OverrideDescription(JUtils.Format(cardData.Strings.EXTENDED_DESCRIPTION[0], inheritedCards.get(0).name, inheritedCards.size() >= 2 ? inheritedCards.get(1).name : "? ? ?"), true);
        } else {
            cardText.OverrideDescription(null, true);
        }
    }

    @Override
    public void OnCostChanged(AbstractCard card, int originalCost, int newCost) {
        for (AbstractCard inheritedCard : inheritedCards) {
            if (card.uuid.equals(inheritedCard.uuid)) {
                if (card.isCostModified) {
                    this.cost += (newCost - originalCost);
                }
                this.costForTurn += (newCost - originalCost);
            }
        }
    }

    @Override
    public void OnTagChanged(AbstractCard card, CardTags tag, boolean value) {
        for (AbstractCard inheritedCard : inheritedCards) {
            if (card.uuid.equals(inheritedCard.uuid)) {
                GameActions.Bottom.ModifyTag(this, tag, value);
            }
        }
    }
}