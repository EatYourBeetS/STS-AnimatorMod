package pinacolada.cards.pcl.series.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.base.cardeffects.GenericEffects.GenericEffect_NextTurnBlock;
import pinacolada.cards.base.cardeffects.GenericEffects.GenericEffect_NextTurnDraw;
import pinacolada.cards.base.cardeffects.GenericEffects.GenericEffect_Scry;
import pinacolada.utilities.PCLActions;

public class OrikoMikuni extends PCLCard
{
    public static final PCLCardData DATA = Register(OrikoMikuni.class)
            .SetSkill(0, CardRarity.COMMON, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public OrikoMikuni()
    {
        super(DATA);

        Initialize(0, 1, 3, 4);
        SetUpgrade(0, 0, 1, 2);

        SetAffinity_Blue(1);
        SetAffinity_Light(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.Blue, 6);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);

        choices.Initialize(this, true);
        choices.AddEffect(new GenericEffect_Scry(magicNumber));
        choices.AddEffect(new GenericEffect_NextTurnDraw(1));
        choices.AddEffect(new GenericEffect_NextTurnBlock(secondaryValue));

        if (TrySpendAffinity(PCLAffinity.Blue))
        {
            choices.Select(3, m);
        }
        else
        {
            choices.Select(1, m);
        }
    }
}