package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CommonTriggerablePower;
import eatyourbeets.powers.common.ElectrifiedPower;
import eatyourbeets.utilities.GameActions;

public class HiiragiKureto extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HiiragiKureto.class)
            .SetAttack(2, CardRarity.RARE)
            .SetSeriesFromClassPackage();
    public static final int AMOUNT = 4;

    public HiiragiKureto()
    {
        super(DATA);

        Initialize(5, 0, AMOUNT, 15);
        SetUpgrade(0, 0);

        SetAffinity_Red(1, 1, 1);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Dark(1, 0, 0);

        SetHitCount(3);

        SetAffinityRequirement(Affinity.Light, 5);

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
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY).forEach(d -> d
                .SetVFXColor(Color.GOLDENROD)
                .AddCallback(e -> {
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

        if (TrySpendAffinity(Affinity.Light)) {
            GameActions.Bottom.Callback(() -> CommonTriggerablePower.AddEffectBonus(ElectrifiedPower.POWER_ID, secondaryValue));
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
        public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target)
        {
            super.onAttack(info, damageAmount, target);

            if (damageAmount > 0 && target != this.owner && info.type == DamageInfo.DamageType.NORMAL)
            {
                GameActions.Top.ApplyElectrified(owner, target, AMOUNT).ShowEffect(true, true);
            }
        }

        @Override
        public void onUseCard(AbstractCard card, UseCardAction action)
        {
            super.onUseCard(card, action);

            if (card.type.equals(CardType.ATTACK) && action.target != null) {
                this.amount -= 1;
                if (this.amount <= 0) {
                    RemovePower();
                }
                this.flash();
            }
        }
    }
}