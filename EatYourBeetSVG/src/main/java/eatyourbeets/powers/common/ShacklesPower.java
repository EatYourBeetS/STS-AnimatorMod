package eatyourbeets.powers.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameActions;

public class ShacklesPower extends CommonPower implements InvisiblePower
{
    public static final String POWER_ID = CreateFullID(ShacklesPower.class);

    public ShacklesPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        this.type = PowerType.DEBUFF;
    }

    @Override
    public void onInitialApplication()
    {
        //GameActions.Instant.Callback(() -> owner.powers.remove(this));
        owner.powers.remove(this);
        GameActions.Top.ReduceStrength(owner, amount, true);
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c)
    {

    }
}