package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard_Status;
import eatyourbeets.powers.common.SelfDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Overheat extends AnimatorCard_Status
{
    public static final String ID = Register(Overheat.class.getSimpleName());

    public Overheat()
    {
        super(ID, 0, CardRarity.COMMON, CardTarget.NONE);

        Initialize(0, 0, 3);
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        // Do not autoplay
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (!this.dontTriggerOnUseCard)
        {
            GameActions.Bottom.Draw(2);
            GameActions.Bottom.StackPower(new SelfDamagePower(p, magicNumber));
            GameActions.Bottom.Callback(__ ->
            {
                for (AbstractCard c : GameUtilities.GetAllCopies(this))
                {
                    c.baseMagicNumber += 1;
                    c.applyPowers();
                }
            });
        }
    }
}