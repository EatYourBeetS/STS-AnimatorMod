package pinacolada.cards.pcl.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.common.DelayedDamagePower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class KireiKotomine extends PCLCard
{
    public static final PCLCardData DATA = Register(KireiKotomine.class)
            .SetSkill(3, CardRarity.RARE, eatyourbeets.cards.base.EYBCardTarget.Normal)
            .SetSeriesFromClassPackage();

    public KireiKotomine()
    {
        super(DATA);

        Initialize(0, 13, 10, 34);
        SetUpgrade(0, 4, 2, 5);

        SetAffinity_Light(1, 0, 2);
        SetAffinity_Dark(1, 0, 2);
        SetAffinity_Orange(1, 0, 0);

        SetExhaust(true);

        SetAffinityRequirement(PCLAffinity.Orange, 3);
        SetAffinityRequirement(PCLAffinity.Light, 5);
        SetAffinityRequirement(PCLAffinity.Dark, 8);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);

        PCLActions.Bottom.TryChooseSpendAffinity(this, PCLAffinity.Orange, PCLAffinity.Light, PCLAffinity.Dark).AddConditionalCallback(afChoices -> {
           for (AffinityChoice af : afChoices) {
               switch (af.Affinity) {
                   case Orange:
                       PCLActions.Bottom.ApplyConstricted(TargetHelper.Normal(m), magicNumber);
                       break;
                   case Light:
                       int toTransfer = PCLGameUtilities.GetPowerAmount(DelayedDamagePower.POWER_ID);
                       PCLActions.Bottom.ReducePower(player, player, DelayedDamagePower.POWER_ID, toTransfer);
                       if (m != null && toTransfer > 0) {
                           PCLActions.Bottom.DealDamageAtEndOfTurn(player, m, toTransfer, AttackEffects.GUNSHOT);
                       }
                       break;
                   case Dark:
                       for (AbstractCreature cr : PCLGameUtilities.GetEnemies(true)) {
                           PCLActions.Bottom.DealDamageAtEndOfTurn(player, cr, secondaryValue, AttackEffects.GUNSHOT);
                       }
                       break;
               }
           }
        });
    }
}