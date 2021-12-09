package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.powers.common.EnchantedArmorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Gargantua extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Gargantua.class)
            .SetAttack(3, CardRarity.UNCOMMON, EYBAttackType.Brutal)
            .SetSeriesFromClassPackage();
    public static final int CHARGE_COST = 5;

    public Gargantua()
    {
        super(DATA);

        Initialize(10, 11, 2, 15);
        SetUpgrade(2, 2, 0, 0);

        SetAffinity_Red(0,0,2);
        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Dark(1);

    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.NONE).forEach(d -> d.SetVFX(true, false)
                .SetDamageEffect(c -> GameEffects.List.Add(VFX.ThrowRock(player.hb, c.hb, 0.5f)).duration).SetRealtime(true));
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.EvokeOrb(1)
                .SetFilter(o -> Earth.ORB_ID.equals(o.ID))
                .AddCallback(orbs ->
                {
                    if (orbs.size() > 0)
                    {
                        GameActions.Bottom.GainPlatedArmor(magicNumber);
                    }
                    else
                    {
                        GameActions.Bottom.ChannelOrb(new Earth());
                    }
                });

        if (GameUtilities.CanSpendAffinityPower(Affinity.Orange, CHARGE_COST) && info.TryActivateLimited()) {
            GameUtilities.TrySpendAffinityPower(Affinity.Orange, CHARGE_COST);
            GameActions.Bottom.StackPower(new EnchantedArmorPower(p, secondaryValue));
        }

    }
}