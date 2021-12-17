package pinacolada.cards.pcl.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.misc.GenericEffects.GenericEffect_Apply;
import pinacolada.misc.GenericEffects.GenericEffect_ChannelOrb;
import pinacolada.orbs.pcl.Air;
import pinacolada.powers.PowerHelper;
import pinacolada.utilities.PCLActions;

public class CatoElAltestan extends PCLCard
{
    public static final PCLCardData DATA = Register(CatoElAltestan.class)
            .SetSkill(1, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.Normal)
            .SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public CatoElAltestan()
    {
        super(DATA);

        Initialize(0, 1, 4, 3);
        SetUpgrade(0, 1, 0, 1);

        SetAffinity_Blue(2, 0, 2);

        SetExhaust(true);

        SetAffinityRequirement(PCLAffinity.Red, 2);
        SetAffinityRequirement(PCLAffinity.Blue, 2);
        SetAffinityRequirement(PCLAffinity.Green, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);

        PCLActions.Bottom.StackPower(TargetHelper.Player(), PowerHelper.Sorcery, magicNumber);

        choices.Initialize(this, true);
        if (TrySpendAffinity(PCLAffinity.Blue))
        {
            choices.AddEffect(new GenericEffect_Apply(TargetHelper.Normal(m), PowerHelper.Freezing, secondaryValue));
        }
        if (TrySpendAffinity(PCLAffinity.Red))
        {
            choices.AddEffect(new GenericEffect_Apply(TargetHelper.Normal(m), PowerHelper.Burning, secondaryValue));
        }
        if (TrySpendAffinity(PCLAffinity.Green))
        {
            choices.AddEffect(new GenericEffect_ChannelOrb(new Air()));
        }
        choices.Select(1, m);
    }
}