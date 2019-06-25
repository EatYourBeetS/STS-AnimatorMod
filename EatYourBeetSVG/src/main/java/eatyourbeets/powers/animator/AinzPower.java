package eatyourbeets.powers.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.Utilities;
import eatyourbeets.actions.animator.AinzAction;

import java.util.ArrayList;

public class AinzPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(AinzPower.class.getSimpleName());

    private final AbstractPlayer player;

    private final ArrayList<Integer> choices;

    public AinzPower(AbstractPlayer owner, int choices)
    {
        super(owner, POWER_ID);
        this.amount = 1;

        this.choices = new ArrayList<>();
        this.choices.add(choices);

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
        if (other != null && power.owner == target)
        {
            this.choices.add(other.choices.get(0));
        }

        super.onApplyPower(power, target, source);
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        for(int i = 0; i < this.amount; i++)
        {
            GameActionsHelper.AddToBottom(new AinzAction(player, choices.get(i),false));
        }

        this.flash();
    }
}
