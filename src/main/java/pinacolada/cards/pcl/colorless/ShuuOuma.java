package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class ShuuOuma extends PCLCard
{
    public static final PCLCardData DATA = Register(ShuuOuma.class)
            .SetSkill(0, CardRarity.RARE, PCLCardTarget.None)
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
        PCLActions.Bottom.StackPower(new ShuuOumaPower(player, secondaryValue));

        (upgraded ? PCLActions.Bottom.FetchFromPile(name, magicNumber, player.drawPile, player.discardPile)
                  : PCLActions.Bottom.FetchFromPile(name, magicNumber, player.drawPile))
        .SetOptions(false, false)
        .SetFilter(c -> c.type.equals(CardType.POWER))
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                final int damage = Math.max(0, c.costForTurn * 2);
                if (damage > 0)
                {
                    PCLActions.Bottom.DealDamageAtEndOfTurn(player, player, damage, AttackEffects.DARK);
                }
            }
        });
    }

    public static class ShuuOumaPower extends PCLPower
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
                PCLActions.Bottom.GainDesecration(amount);
                PCLActions.Bottom.GainWisdom(amount);
                PCLActions.Bottom.GainMight(amount);
                this.flashWithoutSound();
            }
        }
    }
}