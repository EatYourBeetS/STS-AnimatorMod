package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.animator.MagusFormPower;
import eatyourbeets.utilities.GameActions;

public class Alibaba_Aladdin extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Alibaba_Aladdin.class).SetPower(2, CardRarity.SPECIAL).SetSeries(CardSeries.GenshinImpact);

    public Alibaba_Aladdin()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0,0,0,1);
        SetAffinity_Blue(2);
        SetAffinity_Light(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new MagusFormPower(p, this.magicNumber));
        GameActions.Bottom.StackPower(new AladdinPower(p, this.secondaryValue));
    }

    public static class AladdinPower extends AnimatorPower
    {

        public AladdinPower(AbstractPlayer owner, int amount)
        {
            super(owner, Alibaba_Aladdin.DATA);

            Initialize(amount, PowerType.BUFF, true);
        }

        @Override
        public void onCardDraw(AbstractCard c)
        {
            super.onCardDraw(c);
            if (amount > 0 && c instanceof EYBCard && ((EYBCard) c).CanScale()) {
                GameActions.Last.IncreaseScaling(c, Affinity.Blue, 1);
                this.amount -= 1;
            }
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            ResetAmount();
        }
    }
}