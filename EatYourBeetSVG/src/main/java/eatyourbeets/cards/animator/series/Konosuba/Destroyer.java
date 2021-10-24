package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.modifiers.DamageModifiers;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Destroyer extends AnimatorCard {
    public static final EYBCardData DATA = Register(Destroyer.class).SetPower(3, CardRarity.RARE).SetSeriesFromClassPackage();


    public Destroyer() {
        super(DATA);

        Initialize(0, 0, 0);
        SetUpgrade(0, 0, 0);

        SetAffinity_Dark(2);
        SetAffinity_Cyber(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.StackPower(new DestroyerPower(p, 1));
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
    }

    public static class DestroyerPower extends AnimatorClickablePower {
        public DestroyerPower(AbstractPlayer owner, int amount) {
            super(owner, Destroyer.DATA, PowerTriggerConditionType.Energy, 1);

            this.amount = amount;
            this.triggerCondition.SetUses(1,true, false);

            updateDescription();
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            GameActions.Bottom.SelectFromHand(name, 1, false)
            .SetOptions(true, true, true)
            .SetFilter( card -> {
                return card.type == CardType.ATTACK || card.type == CardType.SKILL;
            })
            .SetMessage(DATA.Strings.EXTENDED_DESCRIPTION[1])
            .AddCallback(cards -> {
               for (AbstractCard card : cards)
               {
                   if (card.type == CardType.ATTACK)
                   {
                       int darkAmount = GameUtilities.GetAffinityAmount(Affinity.Dark) * amount;
                       DamageModifiers.For(card).Add(darkAmount);
                   }
                   else
                   {
                       int cyberAmount = GameUtilities.GetAffinityAmount(Affinity.Cyber) * amount;
                       GameActions.Bottom.GainSupportDamage(cyberAmount);
                   }
               }
            });
        }

        @Override
        public String GetUpdatedDescription() {
            return FormatDescription(0, amount);
        }
    }
}