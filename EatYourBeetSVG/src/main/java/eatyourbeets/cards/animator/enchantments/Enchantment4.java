package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.affinity.AbstractAffinityPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Enchantment4 extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(Enchantment4.class);
    public static final int INDEX = 4;
    public Affinity currentAffinity;

    public Enchantment4()
    {
        super(DATA, INDEX);

        Initialize(0, 0, 1, 1);
    }

    @Override
    protected void OnUpgrade()
    {
        if (auxiliaryData.form == 7) {
            upgradeMagicNumber(1);
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
            for (Affinity t : Affinity.Basic())
            {
                final AbstractAffinityPower p = CombatStats.Affinities.GetPower(t);
                if (p.amountGainedThisTurn > 0)
                {
                    p.RetainOnce();
                }
            }
            return;
        }

        currentAffinity = GetAffinity();
        if (currentAffinity != null) {
            final AbstractAffinityPower p = CombatStats.Affinities.GetPower(currentAffinity);
            if (GameUtilities.InStance(NeutralStance.STANCE_ID) || GameUtilities.InStance(currentAffinity)) {
                GameActions.Bottom.StackAffinityPower(currentAffinity, magicNumber, true);
            }
            else {
                GameUtilities.MaintainPower(currentAffinity);
            }

            if (p.GetThresholdBonusPower() == null) {
                GameActions.Bottom.GainEnergyNextTurn(magicNumber);
            }
            else {
                GameActions.Bottom.StackPower(TargetHelper.Player(), p.GetThresholdBonusPower(), magicNumber);
            }

        }
    }

    @Override
    public void AtEndOfTurnEffect(boolean isPlayer) {
        if (currentAffinity != null) {
            final AbstractAffinityPower p = CombatStats.Affinities.GetPower(currentAffinity);
            if (p.GetThresholdBonusPower() != null) {
                GameActions.Bottom.StackPower(TargetHelper.Player(), p.GetThresholdBonusPower(), -magicNumber);
            }

            currentAffinity = null;
        }
    }

    public Affinity GetAffinity()
    {
        switch (auxiliaryData.form)
        {
            case 1: return Affinity.Fire;
            case 2: return Affinity.Air;
            case 3: return Affinity.Water;
            case 4: return Affinity.Earth;
            case 5: return Affinity.Light;
            case 6: return Affinity.Dark;
            default: return null;
        }
    }
}