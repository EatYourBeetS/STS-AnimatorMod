package eatyourbeets.cards.animator.colorless.rare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class TakanashiRikka extends AnimatorCard
{
    public static final EYBCardData DATA = Register(TakanashiRikka.class)
            .SetSkill(2, CardRarity.RARE, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Chuunibyou);

    public TakanashiRikka()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Earth(1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (AbstractCard c : p.hand.getAttacks().group)
        {
            GameActions.Bottom.MakeCardInHand(GameUtilities.Imitate(c));
        }

        GameActions.Bottom.StackPower(new TakanashiRikkaPower(p, magicNumber));
    }

    public static class TakanashiRikkaPower extends AnimatorPower
    {
        public TakanashiRikkaPower(AbstractCreature owner, int amount)
        {
            super(owner, TakanashiRikka.DATA);

            Initialize(amount);
        }

        @Override
        public void onPlayCard(AbstractCard card, AbstractMonster m)
        {
            super.onPlayCard(card, m);

            if (card.type == CardType.ATTACK)
            {
                GameActions.Top.GainBlock(amount);
                this.flashWithoutSound();
            }
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            RemovePower();
        }
    }
}