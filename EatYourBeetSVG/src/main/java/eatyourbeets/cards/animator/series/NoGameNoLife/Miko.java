package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Plasma;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnOrbPassiveEffectSubscriber;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.SuperchargedPower;
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

        SetAffinity_Water(2);
        SetAffinity_Light(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (info.TryActivateLimited()) {
            GameActions.Bottom.GainSupercharged(1);
        }
        GameActions.Bottom.StackPower(new MikoPower(p, magicNumber));
    }

    public static class MikoPower extends AnimatorPower implements OnOrbPassiveEffectSubscriber, OnSynergySubscriber
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
            CombatStats.onSynergy.Subscribe(this);
            SuperchargedPower.AddChargeThreshold(SuperchargedPower.BASE_CHARGE_THRESHOLD);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onOrbPassiveEffect.Unsubscribe(this);
            CombatStats.onSynergy.Unsubscribe(this);
            SuperchargedPower.AddChargeThreshold(-SuperchargedPower.BASE_CHARGE_THRESHOLD);
        }

        @Override
        public void OnOrbPassiveEffect(AbstractOrb orb) {
            if (Plasma.ORB_ID.equals(orb.ID)) {
                GameUtilities.IncreaseSuperchargedCharge(amount * GameUtilities.GetPowerAmount(owner, SuperchargedPower.POWER_ID));
                flash();
            }
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount, SuperchargedPower.CHARGE_THRESHOLD);
        }

        @Override
        public void OnSynergy(AbstractCard card) {
            GameUtilities.IncreaseSuperchargedCharge(amount * GameUtilities.GetPowerAmount(owner, SuperchargedPower.POWER_ID));
            flash();
        }
    }
}