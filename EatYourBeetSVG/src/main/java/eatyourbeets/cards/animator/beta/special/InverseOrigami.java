package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.animator.special.OrbCore;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnEvokeOrbSubscriber;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class InverseOrigami extends AnimatorCard
{
    public static final EYBCardData DATA = Register(InverseOrigami.class).SetPower(2, CardRarity.SPECIAL).SetSeries(CardSeries.DateALive);

    public InverseOrigami() {
        super(DATA);

        Initialize(0, 0, 1);
        SetAffinity_Blue(1, 1, 0);
        SetAffinity_Dark(1, 0, 0);
        SetAutoplay(true);
    }

    @Override
    protected void OnUpgrade() {
        SetAutoplay(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing) {
        GameActions.Bottom.StackPower(new InverseOrigamiPower(p, magicNumber));
    }

    public static class InverseOrigamiPower extends AnimatorClickablePower implements OnEvokeOrbSubscriber {
        public static final int SUPPORT_DAMAGE_AMOUNT = 2;

        public InverseOrigamiPower(AbstractPlayer owner, int amount) {
            super(owner, InverseOrigami.DATA, PowerTriggerConditionType.Special, SUPPORT_DAMAGE_AMOUNT);
            this.triggerCondition.SetCheckCondition((c) -> {
                return GameUtilities.GetPowerAmount(player, SupportDamagePower.POWER_ID) >= SUPPORT_DAMAGE_AMOUNT;
            })
                    .SetPayCost(a -> {
                        SupportDamagePower supportDamage = GameUtilities.GetPower(player, SupportDamagePower.class);
                        supportDamage.ReducePower(a);
                    });

            Initialize(amount);
        }

        public void atStartOfTurn() {
            super.atStartOfTurn();

            ResetAmount();
        }

        @Override
        public String GetUpdatedDescription() {
            return FormatDescription(0, amount, SUPPORT_DAMAGE_AMOUNT);
        }


        @Override
        public void OnUse(AbstractMonster m) {
            GameActions.Bottom.MakeCardInHand(JUtils.Random(OrbCore.GetAllCores()).makeCopy());
        }

        @Override
        public void OnEvokeOrb(AbstractOrb orb) {
            if (this.amount > 0) {
                this.amount -= 1;
                SupportDamagePower supportDamage = GameUtilities.GetPower(player, SupportDamagePower.class);
                if (supportDamage != null && supportDamage.amount > 0) {
                    supportDamage.atEndOfTurn(true);
                }
                updateDescription();
                flash();
            }
        }
    }
}