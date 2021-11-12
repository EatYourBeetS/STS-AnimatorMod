package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.orbs.EarthOrbEvokeAction;
import eatyourbeets.actions.orbs.EarthOrbPassiveAction;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Ningguang extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Ningguang.class).SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Elemental).SetSeriesFromClassPackage(true);

    public Ningguang()
    {
        super(DATA);

        Initialize(3, 0, 5, 3);
        SetUpgrade(3, 0, 0, 0);
        SetAffinity_Blue(2, 0, 2);
        SetAffinity_Orange(2, 0, 0);

        SetAffinityRequirement(Affinity.Orange, 6);
        SetAffinityRequirement(Affinity.Blue, 6);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE).forEach(d -> d.SetVFX(true, false)
                .SetDamageEffect(c -> GameEffects.List.Add(VFX.ThrowRock(player.hb, c.hb, 0.5f)).duration).SetRealtime(true));
        final Earth next = JUtils.SafeCast(GameUtilities.GetFirstOrb(Earth.ORB_ID), Earth.class);
        if (next != null && next.projectilesCount > magicNumber) {
            GameActions.Top.Add(new EarthOrbPassiveAction(next, -magicNumber));
            GameActions.Bottom.Add(new EarthOrbEvokeAction(next, magicNumber));
            GameActions.Delayed.Callback(() -> {
               next.IncreaseBaseEvokeAmount(1);
            });
        }
        else {
            GameActions.Bottom.ChannelOrb(new Earth());
        }

        if (info.CanActivateSemiLimited && (CheckAffinity(Affinity.Orange) || CheckAffinity(Affinity.Blue)) && info.TryActivateSemiLimited()) {
            GameActions.Bottom.TryChooseSpendAffinity(this, Affinity.Blue, Affinity.Orange).AddConditionalCallback(() -> {
                GameActions.Bottom.GainEndurance(secondaryValue, true);
            });
        }
    }
}

