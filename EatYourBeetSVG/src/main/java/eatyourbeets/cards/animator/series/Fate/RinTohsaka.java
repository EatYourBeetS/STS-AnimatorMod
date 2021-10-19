package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.OrbCore;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class RinTohsaka extends AnimatorCard
{
    public static final EYBCardData DATA = Register(RinTohsaka.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .AddPreviews(OrbCore.GetAllCores(), true);

    public RinTohsaka()
    {
        super(DATA);

        Initialize(0, 6, 1);
        SetUpgrade(0, 1, 2);

        SetAffinity_Mind();
        SetAffinity_Dark();
        SetAffinity_Water();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainTemporaryArtifact(magicNumber);
    }
}