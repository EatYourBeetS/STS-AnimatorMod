package pinacolada.cards.pcl.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
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

        Initialize(0, 5, 2, 20);
        SetUpgrade(0, 3, 0, 5);

        SetAffinity_Light(1, 0, 1);
        SetAffinity_Dark(1, 0, 1);
        SetAffinity_Orange(1, 0, 0);

        SetExhaust(true);

        SetAffinityRequirement(PCLAffinity.Green, 2);
        SetAffinityRequirement(PCLAffinity.Light, 8);
        SetAffinityRequirement(PCLAffinity.Dark, 8);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);

        PCLActions.Bottom.TryChooseSpendAffinity(this, PCLAffinity.Green, PCLAffinity.Light, PCLAffinity.Dark).AddConditionalCallback(afChoices -> {
           for (AffinityChoice af : afChoices) {
               switch (af.Affinity) {
                   case Green:
                       PCLActions.Bottom.CreateThrowingKnives(magicNumber);
                       break;
                   case Light:
                       if (player.hasPower(DelayedDamagePower.POWER_ID)) {
                           PCLActions.Bottom.RemovePower(player, player, DelayedDamagePower.POWER_ID);
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