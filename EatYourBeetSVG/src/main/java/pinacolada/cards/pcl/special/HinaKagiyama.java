package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.replacement.Miracle;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class HinaKagiyama extends PCLCard
{
    public static final PCLCardData DATA = Register(HinaKagiyama.class)
            .SetPower(1, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.TouhouProject)
            .SetMultiformData(2)
            .PostInitialize(data -> data.AddPreview(new Miracle(), false));

    public HinaKagiyama()
    {
        super(DATA);

        Initialize(0, 0, HinaKagiyamaPower.CARD_DRAW_AMOUNT);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(new HinaKagiyamaPower(p, 1));
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            SetInnate(form == 0);
        }
        return super.SetForm(form, timesUpgraded);
    };

    public class HinaKagiyamaPower extends PCLPower
    {
        public static final int CARD_DRAW_AMOUNT = 2;

        public HinaKagiyamaPower(AbstractCreature owner, int amount)
        {
            super(owner, HinaKagiyama.DATA);

            Initialize(amount);
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, amount, CARD_DRAW_AMOUNT);

            SetEnabled(amount > 0);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            ResetAmount();
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            PCLActions.Bottom.SelectFromPile(name, baseAmount, player.exhaustPile)
                    .SetOptions(false, true)
                    .SetMessage(FormatDescription(1, baseAmount))
                    .SetFilter(PCLGameUtilities::IsHindrance)
                    .AddCallback(cards ->
                    {
                        for (AbstractCard card : cards)
                        {
                            if (player.exhaustPile.group.remove(card))
                            {
                                PCLActions.Bottom.MakeCardInHand(new Miracle());
                            }
                        }
                    });
        }

        @Override
        public void onCardDraw(AbstractCard c)
        {
            super.onCardDraw(c);

            if (c.type == AbstractCard.CardType.CURSE && this.amount > 0)
            {
                PCLActions.Bottom.Draw(CARD_DRAW_AMOUNT);
                this.amount -= 1;
                updateDescription();
                this.flash();
            }
        }
    }
}