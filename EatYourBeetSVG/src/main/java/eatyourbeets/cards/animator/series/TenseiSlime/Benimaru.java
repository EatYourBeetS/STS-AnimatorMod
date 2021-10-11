package eatyourbeets.cards.animator.series.TenseiSlime;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Benimaru extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Benimaru.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Elemental, EYBCardTarget.Normal)
            .SetSeriesFromClassPackage();

    public Benimaru()
    {
        super(DATA);

        Initialize(3, 0, 2);
        SetUpgrade(4, 0, 0);

        SetAffinity_Fire(1);
        SetAffinity_Water(1);

        SetAffinityRequirement(Affinity.Fire, 4);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.ChannelOrb(new Fire());
            GameActions.Bottom.Flash(this);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.FIRE)
        .SetDamageEffect(e -> GameEffects.List.Add(VFX.Fireball(player.hb, e.hb)).SetColor(Color.RED, Color.ORANGE).SetRealtime(true).duration)
        .AddCallback(m, (enemy, __) -> GameActions.Top.ApplyBurning(player, enemy, CheckAffinity(Affinity.Fire) ? magicNumber + 1 : magicNumber))
        .SetRealtime(true);
    }
}