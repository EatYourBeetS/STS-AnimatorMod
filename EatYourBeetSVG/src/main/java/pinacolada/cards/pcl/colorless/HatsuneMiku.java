package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class HatsuneMiku extends PCLCard
{
    public static final PCLCardData DATA = Register(HatsuneMiku.class).SetPower(1, CardRarity.RARE).SetMaxCopies(1).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Vocaloid);

    public HatsuneMiku()
    {
        super(DATA);

        Initialize(0, 0, 2, 39);
        SetCostUpgrade(-1);
        SetRetain(true);
        SetHarmonic(true);

        SetAffinity_Light(1);
        SetAffinity_Silver(1);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return (player.discardPile.size() + player.exhaustPile.size()) >= secondaryValue;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(new HatsuneMikuPower(p, this.magicNumber));
    }

    public static class HatsuneMikuPower extends PCLPower
    {
        public HatsuneMikuPower(AbstractPlayer owner, int amount)
        {
            super(owner, HatsuneMiku.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            PCLActions.Bottom.Draw(amount)
                    .AddCallback(null, (enemy, cards) -> {
                        for (AbstractCard card : cards)
                        {
                            if (card != null)
                            {
                                card.setCostForTurn(0);
                            }
                        }
                    });
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount);
        }
    }
}