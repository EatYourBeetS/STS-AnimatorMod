package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.powers.PCLPower;
import pinacolada.powers.special.MagusFormPower;
import pinacolada.utilities.PCLActions;

public class Alibaba_Aladdin extends PCLCard
{
    public static final PCLCardData DATA = Register(Alibaba_Aladdin.class).SetPower(2, CardRarity.SPECIAL).SetSeries(CardSeries.Magi);

    public Alibaba_Aladdin()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0,0,0,1);
        SetAffinity_Blue(1);
        SetAffinity_Light(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(new MagusFormPower(p, this.magicNumber));
        PCLActions.Bottom.StackPower(new AladdinPower(p, this.secondaryValue));
    }

    public static class AladdinPower extends PCLPower
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
            if (amount > 0 && c instanceof PCLCard && ((PCLCard) c).CanScale()) {
                PCLActions.Last.IncreaseScaling(c, PCLAffinity.Blue, 1);
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