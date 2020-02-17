package eatyourbeets.cards.animator.beta;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.NoxiousFumesPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class Hans extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Hans.class).SetPower(3, CardRarity.RARE);

    public Hans()
    {
        super(DATA);

        Initialize(0, 0, 3, 1);
        SetUpgrade(0, 0, 1, 0);

        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new NoxiousFumesPower(p, magicNumber));
        GameActions.Bottom.StackPower(new HansPower(p, secondaryValue));
    }

    public static class HansPower extends AnimatorPower
    {
        public HansPower(AbstractCreature owner, int amount)
        {
            super(owner, Hans.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
        {
            if (source == owner && PoisonPower.POWER_ID.equals(power.ID))
            {
                GameActions.Bottom.GainTemporaryHP(amount);
                flash();
            }
        }
    }
}