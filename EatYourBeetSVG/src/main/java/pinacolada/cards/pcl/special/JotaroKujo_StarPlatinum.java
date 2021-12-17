package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import pinacolada.cards.base.*;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class JotaroKujo_StarPlatinum extends PCLCard
{
    public static final PCLCardData DATA = Register(JotaroKujo_StarPlatinum.class).SetPower(2, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Jojo);

    public JotaroKujo_StarPlatinum()
    {
        super(DATA);

        Initialize(0, 0, 1, 1);
        SetUpgrade(0,0,0,1);
        SetAffinity_Star(1);
        SetInnate(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(new IntangiblePlayerPower(p, magicNumber));
        PCLActions.Bottom.StackPower(new StarPlatinumPower(p, this.secondaryValue));
    }

    public static class StarPlatinumPower extends PCLPower
    {

        public StarPlatinumPower(AbstractPlayer owner, int amount)
        {
            super(owner, JotaroKujo_StarPlatinum.DATA);

            Initialize(amount);
        }

        @Override
        public void onPlayCard(AbstractCard card, AbstractMonster m)
        {
            super.onPlayCard(card, m);

            if (card.color == CardColor.COLORLESS || !(card instanceof PCLCard) || ((PCLCard) card).series == null)
            {
                PCLActions.Bottom.AddAffinity(PCLAffinity.Star, amount);
                this.flashWithoutSound();
            }
        }
    }
}