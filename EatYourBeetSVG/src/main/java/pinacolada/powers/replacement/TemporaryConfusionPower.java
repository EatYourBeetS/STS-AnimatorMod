package pinacolada.powers.replacement;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ConfusionPower;
import com.megacrit.cardcrawl.powers.ElectroPower;
import pinacolada.utilities.PCLActions;

public class TemporaryConfusionPower extends ConfusionPower implements CloneablePowerInterface
{
    private boolean permanent;

    public TemporaryConfusionPower(AbstractCreature owner)
    {
        super(owner);

        permanent = false;

        this.ID = ElectroPower.POWER_ID;
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c)
    {
        if (!permanent)
        {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, "1", x, y, this.fontScale, c);
        }
        else
        {
            super.renderAmount(sb, x, y, c);
        }
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        super.onApplyPower(power, target, source);

        if (target == owner && (power instanceof ConfusionPower && !(power instanceof TemporaryConfusionPower)))
        {
            permanent = true;
        }
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        if (!permanent)
        {
            PCLActions.Bottom.RemovePower(owner, owner, this);
        }
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new TemporaryConfusionPower(owner);
    }
}
