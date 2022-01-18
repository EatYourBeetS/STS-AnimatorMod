package pinacolada.cards.pcl.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.orbs.pcl.Earth;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class Gargantua extends PCLCard
{
    public static final PCLCardData DATA = Register(Gargantua.class)
            .SetAttack(3, CardRarity.UNCOMMON, PCLAttackType.Brutal)
            .SetSeriesFromClassPackage();
    public static final int CHARGE_COST = 10;

    public Gargantua()
    {
        super(DATA);

        Initialize(10, 11, 2, 6);
        SetUpgrade(2, 2, 0, 0);

        SetAffinity_Red(1,0,2);
        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Dark(1);

    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.NONE).forEach(d -> d.SetVFX(true, false)
                .SetDamageEffect(c -> PCLGameEffects.List.Add(VFX.ThrowRock(player.hb, c.hb, 0.5f)).duration).SetRealtime(true));
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.EvokeOrb(1)
                .SetFilter(o -> Earth.ORB_ID.equals(o.ID))
                .AddCallback(orbs ->
                {
                    if (orbs.size() > 0)
                    {
                        PCLActions.Bottom.GainPlatedArmor(magicNumber);
                    }
                    else
                    {
                        PCLActions.Bottom.ChannelOrb(new Earth());
                    }
                });

        if (PCLGameUtilities.CanSpendAffinityPower(PCLAffinity.Orange, CHARGE_COST) && info.TryActivateLimited()) {
            PCLGameUtilities.TrySpendAffinityPower(PCLAffinity.Orange, CHARGE_COST);
            PCLActions.Bottom.StackPower(new PlatedArmorPower(p, secondaryValue));
        }

    }
}