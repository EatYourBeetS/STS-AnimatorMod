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
    public static final String FAKE_POWER_ID = "Thanks for hardcoding everything (temp. artifact)";
    public static final String ARTIFACT_ID = ArtifactPower.POWER_ID;
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public TemporaryArtifactPower(AbstractCreature owner, int amount)
    {
        this.name = "Temporary " + NAME;
        this.ID = FAKE_POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.loadRegion("artifact");
        this.type = PowerType.BUFF;
        this.priority = 0;
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        this.ID = ARTIFACT_ID;
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

        GameActions.Top.ReducePower(this, 1);
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        super.onApplyPower(power, target, source);

        if (ArtifactPower.POWER_ID.equals(power.ID))
        {
            // There is no trigger for when the enemy is applying a power to you, which would have
            // made this much easier and cleaner... the alternative (without patching) is to change this
            // power's id when artifact would be applied, then changing it back with the next action
            this.ID = FAKE_POWER_ID;
            GameActions.Top.Callback(__ -> this.ID = ARTIFACT_ID);
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
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        GameActions.Bottom.RemovePower(owner, owner, this);
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new TemporaryArtifactPower(owner, amount);
    }

    static
    {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(ARTIFACT_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}

