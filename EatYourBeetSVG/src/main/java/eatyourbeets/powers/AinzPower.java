package eatyourbeets.powers;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.actions.AinzAction;

public class AinzPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(AinzPower.class.getSimpleName());

    private final boolean upgraded;
    private final AbstractPlayer player;

    private int upgradedPowerStack;

    public AinzPower(AbstractPlayer owner, boolean upgraded)
    {
        super(owner, POWER_ID);
        this.amount = 1;
        this.upgraded = upgraded;
        if (this.upgraded)
        {
            this.upgradedPowerStack = 1;
        }

        this.player = Utilities.SafeCast(this.owner, AbstractPlayer.class);
        updateDescription();
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        GameActionsHelper.AddToBottom(new SFXAction("ORB_LIGHTNING_EVOKE", 0.1f));
        GameActionsHelper.AddToTop(new VFXAction(new BorderLongFlashEffect(Color.valueOf("3d0066"))));
        GameActionsHelper.AddToBottom(new SFXAction("ORB_DARK_EVOKE", 0.1f));
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        AinzPower other = Utilities.SafeCast(power, AinzPower.class);
        if (other != null && power.owner == target && other.upgraded)
        {
            this.upgradedPowerStack += 1;
        }

        super.onApplyPower(power, target, source);
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        for(int i = 0; i < this.amount; i++)
        {
            GameActionsHelper.AddToBottom(new AinzAction(player, i < upgradedPowerStack));
        }

        this.flash();
    }
}
