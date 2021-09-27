package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.animator.special.OrbCore;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnEvokeOrbSubscriber;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.powers.common.SupportDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class InverseOrigami extends AnimatorCard
{
    public static final EYBCardData DATA = Register(InverseOrigami.class).SetPower(2, CardRarity.SPECIAL).SetSeries(CardSeries.DateALive);
    public static final int SUPPORT_DAMAGE_COST = 3;

    public InverseOrigami() {
        super(DATA);

        Initialize(0, 0, 3, SUPPORT_DAMAGE_COST);
        SetAffinity_Blue(1, 1, 0);
        SetAffinity_Dark(1, 0, 0);
        SetAutoplay(true);
    }

    @Override
    protected void OnUpgrade() {
        SetAutoplay(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.StackPower(new InverseOrigamiPower(p, magicNumber));
        GameActions.Bottom.Add(OrbCore.SelectCoreAction(name, 1)
                .AddCallback(c -> {if (c.size() > 0) {GameActions.Bottom.PlayCard(c.get(0), m);}}));
    }

    public static class InverseOrigamiPower extends AnimatorClickablePower implements OnEvokeOrbSubscriber {
        public static final int REFRESH_TIMES = 2;

        public InverseOrigamiPower(AbstractPlayer owner, int amount) {
            super(owner, InverseOrigami.DATA, PowerTriggerConditionType.Special, SUPPORT_DAMAGE_COST);
            this.triggerCondition.SetCheckCondition((c) -> GameUtilities.GetPowerAmount(player, SupportDamagePower.POWER_ID) >= SUPPORT_DAMAGE_COST)
                    .SetPayCost(a -> {
                        SupportDamagePower supportDamage = GameUtilities.GetPower(player, SupportDamagePower.class);
                        if (supportDamage != null) {
                            supportDamage.ReducePower(a);
                        }
                    });

            Initialize(amount);
        }

        @Override
        public void onInitialApplication()
        {
            CombatStats.onEvokeOrb.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            CombatStats.onEvokeOrb.Unsubscribe(this);
        }

        @Override
        public String GetUpdatedDescription() {
            return FormatDescription(0, amount, SUPPORT_DAMAGE_COST, baseAmount);
        }


        @Override
        public void OnUse(AbstractMonster m) {
            this.amount += REFRESH_TIMES;
            GameActions.Bottom.TriggerOrbPassive(baseAmount, true, false);
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