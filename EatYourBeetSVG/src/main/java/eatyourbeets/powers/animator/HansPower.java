package eatyourbeets.powers.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.animator.series.Konosuba.Hans;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class HansPower extends AnimatorPower
{
    protected int tempHP;

    public HansPower(AbstractCreature owner, int poison, int tempHP)
    {
        super(owner, Hans.DATA);

        this.amount = poison;
        this.tempHP = tempHP;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        description = FormatDescription(0, amount, tempHP);
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        super.onApplyPower(power, target, source);

        if (target == owner && power instanceof HansPower)
        {
            this.tempHP += ((HansPower)power).tempHP;
        }
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        GameActions.Bottom.ApplyPoison(TargetHelper.Enemies(owner), amount)
        .AddCallback(poison ->
        {
            if (poison != null && poison.owner != null)
            {
                GameActions.Bottom.GainTemporaryHP(tempHP);
            }
        });
    }

    @Override
    protected ColoredString GetSecondaryAmount(Color c)
    {
        return new ColoredString(tempHP, Color.GREEN, c.a);
    }
}
