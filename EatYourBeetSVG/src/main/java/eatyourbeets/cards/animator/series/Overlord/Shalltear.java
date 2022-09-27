package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Shalltear extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shalltear.class)
            .SetAttack(2, CardRarity.RARE, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetSeries(CardSeries.Overlord);

    public Shalltear()
    {
        super(DATA);

        Initialize(6, 0, 3);
        SetUpgrade(3, 0, 1);

        SetAffinity_Green(1);
        SetAffinity_Blue(2, 0, 3);
        SetAffinity_Dark(2, 0, 9);

        SetHealing(true);
        SetExhaust(true);
    }

    @Override
    public void triggerOnAffinitySeal(boolean reshuffle)
    {
        super.triggerOnAffinitySeal(reshuffle);

        GameActions.Bottom.Callback(() ->
        {
            final int light = CombatStats.Affinities.UseAffinity(Affinity.Light, 99);
            if (light > 0)
            {
                GameActions.Bottom.ShowCopy(this);
                GameActions.Bottom.GainAffinity(Affinity.Dark, light, false);
            }
        });
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.NONE)
        .SetDamageEffect((enemy, __) -> GameEffects.List.Add(VFX.Hemokinesis(player.hb, enemy.hb)));

        final int healAmount = CombatStats.Affinities.GetPowerAmount(Affinity.Dark);
        if (healAmount > 0)
        {
            GameActions.Bottom.HealPlayerLimited(this, healAmount);
        }
    }
}