package pinacolada.cards.pcl.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class MarisaKirisame extends PCLCard
{
    public static final PCLCardData DATA = Register(MarisaKirisame.class).SetAttack(1, CardRarity.COMMON, PCLAttackType.Elemental, eatyourbeets.cards.base.EYBCardTarget.ALL).SetSeriesFromClassPackage();

    public MarisaKirisame()
    {
        super(DATA);

        Initialize(4, 0, 2, 4);
        SetUpgrade(2, 0, 1, 0);
        SetAffinity_Blue(2, 0, 1);
        SetAffinity_Light(1);

        SetAffinityRequirement(PCLAffinity.Blue, 4);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        PCLActions.Bottom.AddAffinity(PCLAffinity.Blue, magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.LIGHTNING);

        if (info.IsSynergizing)
        {
            PCLActions.Bottom.ChannelOrb(new Lightning());
        }
        if (TrySpendAffinity(PCLAffinity.Blue)) {
            PCLActions.Bottom.ChannelOrb(new Lightning());
        }
    }
}

