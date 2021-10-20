package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.*;
import eatyourbeets.orbs.animator.Air;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class YuukaKazami extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YuukaKazami.class).SetSkill(2, CardRarity.COMMON, EYBCardTarget.Self).SetSeriesFromClassPackage();

    public YuukaKazami()
    {
        super(DATA);

        Initialize(0, 8, 2, 3);
        SetUpgrade(0, 3, 0, 0);
        SetAffinity_Blue(2, 0, 1);
        SetAffinity_Green(1, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        if (GameActionManager.turn % 2 == 0) {
            GameActions.Bottom.ChannelOrb(new Air());
        }
        else {
            GameActions.Bottom.ChannelOrb(new Lightning());
        }
    }

    @Override
    public void triggerWhenDrawn()
    {
        CombatStats.Affinities.AddAffinity(CombatStats.Affinities.GetAffinityLevel(Affinity.Blue,true) > CombatStats.Affinities.GetAffinityLevel(Affinity.Green,true) ? Affinity.Green : Affinity.Blue, 1);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        CombatStats.Affinities.AddAffinity(CombatStats.Affinities.GetAffinityLevel(Affinity.Blue,true) > CombatStats.Affinities.GetAffinityLevel(Affinity.Green,true) ? Affinity.Green : Affinity.Blue, 1);
    }
}

