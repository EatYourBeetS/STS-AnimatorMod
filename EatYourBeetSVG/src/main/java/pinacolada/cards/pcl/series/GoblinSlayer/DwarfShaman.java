package pinacolada.cards.pcl.series.GoblinSlayer;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.orbs.pcl.Earth;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

public class DwarfShaman extends PCLCard
{
    public static final PCLCardData DATA = Register(DwarfShaman.class)
            .SetAttack(1, CardRarity.COMMON, PCLAttackType.Elemental)
            .SetSeriesFromClassPackage();

    public DwarfShaman()
    {
        super(DATA);

        Initialize(2, 0, 3);
        SetUpgrade(4, 0, 0);

        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Orange(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.General, 6);
        SetEvokeOrbCount(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.NONE).forEach(d -> d.SetVFX(true, false)
        .SetDamageEffect(c -> PCLGameEffects.List.Add(VFX.ThrowRock(player.hb, c.hb, 0.4f)).duration).SetRealtime(true));
        PCLActions.Bottom.ChannelOrb(new Earth());

        PCLActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback(() -> {
            PCLActions.Bottom.UpgradeFromPile(p.drawPile, 1, false);
        });
    }
}