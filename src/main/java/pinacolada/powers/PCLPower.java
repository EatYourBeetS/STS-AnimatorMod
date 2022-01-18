package pinacolada.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.Mathf;
import pinacolada.actions.powers.IncreasePower;
import pinacolada.actions.powers.ReducePower;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.powers.EYBFlashPowerEffect;
import pinacolada.effects.powers.EYBGainPowerEffect;
import pinacolada.relics.PCLRelic;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLJUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.StringJoiner;

public abstract class PCLPower extends AbstractPower implements CloneablePowerInterface
{
    protected static final FieldInfo<ArrayList<AbstractGameEffect>> _effect = PCLJUtils.GetField("effect", AbstractPower.class);
    protected static final float ICON_SIZE = 32f;
    protected static final float ICON_SIZE2 = 48f;

    protected static final Color disabledColor = new Color(0.5f, 0.5f, 0.5f, 1);
    protected final ArrayList<AbstractGameEffect> effects;
    protected PowerStrings powerStrings;

    public static AbstractPlayer player = null;
    public static Random rng = null;
    public TextureAtlas.AtlasRegion powerIcon;
    public AbstractCreature source;
    public boolean hideAmount = false;
    public boolean canBeZero = false;
    public boolean enabled = true;
    public boolean useTemporaryColoring = false;
    public int maxAmount = 9999;
    public int baseAmount = 0;

    public static String CreateFullID(Class<? extends PCLPower> type)
    {
        return GR.PCL.CreateID(type.getSimpleName());
    }

    public static String DeriveID(String base)
    {
        return base + "Power";
    }

    /** cardData, relic and originalID are exclusive of one another */
    protected PCLPower(AbstractCreature owner, PCLCardData cardData, PCLRelic relic, String originalID)
    {
        this.effects = _effect.Get(this);
        this.owner = owner;

        if (originalID != null)
        {
            final String imagePath = GR.GetPowerImage(originalID);
            if (Gdx.files.internal(imagePath).exists())
            {
                this.img = GR.GetTexture(imagePath);
            }
            if (this.img == null)
            {
                this.img = GR.GetTexture(GR.PCL.CreateID("UnknownPower"));
            }

            this.ID = originalID;
            this.powerStrings = CardCrawlGame.languagePack.getPowerStrings(originalID);
        }
        else
        {
            this.powerStrings = new PowerStrings();

            if (relic != null)
            {
                this.ID = DeriveID(relic.relicId);
                this.powerIcon = relic.GetPowerIcon();
                this.powerStrings.NAME = relic.name;
                this.powerStrings.DESCRIPTIONS = relic.DESCRIPTIONS;
            }
            else
            {
                this.ID = DeriveID(cardData.ID);
                this.powerIcon = cardData.GetCardIcon();
                this.powerStrings.NAME = cardData.Strings.NAME;
                this.powerStrings.DESCRIPTIONS = cardData.Strings.EXTENDED_DESCRIPTION;
            }
        }

        this.name = powerStrings.NAME;
    }

    public PCLPower(AbstractCreature owner, PCLRelic relic)
    {
        this(owner, null, relic, null);
    }

    public PCLPower(AbstractCreature owner, PCLCardData cardData)
    {
        this(owner, cardData, null, null);
    }

    public PCLPower(AbstractCreature owner, String id)
    {
        this(owner, null, null, id);
    }

    public PCLPower(AbstractCreature owner, AbstractCreature source, String id)
    {
        this(owner, id);
        this.source = source;
    }


    protected void Initialize(int amount)
    {
        Initialize(amount, PowerType.BUFF, false);
    }

    protected void Initialize(int amount, PowerType type, boolean turnBased)
    {
        this.baseAmount = this.amount = Mathf.Min(maxAmount, amount);
        this.type = type;
        this.isTurnBased = turnBased;
        updateDescription();
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        super.onApplyPower(power, target, source);

        if (owner == target && power.ID.equals(ID))
        {
            OnSamePowerApplied(power);
        }
    }

