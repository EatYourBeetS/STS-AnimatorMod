package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.powers.ElectroPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.common.TemporaryElectroPower;
import eatyourbeets.stances.DesecrationStance;
import eatyourbeets.stances.InvocationStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.TargetHelper;

public class NarberalGamma extends AnimatorCard
{
    public static final EYBCardData DATA = Register(NarberalGamma.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.Normal)
            .SetSeriesFromClassPackage();

    public NarberalGamma()
    {
        super(DATA);

        Initialize(0, 1, 1, 2);
        SetUpgrade(0,3,0,0);

        SetAffinity_Dark(1, 0, 1);
        SetAffinity_Light(1,0,1);

        SetEvokeOrbCount(1);
    }

    @Override
    public int GetXValue() {
        return secondaryValue * JUtils.Count(player.hand.group, c -> GameUtilities.GetAffinityLevel(c, Affinity.Dark, true) > 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.ChannelOrb(new Lightning());

        if (!p.hasPower(ElectroPower.POWER_ID))
        {
            GameActions.Bottom.ApplyPower(p, p, new TemporaryElectroPower(p));
        }

        if ((InvocationStance.IsActive() || DesecrationStance.IsActive()) && info.TryActivateSemiLimited()) {
            GameActions.Bottom.ApplyElectrified(TargetHelper.Normal(m), GetXValue());
        }
    }
}