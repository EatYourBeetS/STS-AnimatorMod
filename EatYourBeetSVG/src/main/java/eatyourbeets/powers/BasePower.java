package eatyourbeets.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.powers.EYBFlashPowerEffect;
import eatyourbeets.effects.powers.EYBGainPowerEffect;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JavaUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.StringJoiner;

public abstract class BasePower extends AbstractPower implements CloneablePowerInterface
{
    public TextureAtlas.AtlasRegion powerIcon;

    protected static final Logger logger = LogManager.getLogger(BasePower.class.getName());
    protected static final Color disabledColor = new Color(0.5f, 0.5f, 0.5f, 1);
    protected boolean enabled = true;

    protected final ArrayList<AbstractGameEffect> effects;
    protected final PowerStrings powerStrings;

    public BasePower(AbstractCreature owner, EYBCardData cardData)
    {
        this.effects = (ArrayList<AbstractGameEffect>)JavaUtilities.GetField("effect", AbstractPower.class).Get(this);
        this.owner = owner;
        this.ID = cardData.ID + "Power";

        this.powerIcon = cardData.GetCardIcon();
        this.img = null;

        this.powerStrings = new PowerStrings();
        this.powerStrings.NAME = cardData.Strings.NAME;
        this.powerStrings.DESCRIPTIONS = cardData.Strings.EXTENDED_DESCRIPTION;
        this.name = powerStrings.NAME;
    }

    public BasePower(AbstractCreature owner, String id)
    {
        this.effects = (ArrayList<AbstractGameEffect>)JavaUtilities.GetField("effect", AbstractPower.class).Get(this);
        this.owner = owner;
        this.ID = id;

        String imagePath = GR.GetPowerImage(ID);
        if (Gdx.files.internal(imagePath).exists())
        {
            this.img = GR.GetTexture(imagePath);
        }

        this.powerStrings = CardCrawlGame.languagePack.getPowerStrings(this.ID);
        this.name = powerStrings.NAME;
    }

    @Override
    public void updateDescription()
    {
        switch (powerStrings.DESCRIPTIONS.length)
        {
            case 0:
            {
                logger.error("powerStrings.Description was an empty array, " + this.name);
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
        try
        {
            return this.getClass().getDeclaredConstructor(AbstractCreature.class, int.class).newInstance(owner, amount);
        }
        catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e1)
        {
            try
            {
                return this.getClass().getDeclaredConstructor(AbstractCreature.class).newInstance(owner);
            }
            catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e2)
            {
                try
                {
                    return this.getClass().getConstructor().newInstance();
                }
                catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e3)
                {
                    return null;
                }
            }
        }
    }

    public void RemovePower()
    {
        GameActions.Bottom.RemovePower(owner, owner, this);
    }

    @Override
    public void flash()
    {
        this.effects.add(new EYBGainPowerEffect(this, true));
        GameEffects.List.Add(new EYBFlashPowerEffect(this));
    }

    @Override
    public void flashWithoutSound()
    {
        this.effects.add(new EYBGainPowerEffect(this, false));
        GameEffects.List.Add(new EYBFlashPowerEffect(this));
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c)
    {
        sb.setColor(enabled ? c : disabledColor);

        if (this.powerIcon != null)
        {
            sb.draw(this.powerIcon, x - 12f, y - 16f, 16f, 16f, 32.0F, 32.0F, Settings.scale, Settings.scale, 0.0F);
        }
        else
        {
            sb.draw(this.img, x - 12.0F, y - 12.0F, 16.0F, 16.0F, 32.0F, 32.0F, Settings.scale * 1.5F, Settings.scale * 1.5F, 0.0F, 0, 0, 32, 32, false, false);
        }

        for (AbstractGameEffect e : effects)
        {
            e.render(sb, x, y);
        }
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c)
    {
        ColoredString amount = GetPrimaryAmount(c);
        if (amount != null)
        {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, amount.text, x, y, fontScale, amount.color);
        }

        ColoredString amount2 = GetSecondaryAmount(c);
        if (amount2 != null)
        {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, amount2.text, x, y + 15.0F * Settings.scale, 1, amount2.color);
        }
    }

    protected String FormatDescription(int index, Object... args)
    {
        return JavaUtilities.Format(powerStrings.DESCRIPTIONS[index], args);
    }

    protected ColoredString GetPrimaryAmount(Color c)
    {
        if (this.amount > 0)
        {
            if (this.isTurnBased)
            {
                return new ColoredString(amount, Color.WHITE, c.a);
            }
            else
            {
                return new ColoredString(amount, Color.GREEN, c.a);
            }
        }
        else if (this.amount < 0 && this.canGoNegative)
        {
            return new ColoredString(amount, Color.RED, c.a);
        }
        else
        {
            return null;
        }
    }

    protected ColoredString GetSecondaryAmount(Color c)
    {
        return null;
    }
}