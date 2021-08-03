package eatyourbeets.relics.animator.unnamedReign;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class TheEruzaStone extends UnnamedReignRelic
{
    public static final String ID = AnimatorRelic.CreateFullID(TheEruzaStone.class);

    public TheEruzaStone()
    {
        super(ID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public void atTurnStartPostDraw()
    {
        super.atTurnStartPostDraw();

        GameActions.Bottom.MoveCards(player.discardPile, player.drawPile, 1)
        .SetFilter(c -> c.type == AbstractCard.CardType.POWER)
        .ShowEffect(true, true)
        .SetOrigin(CardSelection.Random);
        flash();
    }

    @Override
    public void OnManualEquip()
    {
        player.energy.energyMaster += 2;
    }

    @Override
    public void onUnequip()
    {
        super.onUnequip();

        player.energy.energyMaster -= 2;
    }

    public String GetDoomedString()
    {
        return " NL #y" + name.replace(" ", " #y") + " protects you, increasing the turn limit by #b" + GetDoomedTurnIncrease() + ".";
    }

    public int GetDoomedTurnIncrease()
    {
        return 1;
    }
}