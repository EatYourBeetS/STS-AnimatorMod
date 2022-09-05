package eatyourbeets.powers.animatorClassic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.animator.series.Katanagatari.HigakiRinne;
import eatyourbeets.powers.AnimatorClassicPower;

public class HigakiRinnePower extends AnimatorClassicPower
{
    public static final String POWER_ID = CreateFullID(HigakiRinnePower.class);

    private final HigakiRinne higakiRinne;

    public HigakiRinnePower(AbstractPlayer owner, HigakiRinne higakiRinne, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        this.higakiRinne = higakiRinne;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        this.description = powerStrings.DESCRIPTIONS[0];
    }

//    @Override
//    public void atStartOfTurn()
//    {
//        super.atStartOfTurn();
//
//        for (int i = 0; i < this.amount; i++)
//        {
//            GameActions.Bottom.Add(new HigakiRinneAction(higakiRinne));
//        }
//
//        this.flash();
//    }
}
