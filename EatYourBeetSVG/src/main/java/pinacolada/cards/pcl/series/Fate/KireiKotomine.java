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
            .SetSkill(2, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.Normal)
            .SetSeriesFromClassPackage(true);

    public KireiKotomine()
    {
        super(DATA);

        Initialize(0, 8, 4, 20);
        SetUpgrade(0, 3, 1, 5);

        SetAffinity_Light(1, 0, 1);
        SetAffinity_Dark(1, 0, 1);
        SetAffinity_Orange(1, 0, 0);

        SetExhaust(true);

        SetAffinityRequirement(PCLAffinity.Orange, 3);
        SetAffinityRequirement(PCLAffinity.Light, 7);
        SetAffinityRequirement(PCLAffinity.Dark, 8);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);

        PCLActions.Bottom.TryChooseSpendAffinity(this, PCLAffinity.Green, PCLAffinity.Light, PCLAffinity.Dark).AddConditionalCallback(afChoices -> {
           for (AffinityChoice af : afChoices) {
               switch (af.Affinity) {
                   case Orange:
                       PCLActions.Bottom.ApplyConstricted(TargetHelper.Normal(m), magicNumber);
                       break;
                   case Light:
                       int toTransfer = PCLGameUtilities.GetPowerAmount(DelayedDamagePower.POWER_ID);
                       PCLActions.Bottom.ReducePower(player, player, DelayedDamagePower.POWER_ID, toTransfer);
                       if (m != null && toTransfer > 0) {
                           PCLActions.Bottom.DealDamageAtEndOfTurn(player, m, toTransfer, AttackEffects.CLAW);
                       }
                       break;
                   case Dark:
                       for (AbstractCreature cr : PCLGameUtilities.GetAllCharacters(true)) {
                           PCLActions.Bottom.DealDamageAtEndOfTurn(player, cr, secondaryValue, AttackEffects.GUNSHOT);
                       }
                       break;
               }
           }
        });
    }
}