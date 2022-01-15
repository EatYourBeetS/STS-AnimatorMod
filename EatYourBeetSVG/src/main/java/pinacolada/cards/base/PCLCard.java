package pinacolada.cards.base;

import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.green.Tactician;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FlightPower;
import com.megacrit.cardcrawl.powers.LockOnPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT2;
import eatyourbeets.interfaces.delegates.FuncT0;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnSubscriber;
import eatyourbeets.utilities.AdvancedTexture;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.RotatingList;
import pinacolada.actions.special.HasteAction;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.BlockAttribute;
import pinacolada.cards.base.attributes.DamageAttribute;
import pinacolada.cards.base.modifiers.AfterLifeMod;
import pinacolada.patches.screens.GridCardSelectScreenPatches;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.affinity.AbstractPCLAffinityPower;
import pinacolada.powers.replacement.PlayerFlightPower;
import pinacolada.powers.special.ElementalExposurePower;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLImages;
import pinacolada.ui.cards.DrawPileCardPreview;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static pinacolada.powers.replacement.PCLLockOnPower.GetAttackMultiplier;
import static pinacolada.powers.special.ElementalExposurePower.ELEMENTAL_MODIFIER;
import static pinacolada.resources.GR.Enums.CardTags.*;

public abstract class PCLCard extends PCLCardBase implements OnStartOfTurnSubscriber, OnStartOfTurnPostDrawSubscriber, CustomSavable<PCLCardSaveData>
{
    public static final Color MUTED_TEXT_COLOR = Colors.Lerp(Color.DARK_GRAY, Settings.CREAM_COLOR, 0.5f);
    public static final CardTags HASTE = GR.Enums.CardTags.HASTE;
    public static final CardTags HASTE_INFINITE = GR.Enums.CardTags.HASTE_INFINITE;
    public static final CardTags PURGE = GR.Enums.CardTags.PURGE;
    public static final CardTags DELAYED = GR.Enums.CardTags.DELAYED;
    public static final CardTags AUTOPLAY = GR.Enums.CardTags.AUTOPLAY;
    public static final CardTags LOYAL = GR.Enums.CardTags.LOYAL;
    public static final CardTags HARMONIC = GR.Enums.CardTags.HARMONIC;
    public static final CardTags PCL_INNATE = GR.Enums.CardTags.PCL_INNATE;
    public static final PCLImages IMAGES = GR.PCL.Images;
    protected static final Color defaultGlowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR;
    protected static final Color synergyGlowColor = new Color(1, 0.843f, 0, 0.25f);
    private static final Color COLORLESS_ORB_COLOR = new Color(0.7f, 0.7f, 0.7f, 1);
    public final PCLCardText cardText;
    public final PCLCardData cardData;
    public final PCLCardAffinities affinities;
    public final ArrayList<PCLCardTooltip> tooltips;
    public EYBCardTarget attackTarget = EYBCardTarget.Normal;
    public PCLAttackType attackType = PCLAttackType.Normal;

    protected static final String UNPLAYABLE_MESSAGE = CardCrawlGame.languagePack.getCardStrings(Tactician.ID).EXTENDED_DESCRIPTION[0];
    private static final Map<String, PCLCardData> staticCardData = new HashMap<>();

    public int maxUpgradeLevel;
    public CardSeries series;
    protected boolean unplayable;
    protected int upgrade_damage;
    protected int upgrade_magicNumber;
    protected int upgrade_secondaryValue;
    protected int upgrade_cooldownValue;
    protected int upgrade_hitCount;
    protected int upgrade_block;
    protected int upgrade_cost;

    public PCLCardSaveData auxiliaryData = new PCLCardSaveData();
    protected DrawPileCardPreview drawPileCardPreview;
    protected Color borderIndicatorColor;

    public static PCLCardData GetStaticData(String cardID)
    {
        return staticCardData.get(cardID);
    }

    public static PCLCardData RegisterCardData(Class<? extends PCLCard> type, String cardID)
    {
        return RegisterCardData(new PCLCardData(type, cardID));
    }

    public static PCLCardData RegisterCardData(PCLCardData cardData)
    {
        cardData.Metadata = GR.PCL.CardData.get(cardData.ID);
        staticCardData.put(cardData.ID, cardData);
        return cardData;
    }

    protected PCLCard(PCLCardData cardData)
    {
        this(cardData, cardData.ID, cardData.ImagePath, cardData.BaseCost, cardData.CardType, cardData.CardColor, cardData.CardRarity, cardData.CardTarget.ToCardTarget(), 0, 0);
    }

    protected PCLCard(PCLCardData cardData, int form, int timesUpgraded)
    {
        this(cardData, cardData.ID, cardData.ImagePath, cardData.BaseCost, cardData.CardType, cardData.CardColor, cardData.CardRarity, cardData.CardTarget.ToCardTarget(), form, timesUpgraded);
    }

    protected PCLCard(PCLCardData cardData, String id, String imagePath, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        this(cardData,id,imagePath,cost,type,color,rarity,target,0, 0);
    }

    protected PCLCard(PCLCardData cardData, String id, String imagePath, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target, int form, int timesUpgraded)
    {
        super(id, cardData.Strings.NAME, imagePath, cost, "", type, color, rarity, target);

        this.cropPortrait = cardData.Metadata == null || cardData.Metadata.cropPortrait;
        this.cardData = cardData;
        this.tooltips = new ArrayList<>();
        this.cardText = new PCLCardText(this);
        this.affinities = new PCLCardAffinities(this);

        SetMultiDamage(cardData.CardTarget == EYBCardTarget.ALL);
        if (cardData.CardTarget != null) {
            SetAttackTarget(cardData.CardTarget);
        }
        if (cardData.AttackType != null) {
            SetAttackType(cardData.AttackType);
        }


        if (cardData.Series != null)
        {
            SetSeries(cardData.Series);
        }

        initializeDescription();

        SetForm(form, timesUpgraded);
    }

    @Override
    public AbstractCard makeCopy()
    {
        PCLCard card = cardData.CreateNewInstance();
        if (card != null && auxiliaryData.form > 0) {
            card.SetForm(auxiliaryData.form, timesUpgraded);
        }
        return card;
    }

    protected static PCLCardData Register(Class<? extends PCLCard> type)
    {
        return RegisterCardData(type, GR.PCL.CreateID(type.getSimpleName())).SetColor(GR.PCL.CardColor);
    }

    public boolean HasSynergy()
    {
        return PCLCombatStats.MatchingSystem.IsSynergizing(this) || WouldSynergize();
    }

    public boolean HasSynergy(AbstractCard other)
    {
        return PCLCombatStats.MatchingSystem.IsSynergizing(this) || WouldSynergize(other);
    }

