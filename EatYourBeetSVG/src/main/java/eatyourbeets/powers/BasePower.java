package eatyourbeets.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.StringJoiner;

public abstract class BasePower extends AbstractPower implements CloneablePowerInterface
{
    protected static final Logger logger = LogManager.getLogger(BasePower.class.getName());
    protected static final Color disabledColor = new Color(0.5f, 0.5f, 0.5f, 1);
    protected boolean enabled = true;

    protected final PowerStrings powerStrings;

    public BasePower(AbstractCreature owner, String id)
    {
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
                this.description = powerStrings.DESCRIPTIONS[0];
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
    public void renderIcons(SpriteBatch sb, float x, float y, Color c)
    {
        if (enabled)
        {
            super.renderIcons(sb, x, y, c);
        }
        else
        {
            super.renderIcons(sb, x, y, disabledColor);
        }
    }

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