package eatyourbeets.powers.monsters;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.effects.vfx.HemokinesisEffect2;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class UnnamedDollPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(UnnamedDollPower.class);
    public static final int STRENGTH = 30;

    public UnnamedDollPower(AbstractCreature owner)
    {
        super(owner, POWER_ID);

        Initialize(-1);
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, STRENGTH);
    }

    @Override
    public void onDeath()
    {
        super.onDeath();

        if (!AbstractDungeon.getCurrRoom().isBattleEnding())
        {
            for (AbstractCreature c : GameUtilities.GetAllCharacters(true))
            {
                GameActions.Bottom.VFX(new HemokinesisEffect2(owner.hb.cX, owner.hb.cY, c.hb.cX, c.hb.cY), 0.35f);
                GameActions.Bottom.StackPower(null, new StrengthPower(c, STRENGTH));
            }
        }
    }
}