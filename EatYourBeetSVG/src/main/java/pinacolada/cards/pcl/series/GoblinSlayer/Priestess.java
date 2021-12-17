package pinacolada.cards.pcl.series.GoblinSlayer;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.monsters.PCLEnemyIntent;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Priestess extends PCLCard
{
    public static final PCLCardData DATA = Register(Priestess.class)
            .SetSkill(1, CardRarity.COMMON, eatyourbeets.cards.base.EYBCardTarget.ALL, true)
            .SetSeriesFromClassPackage();

    public Priestess()
    {
        super(DATA);

        Initialize(0, 0, 1, 1);
        SetUpgrade(0, 0, 2, 1);

        SetAffinity_Blue(1);
        SetAffinity_Light(2, 0, 1);

        SetAffinityRequirement(PCLAffinity.Orange, 8);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        for (PCLEnemyIntent intent : PCLGameUtilities.GetPCLIntents())
        {
            intent.AddWeak();
        }
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainTemporaryHP(magicNumber);
        PCLActions.Bottom.ApplyWeak(TargetHelper.Enemies(), 1);

        if (info.IsSynergizing || CheckAffinity(PCLAffinity.Orange))
        {
            PCLActions.Bottom.ExhaustFromPile(name, 1, p.drawPile, p.hand, p.discardPile)
            .ShowEffect(true, true)
            .SetOptions(true, true)
            .SetFilter(PCLGameUtilities::IsHindrance).AddCallback(
                    cards -> {
                        if (cards.size() > 0) {
                            PCLActions.Bottom.ReducePower(player, FrailPower.POWER_ID,1);
                            PCLActions.Bottom.ReducePower(player, VulnerablePower.POWER_ID,1);
                        }
                    }
            );
        }
    }
}