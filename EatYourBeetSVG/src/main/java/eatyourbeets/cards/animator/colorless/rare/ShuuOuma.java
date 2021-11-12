package eatyourbeets.cards.animator.colorless.rare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class ShuuOuma extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ShuuOuma.class)
            .SetSkill(0, CardRarity.RARE, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.GuiltyCrown);

    public ShuuOuma()
    {
        super(DATA);

        Initialize(0, 0, 1, 4);
        SetUpgrade(0,0,1,2);

        SetAffinity_Light(1);
        SetAffinity_Dark(1);
        SetAffinity_Blue(1);

        SetExhaust(true);

        SetDrawPileCardPreview(c -> c.type == CardType.POWER);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new ShuuOumaPower(player, secondaryValue));

        (upgraded ? GameActions.Bottom.FetchFromPile(name, magicNumber, player.drawPile, player.discardPile)
                  : GameActions.Bottom.FetchFromPile(name, magicNumber, player.drawPile))
        .SetOptions(false, false)
        .SetFilter(c -> c.type.equals(CardType.POWER))
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                final int damage = Math.max(0, c.costForTurn * 2);
                if (damage > 0)
                {
                    GameActions.Bottom.DealDamageAtEndOfTurn(player, player, damage, AttackEffects.DARK);
                }
            }
        });
    }

    public static class ShuuOumaPower extends AnimatorPower
    {
        public ShuuOumaPower(AbstractPlayer owner, int amount)
        {
            super(owner, ShuuOuma.DATA);

            Initialize(amount);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            RemovePower();
        }

        @Override
        public void onAfterCardPlayed(AbstractCard card)
        {
            super.onAfterCardPlayed(card);

            if (card.type.equals(CardType.POWER))
            {
                GameActions.Bottom.GainCorruption(amount);
                GameActions.Bottom.GainIntellect(amount);
                GameActions.Bottom.GainForce(amount);
                this.flashWithoutSound();
            }
        }
    }
}