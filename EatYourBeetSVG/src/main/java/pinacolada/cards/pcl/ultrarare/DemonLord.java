package pinacolada.cards.pcl.ultrarare;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.GraveField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;
import pinacolada.cards.base.*;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class DemonLord extends PCLCard_UltraRare
{
    public static final PCLCardData DATA = Register(DemonLord.class)
            .SetPower(4, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.GoblinSlayer);
    public static final int HP_LOSS = 3;
    public static final int HP_LOSS_USE = 10;

    public DemonLord()
    {
        super(DATA);

        Initialize(0, 0, HP_LOSS, HP_LOSS_USE);
        SetCostUpgrade(-1);

        SetAffinity_Dark(2);
        SetDelayed(true);
        GraveField.grave.set(this, true);

    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ApplyPower(p, p, new DemonLordPower(p, 1));
    }

    public static class DemonLordPower extends PCLClickablePower
    {
        public DemonLordPower(AbstractPlayer owner, int amount)
        {
            super(owner, DemonLord.DATA, PowerTriggerConditionType.LoseHP, HP_LOSS_USE);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            PCLActions.Bottom.SelectFromHand(name, amount, false)
                    .SetFilter(c -> c instanceof PCLCard && c.type == CardType.ATTACK && ((PCLCard) c).attackType != PCLAttackType.Brutal)
                    .AddCallback((cards) -> {
                       for (AbstractCard c : cards) {
                           PCLCard pC = JUtils.SafeCast(c, PCLCard.class);
                           if (pC != null) {
                               pC.attackType = PCLAttackType.Brutal;
                           }
                       }
                    });
        }

        @Override
        public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
            return type == DamageInfo.DamageType.NORMAL && card instanceof PCLCard && ((PCLCard) card).attackType == PCLAttackType.Brutal ? damage * 3 : damage;
        }

        @Override
        public void onPlayCard(AbstractCard card, AbstractMonster m)
        {
            super.onPlayCard(card,m);
            PCLCard aCard = PCLJUtils.SafeCast(card, PCLCard.class);
            if (aCard != null && aCard.attackType == PCLAttackType.Brutal)
            {
                GameActions.Bottom.Exhaust(aCard);
            }
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, HP_LOSS_USE);
        }
    }
}