package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.*;
import eatyourbeets.orbs.animator.Air;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class YuukaKazami extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YuukaKazami.class).SetSkill(2, CardRarity.COMMON, EYBCardTarget.Self).SetSeriesFromClassPackage();

    public YuukaKazami()
    {
        super(DATA);

        Initialize(0, 9, 2, 3);
        SetUpgrade(0, 3, 0, 0);
        SetAffinity_Blue(2, 0, 1);
        SetAffinity_Green(1, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        if (CheckPrimaryCondition(true)) {
            GameActions.Bottom.ChannelOrb(new Air());
        }
        else {
            GameActions.Bottom.ChannelOrb(new Lightning());
        }
    }

    @Override
    public void triggerWhenDrawn()
    {
        Affinity lowest = JUtils.FindMin(Affinity.Basic(), af -> CombatStats.Affinities.GetAffinityLevel((Affinity) af,true));
        CombatStats.Affinities.AddAffinity(lowest, 1);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        Affinity lowest = JUtils.FindMin(Affinity.Basic(), af -> CombatStats.Affinities.GetAffinityLevel((Affinity) af,true));
        CombatStats.Affinities.AddAffinity(lowest, 1);
    }

    @Override
    public boolean CheckPrimaryCondition(boolean tryUse)
    {
        return GameActionManager.turn % 2 == 0;
    }
}

