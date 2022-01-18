package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLCombatStats;
import pinacolada.ui.combat.PCLAffinityMeter;
import pinacolada.utilities.PCLActions;

public class Melzalgald_2 extends MelzalgaldAlt
{
    public static final PCLCardData DATA = Register(Melzalgald_2.class)
            .SetAttack(1, CardRarity.SPECIAL)
            .SetSeries(SERIES);

    public Melzalgald_2()
    {
        super(DATA);

        SetAffinity_Blue(0, 0, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (PCLCombatStats.MatchingSystem.AffinityMeter.GetCurrentAffinity() != PCLAffinity.Star) {
            PCLActions.Bottom.RerollAffinity(PCLAffinityMeter.Target.CurrentAffinity, PCLAffinity.Green)
                    .SetOptions(true, true);
        }

        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HEAVY);
        PCLActions.Bottom.GainWisdom(magicNumber);
    }
}