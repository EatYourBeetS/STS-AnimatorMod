package eatyourbeets.blights.animator;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.special.KillCharacterAction;
import eatyourbeets.blights.AnimatorBlight;
import eatyourbeets.effects.combatOnly.BlightAboveCreatureEffect;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Doomed extends AnimatorBlight
{
    public static final String ID = CreateFullID(Doomed.class);

    public Doomed()
    {
        this(4);
    }

    public Doomed(int turns)
    {
        super(ID, turns);
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