package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class JotaroKujo_StarPlatinum extends AnimatorCard
{
    public static final EYBCardData DATA = Register(JotaroKujo_StarPlatinum.class).SetPower(2, CardRarity.SPECIAL).SetSeries(CardSeries.Jojo);

    public JotaroKujo_StarPlatinum()
    {
        super(DATA);

        Initialize(0, 0, 1, 1);
        SetUpgrade(0,0,0,1);
        SetAffinity_Star(2);
        SetInnate(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(TargetHelper.Player(), PowerHelper.Intangible, magicNumber);
        GameActions.Bottom.StackPower(new StarPlatinumPower(p, this.secondaryValue));
    }

    public static class StarPlatinumPower extends AnimatorPower
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

            if (card.color == CardColor.COLORLESS || !(card instanceof AnimatorCard) || ((AnimatorCard) card).series == null)
            {
                GameActions.Bottom.AddAffinity(Affinity.Star, amount);
                this.flashWithoutSound();
            }
        }
    }
}