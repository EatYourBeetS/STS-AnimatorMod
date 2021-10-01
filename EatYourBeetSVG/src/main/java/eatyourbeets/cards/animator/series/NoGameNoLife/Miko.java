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
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.SuperchargedPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Miko extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Miko.class)
            .SetPower(3, CardRarity.RARE)
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
            GameActions.Bottom.GainSupercharged(1);
        }
        GameActions.Bottom.StackPower(new MikoPower(p, magicNumber));
    }

    public static class MikoPower extends AnimatorPower implements OnOrbPassiveEffectSubscriber
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

            SuperchargedPower.AddChargeThreshold(SuperchargedPower.BASE_CHARGE_THRESHOLD);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            SuperchargedPower.AddChargeThreshold(-SuperchargedPower.BASE_CHARGE_THRESHOLD);
        }

        @Override
        public void onAfterCardPlayed(AbstractCard usedCard)
        {
            super.onAfterCardPlayed(usedCard);

            if (CombatStats.Affinities.IsSynergizing(usedCard))
            {
                GameUtilities.IncreaseSuperchargedCharge(amount);
                flash();
            }
        }

        @Override
        public void OnOrbPassiveEffect(AbstractOrb orb) {
            if (Plasma.ORB_ID.equals(orb.ID)) {
                GameUtilities.IncreaseSuperchargedCharge(amount);
                flash();
            }
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount, SuperchargedPower.CHARGE_THRESHOLD);
        }
    }
}