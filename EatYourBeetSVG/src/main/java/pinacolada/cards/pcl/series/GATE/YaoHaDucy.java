package pinacolada.cards.pcl.series.GATE;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.utilities.TupleT3;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class YaoHaDucy extends PCLCard
{
    public static final PCLCardData DATA = Register(YaoHaDucy.class)
            .SetAttack(0, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    private TupleT3<AbstractCard, Boolean, Integer> synergyCheckCache = new TupleT3<>(null, false, 0);

    public YaoHaDucy()
    {
        super(DATA);

        Initialize(2, 0, 1, 2);
        SetUpgrade(3, 0, 0, 0);

        SetAffinity_Green(1);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null && !PCLGameUtilities.HasArtifact(m))
        {
            PCLGameUtilities.GetPCLIntent(m).AddStrength(-magicNumber);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HORIZONTAL);
        PCLActions.Bottom.ReduceStrength(m, magicNumber, true);

        if (m.hasPower(PoisonPower.POWER_ID))
        {
            PCLActions.Bottom.TryChooseGainAffinity(name, secondaryValue, PCLAffinity.Green, PCLAffinity.Orange);
        }
    }
}