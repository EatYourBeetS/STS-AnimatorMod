package eatyourbeets.powers.animatorClassic;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.cards.animator.series.Konosuba.Hans;
import eatyourbeets.powers.AnimatorClassicPower;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class HansPower extends AnimatorClassicPower
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
        GameActions.Bottom.ApplyPoison(TargetHelper.Enemies(owner), amount);
        GameActions.Bottom.Callback(() ->
        {
            for (AbstractMonster m : GameUtilities.GetEnemies(true))
            {
                if (GameUtilities.GetPowerAmount(m, PoisonPower.POWER_ID) > 0)
                {
                    GameActions.Top.GainTemporaryHP(tempHP);
                }
            }
        });
    }

    @Override
    protected ColoredString GetSecondaryAmount(Color c)
    {
        return new ColoredString(tempHP, Color.GREEN, c.a);
    }
}
