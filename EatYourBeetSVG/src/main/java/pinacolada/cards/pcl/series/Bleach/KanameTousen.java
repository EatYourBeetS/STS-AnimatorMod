package pinacolada.cards.pcl.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLPower;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.powers.common.BlindedPower;
import pinacolada.powers.common.ResistancePower;
import pinacolada.stances.WisdomStance;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class KanameTousen extends PCLCard {
    public static final PCLCardData DATA = Register(KanameTousen.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.ALL).SetSeriesFromClassPackage();

    public KanameTousen() {
        super(DATA);

        Initialize(0, 5, 2, 1);
        SetUpgrade(0, 4, 0);
        SetAffinity_Dark(1, 0, 0);
        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Green(1, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        PCLActions.Bottom.GainBlock(block);
        for (AbstractMonster mo : PCLGameUtilities.GetEnemies(true)) {
            if (mo.hasPower(BlindedPower.POWER_ID)) {
                PCLActions.Bottom.GainTemporaryArtifact(secondaryValue);
            }
            else {
                PCLActions.Bottom.ApplyBlinded(player, mo, magicNumber);
            }
        }

        if (WisdomStance.IsActive() && CombatStats.TryActivateLimited(cardID)) {
            PCLActions.Bottom.StackPower(player, new KanameTousenPower(player, 2));
        }
    }


    public static class KanameTousenPower extends PCLPower
    {
        private static final String[] POWER_IDS = {StrengthPower.POWER_ID, DexterityPower.POWER_ID, FocusPower.POWER_ID, ResistancePower.POWER_ID};
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
                PCLActions.Bottom.StackPower(TargetHelper.Source(), PCLPowerHelper.ALL.get(entry.getKey()), entry.getValue());
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
                int powerAmount = PCLGameUtilities.GetPowerAmount(owner, powerID);
                if (powerAmount < 0) {
                    counts.merge(powerID, powerAmount, Integer::sum);
                    if (owner.hasPower(powerID)) {
                        PCLActions.Bottom.RemovePower(owner,owner,powerID);
                    }
                }
            }
        }
    }
}