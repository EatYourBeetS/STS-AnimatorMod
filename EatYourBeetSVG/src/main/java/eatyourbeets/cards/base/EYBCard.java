package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.green.Tactician;
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
import eatyourbeets.interfaces.subscribers.OnStartOfTurnSubscriber;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.replacement.PlayerFlightPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class EYBCard extends EYBCardBase implements OnStartOfTurnSubscriber
{
    public static final Color MUTED_TEXT_COLOR = Colors.Lerp(Color.DARK_GRAY, Settings.CREAM_COLOR, 0.5f);
    public static final CardTags HASTE = GR.Enums.CardTags.HASTE;
    public static final CardTags HASTE_INFINITE = GR.Enums.CardTags.HASTE_INFINITE;
    public static final CardTags PURGE = GR.Enums.CardTags.PURGE;
    public static final CardTags AUTOPLAY = GR.Enums.CardTags.AUTOPLAY;
    public final EYBCardText cardText;
    public final EYBCardData cardData;
    public final EYBCardAffinities affinities;
    public final ArrayList<EYBCardTooltip> tooltips;
    public EYBCardTarget attackTarget = EYBCardTarget.Normal;
    public EYBAttackType attackType = EYBAttackType.Normal;

    protected static final String UNPLAYABLE_MESSAGE = CardCrawlGame.languagePack.getCardStrings(Tactician.ID).EXTENDED_DESCRIPTION[0];
    private static final Map<String, EYBCardData> staticCardData = new HashMap<>();

    public boolean isMultiUpgrade;
    protected int upgrade_damage;
    protected int upgrade_magicNumber;
    protected int upgrade_secondaryValue;
    protected int upgrade_block;
    protected int upgrade_cost;

    public static EYBCardData GetStaticData(String cardID)
    {
        return staticCardData.get(cardID);
    }

    public static EYBCardData RegisterCardData(Class<? extends EYBCard> type, String cardID)
    {
        EYBCardData cardData = new EYBCardData(type, cardID);
        cardData.Metadata = GR.Animator.CardData.get(cardID);
        staticCardData.put(cardID, cardData);
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

        copy.affinities.Initialize(affinities);
        copy.magicNumber = magicNumber;
        copy.isMagicNumberModified = isMagicNumberModified;

        copy.secondaryValue = secondaryValue;
        copy.baseSecondaryValue = baseSecondaryValue;
        copy.isSecondaryValueModified = isSecondaryValueModified;

        copy.tags.clear();
        copy.tags.addAll(tags);

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

    public boolean CheckAffinity(Affinity affinity)
    {
        return GetHandAffinity(affinity, true) >= affinities.GetRequirement(affinity);
    }

    public int GetHandAffinity(Affinity affinity)
    {
        return GetHandAffinity(affinity, true);
    }

    public int GetHandAffinity(Affinity affinity, boolean ignoreSelf)
    {
        return CombatStats.Affinities.GetHandAffinities(ignoreSelf ? this : null).GetLevel(affinity, false);
    }

    public boolean IsStarter()
    {
        ArrayList<AbstractCard> played = AbstractDungeon.actionManager.cardsPlayedThisTurn;
        return played == null || played.isEmpty() || (played.size() == 1 && played.get(0) == this);
    }

    public boolean IsAoE()
    {
        return isMultiDamage;
    }

    public void GenerateDynamicTooltips(ArrayList<EYBCardTooltip> dynamicTooltips)
    {
        if (AfterLifeMod.IsAdded(this))
        {
            dynamicTooltips.add(GR.Tooltips.Afterlife);
        }
        if (isInnate)
        {
            dynamicTooltips.add(GR.Tooltips.Innate);
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
                final int level = hand.GetLevel(t, false);
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

    public void SetHaste(boolean value)
    {
        SetTag(HASTE, value);
    }

    public void SetPermanentHaste(boolean value)
    {
        SetTag(HASTE_INFINITE, value);
        SetTag(HASTE, value);
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

    public void SetObtainableInCombat(boolean value)
    {
        SetHealing(!value);
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

    protected void SetAffinityRequirement(Affinity affinity, int requirement)
    {
        affinities.SetRequirement(affinity, requirement);
    }

    //@Formatter: Off
    protected void SetAffinity_Red(int base) { InitializeAffinity(Affinity.Red, base, 0, 0); }
    protected void SetAffinity_Red(int base, int upgrade, int scaling) { InitializeAffinity(Affinity.Red, base, upgrade, scaling); }
    protected void SetAffinity_Green(int base) { InitializeAffinity(Affinity.Green, base, 0, 0); }
    protected void SetAffinity_Green(int base, int upgrade, int scaling) { InitializeAffinity(Affinity.Green, base, upgrade, scaling); }
    protected void SetAffinity_Blue(int base) { InitializeAffinity(Affinity.Blue, base, 0, 0); }
    protected void SetAffinity_Blue(int base, int upgrade, int scaling) { InitializeAffinity(Affinity.Blue, base, upgrade, scaling); }
    protected void SetAffinity_Orange(int base) { InitializeAffinity(Affinity.Orange, base, 0, 0); }
    protected void SetAffinity_Orange(int base, int upgrade, int scaling) { InitializeAffinity(Affinity.Orange, base, upgrade, scaling); }
    protected void SetAffinity_Light(int base) { InitializeAffinity(Affinity.Light, base, 0, 0); }
    protected void SetAffinity_Light(int base, int upgrade, int scaling) { InitializeAffinity(Affinity.Light, base, upgrade, scaling); }
    protected void SetAffinity_Dark(int base) { InitializeAffinity(Affinity.Dark, base, 0, 0); }
    protected void SetAffinity_Dark(int base, int upgrade, int scaling) { InitializeAffinity(Affinity.Dark, base, upgrade, scaling); }
    protected void SetAffinity_Star(int base) { InitializeAffinity(Affinity.Star, base, 0, 0); }
    protected void SetAffinity_Star(int base, int upgrade, int scaling) { InitializeAffinity(Affinity.Star, base, upgrade, scaling); }
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
                this.name = cardData.Strings.NAME + "+" + this.timesUpgraded;
            }
            else
            {
                this.name = cardData.Strings.NAME + "+";
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
        return super.cardPlayable(m);
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
                        tempDamage *= 1.5f;
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
}
