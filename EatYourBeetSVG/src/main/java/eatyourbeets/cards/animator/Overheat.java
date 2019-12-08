package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard_Status;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
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
            GameActionsHelper2.Draw(2);
            GameActionsHelper.DamageTarget(p, p, magicNumber, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE);

            for (AbstractCard c : GameUtilities.GetAllCopies(this))
            {
                c.baseMagicNumber += 1;
                c.applyPowers();
            }
        }
    }
}