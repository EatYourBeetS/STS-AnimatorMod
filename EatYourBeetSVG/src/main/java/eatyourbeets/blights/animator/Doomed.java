package eatyourbeets.blights.animator;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import eatyourbeets.actions.special.KillCharacterAction;
import eatyourbeets.blights.AnimatorBlight;
import eatyourbeets.effects.combatOnly.BlightAboveCreatureEffect;
import eatyourbeets.relics.animator.unnamedReign.TheEruzaStone;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Doomed extends AnimatorBlight
{
    public static final String ID = CreateFullID(Doomed.class);
    private TheEruzaStone relic;

    public Doomed()
    {
        this(4);
    }

    public Doomed(int turns)
    {
        super(ID, turns);
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        relic = GameUtilities.GetRelic(TheEruzaStone.ID);
        if (relic != null)
        {
            relic.flash();
            this.flash();
            setCounter(counter + relic.GetDoomedTurnIncrease());
        }

        updateDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(name, description));
        initializeTips();
    }

    @Override
    public void updateDescription()
    {
        super.updateDescription();

        description = JUtils.Format(strings.DESCRIPTION[0], counter);

        if (relic != null)
        {
            description += " NL " + relic.GetDoomedString();
        }
    }

    @Override
    public void onPlayerEndTurn()
    {
        super.onPlayerEndTurn();

        flash();
        
        if (counter <= 1)
        {
            GameActions.Bottom.Add(new KillCharacterAction(null, AbstractDungeon.player));
        }
        else
        {
            counter -= 1;

            if (counter <= 1)
            {
                this.pulse = true;
                this.scale = Settings.scale * 1.3f;
                this.flash();

                GameEffects.Queue.Add(new BlightAboveCreatureEffect(Settings.WIDTH / 2f, Settings.HEIGHT / 2f, this));
            }
            else
            {
                this.pulse = false;
                this.scale = Settings.scale;
            }
        }
    }
}