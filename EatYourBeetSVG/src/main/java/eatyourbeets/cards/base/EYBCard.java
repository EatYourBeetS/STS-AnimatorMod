package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.green.Tactician;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FlightPower;
import com.megacrit.cardcrawl.powers.LockOnPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BlueCandle;
import com.megacrit.cardcrawl.relics.MedicalKit;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import eatyourbeets.actions.special.HasteAction;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.BlockAttribute;
import eatyourbeets.cards.base.attributes.DamageAttribute;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT2;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.replacement.PlayerFlightPower;
import eatyourbeets.resources.AbstractResources;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorImages;
import eatyourbeets.ui.cards.CardPreview;
import eatyourbeets.ui.common.EYBCardPopup;
import eatyourbeets.utilities.*;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class EYBCard extends EYBCardBase
{
    private static final Map<String, EYBCardData> staticCardData = new HashMap<>();

    protected static final AnimatorImages ANIMATOR_IMAGES = GR.AnimatorClassic.Images;
    protected static final Color blueGlowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    protected static final Color goldenGlowColor = new Color(1, 0.843f, 0, 0.25f);
    protected static final Color greenGlowColor = AbstractCard.GREEN_BORDER_GLOW_COLOR.cpy();
    protected static final Color redGlowColor = new Color(1, 0.15f, 0.15f, 0.25f);
    protected Color borderIndicatorColor;
    protected CardPreview cardPreview;
    protected boolean usingAffinities;
    protected int affinityRequirement;

    public static final String UNPLAYABLE_MESSAGE = CardCrawlGame.languagePack.getCardStrings(Tactician.ID).EXTENDED_DESCRIPTION[0];
    public static final int UNPLAYABLE_COST = -2;
    public static final int X_COST = -1;
    public static final Color MUTED_TEXT_COLOR = Colors.Lerp(Color.DARK_GRAY, Settings.CREAM_COLOR, 0.5f);
    public static final CardTags HASTE = GR.Enums.CardTags.HASTE;
    public static final CardTags PURGE = GR.Enums.CardTags.PURGE;
    public static final CardTags DELAYED = GR.Enums.CardTags.DELAYED;
    public static final CardTags UNIQUE = GR.Enums.CardTags.UNIQUE;
    public static final CardTags VOLATILE = GR.Enums.CardTags.VOLATILE;
    public static final CardTags SUMMON = GR.Enums.CardTags.SUMMON;
    public static final CardTags ATTACHMENT = GR.Enums.CardTags.ATTACHMENT;
    public static final CardTags RECAST = GR.Enums.CardTags.RECAST;
    public static final CardTags FADING = GR.Enums.CardTags.FADING;
    public final EYBCardText cardText;
    public final EYBCardData cardData;
    public final EYBCardAffinities affinities;
    public final ArrayList<EYBCardTooltip> tooltips;
    public EYBCardTarget attackTarget = EYBCardTarget.Normal;
    public EYBAttackType attackType = EYBAttackType.Normal;

    public EYBCardCooldown cooldown;
    public boolean playAtEndOfTurn;
    public boolean isMultiUpgrade;
    public boolean canUpgrade;
    public boolean unplayable;
    public boolean inBattle;
    public int upgrade_damage;
    public int upgrade_magicNumber;
    public int upgrade_secondaryValue;
    public int upgrade_block;
    public int upgrade_cost;

    public abstract AbstractPlayer.PlayerClass GetPlayerClass();

    public static AbstractCard GetCurrentClassCard(String cardID, boolean upgraded)
    {
        return GR.CardLibrary.GetCurrentClassCard(cardID, upgraded);
    }

    public static EYBCardData GetStaticData(String cardID)
    {
        return staticCardData.get(cardID);
    }

    public static EYBCardData RegisterCardData(Class<? extends EYBCard> type, String cardID, AbstractResources resources)
    {
        final EYBCardData cardData = new EYBCardData(type, cardID, resources);
        staticCardData.put(cardData.ID, cardData);
        return cardData;
    }

    protected EYBCard(EYBCardData cardData)
    {
        this(cardData, cardData.ID, cardData.ImagePath, cardData.BaseCost, cardData.CardType, cardData.CardColor, cardData.CardRarity, cardData.CardTarget.ToCardTarget());
    }

    protected EYBCard(EYBCardData cardData, String id, String imagePath, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(id, cardData.Strings.NAME, imagePath, cost, "", type, color, rarity, target);

        this.cropPortrait = cardData.Metadata == null || cardData.Metadata.cropPortrait;
        this.cardData = cardData;
        this.canUpgrade = true;
        this.tooltips = new ArrayList<>();
        this.cardText = new EYBCardText(this);
        this.affinities = new EYBCardAffinities(this);
        initializeDescription();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return cardData.CreateNewInstance();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        EYBCard copy = (EYBCard) super.makeStatEquivalentCopy();

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

        copy.tags.clear();
        copy.tags.addAll(tags);
        copy.originalName = originalName;
        copy.name = name;
        copy.inBattle = inBattle;

        return copy;
    }

    public EYBCard MakePopupCopy(EYBCardPopup popup)
    {
        EYBCard copy = (EYBCard) makeSameInstanceOf();
        copy.current_x = (float) Settings.WIDTH / 2f;
        copy.current_y = (float) Settings.HEIGHT / 2f;
        copy.drawScale = copy.targetDrawScale = 2f;
        copy.isPopup = true;
        return copy;
    }

    public EYBCardPreview GetCardPreview()
    {
        return cardData.GetCardPreview();
    }

    public ColoredString GetBottomText()
    {
        return null;
    }

    public ColoredString GetHeaderText()
    {
        return null;
    }

    protected String GetRawDescription()
    {
        return GetRawDescription((Object[]) null);
    }

    protected String GetRawDescription(Object... args)
    {
        return upgraded && cardData.Strings.UPGRADE_DESCRIPTION != null
                ? JUtils.Format(cardData.Strings.UPGRADE_DESCRIPTION, args)
                : JUtils.Format(cardData.Strings.DESCRIPTION, args);
    }

    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }

    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }

    @Override
    public void renderUpgradePreview(SpriteBatch sb)
    {
        EYBCard upgrade = cardData.tempCard;

        if (upgrade == null || upgrade.uuid != this.uuid || (upgrade.timesUpgraded != (timesUpgraded + 1)))
        {
            upgrade = cardData.tempCard = (EYBCard) this.makeSameInstanceOf();
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
        // Called at the start of a fight, or when a card is created by MakeTempCard.
        this.inBattle = true;
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        this.dontTriggerOnUseCard = false;
    }

    @Override
    public final void triggerWhenCopied()
    {
        // this is only used by ShowCardAndAddToHandEffect
        triggerWhenDrawn();

        if (hasTag(HASTE))
        {
            GameActions.Bottom.Add(new HasteAction(this));
        }
    }

    @Override
    public void triggerOnGlowCheck()
    {
        super.triggerOnGlowCheck();

        this.glowColor = blueGlowColor;
        this.borderIndicatorColor = null;

        if (CheckSpecialCondition(false))
        {
            this.glowColor = greenGlowColor;
            this.borderIndicatorColor = glowColor;
        }
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        super.triggerOnEndOfTurnForPlayingCard();

        if (playAtEndOfTurn)
        {
            this.dontTriggerOnUseCard = true;

            AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
        }
    }

    public void triggerOnAffinitySeal(boolean reshuffle)
    {
        if (reshuffle)
        {
            GameActions.Top.Reshuffle(affinities.Card, player.hand);
        }
    }

    public void PurgeOnUseOnce()
    {
        if (player.cardInUse != this)
        {
            JUtils.LogError(this, "Only call PurgeOnUseOnce() from AbstractCard.use()");
        }

        unhover();
        untip();
        stopGlowing();

        SetTag(GR.Enums.CardTags.PURGING, true);
        GameEffects.List.Add(new ExhaustCardEffect(this));
    }

    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return affinityRequirement > 0 && CheckAffinities(tryUse);
    }

    public boolean CheckSpecialConditionLimited(boolean tryUse, FuncT1<Boolean, Boolean> condition)
    {
        boolean t = CombatStats.CanActivateLimited(cardID);
        if (t)
        {
            t = condition.Invoke(tryUse);
            if (t)
            {
                if (tryUse)
                {
                    return CombatStats.TryActivateLimited(cardID);
                }

                return true;
            }
        }

        return false;
    }

    public boolean CheckSpecialConditionSemiLimited(boolean tryUse, FuncT1<Boolean, Boolean> condition)
    {
        boolean t = CombatStats.CanActivateSemiLimited(cardID);
        if (t)
        {
            t = condition.Invoke(tryUse);
            if (t)
            {
                if (tryUse)
                {
                    return CombatStats.TryActivateSemiLimited(cardID);
                }

                return true;
            }
        }

        return false;
    }

    public boolean CheckAffinities(boolean tryUse)
    {
        final ArrayList<Pair<Affinity, Integer>> toUse = new ArrayList<>();
        for (Affinity a : Affinity.All(true))
        {
            final int req = affinities.GetRequirement(a);
            if (req > 0)
            {
                if (!CheckAffinity(a, req))
                {
                    return false;
                }
                else if (tryUse)
                {
                    toUse.add(new Pair<>(a, req));
                }
            }
        }

        for (Pair<Affinity, Integer> pair : toUse)
        {
            TryUseAffinity(pair.getKey(), pair.getValue());
        }

        return true;
    }

    public boolean CheckAffinity(Affinity affinity)
    {
        return CheckAffinity(affinity, affinities.GetRequirement(affinity));
    }

    public boolean CheckAffinity(Affinity affinity, int amount)
    {
        if (isInAutoplay || !CombatStats.Affinities.CanUseAffinities())
        {
            return false;
        }

        return CombatStats.Affinities.GetUsableAffinity(affinity) >= amount;
    }

    public boolean CheckAffinities(Affinity... affinities)
    {
        for (Affinity a : affinities)
        {
            if (!CheckAffinity(a))
            {
                return false;
            }
        }

        return true;
    }

    public boolean TryUseAffinities(Affinity... affinities)
    {
        if (CheckAffinities(affinities))
        {
            for (Affinity a : affinities)
            {
                TryUseAffinity(a);
            }

            return true;
        }

        return false;
    }

    public boolean TryUseAffinity(Affinity affinity)
    {
        return TryUseAffinity(affinity, affinities.GetRequirement(affinity));
    }

    public boolean TryUseAffinity(Affinity affinity, int amount)
    {
        if (isInAutoplay || !CombatStats.Affinities.CanUseAffinities())
        {
            return false;
        }

        return CombatStats.Affinities.TryUseAffinity(affinity, amount);
    }

    public int GetPlayerAffinity(Affinity affinity)
    {
        if (!CombatStats.Affinities.CanUseAffinities())
        {
            return -1;
        }

        return CombatStats.Affinities.GetUsableAffinity(affinity);
    }

    public boolean IsStarter()
    {
        return CombatStats.CanActivatedStarter();
//        final ArrayList<AbstractCard> played = AbstractDungeon.actionManager.cardsPlayedThisTurn;
//        return played == null || played.isEmpty() || (played.size() == 1 && played.get(0) == this);
    }

    public boolean IsAoE()
    {
        return isMultiDamage;
    }

    public boolean CanScale()
    {
        return baseBlock >= 0 || baseDamage >= 0;
    }

    public void GenerateDynamicTooltips(ArrayList<EYBCardTooltip> dynamicTooltips)
    {
        if (cardData.popupActions.size() > 0)
        {
            dynamicTooltips.add(GR.Tooltips.SpecialAction);
        }
        if (isInnate)
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
            dynamicTooltips.add(GR.Tooltips.Retain);
        }
        else if (retain)
        {
            dynamicTooltips.add(GR.Tooltips.RetainOnce);
        }
        if (hasTag(HASTE))
        {
            dynamicTooltips.add(GR.Tooltips.Haste);
        }
        if (hasTag(RECAST))
        {
            dynamicTooltips.add(GR.Tooltips.Recast);
        }
        if (purgeOnUse || hasTag(PURGE))
        {
            dynamicTooltips.add(GR.Tooltips.Purge);
        }
        if (exhaust || exhaustOnUseOnce)
        {
            dynamicTooltips.add(GR.Tooltips.Exhaust);
        }
        else if (hasTag(FADING))
        {
            dynamicTooltips.add(GR.Tooltips.Fading);
        }
        if (affinities.HasStar())
        {
            dynamicTooltips.add(GR.Tooltips.Affinity_Star);
        }

        if (attackType == EYBAttackType.Elemental)
        {
            dynamicTooltips.add(GR.Tooltips.Elemental);
        }
        else if (attackType == EYBAttackType.Piercing)
        {
            dynamicTooltips.add(GR.Tooltips.Piercing);
        }
        else if (attackType == EYBAttackType.Ranged)
        {
            dynamicTooltips.add(GR.Tooltips.Ranged);
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
        return baseDamage >= 0 ? DamageAttribute.Instance.SetCard(this) : null;
    }

    public AbstractAttribute GetBlockInfo()
    {
        return baseBlock >= 0 ? BlockAttribute.Instance.SetCard(this) : null;
    }

    public AbstractAttribute GetSpecialInfo()
    {
        return null;
    }

    @Override
    protected AdvancedTexture GetCardBorderIndicator()
    {
        return borderIndicatorColor == null ? null : new AdvancedTexture(ANIMATOR_IMAGES.CARD_BORDER_INDICATOR.Texture(), borderIndicatorColor);
    }

    public ColoredString GetAffinityString(ArrayList<Affinity> types, boolean requireAll)
    {
        if (types == null || types.size() == 0)
        {
            JUtils.LogError(this, "Types was null or empty.");
            return new ColoredString("?", Settings.RED_TEXT_COLOR);
        }

        final ColoredString result = new ColoredString();
        if (player != null && player.hand.contains(this))
        {
            for (Affinity t : types)
            {
                final int req = affinities.GetRequirement(t);
                final int level = GetPlayerAffinity(t);
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

            if (requireAll)
            {
                return result.SetColor(Settings.GREEN_TEXT_COLOR);
            }

            return result.SetColor(MUTED_TEXT_COLOR);
        }

        return result.SetText(affinities.GetRequirement(types.get(0))).SetColor(Settings.CREAM_COLOR);
    }

    public void SetAttackType(EYBAttackType attackType)
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

    public void SetHaste(boolean value)
    {
        SetTag(HASTE, value);
    }

    public void SetDelayed(boolean value)
    {
        SetTag(DELAYED, value);

        if (value)
        {
            SetInnate(false);
        }
    }

    public void SetRetain(boolean value)
    {
        this.selfRetain = value;
    }

    public void SetRetainOnce(boolean value)
    {
        this.retain = value;
    }

    public void SetInnate(boolean value)
    {
        this.isInnate = value;
    }

    public void SetExhaust(boolean value)
    {
        this.exhaust = value;
    }

    public void SetFading(boolean value)
    {
        this.SetTag(FADING, value);
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

    public void SetLoyal(boolean value)
    {
        SetTag(GR.Enums.CardTags.LOYAL, value);
    }

    public void SetObtainableInCombat(boolean value)
    {
        SetHealing(!value);
    }

    public void SetPlayable(boolean playable)
    {
        SetUnplayable(!playable);
    }

    public void SetUnplayable(boolean unplayable)
    {
        this.unplayable = unplayable;
    }

    public void SetEndOfTurnPlay(boolean playAtEndOfTurn)
    {
        this.playAtEndOfTurn = playAtEndOfTurn;
    }

    public void SetVolatile(boolean value)
    {
        SetTag(VOLATILE, value);
    }

    public void SetHealing(boolean value)
    {
        SetTag(CardTags.HEALING, value);
    }

    public void SetRecast(boolean value)
    {
        SetTag(RECAST, value);
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

    public void SetUnique(boolean value, boolean multiUpgrade)
    {
        SetTag(GR.Enums.CardTags.UNIQUE, value);
        isMultiUpgrade = multiUpgrade;
    }

    public void AddScaling(Affinity affinity, int amount)
    {
        affinities.Get(affinity, true).scaling += amount;
    }

    public void SetScaling(EYBCardAffinities affinities)
    {
        if (affinities.HasStar())
        {
            SetScaling(Affinity.Star, affinities.Star.scaling);
        }

        for (EYBCardAffinity a : affinities.List)
        {
            SetScaling(a.type, a.scaling);
        }
    }

    public void SetScaling(Affinity affinity, int amount)
    {
        affinities.Get(affinity, true).scaling = amount;
    }

    protected void SetAffinityRequirement(Affinity affinity, int requirement)
    {
        affinities.SetRequirement(affinity, requirement);
        affinityRequirement = requirement;
    }

    //@Formatter: Off
    protected void SetAffinity_Red(int base) { InitializeAffinity(Affinity.Red, base, 0, 0); }
    protected void SetAffinity_Red(int base, int upgrade, int scaling) { InitializeAffinity(Affinity.Red, base, upgrade, scaling); }
    protected void SetAffinity_Green(int base) { InitializeAffinity(Affinity.Green, base, 0, 0); }
    protected void SetAffinity_Green(int base, int upgrade, int scaling) { InitializeAffinity(Affinity.Green, base, upgrade, scaling); }
    protected void SetAffinity_Blue(int base) { InitializeAffinity(Affinity.Blue, base, 0, 0); }
    protected void SetAffinity_Blue(int base, int upgrade, int scaling) { InitializeAffinity(Affinity.Blue, base, upgrade, scaling); }
    protected void SetAffinity_Light(int base) { InitializeAffinity(Affinity.Light, base, 0, 0); }
    protected void SetAffinity_Light(int base, int upgrade, int scaling) { InitializeAffinity(Affinity.Light, base, upgrade, scaling); }
    protected void SetAffinity_Dark(int base) { InitializeAffinity(Affinity.Dark, base, 0, 0); }
    protected void SetAffinity_Dark(int base, int upgrade, int scaling) { InitializeAffinity(Affinity.Dark, base, upgrade, scaling); }
    protected void SetAffinity_Star(int base) { InitializeAffinity(Affinity.Star, base, 0, 0); }
    protected void SetAffinity_Star(int base, int upgrade, int scaling) { InitializeAffinity(Affinity.Star, base, upgrade, scaling); }
    protected void SetAffinity_General(int base) { InitializeAffinity(Affinity.General, base, 0, 0); }
    protected void InitializeAffinity(Affinity affinity, int base, int upgrade, int scaling) { affinities.Initialize(affinity, base, upgrade, scaling, 0); }
    //@Formatter: On

    public CardPreview SetCardPreview(ActionT2<RotatingList<AbstractCard>, AbstractMonster> findCards)
    {
        return this.cardPreview = new CardPreview(findCards)
                .RequireTarget(target == CardTarget.ENEMY || target == CardTarget.SELF_AND_ENEMY);
    }

    public CardPreview SetCardPreview(FuncT1<Boolean, AbstractCard> findCard)
    {
        return this.cardPreview = new CardPreview(findCard)
                .RequireTarget(target == CardTarget.ENEMY || target == CardTarget.SELF_AND_ENEMY);
    }

    protected boolean TryUpgrade()
    {
        return TryUpgrade(true);
    }

    protected boolean TryUpgrade(boolean updateDescription)
    {
        if (canUpgrade())
        {
            this.timesUpgraded += 1;
            this.upgraded = true;

            if (isMultiUpgrade)
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

    @Override
    public boolean canUpgrade()
    {
        return canUpgrade && (!upgraded || isMultiUpgrade);
    }

    @Override
    public void upgrade()
    {
        final boolean canUpgradeCache = canUpgrade;
        canUpgrade = true;

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

            if (upgrade_cost != 0)
            {
                final int previousCost = cost;
                final int previousCostForTurn = costForTurn;

                this.cost = Math.max(0, previousCost + upgrade_cost);
                this.costForTurn = Math.max(0, previousCostForTurn + upgrade_cost);
                this.upgradedCost = true;
            }

            affinities.ApplyUpgrades();

            OnUpgrade();
        }

        canUpgrade = canUpgradeCache;
    }

    @Override
    public void displayUpgrades()
    {
        displayUpgrades(true);
    }

    public void displayUpgrades(boolean displayUpgrades)
    {
        if (affinities.displayUpgrades = displayUpgrades)
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
        }
        else
        {
            isCostModified = false;
            isMagicNumberModified = false;
            isSecondaryValueModified = false;
            isDamageModified = false;
            isBlockModified = false;
        }
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

    public void OnDrag(AbstractMonster m)
    {
        if (cardPreview != null && cardPreview.enabled)
        {
            cardPreview.Update(this, m);
        }
    }

    protected void OnUpgrade()
    {

    }

    public EYBCardCooldown SetCooldown(int baseCooldown, int cooldownUpgrade, ActionT1<AbstractMonster> onCooldownCompleted)
    {
        return this.cooldown = new EYBCardCooldown(this, baseCooldown, cooldownUpgrade, onCooldownCompleted);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        if (cooldown != null)
        {
            cooldown.ProgressCooldown(false);
        }
    }

    @Override
    public boolean hasEnoughEnergy()
    {
        final boolean turnHasEnded = AbstractDungeon.actionManager.turnHasEnded;
        AbstractDungeon.actionManager.turnHasEnded = false;
        final boolean result = super.hasEnoughEnergy();
        AbstractDungeon.actionManager.turnHasEnded = turnHasEnded;

        return result;
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        cantUseMessage = UNPLAYABLE_MESSAGE;
        if (attackTarget == EYBCardTarget.Minion && !CanPlayOnMinion())
        {
            return false;
        }

        return !unplayable && super.cardPlayable(m);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m)
    {
        if (this.costForTurn == UNPLAYABLE_COST)
        {
            if ((this.type == CardType.STATUS && GameUtilities.HasRelic(MedicalKit.ID))
                    || (this.type == CardType.CURSE && GameUtilities.HasRelic(BlueCandle.ID)))
            {
                return super.cardPlayable(m) && super.hasEnoughEnergy();
            }
        }

        return !unplayable && super.canUse(p, m);
    }

    @Override
    public final void applyPowers()
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

        if (cooldown == null)
        {
            this.secondaryValue = this.baseSecondaryValue;
            this.isSecondaryValueModified = false;
        }
    }

    @Override
    public void Render(SpriteBatch sb, boolean hovered, boolean selected, boolean library)
    {
        super.Render(sb, hovered, selected, library);

        if (!library && cardPreview != null && cardPreview.enabled)
        {
            cardPreview.Render(sb);
        }
    }

    protected void Refresh(AbstractMonster enemy)
    {
        boolean applyEnemyPowers = (enemy != null && !GameUtilities.IsDeadOrEscaped(enemy));
        float tempBlock = GetInitialBlock();
        float tempDamage = GetInitialDamage();

        for (AbstractRelic r : player.relics)
        {
            tempDamage = r.atDamageModify(tempDamage, this);
        }

        tempBlock = CombatStats.Affinities.ModifyBlock(tempBlock, this);
        tempDamage = CombatStats.Affinities.ModifyDamage(tempDamage, this);

        for (AbstractPower p : player.powers)
        {
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
            if (attackType == EYBAttackType.Elemental)
            {
                if (enemy.currentBlock > 0)
                {
                    tempDamage *= 1.3f;
                }
            }
            else if (attackType == EYBAttackType.Ranged)
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
                        tempDamage *= 1.25f;
                    }
                }
            }

            for (AbstractPower p : enemy.powers)
            {
                tempDamage = p.atDamageReceive(tempDamage, damageTypeForTurn, this);
            }
        }

        tempDamage = player.stance.atDamageGive(tempDamage, damageTypeForTurn, this);

        for (AbstractPower p : player.powers)
        {
            tempDamage = p.atDamageFinalGive(tempDamage, damageTypeForTurn, this);
        }

        if (applyEnemyPowers)
        {
            for (AbstractPower p : enemy.powers)
            {
                tempDamage = p.atDamageFinalReceive(tempDamage, damageTypeForTurn, this);
            }
        }

        UpdateBlock(tempBlock);
        UpdateDamage(tempDamage);
    }

    protected void UpdateBlock(float amount)
    {
        block = MathUtils.floor(amount);
        if (block < 0 || baseBlock < 0)
        {
            block = 0;
        }
        this.isBlockModified = (baseBlock != block);
    }

    protected void UpdateDamage(float amount)
    {
        damage = MathUtils.floor(amount);
        if (damage < 0 || baseDamage < 0)
        {
            damage = 0;
        }
        this.isDamageModified = (baseDamage != damage);
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

    protected boolean CanPlayOnMinion()
    {
        return false;
    }
}
