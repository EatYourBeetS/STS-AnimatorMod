package eatyourbeets.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.resources.animator.AnimatorResources;
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

        String imagePath = AnimatorResources.GetPowerImage(ID);
        if (Gdx.files.internal(imagePath).exists())
        {
            this.img = new Texture(imagePath);
        }

        powerStrings = CardCrawlGame.languagePack.getPowerStrings(this.ID);

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
}