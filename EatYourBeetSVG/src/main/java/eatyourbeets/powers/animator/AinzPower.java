package eatyourbeets.powers.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.actions.animator.AinzAction;

public class AinzPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(AinzPower.class.getSimpleName());
    public static final int CHOICES = 4;

    public AinzPower(AbstractPlayer owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        GameActions.Bottom.SFX("ORB_LIGHTNING_EVOKE", 0.1f);
        GameActions.Bottom.VFX(new BorderLongFlashEffect(Color.valueOf("3d0066")));
        GameActions.Bottom.SFX("ORB_DARK_EVOKE", 0.1f);
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        for(int i = 0; i < this.amount; i++)
        {
            GameActions.Bottom.Add(new AinzAction(CHOICES));
        }

        this.flash();
    }
}
