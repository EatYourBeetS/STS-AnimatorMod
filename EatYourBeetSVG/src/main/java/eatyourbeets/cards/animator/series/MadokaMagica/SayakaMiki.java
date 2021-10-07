package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.animator.curse.Curse_GriefSeed;
import eatyourbeets.cards.animator.special.SayakaMiki_Oktavia;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.utilities.GameActions;

public class SayakaMiki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SayakaMiki.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(new SayakaMiki_Oktavia(), true);
                data.AddPreview(new Curse_GriefSeed(), false);
            });

    public SayakaMiki()
    {
        super(DATA);

        Initialize(0, 0, 0, 0);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Green(1);
        SetAffinity_Blue(1);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.MakeCardInDiscardPile(new SayakaMiki_Oktavia()).SetUpgrade(upgraded, false);
        GameActions.Bottom.MakeCardInDiscardPile(new Curse_GriefSeed());
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        magicNumber = GetHandAffinity(Affinity.Blue);
        isMagicNumberModified = magicNumber > 0;
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.ChannelOrb(new Frost());

        GameActions.Bottom.StackAffinityPower(Affinity.Blue, 1, upgraded);
    }
}
