package pinacolada.cards.pcl.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.RainbowCardEffect;
import pinacolada.actions.orbs.WaterOrbEvokeAction;
import pinacolada.cards.base.*;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.HPAttribute;
import pinacolada.monsters.PCLEnemyIntent;
import pinacolada.orbs.pcl.Water;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class BarbaraPegg extends PCLCard
{
    public static final PCLCardData DATA = Register(BarbaraPegg.class)
            .SetSkill(1, CardRarity.UNCOMMON, PCLCardTarget.None)
            .SetMaxCopies(3)
            .SetSeriesFromClassPackage();
    private static final int UNIQUE_THRESHOLD = 4;

    public BarbaraPegg()
    {
        super(DATA);

        Initialize(0, 0, 4, 1);
        SetUpgrade(0, 0, 3, 0);
        SetAffinity_Light(1);
        SetAffinity_Blue(1);


        SetAffinityRequirement(PCLAffinity.Blue, 7);
        SetHealing(true);
        SetPurge(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return HPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.VFX(new RainbowCardEffect());
        PCLActions.Bottom.Heal(magicNumber);
        Water waterOrb = new Water();
        PCLActions.Bottom.ChannelOrb(waterOrb).AddCallback(orbs -> {
            int attackingCount = PCLJUtils.Count(PCLGameUtilities.GetPCLIntents(), PCLEnemyIntent::IsAttacking);
            if (attackingCount > 0) {
                PCLActions.Bottom.TriggerOrbPassive(waterOrb, attackingCount).AddCallback(__ -> {
                    if (info.CanActivateSemiLimited && TrySpendAffinity(PCLAffinity.Blue) && info.TryActivateSemiLimited()) {
                        int amount = waterOrb.evokeAmount / 2;
                        PCLActions.Bottom.Add(new WaterOrbEvokeAction(waterOrb.hb, amount));
                        PCLActions.Delayed.Callback(() -> {
                            waterOrb.SetBaseEvokeAmount(amount, true);
                        });
                    }
                });
            }


        });

    }
}