package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.effects.Hemokinesis2Effect;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class UnnamedDollPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(UnnamedDollPower.class.getSimpleName());

    private static final int STRENGTH = 30;

    public UnnamedDollPower(AbstractCreature owner)
    {
        super(owner, POWER_ID);

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        String[] text = powerStrings.DESCRIPTIONS;
        this.description = text[3] + STRENGTH + text[4];
    }

    @Override
    public void onDeath()
    {
        if (!AbstractDungeon.getCurrRoom().isBattleEnding())
        {
            for (AbstractCreature c : GameUtilities.GetAllCharacters(true))
            {
                GameActions.Bottom.VFX(new Hemokinesis2Effect(owner.hb.cX, owner.hb.cY, c.hb.cX, c.hb.cY), 0.35f);
                GameActions.Bottom.StackPower(null, new StrengthPower(c, STRENGTH));
            }
        }
    }
}