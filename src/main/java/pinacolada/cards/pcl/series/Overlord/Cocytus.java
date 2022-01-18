package pinacolada.cards.pcl.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import pinacolada.actions.orbs.EvokeOrb;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Cocytus extends PCLCard
{
    public static final PCLCardData DATA = Register(Cocytus.class)
            .SetAttack(2, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Cocytus()
    {
        super(DATA);

        Initialize(13, 0, 2, 1);
        SetUpgrade(3, 0, 1, 0);

        SetAffinity_Red(1, 0, 2);
        SetAffinity_Orange(1,0,0);

        SetAffinityRequirement(PCLAffinity.Red, 2);
        SetAffinityRequirement(PCLAffinity.Blue, 2);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        final AbstractOrb orb = PCLGameUtilities.GetFirstOrb(Frost.ORB_ID);
        if (orb != null)
        {
            orb.showEvokeValue();
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY);
        PCLActions.Bottom.EvokeOrb(2, EvokeOrb.Mode.SameOrb)
        .SetFilter(o -> Frost.ORB_ID.equals(o.ID))
        .AddCallback(orbs ->
        {
            if (orbs.size() == 0)
            {
                PCLActions.Bottom.ChannelOrb(new Frost());
            }
        });

        if (TrySpendAffinity(PCLAffinity.Red) && TrySpendAffinity(PCLAffinity.Blue))
        {
            PCLActions.Bottom.GainPlatedArmor(secondaryValue);
        }
    }
}