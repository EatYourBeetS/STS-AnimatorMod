package eatyourbeets.blights.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.blights.AnimatorBlight;
import eatyourbeets.cards.animator.status.TheHaunt;
import eatyourbeets.utilities.GameActions;

public class Haunted extends AnimatorBlight
{
    public static final String ID = CreateFullID(Haunted.class);

    public Haunted()
    {
        super(ID);

        this.counter = 1;
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        boolean activated = false;

        for (int i = 0; i < counter; i++)
        {
            if (AbstractDungeon.cardRandomRng.random(100) < 6)
            {
                activated = true;

                AbstractDungeon.player.drawPile.addToRandomSpot(new TheHaunt());
            }
        }

        if (activated)
        {
            flash();
        }
    }

    public void atBattleStart()
    {
        AbstractPlayer p = AbstractDungeon.player;
        if (counter >= 10)
        {
            GameActions.Bottom.ApplyVulnerable(p, p, 1);
            flash();
        }
        if (counter >= 20)
        {
            GameActions.Bottom.ApplyWeak(p, p, 1);
        }
        if (counter >= 30)
        {
            GameActions.Bottom.ApplyFrail(p, p, 2);
        }
    }
}