package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.powers.PCLPower;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLJUtils;

public class VioletEvergarden extends PCLCard
{
    public static final PCLCardData DATA = Register(VioletEvergarden.class)
            .SetSkill(1, CardRarity.RARE, PCLCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.VioletEvergarden);

    public VioletEvergarden()
    {
        super(DATA);

        Initialize(0, 2, 1, 2);
        SetCostUpgrade(-1);
        SetExhaust(true);

        SetAffinity_Light(1, 0, 1);
        SetAffinity_Orange(1, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);

        PCLActions.Delayed.SelectFromHand(name, magicNumber, false)
                .SetMessage(GR.PCL.Strings.HandSelection.GenericBuff)
                .AddCallback(cards ->
                {
                    if (cards.size() > 0)
                    {
                        AbstractCard card = cards.get(0);
                        PCLActions.Bottom.ModifyTag(card, LOYAL, true);
                        PCLActions.Bottom.ModifyTag(card, AUTOPLAY, true);
                        if (info.TryActivateLimited()) {
                            PCLActions.Bottom.ApplyPower(new VioletEvergardenPower(p, card, secondaryValue));
                        }
                    }
                });
    }

    public static class VioletEvergardenPower extends PCLPower
    {
        private final AbstractCard card;
        public VioletEvergardenPower(AbstractPlayer owner, AbstractCard card, int amount)
        {
            super(owner, VioletEvergarden.DATA);

            this.card = card;
            Initialize(amount);

            updateDescription();
        }

        @Override
        public void onPlayCard(AbstractCard card, AbstractMonster m)
        {
            super.onPlayCard(card, m);

            if (this.card == card)
            {
                this.amount -= 1;
                if (this.amount == 0) {
                    PCLActions.Bottom.ModifyCost(card, -1, true, true).AddCallback(__ -> {
                        PCLGameEffects.Queue.ShowCardBriefly(card.makeStatEquivalentCopy());
                    });
                    RemovePower();
                }
                else {
                    this.flashWithoutSound();
                }
            }
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, PCLJUtils.ModifyString(card.name, w -> "#y" + w), amount);
        }
    }
}