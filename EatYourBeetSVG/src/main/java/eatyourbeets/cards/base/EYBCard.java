package eatyourbeets.cards.base;

import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
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
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import eatyourbeets.actions.special.HasteAction;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.BlockAttribute;
import eatyourbeets.cards.base.attributes.DamageAttribute;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.FuncT0;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnSubscriber;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.ElementalExposurePower;
import eatyourbeets.powers.replacement.PlayerFlightPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static eatyourbeets.powers.animator.ElementalExposurePower.ELEMENTAL_MODIFIER;
import static eatyourbeets.powers.replacement.AnimatorLockOnPower.GetAttackMultiplier;

public abstract class EYBCard extends EYBCardBase implements OnStartOfTurnSubscriber, OnStartOfTurnPostDrawSubscriber, CustomSavable<EYBCardSaveData>
{
    public static final Color MUTED_TEXT_COLOR = Colors.Lerp(Color.DARK_GRAY, Settings.CREAM_COLOR, 0.5f);
    public static final CardTags HASTE = GR.Enums.CardTags.HASTE;
    public static final CardTags HASTE_INFINITE = GR.Enums.CardTags.HASTE_INFINITE;
    public static final CardTags PURGE = GR.Enums.CardTags.PURGE;
    public static final CardTags DELAYED = GR.Enums.CardTags.DELAYED;
    public static final CardTags AUTOPLAY = GR.Enums.CardTags.AUTOPLAY;
    public static final CardTags LOYAL = GR.Enums.CardTags.LOYAL;
    public static final CardTags PROTAGONIST = GR.Enums.CardTags.PROTAGONIST;
    public static final CardTags ANIMATOR_INNATE = GR.Enums.CardTags.ANIMATOR_INNATE;
    public final EYBCardText cardText;
    public final EYBCardData cardData;
    public final EYBCardAffinities affinities;
    public final ArrayList<EYBCardTooltip> tooltips;
    public EYBCardTarget attackTarget = EYBCardTarget.Normal;
    public EYBAttackType attackType = EYBAttackType.Normal;

    protected static final String UNPLAYABLE_MESSAGE = CardCrawlGame.languagePack.getCardStrings(Tactician.ID).EXTENDED_DESCRIPTION[0];
    private static final Map<String, EYBCardData> staticCardData = new HashMap<>();

    public boolean isMultiUpgrade;
    protected boolean unplayable;
    protected int upgrade_damage;
    protected int upgrade_magicNumber;
    protected int upgrade_secondaryValue;
    protected int upgrade_cooldownValue;
    protected int upgrade_block;
    protected int upgrade_cost;

    public EYBCardSaveData auxiliaryData = new EYBCardSaveData();

    public static EYBCardData GetStaticData(String cardID)
    {
        return staticCardData.get(cardID);
    }

    public static EYBCardData RegisterCardData(Class<? extends EYBCard> type, String cardID)
    {
        return RegisterCardData(new EYBCardData(type, cardID));
    }

    public static EYBCardData RegisterCardData(EYBCardData cardData)
    {
        cardData.Metadata = GR.Animator.CardData.get(cardData.ID);
        staticCardData.put(cardData.ID, cardData);
        return cardData;
    }

    protected EYBCard(EYBCardData cardData)
    {
        this(cardData, cardData.ID, cardData.ImagePath, cardData.BaseCost, cardData.CardType, cardData.CardColor, cardData.CardRarity, cardData.CardTarget.ToCardTarget(), 0, 0);
    }

    protected EYBCard(EYBCardData cardData, int form, int timesUpgraded)
    {
        this(cardData, cardData.ID, cardData.ImagePath, cardData.BaseCost, cardData.CardType, cardData.CardColor, cardData.CardRarity, cardData.CardTarget.ToCardTarget(), form, timesUpgraded);
    }

    protected EYBCard(EYBCardData cardData, String id, String imagePath, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        this(cardData,id,imagePath,cost,type,color,rarity,target,0, 0);
    }

