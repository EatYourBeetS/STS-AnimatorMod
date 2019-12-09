package eatyourbeets.blights.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions._legacy.animator.KillCharacterAction;
import eatyourbeets.blights.AnimatorBlight;
import eatyourbeets.effects.BlightAboveCreatureEffect;
import eatyourbeets.utilities.GameActionsHelper_Legacy;

public class Doomed extends AnimatorBlight
{
    public static final String ID = CreateFullID(Doomed.class.getSimpleName());

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

        AbstractPlayer p = AbstractDungeon.player;
        if (counter <= 1)
        {
            GameActionsHelper_Legacy.AddToBottom(new KillCharacterAction(p, p));
        }
        else
        {
            counter -= 1;

            if (counter <= 1)
            {
                this.pulse = true;
                this.scale = Settings.scale * 1.3f;
                this.flash();

                AbstractDungeon.effectsQueue.add(new BlightAboveCreatureEffect(Settings.WIDTH / 2f, Settings.HEIGHT / 2f, this));
            }
            else
            {
                this.pulse = false;
                this.scale = Settings.scale;
            }
        }
    }
}