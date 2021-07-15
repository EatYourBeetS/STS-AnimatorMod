package eatyourbeets.relics.animator.unnamedReign;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;

public class TheEruzaStone extends UnnamedReignRelic
{
    public static final String ID = AnimatorRelic.CreateFullID(TheEruzaStone.class);

    public TheEruzaStone()
    {
        super(ID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m)
    {
        super.onPlayCard(c, m);

        if (IsEnabled() && c.type == AbstractCard.CardType.POWER)
        {
            SetEnabled(false);
            GameActions.Bottom.Draw(1);
            flash();
        }
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

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        SetEnabled(true);
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