    protected EYBCard(EYBCardData cardData, String id, String imagePath, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target, int form, int timesUpgraded)
    {
        super(id, cardData.Strings.NAME, imagePath, cost, "", type, color, rarity, target);

        this.cropPortrait = cardData.Metadata == null || cardData.Metadata.cropPortrait;
        this.cardData = cardData;
        this.tooltips = new ArrayList<>();
        this.cardText = new EYBCardText(this);
        this.affinities = new EYBCardAffinities(this);
        initializeDescription();

        SetForm(form, timesUpgraded);
    }

    @Override
    public AbstractCard makeCopy()
    {
        AbstractCard card = cardData.CreateNewInstance();
        if (card instanceof EYBCard && auxiliaryData.form > 0) {
            ((EYBCard) card).SetForm(auxiliaryData.form, timesUpgraded);
        }
        return card;
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
        copy.auxiliaryData = new EYBCardSaveData(auxiliaryData.form, auxiliaryData.additionalData);

        copy.tags.clear();
        copy.tags.addAll(tags);
        copy.originalName = originalName;
        copy.name = name;

        return copy;
    }

    public EYBCard MakePopupCopy()
    {
        EYBCard copy = (EYBCard) makeStatEquivalentCopy();
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

    @Override
    public void renderUpgradePreview(SpriteBatch sb)
    {
        EYBCard upgrade = cardData.tempCard;

        if (upgrade == null || upgrade.uuid != this.uuid || (upgrade.timesUpgraded != (timesUpgraded + 1)))
        {
            upgrade = cardData.tempCard = (EYBCard) this.makeSameInstanceOf();
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
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (hasTag(AUTOPLAY))
        {
            GameActions.Bottom.PlayCard(this, player.hand, null)
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
            GameActions.Bottom.Add(new HasteAction(this));
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
            JUtils.LogError(this, "Only call PurgeOnUseOnce() from AbstractCard.use()");
        }

        unhover();
        untip();
        stopGlowing();

        SetTag(GR.Enums.CardTags.PURGING, true);
        GameEffects.List.Add(new ExhaustCardEffect(this));
    }

    // Condition text will be green if this passes
    public boolean CheckPrimaryCondition(boolean tryUse) {
        return false;
    }

    // Card will glow if this passes
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return false;
    }

    public boolean CheckAffinity(Affinity affinity)
    {
        return GetLevelAffinity(affinity) >= affinities.GetRequirement(affinity);
    }

    public int GetLevelAffinity(Affinity affinity)
    {
        return GameUtilities.GetAffinityAmount(affinity);
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
        return baseBlock >= 0 || baseDamage >= 0;
    }

    public void GenerateDynamicTooltips(ArrayList<EYBCardTooltip> dynamicTooltips)
    {
        if (AfterLifeMod.IsAdded(this))
        {
            dynamicTooltips.add(GR.Tooltips.Afterlife);
        }
        if (isInnate || hasTag(ANIMATOR_INNATE))
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
        if (hasTag(HASTE_INFINITE))
        {
            dynamicTooltips.add(GR.Tooltips.HasteInfinite);
        }
        else if (hasTag(HASTE))
        {
            dynamicTooltips.add(GR.Tooltips.Haste);
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
        if (hasTag(PROTAGONIST))
        {
            dynamicTooltips.add(GR.Tooltips.Protagonist);
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
            final EYBCardAffinities hand = CombatStats.Affinities.GetHandAffinities(this);
            for (Affinity t : types)
            {
                final int req = affinities.GetRequirement(t);
                final int level = GameUtilities.GetAffinityAmount(t);
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
        SetTag(ANIMATOR_INNATE, value);

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
            SetTag(ANIMATOR_INNATE, false);
        }
    }

    public void SetProtagonist(boolean value)
    {
        SetTag(PROTAGONIST, value);
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
            CombatStats.onStartOfTurn.Subscribe(this);
        }
        else {
            CombatStats.onStartOfTurn.Unsubscribe(this);
        }
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

    //Changed to count affinity level amounts
    protected void SetAffinityRequirement(Affinity affinity, int requirement)
    {
        affinities.SetRequirement(affinity, requirement);
    }

    //@Formatter: Off
    protected void SetAffinity_Fire() { InitializeAffinity(Affinity.Fire, 1, 0, 0); }
    protected void SetAffinity_Fire(int base) { InitializeAffinity(Affinity.Fire, base, 0, 0); }
    protected void SetAffinity_Fire(int base, int upgrade, int scaling) { InitializeAffinity(Affinity.Fire, base, upgrade, scaling); }
    protected void SetAffinity_Air() { InitializeAffinity(Affinity.Air, 1, 0, 0); }
    protected void SetAffinity_Air(int base) { InitializeAffinity(Affinity.Air, base, 0, 0); }
    protected void SetAffinity_Air(int base, int upgrade, int scaling) { InitializeAffinity(Affinity.Air, base, upgrade, scaling); }
    protected void SetAffinity_Mind() { InitializeAffinity(Affinity.Mind, 1, 0, 0); }
    protected void SetAffinity_Mind(int base) { InitializeAffinity(Affinity.Mind, base, 0, 0); }
    protected void SetAffinity_Mind(int base, int upgrade, int scaling) { InitializeAffinity(Affinity.Mind, base, upgrade, scaling); }
    protected void SetAffinity_Earth() { InitializeAffinity(Affinity.Earth, 1, 0, 0); }
    protected void SetAffinity_Earth(int base) { InitializeAffinity(Affinity.Earth, base, 0, 0); }
    protected void SetAffinity_Earth(int base, int upgrade, int scaling) { InitializeAffinity(Affinity.Earth, base, upgrade, scaling); }
    protected void SetAffinity_Light() { InitializeAffinity(Affinity.Light, 1, 0, 0); }
    protected void SetAffinity_Light(int base) { InitializeAffinity(Affinity.Light, base, 0, 0); }
    protected void SetAffinity_Light(int base, int upgrade, int scaling) { InitializeAffinity(Affinity.Light, base, upgrade, scaling); }
    protected void SetAffinity_Dark() { InitializeAffinity(Affinity.Dark, 1, 0, 0); }
    protected void SetAffinity_Dark(int base) { InitializeAffinity(Affinity.Dark, base, 0, 0); }
    protected void SetAffinity_Dark(int base, int upgrade, int scaling) { InitializeAffinity(Affinity.Dark, base, upgrade, scaling); }
    protected void SetAffinity_Water() { InitializeAffinity(Affinity.Water, 1, 0, 0); }
    protected void SetAffinity_Water(int base) { InitializeAffinity(Affinity.Water, base, 0, 0); }
    protected void SetAffinity_Water(int base, int upgrade, int scaling) { InitializeAffinity(Affinity.Water, base, upgrade, scaling); }
    protected void SetAffinity_Poison() { InitializeAffinity(Affinity.Poison, 1, 0, 0); }
    protected void SetAffinity_Poison(int base) { InitializeAffinity(Affinity.Poison, base, 0, 0); }
    protected void SetAffinity_Poison(int base, int upgrade, int scaling) { InitializeAffinity(Affinity.Poison, base, upgrade, scaling); }
    protected void SetAffinity_Steel() { InitializeAffinity(Affinity.Steel, 1, 0, 0); }
    protected void SetAffinity_Steel(int base) { InitializeAffinity(Affinity.Steel, base, 0, 0); }
    protected void SetAffinity_Steel(int base, int upgrade, int scaling) { InitializeAffinity(Affinity.Steel, base, upgrade, scaling); }
    protected void SetAffinity_Thunder() { InitializeAffinity(Affinity.Thunder, 1, 0, 0); }
    protected void SetAffinity_Thunder(int base) { InitializeAffinity(Affinity.Thunder, base, 0, 0); }
    protected void SetAffinity_Thunder(int base, int upgrade, int scaling) { InitializeAffinity(Affinity.Thunder, base, upgrade, scaling); }
    protected void SetAffinity_Nature() { InitializeAffinity(Affinity.Nature, 1, 0, 0); }
    protected void SetAffinity_Nature(int base) { InitializeAffinity(Affinity.Nature, base, 0, 0); }
    protected void SetAffinity_Nature(int base, int upgrade, int scaling) { InitializeAffinity(Affinity.Nature, base, upgrade, scaling); }
    protected void SetAffinity_Cyber() { InitializeAffinity(Affinity.Cyber, 1, 0, 0); }
    protected void SetAffinity_Cyber(int base) { InitializeAffinity(Affinity.Cyber, base, 0, 0); }
    protected void SetAffinity_Cyber(int base, int upgrade, int scaling) { InitializeAffinity(Affinity.Cyber, base, upgrade, scaling); }
    protected void SetAffinity_Star(int base) { InitializeAffinity(Affinity.Star, base, 0, 0); }
    protected void SetAffinity_Star(int base, int upgrade, int scaling) { InitializeAffinity(Affinity.Star, base, upgrade, scaling); }
    protected void SetAffinity_General(int base) { InitializeAffinity(Affinity.General, base, 0, 0); }
    protected void InitializeAffinity(Affinity affinity, int base, int upgrade, int scaling) { affinities.Initialize(affinity, base, upgrade, scaling, 0); }
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
        return !upgraded || isMultiUpgrade;
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

    public EYBCardCooldown SetCooldown(int baseCooldown, int cooldownUpgrade, ActionT1<AbstractMonster> onCooldownCompleted)
    {
        this.cooldown = new EYBCardCooldown(this, baseCooldown, cooldownUpgrade, onCooldownCompleted);
        return this.cooldown;
    }

    public EYBCardCooldown SetCooldown(int baseCooldown, int cooldownUpgrade, ActionT1<AbstractMonster> onCooldownCompleted, boolean canProgressOnManualDiscard, boolean canProgressFromExhaustPile, boolean canProgressOnDraw)
    {
        this.cooldown = new EYBCardCooldown(this, baseCooldown, cooldownUpgrade, onCooldownCompleted, canProgressOnManualDiscard, canProgressFromExhaustPile, canProgressOnDraw);
        if (canProgressFromExhaustPile) {
            CombatStats.onStartOfTurnPostDraw.Subscribe(this);
        }
        return this.cooldown;
    }

    public EYBCardCooldown SetRicochet(int baseCooldown, int cooldownUpgrade, ActionT1<AbstractMonster> onCooldownCompleted)
    {
        this.cooldown = new EYBCardCooldown(this, baseCooldown, cooldownUpgrade, onCooldownCompleted, false, true, false);
        return this.cooldown;
    }

    public EYBCardCooldown SetSoul(int baseCooldown, int cooldownUpgrade, FuncT0<EYBCard> cardConstructor)
    {
        this.cooldown = new EYBCardCooldown(this, baseCooldown, cooldownUpgrade, cardConstructor, false, false, true, false);
        return this.cooldown;
    }


    public void OnDrag(AbstractMonster m)
    {

    }

    protected void OnUpgrade()
    {

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
                for (AbstractPower power : enemy.powers) {
                    if (ElementalExposurePower.POWER_ID.equals(power.ID))
                    {
                        tempDamage *= (1 + (ELEMENTAL_MODIFIER / 100f));
                    }
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
                        tempDamage *= (1 + (GetAttackMultiplier() / 100f));
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
            tempDamage = CombatStats.OnDamageOverride(enemy, damageTypeForTurn, tempDamage, this);
        }

        UpdateBlock(tempBlock);
        UpdateDamage(tempDamage);
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
    public EYBCardSaveData onSave()
    {
        return auxiliaryData;
    }

    @Override
    public void onLoad(EYBCardSaveData data)
    {
        if (data != null) {
            SetForm(data.form, timesUpgraded);
        }
    }

    @Override
    public Type savedType() {
        return new TypeToken<EYBCardSaveData>(){}.getType();
    }

    public int SetForm(Integer form, int timesUpgraded) {
        this.auxiliaryData.form = (form == null) ? 0 : MathUtils.clamp(form,0,this.cardData.MaxForms - 1);
        this.cardText.ForceRefresh();
        return this.auxiliaryData.form;
    };

}
