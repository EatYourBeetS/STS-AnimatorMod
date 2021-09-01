package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.common.BalancePower;
import eatyourbeets.powers.common.BlindedPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class KanameTousen extends AnimatorCard {
    public static final EYBCardData DATA = Register(KanameTousen.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.ALL).SetSeriesFromClassPackage();

    public KanameTousen() {
        super(DATA);

        Initialize(0, 5, 2, 1);
        SetUpgrade(0, 4, 0);
        SetAffinity_Dark(1, 0, 0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Green(1, 1, 0);

        SetAffinityRequirement(Affinity.Dark, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing) {
        GameActions.Bottom.GainBlock(block);
        for (AbstractMonster mo : GameUtilities.GetEnemies(true)) {
            if (mo.hasPower(BlindedPower.POWER_ID)) {
                GameActions.Bottom.GainTemporaryArtifact(secondaryValue);
            }
            else {
                GameActions.Bottom.ApplyBlinded(player, mo, magicNumber);
            }
        }

        if (CheckAffinity(Affinity.Dark) && CombatStats.TryActivateLimited(cardID)) {
            GameActions.Bottom.StackPower(player, new KanameTousenPower(player, 2));
        }
    }


    public static class KanameTousenPower extends AnimatorPower
    {
        private static final String[] POWER_IDS = {StrengthPower.POWER_ID, DexterityPower.POWER_ID, FocusPower.POWER_ID, BalancePower.POWER_ID};
        private final HashMap<String, Integer> counts = new HashMap<>();

        public KanameTousenPower(AbstractPlayer owner, int amount)
        {
            super(owner, KanameTousen.DATA);

            this.amount = amount;
            RefreshCounts();
            updateDescription();
        }

        @Override
        public void atEndOfRound() {
            ReducePower(1);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            for (Map.Entry<String, Integer> entry : counts.entrySet()) {
                GameActions.Bottom.StackPower(TargetHelper.Source(), PowerHelper.ALL.get(entry.getKey()), entry.getValue());
            }
        }

        @Override
        public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
        {
            super.onApplyPower(power, target, source);

            if (target == owner && Arrays.stream(POWER_IDS).anyMatch(s -> s.equals(power.ID))) {
                RefreshCounts();
            }
        }

        private void RefreshCounts() {
            for (String powerID : POWER_IDS) {
                int powerAmount = GameUtilities.GetPowerAmount(owner, powerID);
                if (powerAmount < 0) {
                    counts.merge(powerID, powerAmount, Integer::sum);
                    if (owner.hasPower(powerID)) {
                        GameActions.Bottom.RemovePower(owner,owner,powerID);
                    }
                }
            }
        }
    }
}