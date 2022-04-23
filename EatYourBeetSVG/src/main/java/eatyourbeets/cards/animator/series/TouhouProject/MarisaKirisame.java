package eatyourbeets.cards.animator.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;

public class MarisaKirisame extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MarisaKirisame.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();

    public MarisaKirisame()
    {
        super(DATA);

        Initialize(4, 0, 2);
        SetUpgrade(2, 0, 0);

        SetAffinity_Light(1);
        SetAffinity_Blue(1, 1, 1);

        SetAffinityRequirement(Affinity.Blue, 4);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.VFX(VFX.ShootingStars(player.hb, player.hb.height));
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.NONE);

        GameActions.Bottom.TriggerOrbPassive(1)
        .SetFilter(orb -> Lightning.ORB_ID.equals(orb.ID))
        .AddCallback(orbs ->
        {
            if (orbs.isEmpty())
            {
                GameActions.Bottom.ChannelOrb(new Lightning());
            }
        });

        if (TryUseAffinity(Affinity.Blue))
        {
            GameActions.Bottom.TriggerOrbPassive(1)
            .SetFilter(orb -> Fire.ORB_ID.equals(orb.ID))
            .AddCallback(orbs ->
            {
                if (orbs.isEmpty())
                {
                    GameActions.Bottom.ChannelOrb(new Fire());
                }
            });
        }
    }
}

