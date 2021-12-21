package pinacolada.cards.pcl.colorless;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.delegates.ActionT0;
import eatyourbeets.interfaces.listeners.OnAddToDeckListener;
import eatyourbeets.utilities.RotatingList;
import pinacolada.actions.special.KirbyAction;
import pinacolada.cards.base.*;
import pinacolada.interfaces.subscribers.OnCostChangedSubscriber;
import pinacolada.interfaces.subscribers.OnTagChangedSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLHotkeys;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

import static pinacolada.resources.GR.Enums.CardTags.AFTERLIFE;

public class Kirby extends PCLCard implements
        OnAddToDeckListener,
        OnCostChangedSubscriber,
        OnTagChangedSubscriber,
        StartupCard {
    public static final PCLCardData DATA = Register(Kirby.class).SetSkill(-2, CardRarity.RARE, eatyourbeets.cards.base.EYBCardTarget.Normal, true).SetColor(CardColor.COLORLESS).SetMaxCopies(1).SetSeries(CardSeries.Kirby).SetMaxCopies(1);
    public static final int COPIED_CARDS = 2;
    protected final RotatingList<PCLCardPreview> previews = new RotatingList<>();
    protected final ArrayList<AbstractCard> inheritedCards = new ArrayList<>(COPIED_CARDS);
    protected boolean hasAttackOrSkill;

    public Kirby() {
        super(DATA);

        SetAffinity_Star(1);
        SetObtainableInCombat(false);
        SetHarmonic(true);
        SetVolatile(true);
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
    public void AddScaling(PCLAffinity affinity, int amount)
    {
        super.AddScaling(affinity, amount);
        for (AbstractCard card : inheritedCards) {
            if (card instanceof PCLCard) {
                ((PCLCard) card).AddScaling(affinity, amount);
            }
        }
    }

    @Override
    protected void OnUpgrade() {
        for (AbstractCard card : inheritedCards) {
            if ((card instanceof PCLCard && ((PCLCard) card).isMultiUpgrade && card.timesUpgraded < this.timesUpgraded)
                    || !card.upgraded) {
                card.upgrade();
            }
        }
        updateProperties();
    }

    @Override
    public void renderUpgradePreview(SpriteBatch sb) {
        PCLCard upgrade = cardData.tempCard;
        if (upgrade == null || upgrade.uuid != this.uuid || (upgrade.timesUpgraded != (timesUpgraded + 1))) {
            upgrade = cardData.tempCard = (PCLCard) this.makeSameInstanceOf();
            upgrade.isPreview = true;
            for (AbstractCard iCard : inheritedCards) {
                PCLCard innerCard = (PCLCard) iCard.makeSameInstanceOf();
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
    public PCLCardPreview GetCardPreview() {
        if (previews.Count() == 0) {
            for (AbstractCard card : inheritedCards) {
                previews.Add(GeneratePreviewCard(card));
            }
        }
        if (previews.Count() > 0) {
            PCLCardPreview currentPreview;
            if (PCLHotkeys.cycle.isJustPressed()) {
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
            if (card instanceof PCLCard) {
                ((PCLCard) card).Refresh(enemy);
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
            if (card instanceof PCLCard) {
                ((PCLCard) card).OnUse(p, m, info);
                ((PCLCard) card).OnLateUse(p, m, info);
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
        super.triggerOnEndOfTurnForPlayingCard();
        for (AbstractCard card : inheritedCards) {
            card.triggerOnEndOfTurnForPlayingCard();
        }
    }

    @Override
    public void triggerOnExhaust() {
        super.triggerOnExhaust();
        for (AbstractCard card : inheritedCards) {
            card.triggerOnExhaust();
        }
    }

    @Override
    public void triggerOnManualDiscard() {
        super.triggerOnManualDiscard();
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
                if (newCard instanceof PCLCard) {
                    ((PCLCard) newCard).triggerWhenCreated(startOfBattle);
                }
            }
            else if (card instanceof PCLCard) {
                ((PCLCard) card).triggerWhenCreated(startOfBattle);
            }
            card.isLocked = false;
            card.isSeen = true;
        }
        PCLCombatStats.onCostChanged.Subscribe(this);
        PCLCombatStats.onTagChanged.Subscribe(this);

        updateProperties();
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        for (AbstractCard card : inheritedCards) {
            card.triggerWhenDrawn();
        }
        updateProperties();
    }

    @Override
    public boolean cardPlayable(AbstractMonster m) {
        return PCLJUtils.Find(inheritedCards, card -> !card.cardPlayable(m)) == null;
    }

    @Override
    public PCLCardSaveData onSave() {

        ArrayList<String> ids = new ArrayList<>();
        for (AbstractCard card : inheritedCards) {
            ids.add(card.cardID);
        }
        auxiliaryData.additionalData = ids;
        return auxiliaryData;
    }

    @Override
    public void onLoad(PCLCardSaveData data) {
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
        PCLGameEffects.Queue.Callback(() -> {
            for (AbstractCard card : inheritedCards) {
                if (!(card instanceof AbstractMysteryCard)) {
                    PCLGameEffects.TopLevelList.ShowAndObtain(card);
                }
            }
            this.inheritedCards.clear();
            this.previews.Clear();
            updateProperties();
        }).AddCallback((ActionT0) this::OnAddToDeck);
    }

    @Override
    public boolean OnAddToDeck() {
        PCLGameEffects.Queue.Callback(new KirbyAction(this, 1, 1)
                .AddCallback(() ->
                        PCLGameEffects.Queue.Callback(new KirbyAction(this, 1, 2))));
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

    protected PCLCardPreview GeneratePreviewCard(AbstractCard card) {
        return (card instanceof PCLCardBase) ? new PCLCardPreview((PCLCardBase) card, false) : new PCLCardPreview(new FakeAbstractCard(card), false);
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
        } else if (card.isInnate || card.hasTag(GR.Enums.CardTags.PCL_INNATE)) {
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
        if (card.hasTag(AFTERLIFE) && !card.hasTag(GR.Enums.CardTags.PURGE)) {
            SetAfterlife(true, PCLGameUtilities.InGame() && PCLGameUtilities.InBattle());
        }
        if (card.hasTag(AUTOPLAY)) {
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
            for (PCLAffinity affinity : PCLAffinity.All()) {
                int scaling = affinities.GetScaling(affinity, false);
                if (card instanceof PCLCard && scaling > 0) {
                    ((PCLCard)card).AddScaling(affinity, scaling);
                }
            }
        }
        for (PCLAffinity affinity : PCLAffinity.All()) {
            SetScaling(affinity, 0);
        }
        refreshDescription();
    }

    public void refreshDescription() {
        if (inheritedCards.size() > 0) {
            cardText.OverrideDescription(PCLJUtils.Format(cardData.Strings.EXTENDED_DESCRIPTION[0], inheritedCards.get(0).name, inheritedCards.size() >= 2 ? inheritedCards.get(1).name : "? ? ?"), true);
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
                PCLActions.Bottom.ModifyTag(this, tag, value);
            }
        }
    }
}