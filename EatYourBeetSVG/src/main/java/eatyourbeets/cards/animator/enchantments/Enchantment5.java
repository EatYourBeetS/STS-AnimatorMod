package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.ui.animator.combat.EYBAffinityMeter;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Enchantment5 extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(Enchantment5.class);
    public static final int INDEX = 5;
    public Affinity currentAffinity;

    public Enchantment5()
    {
        super(DATA, INDEX);

        Initialize(0, 0, 1, 4);
    }

    @Override
    protected void OnUpgrade()
    {
        if (auxiliaryData.form == 7) {
            upgradeMagicNumber(1);
        }
    }

    @Override
    public boolean CanUsePower(int cost)
    {
        if (GetAffinity() == null) {
            return CombatStats.Affinities.GetAffinityLevel(Affinity.General, true) >= cost;
        }
        return CombatStats.Affinities.GetAffinityLevel(GetAffinity(), true) >= cost;
    }

    @Override
    public void PayPowerCost(int cost)
    {
        if (GetAffinity() == null) {
            GameActions.Bottom.TryChooseSpendAffinity(name, cost);
        }
        else {
            GameUtilities.TrySpendAffinity(GetAffinity(), affinities.GetRequirement(GetAffinity()), true);
        }
    }

    @Override
    public int GetMaxUpgradeIndex()
    {
        return 6;
    }

    @Override
    public void UsePower(AbstractMonster m)
    {
        if (!upgraded)
        {
            GameActions.Bottom.RerollAffinity(EYBAffinityMeter.Target.CurrentAffinity)
                    .SetOptions(true, true);
            GameActions.Bottom.RerollAffinity(EYBAffinityMeter.Target.NextAffinity)
                    .SetOptions(true, true);
            return;
        }

        currentAffinity = GetAffinity();
        if (currentAffinity != null) {
            GameActions.Bottom.RerollAffinity(EYBAffinityMeter.Target.CurrentAffinity)
                    .SetAffinityChoices(this.currentAffinity)
                    .SetOptions(true, true);
            GameActions.Bottom.RerollAffinity(EYBAffinityMeter.Target.NextAffinity)
                    .SetAffinityChoices(this.currentAffinity)
                    .SetOptions(true, true);
        }
        else {
            Affinity af1 = JUtils.Random(Affinity.Basic());
            Affinity af2 = JUtils.Random(JUtils.Filter(Affinity.Extended(), a -> a != af1));
            GameActions.Bottom.RerollAffinity(EYBAffinityMeter.Target.CurrentAffinity)
                    .SetAffinityChoices(af1, af2)
                    .SetOptions(false, true).AddCallback(newAf -> {
                        GameActions.Bottom.RerollAffinity(EYBAffinityMeter.Target.NextAffinity)
                                .SetAffinityChoices(newAf)
                                .SetOptions(true, true);
                    });
        }
    }

    public Affinity GetAffinity()
    {
        switch (auxiliaryData.form)
        {
            case 1: return Affinity.Red;
            case 2: return Affinity.Green;
            case 3: return Affinity.Blue;
            case 4: return Affinity.Orange;
            case 5: return Affinity.Light;
            case 6: return Affinity.Dark;
            default: return null;
        }
    }
}