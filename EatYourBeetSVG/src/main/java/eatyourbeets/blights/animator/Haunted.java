package eatyourbeets.blights.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.blights.AnimatorBlight;
import eatyourbeets.cards.animator.TheHaunt;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;

public class Haunted extends AnimatorBlight
{
    public static final String ID = CreateFullID(Haunted.class.getSimpleName());

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
            this.flash();
        }
    }

    public void atBattleStart()
    {
        AbstractPlayer p = AbstractDungeon.player;
        if (counter >= 10)
        {
            this.flash();
            GameActionsHelper.ApplyPower(p, p, new VulnerablePower(p, 1, false), 1);
        }
        if (counter >= 20)
        {
            GameActionsHelper.ApplyPower(p, p, new WeakPower(p, 2, false), 2);
        }
        if (counter >= 30)
        {
            GameActionsHelper.ApplyPower(p, p, new FrailPower(p, 2, false), 2);
        }
    }
}