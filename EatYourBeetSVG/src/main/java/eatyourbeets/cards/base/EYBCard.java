package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FlightPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import eatyourbeets.actions.special.HasteAction;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.BlockAttribute;
import eatyourbeets.cards.base.attributes.DamageAttribute;
import eatyourbeets.powers.common.PlayerFlightPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class EYBCard extends EYBCardBase
{
    private static final Map<String, EYBCardData> staticCardData = new HashMap<>();

    protected boolean isMultiUpgrade;
    protected int upgrade_damage;
    protected int upgrade_magicNumber;
    protected int upgrade_secondaryValue;
    protected int upgrade_block;
    protected int upgrade_cost;

    public static final CardTags HASTE = GR.Enums.CardTags.HASTE;
    public static final CardTags PURGE = GR.Enums.CardTags.PURGE;
    public final EYBCardText cardText;
    public final EYBCardData cardData;
    public final ArrayList<EYBCardTooltip> tooltips;
    public EYBCardTarget attackTarget = EYBCardTarget.Normal;
    public EYBAttackType attackType = EYBAttackType.Normal;
    public float forceScaling = 0;
    public float intellectScaling = 0;
    public float agilityScaling = 0;

    public abstract ColoredString GetBottomText();
    public abstract ColoredString GetHeaderText();

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

        if (cardData.Metadata != null)
        {
            this.cropPortrait = cardData.Metadata.cropPortrait;
        }
        else
        {
            this.cropPortrait = true;
        }

        this.cardData = cardData;
        this.tooltips = new ArrayList<>();
        this.cardText = new EYBCardText(this);
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

        copy.forceScaling = forceScaling;
        copy.agilityScaling = agilityScaling;
        copy.intellectScaling = intellectScaling;

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

    protected String GetRawDescription()
    {
        if (upgraded && cardData.Strings.UPGRADE_DESCRIPTION != null)
        {
            return cardData.Strings.UPGRADE_DESCRIPTION;
        }
        else
        {
            return cardData.Strings.DESCRIPTION;
        }
    }

    protected String GetRawDescription(Object... args)
    {
        if (upgraded && cardData.Strings.UPGRADE_DESCRIPTION != null)
        {
            return JavaUtilities.Format(cardData.Strings.UPGRADE_DESCRIPTION, args);
        }
        else
        {
            return JavaUtilities.Format(cardData.Strings.DESCRIPTION, args);
        }
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

    @Override
    public final void triggerOnScry()
    {

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

    public void PurgeOnUseOnce()
    {
        if (player.cardInUse != this)
        {
            JavaUtilities.GetLogger(this).error("You should only call PurgeOnUseOnce() within use(p, m)");
        }

        unhover();
        untip();
        stopGlowing();

        SetTag(GR.Enums.CardTags.PURGING, true);
        GameEffects.List.Add(new ExhaustCardEffect(this));
    }

    public boolean IsStarter()
    {
        ArrayList<AbstractCard> played = AbstractDungeon.actionManager.cardsPlayedThisTurn;
        if (played == null || played.isEmpty())
        {
            return true;
        }

        return played.get(0) == this;
    }

    public boolean IsAoE()
    {
        return isMultiDamage;
    }

    public void GenerateDynamicTooltips(ArrayList<EYBCardTooltip> dynamicTooltips)
    {
        if (isInnate)
        {
            dynamicTooltips.add(GR.Tooltips.Innate);
        }
        if (isEthereal)
        {
            dynamicTooltips.add(GR.Tooltips.Ethereal);
        }
        if (retain || selfRetain)
        {
            dynamicTooltips.add(GR.Tooltips.Retain);
        }
        if (hasTag(HASTE))
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
        if (type == CardType.ATTACK)
        {
            return GetDamageInfo();
        }
        else
        {
            return GetBlockInfo();
        }
    }

    public AbstractAttribute GetSecondaryInfo()
    {
        if (type == CardType.ATTACK)
        {
            return GetBlockInfo();
        }
        else
        {
            return GetDamageInfo();
        }
    }

    public AbstractAttribute GetDamageInfo()
    {
        if (baseDamage >= 0)
        {
            return DamageAttribute.Instance.SetCard(this);
        }
        else
        {
            return null;
        }
    }

    public AbstractAttribute GetBlockInfo()
    {
        if (baseBlock >= 0)
        {
            return BlockAttribute.Instance.SetCard(this);
        }
        else
        {
            return null;
        }
    }

    public AbstractAttribute GetSpecialInfo()
    {
        return null;
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

    public void SetRetain(boolean value)
    {
        this.selfRetain = value;
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

    public void SetHealing(boolean value)
    {
        SetTag(CardTags.HEALING, value);
    }

    public void SetPurge(boolean value)
    {
        SetTag(PURGE, value);

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

    protected boolean CanSubscribeToEvents()
    {
        return GameUtilities.InBattle() && !CardCrawlGame.isPopupOpen;
    }

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
    }

    protected void Initialize(int damage, int block)
    {
        Initialize(damage, block, -1, 0);
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

    protected void SetScaling(float intellect, float agility, float force)
    {
        this.intellectScaling = intellect;
        this.agilityScaling = agility;
        this.forceScaling = force;
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

    protected void OnUpgrade()
    {

    }

    @Override
    protected final void applyPowersToBlock()
    {
        throw new RuntimeException("This method must not be called");
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

    protected void Refresh(AbstractMonster enemy)
    {
        boolean applyEnemyPowers = (enemy != null && !GameUtilities.IsDeadOrEscaped(enemy));
        float tempBlock = GetInitialBlock();
        float tempDamage = GetInitialDamage();

        for (AbstractRelic r : player.relics)
        {
            tempDamage = r.atDamageModify(tempDamage, this);
        }

        for (AbstractPower p : player.powers)
        {
            tempBlock = p.modifyBlock(tempBlock, this);
            tempDamage = p.atDamageGive(tempDamage, damageTypeForTurn, this);
        }

        tempBlock = ModifyBlock(enemy, tempBlock);
        tempDamage = ModifyDamage(enemy, tempDamage);

        if (applyEnemyPowers)
        {
            if (attackType == EYBAttackType.Elemental && enemy.currentBlock > 0)
            {
                tempDamage *= 1.3f;
            }
            else if (attackType == EYBAttackType.Ranged && (enemy.hasPower(FlightPower.POWER_ID) || enemy.hasPower(PlayerFlightPower.POWER_ID)))
            {
                for (AbstractPower power : enemy.powers)
                {
                    if (FlightPower.POWER_ID.equals(power.ID) || PlayerFlightPower.POWER_ID.equals(power.ID))
                    {
                        tempDamage *= 2f;
                    }
//                  else if (LockOnPower.POWER_ID.equals(power.ID))
//                  {
//                      tempDamage *= 1.3f;
//                  }
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
