package eatyourbeets.cards.animator.beta.colorless;

import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.special.KirbyEffect;
import eatyourbeets.interfaces.listeners.OnAddToDeckListener;
import eatyourbeets.interfaces.subscribers.OnApplyPowerSubscriber;
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

import static eatyourbeets.resources.GR.Enums.CardTags.LOYAL;

public class Kirby extends AnimatorCard implements CustomSavable<ArrayList<String>>,
                                                   OnAddToDeckListener,
        OnCostChangedSubscriber,
        OnTagChangedSubscriber,
                                                   OnApplyPowerSubscriber,
                                                   StartupCard
{
    public static final EYBCardData DATA = Register(Kirby.class).SetSkill(-2, CardRarity.RARE).SetColor(CardColor.COLORLESS).SetMaxCopies(2).SetSeries(CardSeries.Kirby).SetMaxCopies(1);
    public static final int COPIED_CARDS = 2;
    protected final RotatingList<EYBCardPreview> previews = new RotatingList<>();
    protected final ArrayList<AbstractCard> inheritedCards = new ArrayList<>(COPIED_CARDS);
    protected boolean hasAttackOrSkill = false;

    public Kirby()
    {
        super(DATA);

        SetAffinity_Star(2);
        SetObtainableInCombat(false);
        SetUnique(true, true);
        hasAttackOrSkill = false;
        previews.Clear();
    }

    @Override
    public void OnApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        if (target == player) {
            for (AbstractCard card: inheritedCards) {
                card.applyPowers();
            }
        }
    }

    @Override
    protected void OnUpgrade()
    {
        for (AbstractCard card : inheritedCards) {
            if ((card instanceof EYBCard && ((EYBCard) card).isMultiUpgrade && card.timesUpgraded < this.timesUpgraded)
                || !card.upgraded)
            {
                card.upgrade();
            }
        }
        updateProperties();
    }

    @Override
    public void renderUpgradePreview(SpriteBatch sb)
    {
        EYBCard upgrade = cardData.tempCard;
        if (upgrade == null || upgrade.uuid != this.uuid || (upgrade.timesUpgraded != (timesUpgraded + 1)))
        {
            upgrade = cardData.tempCard = (EYBCard) this.makeSameInstanceOf();
            upgrade.isPreview = true;
            for (AbstractCard iCard : inheritedCards)
            {
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
    public EYBCardPreview GetCardPreview()
    {
        if (previews.Count() == 0) {
            for (AbstractCard card: inheritedCards) {
                previews.Add(GeneratePreviewCard(card));
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
        for (AbstractCard card : inheritedCards) {
            if (card instanceof AnimatorCard)
            {
                ((AnimatorCard) card).Refresh(enemy);
            }
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        ArrayList<AbstractCard> played = AbstractDungeon.actionManager.cardsPlayedThisTurn;
        if (played != null && (played.isEmpty() || (played.size() == 1 && played.get(0) == this))) {
            AbstractDungeon.actionManager.cardsPlayedThisTurn.clear();
        }
        for (AbstractCard card : inheritedCards) {
            if (card instanceof AnimatorCard)
            {
                ((AnimatorCard) card).OnUse(p, m, isSynergizing);
            }
            else {
                card.use(p,m);
            }
        }
        if (played != null && !played.isEmpty() && played.get(played.size() -1) != this) {
            AbstractDungeon.actionManager.cardsPlayedThisTurn.add(this);
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        ArrayList<AbstractCard> played = AbstractDungeon.actionManager.cardsPlayedThisTurn;
        if (played != null && (played.isEmpty() || (played.size() == 1 && played.get(0) == this))) {
            AbstractDungeon.actionManager.cardsPlayedThisTurn.clear();
        }
        for (AbstractCard card : inheritedCards) {
            if (card instanceof AnimatorCard)
            {
                ((AnimatorCard) card).OnLateUse(p, m, isSynergizing);
            }
        }
        if (played != null && !played.isEmpty() && played.get(played.size() -1) != this) {
            AbstractDungeon.actionManager.cardsPlayedThisTurn.add(this);
        }
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        for (AbstractCard card : inheritedCards) {
            if (card instanceof StartupCard) {
                ((StartupCard) card).atBattleStartPreDraw();
            }
        }
        return false;
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        for (AbstractCard card : inheritedCards) {
            card.triggerOnEndOfTurnForPlayingCard();
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        for (AbstractCard card : inheritedCards) {
            card.triggerOnExhaust();
        }
    }

    @Override
    public void triggerOnManualDiscard()
    {
        for (AbstractCard card : inheritedCards) {
            card.triggerOnManualDiscard();
        }
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (inheritedCards.size() == 0) {
            this.cost = this.costForTurn = -2;
            OnAddToDeck();
        }
        for (AbstractCard card : inheritedCards) {
            if (card instanceof AnimatorCard) {
                ((AnimatorCard) card).triggerWhenCreated(startOfBattle);
            }
        }
        CombatStats.onCostChanged.Subscribe(this);
        CombatStats.onTagChanged.Subscribe(this);

        refreshDescription();
    }

    @Override
    public void triggerWhenDrawn()
    {
        for (AbstractCard card : inheritedCards) {
            card.triggerWhenDrawn();
        }
        updateProperties();
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return JUtils.Find(inheritedCards, card -> !card.cardPlayable(m)) == null;
    }

    @Override
    public ArrayList<String> onSave()
    {
        ArrayList<String> ids = new ArrayList<>();
        for (AbstractCard card : inheritedCards) {
            ids.add(card.cardID);
        }
        return ids;
    }

    @Override
    public void onLoad(ArrayList<String> cardIDs)
    {
        inheritedCards.clear();
        previews.Clear();
        for (String id: cardIDs) {
            AbstractCard card = CardLibrary.getCard(id);
            for (int i = 0; i < this.timesUpgraded; i++) {
                card.upgrade();
            }

            AddInheritedCard(card);
        }
        refreshDescription();
    }

    @Override
    public boolean OnAddToDeck()
    {
        GameEffects.Queue.Add(new KirbyEffect(this, COPIED_CARDS));
        return true;
    }

    @Override
    public AbstractCard makeCopy()
    {
        Kirby other = (Kirby) super.makeCopy();
        for (AbstractCard iCard : inheritedCards)
        {
            other.AddInheritedCard(iCard.makeSameInstanceOf());
        }
        other.updateProperties();

        return other;
    }

    public ArrayList<AbstractCard> RemoveInheritedCards() {
        ArrayList<AbstractCard> removedCards = new ArrayList<>();
        for (AbstractCard card : inheritedCards) {
            removedCards.add(card.makeCopy());
        }

        this.inheritedCards.clear();
        this.previews.Clear();
        updateProperties();

        return removedCards;
    }

    public void AddInheritedCard(AbstractCard card) {
        inheritedCards.add(card);
        previews.Add(GeneratePreviewCard(card));

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


        addCardProperties(card);

    }

    protected EYBCardPreview GeneratePreviewCard(AbstractCard card) {
        return (card instanceof EYBCardBase) ? new EYBCardPreview((EYBCardBase) card, false) : new EYBCardPreview(new FakeAbstractCard(card), false);
    }

    protected void addCardProperties(AbstractCard card) {
        if (this.cost == -2 || card.cost == -1) {
            this.cost = this.costForTurn = card.cost;
        }
        else if (card.cost > 0 && this.cost > -1) {
            this.cost = this.costForTurn = this.cost + card.cost;
        }

        if (card.hasTag(DELAYED)) {
            SetDelayed(true);
        }
        else if (card.isInnate) {
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

    protected void updateProperties() {
        this.cost = -2;
        this.exhaust = false;
        this.isEthereal = false;
        for (AbstractCard card : inheritedCards) {
            addCardProperties(card);
        }
        refreshDescription();
    }

    public void refreshDescription() {
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