    public boolean HasDirectSynergy(AbstractCard other)
    {
        if (hasTag(HARMONIC)) {
            if (PCLGameUtilities.IsSameSeries(this,other)) {
                return true;
            }
        }
        return PCLCombatStats.MatchingSystem.HasDirectSynergy(this, other);
    }

    public boolean WouldSynergize()
    {
        return PCLCombatStats.MatchingSystem.WouldMatch(this);
    }

    public boolean WouldSynergize(AbstractCard other)
    {
        return PCLCombatStats.MatchingSystem.WouldSynergize(this, other);
    }

    public void SetSeries(CardSeries series)
    {
        this.series = series;
    }

    public DrawPileCardPreview SetDrawPileCardPreview(ActionT2<RotatingList<AbstractCard>, AbstractMonster> findCards)
    {
        return this.drawPileCardPreview = new DrawPileCardPreview(findCards)
        .RequireTarget(target == CardTarget.ENEMY || target == CardTarget.SELF_AND_ENEMY);
    }

    public DrawPileCardPreview SetDrawPileCardPreview(FuncT1<Boolean, AbstractCard> findCard)
    {
        return this.drawPileCardPreview = new DrawPileCardPreview(findCard)
        .RequireTarget(target == CardTarget.ENEMY || target == CardTarget.SELF_AND_ENEMY);
    }

    @Override
    public void triggerOnGlowCheck()
    {
        this.glowColor = PCLCard.defaultGlowColor;
        this.borderIndicatorColor = null;

        if (CheckSpecialCondition(false))
        {
            this.glowColor = AbstractCard.GREEN_BORDER_GLOW_COLOR;
            this.borderIndicatorColor = glowColor;
        }

        if (HasSynergy())
        {
            this.glowColor = PCLCard.synergyGlowColor;
        }
    }

    @Override
    public void hover()
    {
        super.hover();

        if (player != null && player.hand.contains(this))
        {
            if (hb.justHovered)
            {
                triggerOnGlowCheck();
            }

            for (AbstractCard c : player.hand.group)
            {
                if (c == this || WouldSynergize(c))
                {
                    c.transparency = 1f;
                }
                else
                {
                    c.transparency = 0.2f;
                }
            }
        }
    }

    @Override
    public void unhover()
    {
        if (hovered && player != null && player.hand.contains(this))
        {
            for (AbstractCard c : player.hand.group)
            {
                c.transparency = 0.35f;
            }
        }

        super.unhover();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        PCLCard copy = (PCLCard) super.makeStatEquivalentCopy();
        copy.SetForm(auxiliaryData.form, timesUpgraded);

        copy.retain = retain;
        copy.exhaust = exhaust;
        copy.exhaustOnUseOnce = exhaustOnUseOnce;
        copy.selfRetain = selfRetain;
        copy.isEthereal = isEthereal;
        copy.isInnate = isInnate;
        copy.heal = heal;

        copy.affinities.Initialize(affinities);
        copy.magicNumber = magicNumber;
        copy.isMagicNumberModified = isMagicNumberModified;

        copy.secondaryValue = secondaryValue;
        copy.baseSecondaryValue = baseSecondaryValue;
        copy.isSecondaryValueModified = isSecondaryValueModified;

        copy.hitCount = hitCount;
        copy.baseHitCount = baseHitCount;
        copy.isHitCountModified = isHitCountModified;

        copy.cooldownValue = cooldownValue;
        copy.baseCooldownValue = baseCooldownValue;
        copy.auxiliaryData = new PCLCardSaveData(auxiliaryData);

        copy.tags.clear();
        copy.tags.addAll(tags);
        copy.originalName = originalName;
        copy.name = name;
        copy.series = series;

        return copy;
    }

    public PCLCard MakePopupCopy()
    {
        PCLCard copy = (PCLCard) makeStatEquivalentCopy();
        copy.current_x = (float) Settings.WIDTH / 2f;
        copy.current_y = (float) Settings.HEIGHT / 2f;
        copy.drawScale = copy.targetDrawScale = 2f;
        copy.isPopup = true;
        return copy;
    }

    public PCLCardPreview GetCardPreview()
    {
        return cardData.GetCardPreview();
    }

    public void OnDrag(AbstractMonster m)
    {
        if (drawPileCardPreview != null && drawPileCardPreview.enabled)
        {
            drawPileCardPreview.Update(this, m);
        }
    }

    @Override
    public void Render(SpriteBatch sb, boolean hovered, boolean selected, boolean library)
    {
        super.Render(sb, hovered, selected, library);

        if (!library && drawPileCardPreview != null && drawPileCardPreview.enabled)
        {
            drawPileCardPreview.Render(sb);
        }
    }

    @Override
    public final void use(AbstractPlayer p1, AbstractMonster m1)
    {
        final CardUseInfo info = new CardUseInfo(this);

        OnUse(p1, m1, info);
        OnLateUse(p1, m1, info);
    }

    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }

    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }

    public ColoredString GetHeaderText()
    {
        return null;
    }

    public String GetRawDescription()
    {
        return GetRawDescription((Object[]) null);
    }

    protected String GetRawDescription(Object... args)
    {
        return upgraded && cardData.Strings.UPGRADE_DESCRIPTION != null
                ? PCLJUtils.Format(cardData.Strings.UPGRADE_DESCRIPTION, args)
                : PCLJUtils.Format(cardData.Strings.DESCRIPTION, args);
    }

    @Override
    public void renderUpgradePreview(SpriteBatch sb)
    {
        PCLCard upgrade = cardData.tempCard;

        if (upgrade == null || upgrade.uuid != this.uuid || (upgrade.timesUpgraded != (timesUpgraded + 1)))
        {
            upgrade = cardData.tempCard = (PCLCard) this.makeSameInstanceOf();
            upgrade.SetForm(auxiliaryData.form, timesUpgraded);
            upgrade.isPreview = true;
            upgrade.upgrade();
            upgrade.displayUpgrades();
        }

        upgrade.current_x = this.current_x;
        upgrade.current_y = this.current_y;
        upgrade.drawScale = this.drawScale;
        upgrade.render(sb, false);
    }

    @Override
    public void update()
    {
        super.update();

        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GRID && AbstractDungeon.gridSelectScreen.forUpgrade && hb.hovered && InputHelper.justClickedLeft) {
            if (this.cardData.CanToggleOnUpgrade) {
                GridCardSelectScreenPatches.BranchSelectFields.branchUpgradeForm.set(AbstractDungeon.gridSelectScreen, auxiliaryData.form);
                beginGlowing();
                GridCardSelectScreenPatches.cardList.forEach((c) -> {
                    if (c != this) {
                        c.stopGlowing();
                    }
                });
            }
            GridCardSelectScreenPatches.BranchSelectFields.waitingForBranchUpgradeSelection.set(AbstractDungeon.gridSelectScreen, false);
        }
    }

    @Override
    public void initializeDescription()
    {
        if (cardText != null)
        {
            this.cardText.ForceRefresh();
        }
    }

    @Override
    public void renderDescription(SpriteBatch sb)
    {
        if (!Settings.hideCards && !isFlipped)
        {
            this.cardText.RenderDescription(sb);
        }
    }

    @Override
    public void renderCardTip(SpriteBatch sb)
    {
        if (!Settings.hideCards && !isFlipped && !isLocked && isSeen && (isPopup || renderTip))
        {
            this.cardText.RenderTooltips(sb);
        }
    }

