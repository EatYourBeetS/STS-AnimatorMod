package eatyourbeets.powers.common;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import eatyourbeets.utilities.GameActions;

public class TemporaryArtifactPower extends AbstractPower implements CloneablePowerInterface
{
    public static final String POWER_ID = "TemporaryArtifact";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public TemporaryArtifactPower(AbstractCreature owner, int amount)
    {
        this.name = "Temporary " + NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.loadRegion("artifact");
        this.type = PowerType.BUFF;
        this.priority = 0;
    }

    public void updateDescription()
    {
        if (this.amount == 1)
        {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        }
        else
        {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public void onSpecificTrigger()
    {
        super.onSpecificTrigger();

        this.ID = POWER_ID;
        GameActions.Top.ReducePower(this, 1);
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        super.onApplyPower(power, target, source);

        if (power.type == PowerType.DEBUFF && owner == target)
        {
            this.ID = ArtifactPower.POWER_ID;
        }
        else
        {
            this.ID = POWER_ID;
        }
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c)
    {
        Color overrideColor = Color.ORANGE.cpy();
        overrideColor.a = c.a;
        super.renderIcons(sb, x, y, overrideColor);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        GameActions.Bottom.RemovePower(owner, owner, this);
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new TemporaryArtifactPower(owner, amount);
    }

    static
    {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(ArtifactPower.POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