    @Override
    public void updateDescription()
    {
        if (this instanceof InvisiblePower)
        {
            this.description = "";
            return;
        }

        if (powerStrings == null || powerStrings.DESCRIPTIONS == null) {
            this.description = "";
            PCLJUtils.LogError(this, "powerStrings.Description does not exist, " + this.name);
            return;
        }

        switch (powerStrings.DESCRIPTIONS.length)
        {
            case 0:
            {
                PCLJUtils.LogError(this, "powerStrings.Description was an empty array, " + this.name);
                break;
            }

            case 1:
            {
                this.description = FormatDescription(0, amount);
                break;
            }

            case 2:
            {
                this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1];
                break;
            }

            default:
            {
                StringJoiner stringJoiner = new StringJoiner(Integer.toString(this.amount));
                for (String s : powerStrings.DESCRIPTIONS)
                {
                    stringJoiner.add(s);
                }
                this.description = stringJoiner.toString();
            }
        }
    }

    @Override
    public AbstractPower makeCopy()
    {
        if (this instanceof InvisiblePower)
        {
            PCLJUtils.LogError(this, "Do not clone powers which implement InvisiblePower");
            return null;
        }

        Constructor<? extends PCLPower> c;
        try
        {
            c = PCLJUtils.TryGetConstructor(getClass(), AbstractCreature.class, int.class);
            if (c != null)
            {
                return c.newInstance(owner, amount);
            }
            c = PCLJUtils.TryGetConstructor(getClass(), AbstractCreature.class);
            if (c != null)
            {
                return c.newInstance(owner);
            }
            c = PCLJUtils.TryGetConstructor(getClass(), AbstractCreature.class, AbstractCreature.class, int.class);
            if (c != null)
            {
                return c.newInstance(owner, source, amount);
            }
            c = PCLJUtils.TryGetConstructor(getClass());
            if (c != null)
            {
                return c.newInstance();
            }
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void flash()
    {
        this.effects.add(new EYBGainPowerEffect(this, true));
        PCLGameEffects.List.Add(new EYBFlashPowerEffect(this));
    }

    @Override
    public void flashWithoutSound()
    {
        this.effects.add(new EYBGainPowerEffect(this, false));
        PCLGameEffects.List.Add(new EYBFlashPowerEffect(this));
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c)
    {
        if (!enabled)
        {
            disabledColor.a = c.a;
            c = disabledColor;
        }

        if (this.useTemporaryColoring) {
            Color finalC = c;
            pinacolada.utilities.PCLRenderHelpers.DrawSepia(sb, () ->
                this.renderIconsImpl(sb,x,y, finalC)
            );
        }
        else {
            this.renderIconsImpl(sb,x,y, c);
        }

        for (AbstractGameEffect e : effects)
        {
            e.render(sb, x, y);
        }
    }

    private void renderIconsImpl(SpriteBatch sb, float x, float y, Color c)
    {
        if (this.powerIcon != null)
        {
            pinacolada.utilities.PCLRenderHelpers.DrawCentered(sb, c, this.powerIcon, x, y, ICON_SIZE, ICON_SIZE, 1, 0);
        }
        else
        {
            float scale = ICON_SIZE2 / this.img.getWidth();
            pinacolada.utilities.PCLRenderHelpers.DrawCentered(sb, c, this.img, x, y, this.img.getWidth(), this.img.getHeight(), scale, 0);
        }
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c)
    {
        if (hideAmount)
        {
            return;
        }

        ColoredString amount = GetPrimaryAmount(c);
        if (amount != null)
        {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, amount.text, x, y, fontScale, amount.color);
        }

        ColoredString amount2 = GetSecondaryAmount(c);
        if (amount2 != null)
        {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, amount2.text, x, y + 15f * Settings.scale, 1, amount2.color);
        }
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        onAmountChanged(0, amount);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        if (amount > 0)
        {
            final int previous = amount;
            amount = 0;
            onAmountChanged(previous, -previous);
        }
    }

    @Override
    public void stackPower(int stackAmount)
    {
        stackPower(stackAmount, true);
    }

    public void stackPower(int stackAmount, boolean updateBaseAmount)
    {
        if (updateBaseAmount && (baseAmount += stackAmount) > maxAmount)
        {
            baseAmount = maxAmount;
        }
        if ((amount + stackAmount) > maxAmount)
        {
            stackAmount = maxAmount - amount;
        }

        final int previous = amount;
        super.stackPower(stackAmount);

        onAmountChanged(previous, stackAmount);
    }

    @Override
    public void reducePower(int reduceAmount)
    {
        final int previous = amount;
        super.reducePower(reduceAmount);

        onAmountChanged(previous, -Math.max(0, reduceAmount));
    }

    public int ResetAmount()
    {
        final int previous = amount;
        this.amount = baseAmount;
        onAmountChanged(previous, (amount - previous));
        return amount;
    }

    public IncreasePower IncreasePower(int amount)
    {
        return (IncreasePower) PCLActions.Bottom.IncreasePower(this, amount).SetDuration(0.05f, false);
    }

    public ReducePower ReducePower(int amount)
    {
        return ReducePower(PCLActions.Bottom, amount);
    }

    public ReducePower ReducePower(PCLActions order, int amount)
    {
        return PCLActions.Bottom.ReducePower(this, amount);
    }

    public RemoveSpecificPowerAction RemovePower()
    {
        return RemovePower(PCLActions.Bottom);
    }

    public RemoveSpecificPowerAction RemovePower(PCLActions order)
    {
        return order.RemovePower(owner, owner, this);
    }

    public PCLPower SetEnabled(boolean enable)
    {
        this.enabled = enable;

        return this;
    }

    protected String FormatDescription(int index, Object... args)
    {
        if (powerStrings.DESCRIPTIONS == null || powerStrings.DESCRIPTIONS.length <= index) {
            PCLJUtils.LogError(this, "powerStrings.Description does not exist, " + this.name);
            return "";
        }
        return PCLJUtils.Format(powerStrings.DESCRIPTIONS[index], args);
    }

    protected ColoredString GetPrimaryAmount(Color c)
    {
        if (canBeZero || amount != 0)
        {
            if (isTurnBased || amount == 0)
            {
                return new ColoredString(amount, Color.WHITE, c.a);
            }
            else if (this.amount >= 0)
            {
                return new ColoredString(amount, Color.GREEN, c.a);
            }
            else if (this.canGoNegative)
            {
                return new ColoredString(amount, Color.RED, c.a);
            }
        }

        return null;
    }

    protected ColoredString GetSecondaryAmount(Color c)
    {
        return null;
    }

    protected void OnSamePowerApplied(AbstractPower power)
    {

    }

    protected void onAmountChanged(int previousAmount, int difference)
    {
        if (difference != 0)
        {
            updateDescription();
        }
    }
}