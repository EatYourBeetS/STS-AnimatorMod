package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Plasma;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnOrbPassiveEffectSubscriber;
import eatyourbeets.interfaces.subscribers.OnSpendEnergySubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.affinity.SuperchargePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Miko extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Miko.class)
            .SetPower(3, CardRarity.RARE)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public Miko()
    {
        super(DATA);

        Initialize(0, 0, 2);

        SetAffinity_Blue(2);
        SetAffinity_Light(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (info.TryActivateLimited()) {
            GameActions.Bottom.IncreaseAffinityPowerLevel(Affinity.Light, 1);
        }
        GameActions.Bottom.StackPower(new MikoPower(p, magicNumber));
    }

    public static class MikoPower extends AnimatorPower implements OnOrbPassiveEffectSubscriber, OnSpendEnergySubscriber
    {
        public MikoPower(AbstractCreature owner, int amount)
        {
            super(owner, Miko.DATA);

            Initialize(amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.onOrbPassiveEffect.Subscribe(this);
            CombatStats.onSpendEnergy.Subscribe(this);
            GameUtilities.SetAffinityPowerThreshold(Affinity.Light, SuperchargePower.BASE_CHARGE_THRESHOLD, true);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onOrbPassiveEffect.Unsubscribe(this);
            CombatStats.onSpendEnergy.Unsubscribe(this);
            GameUtilities.SetAffinityPowerThreshold(Affinity.Light, -SuperchargePower.BASE_CHARGE_THRESHOLD, true);
        }

        @Override
        public void OnOrbPassiveEffect(AbstractOrb orb) {
            if (Plasma.ORB_ID.equals(orb.ID)) {
                GameActions.Bottom.GainSupercharge(amount * orb.passiveAmount);
                flash();
            }
        }

        @Override
        public void onEvokeOrb(AbstractOrb orb)
        {
            super.onEvokeOrb(orb);

            if (Plasma.ORB_ID.equals(orb.ID)) {
                GameActions.Bottom.GainSupercharge(amount * orb.evokeAmount);
                flash();
            }
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount, GameUtilities.GetAffinityPowerThreshold(Affinity.Light));
        }

        @Override
        public int OnSpendEnergy(int spendAmount) {
            GameActions.Bottom.GainSupercharge(amount * spendAmount);
            flash();
            return spendAmount;
        }
    }
}