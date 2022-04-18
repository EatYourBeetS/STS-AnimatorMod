package eatyourbeets.powers.replacement;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import eatyourbeets.actions.powers.ApplyPower;
import eatyourbeets.interfaces.listeners.OnTryApplyPowerListener;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class TemporaryArtifactPower extends AbstractPower implements CloneablePowerInterface, OnTryApplyPowerListener
{
    public static final String POWER_ID = GR.Common.CreateID(TemporaryArtifactPower.class.getSimpleName());
    public static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(ArtifactPower.POWER_ID);

    public TemporaryArtifactPower(AbstractCreature owner, int amount)
    {
        this.name = "Temporary " + STRINGS.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.loadRegion("artifact");
        this.type = PowerType.BUFF;
        this.priority = 0;
    }

    @Override
    public boolean TryApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source, AbstractGameAction action)
    {
        if (target == owner && power.type == PowerType.DEBUFF)
        {
            final ApplyPower applyPowerAction = JUtils.SafeCast(action, ApplyPower.class);
            if (applyPowerAction == null || !applyPowerAction.ignoreArtifact)
            {
                GameActions.Top.SFX("NULLIFY_SFX");
                flashWithoutSound();
                onSpecificTrigger();
                return false;
            }
        }

        return true;
    }

    public void updateDescription()
    {
        String[] TEXT = STRINGS.DESCRIPTIONS;
        if (this.amount == 1)
        {
            this.description = TEXT[0] + amount + TEXT[1];
        }
        else
        {
            this.description = TEXT[0] + amount + TEXT[2];
        }
    }

    @Override
    public void onSpecificTrigger()
    {
        super.onSpecificTrigger();

        GameActions.Top.ReducePower(this, 1);
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c)
    {
        super.renderIcons(sb, x, y, Colors.Copy(Color.ORANGE, c.a));
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        GameActions.Last.RemovePower(owner, owner, this);
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new TemporaryArtifactPower(owner, amount);
    }
}

