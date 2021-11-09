package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Evileye extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Evileye.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.Normal)
            .SetSeriesFromClassPackage();

    public Evileye()
    {
        super(DATA);

        Initialize(0,0, 2, 3);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);
        SetAffinity_Dark(1);

        SetEthereal(true);
        SetExhaust(true);

        SetAffinityRequirement(Affinity.Blue, 3);
        SetAffinityRequirement(Affinity.Dark, 3);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (TrySpendAffinity(Affinity.Blue, Affinity.Dark)) {
            int totalToTransfer = magicNumber;
            for (AbstractPower power : player.powers) {
                if (totalToTransfer <= 0) {
                    break;
                }
                for (PowerHelper commonDebuffHelper : GameUtilities.GetCommonDebuffs()) {
                    if (commonDebuffHelper.ID.equals(power.ID)) {
                        int toTransfer = Math.min(totalToTransfer, power.amount);
                        GameActions.Top.ReducePower(power, toTransfer);
                        GameActions.Top.ApplyPower(TargetHelper.RandomEnemy(), commonDebuffHelper, toTransfer);
                        totalToTransfer -= toTransfer;
                        break;
                    }
                }
            }
        }
        GameActions.Bottom.ApplyBlinded(p, m, magicNumber);
        Dark d = new Dark();
        GameActions.Bottom.ChannelOrb(d).AddCallback(() -> {
            int debuffCount = GameUtilities.GetDebuffsCount(m);
            GameActions.Bottom.TriggerOrbPassive(d, debuffCount);
        });
    }
}