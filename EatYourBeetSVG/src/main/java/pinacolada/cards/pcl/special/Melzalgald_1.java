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

public class Melzalgald_1 extends MelzalgaldAlt
{
    public static final PCLCardData DATA = Register(Melzalgald_1.class)
            .SetAttack(1, CardRarity.SPECIAL)
            .SetSeries(SERIES);

    public Melzalgald_1()
    {
        super(DATA);

        SetAffinity_Red(0, 0, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (PCLCombatStats.MatchingSystem.AffinityMeter.GetCurrentAffinity() != PCLAffinity.Star) {
            PCLActions.Bottom.RerollAffinity(PCLAffinityMeter.Target.CurrentAffinity, PCLAffinity.Red)
                    .SetOptions(true, true);
        }

        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HEAVY);
        PCLActions.Bottom.GainMight(magicNumber);
    }
}