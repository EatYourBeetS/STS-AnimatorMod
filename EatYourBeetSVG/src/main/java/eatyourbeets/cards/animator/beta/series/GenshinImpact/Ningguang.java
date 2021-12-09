package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.orbs.EarthOrbEvokeAction;
import eatyourbeets.actions.orbs.EarthOrbPassiveAction;
import eatyourbeets.cards.base.*;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Ningguang extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Ningguang.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.Self).SetSeriesFromClassPackage(true);

    public Ningguang()
    {
        super(DATA);

        Initialize(0, 2, 5, 4);
        SetUpgrade(0, 1, -1, 0);
        SetAffinity_Blue(1, 0, 2);
        SetAffinity_Orange(1, 0, 0);

        SetAffinityRequirement(Affinity.Orange, 5);
        SetAffinityRequirement(Affinity.Blue, 5);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        final Earth next = JUtils.SafeCast(GameUtilities.GetFirstOrb(Earth.ORB_ID), Earth.class);
        if (next != null && next.projectilesCount > magicNumber) {
            GameActions.Top.Add(new EarthOrbPassiveAction(next, -magicNumber));
            GameActions.Bottom.Add(new EarthOrbEvokeAction(next, magicNumber));
            GameActions.Delayed.Callback(() -> {
               next.IncreaseBaseEvokeAmount(1);
               next.evokeAmount = next.GetBaseEvokeAmount();
            });
        }
        else {
            GameActions.Bottom.ChannelOrb(new Earth());
        }

        if (info.CanActivateSemiLimited && (CheckAffinity(Affinity.Orange) || CheckAffinity(Affinity.Blue)) && info.TryActivateSemiLimited()) {
            GameActions.Bottom.TryChooseSpendAffinity(this, Affinity.Blue, Affinity.Orange).AddConditionalCallback(() -> {
                GameActions.Bottom.GainResistance(secondaryValue, true);
            });
        }
    }
}

