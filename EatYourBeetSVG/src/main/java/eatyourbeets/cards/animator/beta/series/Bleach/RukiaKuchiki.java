package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.animator.beta.special.RukiaBankai;
import eatyourbeets.cards.base.*;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;

public class RukiaKuchiki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(RukiaKuchiki.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new RukiaBankai(), false));

    public RukiaKuchiki()
    {
        super(DATA);

        Initialize(0, 2, 2, 3);
        SetUpgrade(0, 1, 0, 0);
        SetAffinity_Green(1, 1, 1);
        SetAffinity_Blue(1, 0, 0);

        SetAffinityRequirement(Affinity.Green, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        if (IsStarter())
        {
            GameActions.Bottom.ChannelOrbs(Frost::new, magicNumber);
        }

        if (TrySpendAffinity(Affinity.Green) || IntellectStance.IsActive())
        {
            if (player.filledOrbCount() >= secondaryValue)
            {
                GameActions.Bottom.MakeCardInDrawPile(new RukiaBankai());
                GameActions.Bottom.Exhaust(this);
            }
        }
    }
}