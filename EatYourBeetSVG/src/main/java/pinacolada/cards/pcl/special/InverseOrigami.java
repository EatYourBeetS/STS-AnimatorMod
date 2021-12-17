package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.interfaces.subscribers.OnEvokeOrbSubscriber;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.powers.common.SupportDamagePower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class InverseOrigami extends PCLCard
{
    public static final PCLCardData DATA = Register(InverseOrigami.class).SetPower(2, CardRarity.SPECIAL).SetSeries(CardSeries.DateALive);
    public static final int SUPPORT_DAMAGE_COST = 3;

    public InverseOrigami() {
        super(DATA);

        Initialize(0, 0, 3, SUPPORT_DAMAGE_COST);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Dark(1, 0, 0);
        SetAutoplay(true);
    }

    @Override
    protected void OnUpgrade() {
        SetAutoplay(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        PCLActions.Bottom.StackPower(new InverseOrigamiPower(p, magicNumber));
        PCLActions.Bottom.Add(OrbCore.SelectCoreAction(name, 1)
                .AddCallback(c -> {if (c.size() > 0) {
                    PCLActions.Bottom.PlayCard(c.get(0), m);}}));
    }

    public static class InverseOrigamiPower extends PCLClickablePower implements OnEvokeOrbSubscriber {
        public static final int REFRESH_TIMES = 2;

        public InverseOrigamiPower(AbstractPlayer owner, int amount) {
            super(owner, InverseOrigami.DATA, PowerTriggerConditionType.Special, SUPPORT_DAMAGE_COST);
            this.triggerCondition.SetCheckCondition((c) -> PCLGameUtilities.GetPowerAmount(player, SupportDamagePower.POWER_ID) >= SUPPORT_DAMAGE_COST)
                    .SetPayCost(a -> {
                        SupportDamagePower supportDamage = PCLGameUtilities.GetPower(player, SupportDamagePower.class);
                        if (supportDamage != null) {
                            supportDamage.ReducePower(a);
                        }
                    });

            Initialize(amount);
        }

        @Override
        public void onInitialApplication()
        {
            PCLCombatStats.onEvokeOrb.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            PCLCombatStats.onEvokeOrb.Unsubscribe(this);
        }

        @Override
        public String GetUpdatedDescription() {
            return FormatDescription(0, amount, SUPPORT_DAMAGE_COST, baseAmount);
        }


        @Override
        public void OnUse(AbstractMonster m, int cost) {
            this.amount += REFRESH_TIMES;
            PCLActions.Bottom.TriggerOrbPassive(baseAmount, true, false);
        }

        @Override
        public void OnEvokeOrb(AbstractOrb orb) {
            if (this.amount > 0) {
                this.amount -= 1;
                SupportDamagePower supportDamage = PCLGameUtilities.GetPower(player, SupportDamagePower.class);
                if (supportDamage != null && supportDamage.amount > 0) {
                    supportDamage.atEndOfTurn(true);
                }
                updateDescription();
                flash();
            }
        }
    }
}