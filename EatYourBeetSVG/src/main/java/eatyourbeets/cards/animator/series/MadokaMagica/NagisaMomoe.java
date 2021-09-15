package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.curse.Curse_GriefSeed;
import eatyourbeets.cards.animator.special.NagisaMomoe_Charlotte;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class NagisaMomoe extends AnimatorCard
{
    public static final EYBCardData DATA = Register(NagisaMomoe.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(new NagisaMomoe_Charlotte(), true);
                data.AddPreview(new Curse_GriefSeed(), false);
            });

    public NagisaMomoe()
    {
        super(DATA);

        Initialize(0, 0, 3);

        SetAffinity_Blue(1);

        SetExhaust(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.MakeCardInDiscardPile(new NagisaMomoe_Charlotte()).SetUpgrade(upgraded, false);
        GameActions.Bottom.MakeCardInDiscardPile(new Curse_GriefSeed());
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.GainRandomAffinityPower(1, upgraded, Affinity.Green, Affinity.Blue, Affinity.Light);
        }
    }
}