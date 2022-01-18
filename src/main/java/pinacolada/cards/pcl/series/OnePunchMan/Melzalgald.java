package pinacolada.cards.pcl.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.special.Melzalgald_1;
import pinacolada.cards.pcl.special.Melzalgald_2;
import pinacolada.cards.pcl.special.Melzalgald_3;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLCombatStats;
import pinacolada.ui.combat.PCLAffinityMeter;
import pinacolada.utilities.PCLActions;

public class Melzalgald extends PCLCard
{
    public static final PCLCardData DATA = Register(Melzalgald.class)
            .SetAttack(3, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(new Melzalgald_1(), true);
                data.AddPreview(new Melzalgald_2(), true);
                data.AddPreview(new Melzalgald_3(), true);
            });

    public Melzalgald()
    {
        super(DATA);

        Initialize(18, 2, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Star(1, 0, 0);
        SetAffinity_Red(0, 0, 1);
        SetAffinity_Green(0, 0, 1);
        SetAffinity_Blue(0, 0, 1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HEAVY);
        PCLActions.Bottom.GainBlock(block);
        if (PCLCombatStats.MatchingSystem.AffinityMeter.GetCurrentAffinity() != PCLAffinity.Star) {
            PCLActions.Bottom.RerollAffinity(PCLAffinityMeter.Target.CurrentAffinity, PCLAffinity.Red, PCLAffinity.Green, PCLAffinity.Blue)
                    .SetOptions(false, true);
        }

        PCLActions.Bottom.MakeCardInHand(new Melzalgald_1()).SetUpgrade(upgraded, false);
        PCLActions.Bottom.MakeCardInHand(new Melzalgald_2()).SetUpgrade(upgraded, false);
        PCLActions.Bottom.MakeCardInHand(new Melzalgald_3()).SetUpgrade(upgraded, false);
    }
}