package pinacolada.cards.pcl.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardEffectChoice;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.stances.DesecrationStance;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Caster extends PCLCard
{
    public static final PCLCardData DATA = Register(Caster.class)
            .SetSkill(1, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public Caster()
    {
        super(DATA);

        Initialize(0, 0, 2, 3);
        SetUpgrade(0, 0, 0, -1);

        SetAffinity_Blue(1);
        SetAffinity_Dark(1);

        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null)
        {
            PCLGameUtilities.GetPCLIntent(m).AddStrength(-magicNumber);
        }
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetEvokeOrbCount(HasSynergy() ? 1 : 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ReduceStrength(m, magicNumber, false).SetStrengthGain(true);
        PCLActions.Bottom.StackPower(TargetHelper.Player(), PCLPowerHelper.Resistance, -secondaryValue);

        if (DesecrationStance.IsActive() || info.IsSynergizing)
        {
            PCLActions.Bottom.ChannelOrb(new Dark()).AddCallback(o -> {
                if (o.size() > 0) {
                    PCLActions.Bottom.TriggerOrbPassive(o.get(0),1);
                }
            });
        }
    }
}