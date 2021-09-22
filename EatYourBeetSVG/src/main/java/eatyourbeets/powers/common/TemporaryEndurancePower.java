package eatyourbeets.powers.common;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class TemporaryEndurancePower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(TemporaryEndurancePower.class);

    public TemporaryEndurancePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        this.type = PowerType.BUFF;
        final String imagePath = GR.GetPowerImage(EndurancePower.POWER_ID);
        if (Gdx.files.internal(imagePath).exists())
        {
            this.img = GR.GetTexture(imagePath);
        }
        enabled = false;

        updateDescription();
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        GameActions.Bottom.GainEndurance(this.amount);
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        GameActions.Bottom.ReducePower(owner, owner, EndurancePower.POWER_ID, this.amount);
        GameActions.Bottom.RemovePower(owner, owner, this);
    }

    @Override
    public void updateDescription()
    {
        description = FormatDescription(0, amount);
    }
}