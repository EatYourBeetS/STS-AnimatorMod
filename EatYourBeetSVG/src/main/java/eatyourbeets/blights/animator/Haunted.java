package eatyourbeets.blights.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FrailPower;
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

        final int rng = AbstractDungeon.cardRandomRng.random(100);
        if (rng < (counter * 5))
        {
//            if (counter >= 4 && GameUtilities.GetAscensionLevel() >= 16 && GameUtilities.GetPowerAmount(Affinity.Dark) >= 6 && CombatStats.TryActivateLimited(ID))
//            {
//                AnimatorCard_UltraRare.MarkAsSeen(Azami.DATA.ID);
//                AbstractDungeon.player.drawPile.addToTop(new Azami());
//            }
//            else
//            {
                AbstractDungeon.player.drawPile.addToRandomSpot(new TheHaunt());
//            }

            flash();
        }
    }

    @Override
    public void atBattleStart()
    {
        final AbstractPlayer p = AbstractDungeon.player;
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
            GameActions.Bottom.StackPower(p, new FrailPower(p, 2, false));
        }
    }
}