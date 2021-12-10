package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.powers.ElectroPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.common.TemporaryElectroPower;
import eatyourbeets.stances.InvocationStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.TargetHelper;

public class NarberalGamma extends AnimatorCard
{
    public static final EYBCardData DATA = Register(NarberalGamma.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public NarberalGamma()
    {
        super(DATA);

        Initialize(0, 1, 1, 2);

        SetAffinity_Star(1);
        SetAffinity_Light(0,0,1);

        SetEvokeOrbCount(1);
    }

    @Override
    public int GetXValue() {
        return JUtils.Count(player.hand.group, c -> GameUtilities.GetAffinityLevel(c, Affinity.General, true) == 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.ChannelOrb(new Lightning());

        if (upgraded)
        {
            GameActions.Bottom.Draw(1);
        }

        if (!p.hasPower(ElectroPower.POWER_ID))
        {
            GameActions.Bottom.ApplyPower(p, p, new TemporaryElectroPower(p));
        }

        if (InvocationStance.IsActive() && info.TryActivateSemiLimited()) {
            for (int i = 0; i < GetXValue(); i++) {
                GameActions.Bottom.ApplyElectrified(TargetHelper.RandomEnemy(), secondaryValue);
            }
        }
    }
}