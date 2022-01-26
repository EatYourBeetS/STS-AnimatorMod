package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class TakanashiRikka extends PCLCard
{
    public static final PCLCardData DATA = Register(TakanashiRikka.class)
            .SetSkill(2, CardRarity.RARE, PCLCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Chuunibyou);

    public TakanashiRikka()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Orange(1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (AbstractCard c : p.hand.getAttacks().group)
        {
            PCLActions.Bottom.MakeCardInHand(PCLGameUtilities.Imitate(c));
        }

        PCLActions.Bottom.StackPower(new TakanashiRikkaPower(p, magicNumber));
    }

    public static class TakanashiRikkaPower extends PCLPower
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
                PCLActions.Top.GainBlock(amount);
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