//    Uncomment to render affinity behind banner
    @Override
    protected void renderBannerImage(SpriteBatch sb, float drawX, float drawY)
    {
        if (isSeen)
        {
            affinities.RenderOnCard(sb, this, player != null && player.hand.contains(this));
        }

        super.renderBannerImage(sb, drawX, drawY);
    }

    public void triggerWhenCreated(boolean startOfBattle)
    {
        if (cooldown != null && cooldown.canProgressFromExhaustPile) {
            PCLCombatStats.onStartOfTurnPostDraw.Subscribe(this);
        }
        if (hasTag(AFTERLIFE)) {
            AfterLifeMod.Add(this);
        }
        // Called at the start of a fight, or when a card is created by MakeTempCard.
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (hasTag(AUTOPLAY))
        {
            PCLActions.Last.PlayCard(this, player.hand, null)
                    .SpendEnergy(true)
                    .AddCondition(AbstractCard::hasEnoughEnergy);
        }

        if (cooldown != null && cooldown.canProgressOnDraw)
        {
            cooldown.ProgressCooldownAndTrigger(null);
        }
    }


    @Override
    public final void triggerWhenCopied()
    {
        // this is only used by ShowCardAndAddToHandEffect
        triggerWhenDrawn();

        if (hasTag(HASTE))
        {
            PCLActions.Bottom.Add(new HasteAction(this));
        }
    }

    @Override
    public void OnStartOfTurn()
    {
        if (hasTag(HASTE_INFINITE))
        {
            SetTag(HASTE,true);
        }
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        if (cooldown != null && cooldown.canProgressFromExhaustPile && player != null && player.exhaustPile.contains(this))
        {
            cooldown.ProgressCooldownAndTrigger(null);
        }
    }


    public void PurgeOnUseOnce()
    {
        if (player.cardInUse != this)
        {
            PCLJUtils.LogError(this, "Only call PurgeOnUseOnce() from AbstractCard.use()");
        }

        unhover();
        untip();
        stopGlowing();

        SetTag(GR.Enums.CardTags.PURGING, true);
        PCLGameEffects.List.Add(new ExhaustCardEffect(this));
    }

    // Condition text will be green if this passes
    public boolean CheckPrimaryCondition(boolean tryUse) {
        return false;
    }

    // Card will glow green if this passes
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return false;
    }

    public boolean CheckHandAffinity(PCLAffinity affinity)
    {
        return GetHandAffinity(affinity, true) >= affinities.GetRequirement(affinity);
    }

    public boolean CheckAffinity(PCLAffinity affinity)
    {
        return PCLCombatStats.MatchingSystem.GetAffinityLevel(affinity, true) >= affinities.GetRequirement(affinity);
    }

    public boolean CheckAffinity(PCLAffinity affinity, int amount)
    {
        return PCLCombatStats.MatchingSystem.GetAffinityLevel(affinity, true) >= amount;
    }

    public boolean TrySpendAffinity(PCLAffinity affinity)
    {
        return PCLGameUtilities.TrySpendAffinity(affinity, affinities.GetRequirement(affinity), true);
    }

    public boolean TrySpendAffinity(PCLAffinity affinity, int amount)
    {
        return PCLGameUtilities.TrySpendAffinity(affinity, amount, true);
    }

    public boolean TrySpendAffinity(PCLAffinity... affinityList)
    {
        for (PCLAffinity affinity : affinityList) {
            if (!CheckAffinity(affinity)) {
                return false;
            }
        }
        for (PCLAffinity affinity : affinityList) {
            if (!PCLGameUtilities.TrySpendAffinity(affinity, affinities.GetRequirement(affinity), true)) {
                return false;
            }
        }
        return true;
    }


    public int GetHandAffinity(PCLAffinity affinity)
    {
        return GetHandAffinity(affinity, true);
    }

    public int GetHandAffinity(PCLAffinity affinity, boolean ignoreSelf)
    {
        PCLCardAffinities handAffinities = PCLCombatStats.MatchingSystem.GetHandAffinities(ignoreSelf ? this : null);
        return handAffinities != null ? handAffinities.GetLevel(affinity, false) : 0;
    }

    public boolean IsStarter()
    {
        final ArrayList<AbstractCard> played = AbstractDungeon.actionManager.cardsPlayedThisTurn;
        return played == null || played.isEmpty() || (played.size() == 1 && played.get(0) == this);
    }

    public boolean IsAoE()
    {
        return isMultiDamage;
    }

    public boolean CanScale()
    {
        return baseBlock >= 0 || baseDamage >= 0 || cardData.CanScaleMagicNumber;
    }

    public void GenerateDynamicTooltips(ArrayList<PCLCardTooltip> dynamicTooltips)
    {
        // Only show these tooltips outside of combat
        if (!PCLGameUtilities.InBattle() || isPopup || (player != null && player.masterDeck.contains(this))) {
            if (cardData.CanToggleFromPopup && upgraded || cardData.UnUpgradedCanToggleForms)
            {
                dynamicTooltips.add(GR.Tooltips.Multiform);
            }
            if (hasTag(UNIQUE))
            {
                dynamicTooltips.add(GR.Tooltips.Unique);
            }
            if (cardData.CanToggleOnUpgrade)
            {
                dynamicTooltips.add(GR.Tooltips.BranchUpgrade);
            }
        }

        if (hasTag(AFTERLIFE))
        {
            dynamicTooltips.add(GR.Tooltips.Afterlife);
        }
        if (unplayable || hasTag(PCL_UNPLAYABLE) || (PCLGameUtilities.IsUnplayableThisTurn(this)))
        {
            dynamicTooltips.add(GR.Tooltips.Unplayable);
        }
        if (isInnate || hasTag(PCL_INNATE))
        {
            dynamicTooltips.add(GR.Tooltips.Innate);
        }
        if (hasTag(DELAYED))
        {
            dynamicTooltips.add(GR.Tooltips.Delayed);
        }
        if (isEthereal)
        {
            dynamicTooltips.add(GR.Tooltips.Ethereal);
        }
        if (selfRetain)
        {
            dynamicTooltips.add(PCLGameUtilities.InGame() ? GR.Tooltips.RetainInfinite : GR.Tooltips.Retain);
        }
        else if (retain)
        {
            dynamicTooltips.add(PCLGameUtilities.InGame() ? GR.Tooltips.RetainOnce : GR.Tooltips.Retain);
        }
        if (hasTag(HASTE_INFINITE))
        {
            dynamicTooltips.add(PCLGameUtilities.InGame() ? GR.Tooltips.HasteInfinite : GR.Tooltips.Haste);
        }
        else if (hasTag(HASTE))
        {
            dynamicTooltips.add(PCLGameUtilities.InGame() ? GR.Tooltips.HasteOnce : GR.Tooltips.Haste);
        }
        if (purgeOnUse || hasTag(PURGE))
        {
            dynamicTooltips.add(GR.Tooltips.Purge);
        }
        if (exhaust || exhaustOnUseOnce)
        {
            dynamicTooltips.add(GR.Tooltips.Exhaust);
        }
        if (hasTag(AUTOPLAY))
        {
            dynamicTooltips.add(GR.Tooltips.Autoplay);
        }
        if (hasTag(LOYAL))
        {
            dynamicTooltips.add(GR.Tooltips.Loyal);
        }
        if (hasTag(HARMONIC))
        {
            dynamicTooltips.add(GR.Tooltips.Harmonic);
        }
        //if (affinities.HasStar())
        //{
        //    dynamicTooltips.add(GR.Tooltips.Affinity_Star);
        //}

        if (attackType == PCLAttackType.Brutal)
        {
            dynamicTooltips.add(GR.Tooltips.Brutal);
        }
        else if (attackType == PCLAttackType.Dark)
        {
            dynamicTooltips.add(GR.Tooltips.DarkDamage);
        }
        else if (attackType == PCLAttackType.Electric)
        {
            dynamicTooltips.add(GR.Tooltips.ElectricDamage);
        }
        else if (attackType == PCLAttackType.Fire)
        {
            dynamicTooltips.add(GR.Tooltips.FireDamage);
        }
        else if (attackType == PCLAttackType.Ice)
        {
            dynamicTooltips.add(GR.Tooltips.IceDamage);
        }
        else if (attackType == PCLAttackType.Piercing)
        {
            dynamicTooltips.add(GR.Tooltips.Piercing);
        }
        else if (attackType == PCLAttackType.Ranged)
        {
            dynamicTooltips.add(GR.Tooltips.Ranged);
        }

        if (cardData.BlockScalingAttack)
        {
            dynamicTooltips.add(GR.Tooltips.BlockScaling);
        }

    }

    public AbstractAttribute GetPrimaryInfo()
    {
        return type == CardType.ATTACK ? GetDamageInfo() : GetBlockInfo();
    }

    public AbstractAttribute GetSecondaryInfo()
    {
        return type == CardType.ATTACK ? GetBlockInfo() : GetDamageInfo();
    }

    public AbstractAttribute GetDamageInfo()
    {
        if (this.baseDamage <= 0 || this.hitCount <= 0) {
            return null;
        }
        if (this.hitCount <= 1) {
            return DamageAttribute.Instance.SetCard(this);
        }
        return DamageAttribute.Instance.SetCard(this).AddMultiplier(hitCount);
    }

    public AbstractAttribute GetBlockInfo()
    {
        return baseBlock >= 0 ? BlockAttribute.Instance.SetCard(this) : null;
    }

    public AbstractAttribute GetSpecialInfo()
    {
        return null;
    }

    public ColoredString GetAffinityString(ArrayList<PCLAffinity> types, boolean requireAll)
    {
        if (types == null || types.size() == 0)
        {
            PCLJUtils.LogError(this, "Types was null or empty.");
            return new ColoredString("?", Settings.RED_TEXT_COLOR);
        }

        final ColoredString result = new ColoredString();
        if (player != null && player.hand.contains(this))
        {
            for (PCLAffinity t : types)
            {
                final int req = affinities.GetRequirement(t);
                final int level = PCLCombatStats.MatchingSystem.GetAffinityLevel(t, true);
                result.SetText(req);

                if (requireAll)
                {
                    if (level < req)
                    {
                        return result.SetColor(MUTED_TEXT_COLOR);
                    }
                }
                else if (level >= req)
                {
                    return result.SetColor(Settings.GREEN_TEXT_COLOR);
                }
            }

            return result.SetColor(requireAll ? Settings.GREEN_TEXT_COLOR : MUTED_TEXT_COLOR);
        }

        return result.SetText(affinities.GetRequirement(types.get(0))).SetColor(Settings.CREAM_COLOR);
    }

    public void SetAttackType(PCLAttackType attackType)
    {
        this.attackType = attackType;
    }

    public void SetAttackTarget(EYBCardTarget attackTarget)
    {
        this.attackTarget = attackTarget;
        this.target = attackTarget.ToCardTarget();
    }

    public void SetMultiDamage(boolean value)
    {
        this.isMultiDamage = value;
    }

    public void SetRetain(boolean value)
    {
        this.selfRetain = value;
    }

    public void SetRetainOnce(boolean value)
    {
        this.retain = value;
    }

    public void SetAfterlife(boolean value) {
        SetTag(AFTERLIFE, value);
        if (value) {
            AfterLifeMod.Add(this);
        }
    }

    public void SetInnate(boolean value)
    {
        this.isInnate = value;
        SetTag(PCL_INNATE, value);

        if (value)
        {
            SetTag(DELAYED, false);
        }
    }

    public void SetExhaust(boolean value)
    {
        this.exhaust = value;
    }

    public void SetEthereal(boolean value)
    {
        this.isEthereal = value;
    }

    public void SetEvokeOrbCount(int count)
    {
        this.showEvokeValue = count > 0;
        this.showEvokeOrbCount = count;
    }

    public void SetHitCount(int count)
    {
        this.baseHitCount = this.hitCount = count;
    }

    public void SetHitCount(int count, int upgrade)
    {
        this.baseHitCount = this.hitCount = count;
        this.upgrade_hitCount = upgrade;
    }

    public void SetLoyal(boolean value)
    {
        SetTag(GR.Enums.CardTags.LOYAL, value);
    }

    public void SetAutoplay(boolean value)
    {
        SetTag(GR.Enums.CardTags.AUTOPLAY, value);
    }

    public void SetDelayed(boolean value)
    {
        SetTag(DELAYED, value);

        if (value)
        {
            SetInnate(false);
            SetTag(PCL_INNATE, false);
        }
    }

    public void SetHarmonic(boolean value)
    {
        SetTag(GR.Enums.CardTags.HARMONIC, value);
    }

    public void SetHaste(boolean value)
    {
        SetTag(HASTE, value);
    }

    public void SetPermanentHaste(boolean value)
    {
        SetTag(HASTE_INFINITE, value);
        SetTag(HASTE, value);
        if (value) {
            PCLCombatStats.onStartOfTurn.Subscribe(this);
        }
        else {
            PCLCombatStats.onStartOfTurn.Unsubscribe(this);
        }
    }

    public void SetProtagonist(boolean value)
    {
        SetTag(PROTAGONIST, value);
    }

    public void SetObtainableInCombat(boolean value)
    {
        SetHealing(!value);
    }

    public void SetUnplayable(boolean unplayable)
    {
        this.unplayable = unplayable;
    }

    public void SetVolatile(boolean value)
    {
        SetTag(GR.Enums.CardTags.VOLATILE, value);
    }

    public void SetHealing(boolean value)
    {
        SetTag(CardTags.HEALING, value);
    }

    public void SetPurge(boolean value)
    {
        SetPurge(value, true);
    }

    public void SetPurge(boolean value, boolean canBePlayedTwice)
    {
        if (canBePlayedTwice)
        {
            SetTag(PURGE, value);
        }
        else
        {
            purgeOnUse = value;
        }

        if (!value)
        {
            tags.remove(GR.Enums.CardTags.PURGING);
            purgeOnUse = false;
        }
    }

    public void SetUnique(boolean value, int maxUpgradeLevel)
    {
        SetTag(UNIQUE, value);
        this.maxUpgradeLevel = maxUpgradeLevel;
    }

    public void AddScaling(PCLAffinity affinity, int amount)
    {
        affinities.Get(affinity, true).scaling += amount;
    }

    public void SetScaling(PCLCardAffinities affinities)
    {
        if (affinities.HasStar())
        {
            SetScaling(PCLAffinity.Star, affinities.Star.scaling);
        }

        for (PCLCardAffinity a : affinities.List)
        {
            SetScaling(a.type, a.scaling);
        }
    }

    public void SetScaling(PCLAffinity affinity, int amount)
    {
        affinities.Get(affinity, true).scaling = amount;
    }

    protected void SetAffinityRequirement(PCLAffinity affinity, int requirement)
    {
        affinities.SetRequirement(affinity, requirement);
    }

    //@Formatter: Off
    protected void SetAffinity_Red(int base) { InitializeAffinity(PCLAffinity.Red, base, 0, 0); }
    protected void SetAffinity_Red(int base, int scaling) { InitializeAffinity(PCLAffinity.Red, base, 0, scaling); }
    protected void SetAffinity_Red(int base, int upgrade, int scaling) { InitializeAffinity(PCLAffinity.Red, base, upgrade, scaling); }
    protected void SetAffinity_Green(int base) { InitializeAffinity(PCLAffinity.Green, base, 0, 0); }
    protected void SetAffinity_Green(int base, int scaling) { InitializeAffinity(PCLAffinity.Green, base, 0, scaling); }
    protected void SetAffinity_Green(int base, int upgrade, int scaling) { InitializeAffinity(PCLAffinity.Green, base, upgrade, scaling); }
    protected void SetAffinity_Blue(int base) { InitializeAffinity(PCLAffinity.Blue, base, 0, 0); }
    protected void SetAffinity_Blue(int base, int scaling) { InitializeAffinity(PCLAffinity.Blue, base, 0, scaling); }
    protected void SetAffinity_Blue(int base, int upgrade, int scaling) { InitializeAffinity(PCLAffinity.Blue, base, upgrade, scaling); }
    protected void SetAffinity_Orange(int base) { InitializeAffinity(PCLAffinity.Orange, base, 0, 0); }
    protected void SetAffinity_Orange(int base, int scaling) { InitializeAffinity(PCLAffinity.Orange, base, 0, scaling); }
    protected void SetAffinity_Orange(int base, int upgrade, int scaling) { InitializeAffinity(PCLAffinity.Orange, base, upgrade, scaling); }
    protected void SetAffinity_Light(int base) { InitializeAffinity(PCLAffinity.Light, base, 0, 0); }
    protected void SetAffinity_Light(int base, int scaling) { InitializeAffinity(PCLAffinity.Light, base, 0, scaling); }
    protected void SetAffinity_Light(int base, int upgrade, int scaling) { InitializeAffinity(PCLAffinity.Light, base, upgrade, scaling); }
    protected void SetAffinity_Dark(int base) { InitializeAffinity(PCLAffinity.Dark, base, 0, 0); }
    protected void SetAffinity_Dark(int base, int scaling) { InitializeAffinity(PCLAffinity.Dark, base, 0, scaling); }
    protected void SetAffinity_Dark(int base, int upgrade, int scaling) { InitializeAffinity(PCLAffinity.Dark, base, upgrade, scaling); }
    protected void SetAffinity_Silver(int base) { InitializeAffinity(PCLAffinity.Silver, base, 0, 0); }
    protected void SetAffinity_Silver(int base, int scaling) { InitializeAffinity(PCLAffinity.Silver, base, 0, scaling); }
    protected void SetAffinity_Silver(int base, int upgrade, int scaling) { InitializeAffinity(PCLAffinity.Silver, base, upgrade, scaling); }
    protected void SetAffinity_Star(int base) { InitializeAffinity(PCLAffinity.Star, base, 0, 0); }
    protected void SetAffinity_Star(int base, int scaling) { InitializeAffinity(PCLAffinity.Star, base, 0, scaling); }
    protected void SetAffinity_Star(int base, int upgrade, int scaling) { InitializeAffinity(PCLAffinity.Star, base, upgrade, scaling); }
    protected void SetAffinity_General(int base) { InitializeAffinity(PCLAffinity.General, base, 0, 0); }
    protected void InitializeAffinity(PCLAffinity affinity, int base, int upgrade, int scaling) { affinities.Initialize(affinity, base, upgrade, scaling, 0); }
    //@Formatter: On

    protected boolean TryUpgrade()
    {
        return TryUpgrade(true);
    }

    protected boolean TryUpgrade(boolean updateDescription)
    {
        if (this.canUpgrade())
        {
            this.timesUpgraded += 1;
            this.upgraded = true;

            if (maxUpgradeLevel < 0 || maxUpgradeLevel > 1)
            {
                this.name = originalName + "+" + this.timesUpgraded;
            }
            else
            {
                this.name = originalName + "+";
            }

            initializeTitle();

            if (updateDescription)
            {
                initializeDescription();
            }

            return true;
        }

        return false;
    }

    public boolean isMultiUpgrade() {
        return maxUpgradeLevel < 0 || maxUpgradeLevel > 1;
    }

    @Override
    public boolean canUpgrade()
    {
        return !upgraded || maxUpgradeLevel < 0 || timesUpgraded < maxUpgradeLevel;
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            if (upgrade_damage != 0)
            {
                if (baseDamage < 0)
                {
                    baseDamage = 0;
                }

                upgradeDamage(upgrade_damage);
            }

            if (upgrade_block != 0)
            {
                if (baseBlock < 0)
                {
                    baseBlock = 0;
                }

                upgradeBlock(upgrade_block);
            }

            if (upgrade_secondaryValue != 0)
            {
                if (isSecondaryValueModified)
                {
                    int temp = secondaryValue;
                    upgradeSecondaryValue(upgrade_secondaryValue);
                    secondaryValue = Math.max(0, temp + upgrade_secondaryValue);
                }
                else
                {
                    upgradeSecondaryValue(upgrade_secondaryValue);
                }
            }

            if (upgrade_magicNumber != 0)
            {
                if (isMagicNumberModified)
                {
                    int temp = magicNumber;
                    upgradeMagicNumber(upgrade_magicNumber);
                    magicNumber = Math.max(0, temp + upgrade_magicNumber);
                }
                else
                {
                    upgradeMagicNumber(upgrade_magicNumber);
                }
            }

            if (upgrade_cooldownValue != 0)
            {
                if (baseCooldownValue < 0)
                {
                    baseCooldownValue = 0;
                }

                upgradeCooldownValue(upgrade_cooldownValue);
            }

            if (upgrade_hitCount != 0)
            {
                if (isHitCountModified)
                {
                    int temp = hitCount;
                    upgradeHitCount(upgrade_hitCount);
                    hitCount = Math.max(0, temp + upgrade_hitCount);
                }
                else
                {
                    upgradeHitCount(upgrade_hitCount);
                }
            }


            if (upgrade_cost != 0)
            {
                int previousCost = cost;
                int previousCostForTurn = costForTurn;

                this.cost = Math.max(0, previousCost + upgrade_cost);
                this.costForTurn = Math.max(0, previousCostForTurn + upgrade_cost);
                this.upgradedCost = true;
            }

            affinities.ApplyUpgrades();

            OnUpgrade();
        }
    }

    @Override
    public void displayUpgrades()
    {
        isCostModified = upgradedCost;
        isMagicNumberModified = upgradedMagicNumber;
        isSecondaryValueModified = upgradedSecondaryValue;

        if (isDamageModified = upgradedDamage)
        {
            damage = baseDamage;
        }
        if (isBlockModified = upgradedBlock)
        {
            block = baseBlock;
        }

        affinities.displayUpgrades = true;
    }

    @Override
    protected void upgradeName() {
        ++this.timesUpgraded;
        this.upgraded = true;
        if (this.cardData.MaxForms > 1 && !this.cardData.CanToggleFromPopup) {
            switch (this.auxiliaryData.form) {
                case 2:
                    this.name = this.name + "+γ";
                    break;
                case 1:
                    this.name = this.name + "+β";
                    break;
                default:
                    this.name = this.name + "+α";
                    break;
            }
        }
        else {
            this.name = this.name + "+";
        }
        this.initializeTitle();
    }

    protected void Initialize(int damage, int block)
    {
        Initialize(damage, block, 0, 0);
    }

    protected void Initialize(int damage, int block, int magicNumber)
    {
        Initialize(damage, block, magicNumber, 0);
    }

    protected void Initialize(int damage, int block, int magicNumber, int secondaryValue)
    {
        this.baseDamage = this.damage = damage > 0 ? damage : -1;
        this.baseBlock = this.block = block > 0 ? block : -1;
        this.baseMagicNumber = this.magicNumber = magicNumber;
        this.baseSecondaryValue = this.secondaryValue = secondaryValue;
    }

    protected void SetUpgrade(int damage, int block)
    {
        SetUpgrade(damage, block, 0, 0);
    }

    protected void SetUpgrade(int damage, int block, int magicNumber)
    {
        SetUpgrade(damage, block, magicNumber, 0);
    }

    protected void SetUpgrade(int damage, int block, int magicNumber, int secondaryValue)
    {
        this.upgrade_damage = damage;
        this.upgrade_block = block;
        this.upgrade_magicNumber = magicNumber;
        this.upgrade_secondaryValue = secondaryValue;
    }

    protected void SetCostUpgrade(int value)
    {
        this.upgrade_cost = value;
    }

    public PCLCardCooldown SetCooldown(int baseCooldown, int cooldownUpgrade, ActionT1<AbstractMonster> onCooldownCompleted)
    {
        this.cooldown = new PCLCardCooldown(this, baseCooldown, cooldownUpgrade, onCooldownCompleted);
        return this.cooldown;
    }

    public PCLCardCooldown SetCooldown(int baseCooldown, int cooldownUpgrade, ActionT1<AbstractMonster> onCooldownCompleted, boolean canProgressOnManualDiscard, boolean canProgressFromExhaustPile, boolean canProgressOnDraw)
    {
        this.cooldown = new PCLCardCooldown(this, baseCooldown, cooldownUpgrade, onCooldownCompleted, canProgressOnManualDiscard, canProgressFromExhaustPile, canProgressOnDraw);
        return this.cooldown;
    }

    public PCLCardCooldown SetRicochet(int baseCooldown, int cooldownUpgrade, ActionT1<AbstractMonster> onCooldownCompleted)
    {
        this.cooldown = new PCLCardCooldown(this, baseCooldown, cooldownUpgrade, onCooldownCompleted, false, true, false);
        return this.cooldown;
    }

    public PCLCardCooldown SetSoul(int baseCooldown, int cooldownUpgrade, FuncT0<PCLCard> cardConstructor)
    {
        this.cooldown = new PCLCardCooldown(this, baseCooldown, cooldownUpgrade, cardConstructor, false, false, false, false);
        return this.cooldown;
    }

    protected void OnUpgrade()
    {
        SetForm(auxiliaryData.form, timesUpgraded);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        cantUseMessage = UNPLAYABLE_MESSAGE;
        return !unplayable && super.cardPlayable(m);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m)
    {
        return !unplayable && super.canUse(p, m);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();
        if (cooldown != null && cooldown.canProgressOnManualDiscard)
        {
            cooldown.ProgressCooldownAndTrigger(null);
        }
    }

    @Override
    public void applyPowers()
    {
        if (isMultiDamage)
        {
            calculateCardDamage(null);
        }
        else
        {
            Refresh(null);
        }
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo)
    {
        if (isMultiDamage)
        {
            ArrayList<AbstractMonster> m = AbstractDungeon.getCurrRoom().monsters.monsters;
            multiDamage = new int[m.size()];

            int best = -999;
            for (int i = 0; i < multiDamage.length; i++)
            {
                Refresh(m.get(i));
                multiDamage[i] = damage;

                if (damage > best)
                {
                    best = damage;
                }
            }

            if (best > 0)
            {
                UpdateDamage(best);
            }
        }
        else
        {
            Refresh(mo);
        }
    }

    @Override
    public final void resetAttributes()
    {
        // Triggered after being played, discarded, or at end of turn
        super.resetAttributes();
    }

    public void Refresh(AbstractMonster enemy)
    {
        boolean applyEnemyPowers = (enemy != null && !PCLGameUtilities.IsDeadOrEscaped(enemy));
        float tempBlock = GetInitialBlock();
        float tempDamage = GetInitialDamage();
        int applyCount = attackType == PCLAttackType.Brutal ? 2 : 1;

        for (AbstractRelic r : player.relics)
        {
            tempDamage = r.atDamageModify(tempDamage, this);
        }

        tempBlock = PCLCombatStats.MatchingSystem.ModifyBlock(tempBlock, this);
        tempDamage = PCLCombatStats.MatchingSystem.ModifyDamage(tempDamage, this);

        for (AbstractPower p : player.powers)
        {
            tempBlock = p.modifyBlock(tempBlock, this);
            for (int i = 0; i < applyCount; i++) {
                tempDamage = p.atDamageGive(tempDamage, damageTypeForTurn, this);
            }

        }

        for (AbstractPCLAffinityPower p : PCLCombatStats.MatchingSystem.Powers) {
            tempBlock = p.modifyBlock(tempBlock, this);
            tempDamage = p.atDamageGive(tempDamage, damageTypeForTurn, this);
        }

        tempBlock = ModifyBlock(enemy, tempBlock);

        for (AbstractPower p : player.powers)
        {
            tempBlock = p.modifyBlockLast(tempBlock);
        }

        tempDamage = ModifyDamage(enemy, tempDamage);

        if (applyEnemyPowers)
        {
            if (attackType.powerToRemove != null)
            {
                for (AbstractPower power : enemy.powers) {
                    if (attackType.powerToRemove.equals(power.ID)) {
                        tempDamage *= PCLAttackType.DAMAGE_MULTIPLIER;
                    }
                    else if (ElementalExposurePower.POWER_ID.equals(power.ID))
                    {
                        tempDamage *= (1 + (ELEMENTAL_MODIFIER / 100f));
                    }
                }
            }
            if (attackType == PCLAttackType.Ranged)
            {
                boolean hasFlight = false;
                for (AbstractPower power : enemy.powers)
                {
                    if (!hasFlight && (FlightPower.POWER_ID.equals(power.ID) || PlayerFlightPower.POWER_ID.equals(power.ID)))
                    {
                        tempDamage *= 2f;
                        hasFlight = true;
                    }
                    else if (LockOnPower.POWER_ID.equals(power.ID))
                    {
                        tempDamage *= (1 + (GetAttackMultiplier() / 100f));
                    }
                }
            }

            for (AbstractPower p : enemy.powers)
            {
                for (int i = 0; i < applyCount; i++) {
                    tempDamage = p.atDamageReceive(tempDamage, damageTypeForTurn, this);
                }
            }
        }

        tempDamage = player.stance.atDamageGive(tempDamage, damageTypeForTurn, this);

        for (AbstractPower p : player.powers)
        {
            for (int i = 0; i < applyCount; i++) {
                tempDamage = p.atDamageFinalGive(tempDamage, damageTypeForTurn, this);
            }
        }

        if (applyEnemyPowers)
        {
            for (AbstractPower p : enemy.powers)
            {
                for (int i = 0; i < applyCount; i++) {
                    tempDamage = p.atDamageFinalReceive(tempDamage, damageTypeForTurn, this);
                }
            }
            tempDamage = PCLCombatStats.OnDamageOverride(enemy, damageTypeForTurn, tempDamage, this);
        }

        UpdateBlock(tempBlock);
        UpdateDamage(tempDamage);
        UpdateMagicNumber(PCLCombatStats.MatchingSystem.ModifyMagicNumber(baseMagicNumber, this));
    }

    protected void UpdateBlock(float amount)
    {
        block = MathUtils.floor(amount);
        if (block < 0)
        {
            block = 0;
        }
        this.isBlockModified = (baseBlock != block);
    }

    protected void UpdateDamage(float amount)
    {
        damage = MathUtils.floor(amount);
        if (damage < 0)
        {
            damage = 0;
        }
        this.isDamageModified = (baseDamage != damage);
    }

    protected void UpdateMagicNumber(float amount)
    {
        magicNumber = MathUtils.floor(amount);
        this.isMagicNumberModified = (baseMagicNumber != magicNumber);
    }

    protected float GetInitialBlock()
    {
        return baseBlock;
    }

    protected float GetInitialDamage()
    {
        return baseDamage;
    }

    protected float ModifyBlock(AbstractMonster enemy, float amount)
    {
        return amount;
    }

    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        return amount;
    }

    @Override
    public PCLCardSaveData onSave()
    {
        return auxiliaryData;
    }

    @Override
    public void onLoad(PCLCardSaveData data)
    {
        if (data != null) {
            SetForm(data.form, timesUpgraded);
            if (data.modifiedDamage != 0) {
                PCLGameUtilities.ModifyDamage(this, baseDamage + data.modifiedDamage, false);
            }
            if (data.modifiedBlock != 0) {
                PCLGameUtilities.ModifyBlock(this, baseBlock + data.modifiedBlock, false);
            }
            if (data.modifiedMagicNumber != 0) {
                PCLGameUtilities.ModifyMagicNumber(this, baseMagicNumber + data.modifiedMagicNumber, false);
            }
            if (data.modifiedSecondaryValue != 0) {
                PCLGameUtilities.ModifySecondaryValue(this, baseSecondaryValue + data.modifiedSecondaryValue, false);
            }
            if (data.modifiedHitCount != 0) {
                PCLGameUtilities.ModifyHitCount(this, baseHitCount + data.modifiedHitCount, false);
            }
            if (cost >= 0 && data.modifiedCost != 0) {
                PCLGameUtilities.ModifyCostForCombat(this, data.modifiedCost, true);
            }
            if (data.modifiedAffinities != null) {
                for (PCLAffinity affinity : PCLAffinity.Extended()) {
                    affinities.Add(affinity, data.modifiedAffinities[affinity.ID]);
                }
            }
            if (data.modifiedScaling != null) {
                for (PCLAffinity affinity : PCLAffinity.Extended()) {
                    affinities.Add(affinity, data.modifiedScaling[affinity.ID]);
                }
            }
            if (data.modifiedTags != null) {
                for (CardTags tag : data.modifiedTags) {
                    PCLGameUtilities.ModifyCardTag(this, tag, true);
                }
            }
        }
    }

    @Override
    public Type savedType() {
        return new TypeToken<PCLCardSaveData>(){}.getType();
    }

    public int SetForm(Integer form, int timesUpgraded) {
        this.auxiliaryData.form = (form == null) ? 0 : MathUtils.clamp(form,0,this.cardData.MaxForms - 1);
        this.cardText.ForceRefresh();
        return this.auxiliaryData.form;
    };

    public ColoredString GetBottomText()
    {
        return (series == null) ? null : new ColoredString(series.LocalizedName, Settings.CREAM_COLOR);
    }

    @Override
    protected Texture GetCardBackground()
    {
        if (color == GR.PCL.CardColor || color == CardColor.COLORLESS)
        {

            switch (type)
            {
                case ATTACK: return isPopup ? PCLCard.IMAGES.CARD_BACKGROUND_ATTACK_L.Texture() : PCLCard.IMAGES.CARD_BACKGROUND_ATTACK.Texture();
                case POWER: return isPopup ? PCLCard.IMAGES.CARD_BACKGROUND_POWER_L.Texture() : PCLCard.IMAGES.CARD_BACKGROUND_POWER.Texture();
                default: return isPopup ? PCLCard.IMAGES.CARD_BACKGROUND_SKILL_L.Texture() : PCLCard.IMAGES.CARD_BACKGROUND_SKILL.Texture();
            }
        }

        return super.GetCardBackground();
    }

    @Override
    public AdvancedTexture GetCardAttributeBanner()
    {
        return new AdvancedTexture((isPopup ? PCLCard.IMAGES.CARD_BANNER_ATTRIBUTE_L : PCLCard.IMAGES.CARD_BANNER_ATTRIBUTE).Texture(), GetRarityColor(false));
    }

    @Override
    protected Texture GetEnergyOrb()
    {
        return (isPopup ? PCLCard.IMAGES.CARD_ENERGY_ORB_ANIMATOR_L : PCLCard.IMAGES.CARD_ENERGY_ORB_ANIMATOR).Texture();
    }

    @Override
    protected AdvancedTexture GetCardBorderIndicator()
    {
        return borderIndicatorColor == null ? null : new AdvancedTexture(PCLCard.IMAGES.CARD_BORDER_INDICATOR.Texture(), borderIndicatorColor);
    }

    @Override
    protected AdvancedTexture GetCardBanner()
    {
        return new AdvancedTexture((isPopup ? PCLCard.IMAGES.CARD_BANNER_L : PCLCard.IMAGES.CARD_BANNER).Texture(), GetRarityColor(false));
    }

    @Override
    protected AdvancedTexture GetPortraitFrame()
    {
        switch (type)
        {
            case ATTACK:
                return new AdvancedTexture(isPopup ? PCLCard.IMAGES.CARD_FRAME_ATTACK_L.Texture() : PCLCard.IMAGES.CARD_FRAME_ATTACK.Texture(), GetRarityColor(false));

            case POWER:
                return new AdvancedTexture(isPopup ? PCLCard.IMAGES.CARD_FRAME_POWER_L.Texture() : PCLCard.IMAGES.CARD_FRAME_POWER.Texture(), GetRarityColor(false));

            case SKILL:
            case CURSE:
            case STATUS:
            default:
                return new AdvancedTexture(isPopup ? PCLCard.IMAGES.CARD_FRAME_SKILL_L.Texture() : PCLCard.IMAGES.CARD_FRAME_SKILL.Texture(), GetRarityColor(false));
        }
    }

    @SpireOverride
    protected void renderCardBg(SpriteBatch sb, float x, float y)
    {
        Texture card = GetCardBackground();
        float popUpMultiplier = isPopup ? 0.5f : 1f;
        if (this.color == CardColor.COLORLESS) {
            pinacolada.utilities.PCLRenderHelpers.DrawGrayscale(sb, () ->
                pinacolada.utilities.PCLRenderHelpers.DrawOnCardAuto(sb, this, card, new Vector2(0,0), card.getWidth(), card.getHeight(), PCLCard.COLORLESS_ORB_COLOR, transparency, popUpMultiplier));
        }
        else {
            pinacolada.utilities.PCLRenderHelpers.DrawOnCardAuto(sb, this, card, new Vector2(0,0), card.getWidth(), card.getHeight(), _renderColor.Get(this), transparency, popUpMultiplier);
        }
    }

    @SpireOverride
    protected void renderEnergy(SpriteBatch sb)
    {
        if (this.cost > -2 && !_darken.Get(this) && !this.isLocked && this.isSeen)
        {
            Texture baseCard = GetCardBackground();
            float popUpMultiplier = isPopup ? 0.5f : 1f;
            Vector2 offset = new Vector2(-baseCard.getWidth() / (isPopup ? 7.7f : 3.85f), baseCard.getHeight() / (isPopup ? 5.3f : 2.64f));
            Texture energyOrb = GetEnergyOrb();
            if (this.color == CardColor.COLORLESS && !(this instanceof PCLCard_UltraRare)) {
                pinacolada.utilities.PCLRenderHelpers.DrawGrayscale(sb, () ->
                    pinacolada.utilities.PCLRenderHelpers.DrawOnCardAuto(sb, this, energyOrb, offset, energyOrb.getWidth(), energyOrb.getHeight(), PCLCard.COLORLESS_ORB_COLOR, transparency, popUpMultiplier));
            }
            else {
                pinacolada.utilities.PCLRenderHelpers.DrawOnCardAuto(sb, this, energyOrb, offset, energyOrb.getWidth(), energyOrb.getHeight(), _renderColor.Get(this), transparency, popUpMultiplier);
            }

            ColoredString costString = GetCostString();
            if (costString != null)
            {
                BitmapFont font = pinacolada.utilities.PCLRenderHelpers.GetEnergyFont(this);
                pinacolada.utilities.PCLRenderHelpers.WriteOnCard(sb, this, font, costString.text, -132f, 192f, costString.color);
                pinacolada.utilities.PCLRenderHelpers.ResetFont(font);
            }
        }
    }
}
