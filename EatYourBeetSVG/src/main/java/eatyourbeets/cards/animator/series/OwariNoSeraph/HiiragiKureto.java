package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.stances.WisdomStance;
import eatyourbeets.utilities.GameActions;

public class HiiragiKureto extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HiiragiKureto.class)
            .SetAttack(2, CardRarity.RARE)
            .SetSeriesFromClassPackage();
    public static final int AMOUNT = 2;

    public HiiragiKureto()
    {
        super(DATA);

        Initialize(5, 0, AMOUNT, 3);
        SetUpgrade(0, 0);

        SetAffinity_Red(1, 1, 1);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Dark(1, 0, 0);

        SetHitCount(3);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY).forEach(d -> d.AddCallback(e -> {
            if (e.lastDamageTaken > 0) {
                GameActions.Bottom.ExhaustFromHand(name,1,true).AddCallback(
                        cards -> {
                            if (cards.size() > 0) {
                                GameActions.Bottom.StackPower(new HiiragiKuretoPower(player, 1));
                            }
                        }
                );
            }
        }));

        if (WisdomStance.IsActive()) {
            GameActions.Bottom.ModifyTag(player.hand, 1, HASTE, true);
        }
    }

    public class HiiragiKuretoPower extends AnimatorPower
    {
        public HiiragiKuretoPower(AbstractCreature owner, int amount)
        {
            super(owner, HiiragiKureto.DATA);

            Initialize(amount, PowerType.BUFF, true);
        }

        @Override
        public void onUseCard(AbstractCard card, UseCardAction action)
        {
            super.onUseCard(card, action);

            if (card.type.equals(CardType.ATTACK) && action.target != null) {
                GameActions.Bottom.ApplyElectrified(player, action.target, AMOUNT);
                this.amount -= 1;
                if (this.amount <= 0) {
                    RemovePower();
                }
                this.flash();
            }
        }
    }
}