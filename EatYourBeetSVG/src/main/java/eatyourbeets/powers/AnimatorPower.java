package eatyourbeets.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.AnimatorResources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.StringJoiner;

public abstract class AnimatorPower extends AbstractPower implements CloneablePowerInterface
{
    protected static final Logger logger = LogManager.getLogger(AnimatorPower.class.getName());

    protected final PowerStrings powerStrings;

    public static String CreateFullID(String id)
    {
        return "animator_" + id;
    }

    public AnimatorPower(AbstractCreature owner, String id)
    {
        this.owner = owner;
        this.ID = id;
        this.img = new Texture(AnimatorResources.GetPowerImage(ID));

        powerStrings = CardCrawlGame.languagePack.getPowerStrings(this.ID);

        this.name = powerStrings.NAME;
    }

    @Override
    public  void updateDescription()
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
        //logger.info(powerStrings.DESCRIPTIONS.length + ": " + powerStrings.DESCRIPTIONS[0]);
        //logger.info(this.description);
    }

    @Override
    public AbstractPower makeCopy()
    {
        try
        {
            return this.getClass().newInstance();
        }
        catch (InstantiationException | IllegalAccessException e)
        {
            return null;
        }
    }
}
