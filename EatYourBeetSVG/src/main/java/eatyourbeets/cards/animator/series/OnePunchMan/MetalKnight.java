package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class MetalKnight extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MetalKnight.class)
            .SetAttack(3, CardRarity.UNCOMMON, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage();

    public MetalKnight()
    {
        super(DATA);

        Initialize(13, 6, 3, 2);
        SetUpgrade(3, 0, 0, 0);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Silver(2, 0, 1);
        SetAffinity_Dark(1);
        SetAffinity_Blue(0,0,1);

        SetAffinityRequirement(Affinity.Silver, 3);
        SetEvokeOrbCount(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.VFX(new WeightyImpactEffect(m.hb.cX, m.hb.cY), 0.6f, true);
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY);
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainMetallicize(magicNumber);
        GameActions.Bottom.ModifyAllInstances(uuid, c -> GameUtilities.DecreaseMagicNumber(c, 1, false));
        if (TrySpendAffinity(Affinity.Silver)) {
            GameActions.Bottom.ChannelOrbs(Plasma::new, 1);
        }
    }
}