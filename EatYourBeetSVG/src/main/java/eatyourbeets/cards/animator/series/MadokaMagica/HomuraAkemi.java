package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class HomuraAkemi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HomuraAkemi.class)
            .SetSkill(1, CardRarity.RARE, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public HomuraAkemi()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);

        SetAffinity_Blue(2);
        SetAffinity_Dark(1);

        SetExhaust(true);
        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.PurgeFromPile(name,1,player.hand).SetFilter(c -> c.type.equals(CardType.CURSE)).AddCallback(
                pc -> {
                    if (pc.size() > 0) {
                        GameActions.Bottom.ApplyPower(new HomuraAkemiPower(player, this, secondaryValue));
                    }
                });
    }

    public static class HomuraAkemiPower extends AnimatorPower
    {
        private final AbstractCard sourceCard;

        public HomuraAkemiPower(AbstractPlayer owner, AbstractCard sourceCard, int amount)
        {
            super(owner, HomuraAkemi.DATA);

            this.amount = amount;
            this.sourceCard = sourceCard;
            this.isTurnBased = true;
            updateDescription();
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            GameActions.Bottom.ReducePower(this, 1);

            if (amount == 0) {
                GameActions.Bottom.SelectFromPile(name, 1, player.masterDeck)
                        .SetFilter(c -> !c.cardID.equals(sourceCard.cardID) && GameUtilities.IsSameSeries(sourceCard,c))
                        .SetOptions(false, false)
                        .AddCallback(cards -> GameActions.Bottom.MakeCardInDrawPile(cards.get(0)).AddCallback(GameUtilities::Retain));
            }
        }
    }


}
