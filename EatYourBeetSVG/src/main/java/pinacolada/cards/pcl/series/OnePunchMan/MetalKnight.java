package pinacolada.cards.pcl.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class MetalKnight extends PCLCard
{
    public static final PCLCardData DATA = Register(MetalKnight.class)
            .SetAttack(3, CardRarity.UNCOMMON, PCLAttackType.Ranged)
            .SetSeriesFromClassPackage();

    public MetalKnight()
    {
        super(DATA);

        Initialize(13, 6, 3, 2);
        SetUpgrade(3, 0, 0, 0);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Silver(1, 0, 1);
        SetAffinity_Dark(1);
        SetAffinity_Blue(0,0,1);

        SetAffinityRequirement(PCLAffinity.Silver, 3);
        SetEvokeOrbCount(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.VFX(new WeightyImpactEffect(m.hb.cX, m.hb.cY), 0.6f, true);
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY);
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.GainMetallicize(magicNumber);
        PCLActions.Bottom.ModifyAllInstances(uuid, c -> PCLGameUtilities.DecreaseMagicNumber(c, 1, false));
        if (TrySpendAffinity(PCLAffinity.Silver)) {
            PCLActions.Bottom.ChannelOrbs(Plasma::new, 1);
        }
    }
}