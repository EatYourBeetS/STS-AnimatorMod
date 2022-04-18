package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ChlammyZell_Scheme;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class ChlammyZell extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ChlammyZell.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new ChlammyZell_Scheme(), false));

    public ChlammyZell()
    {
        super(DATA);

        Initialize(0, 3, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Blue(1);
        SetAffinity_Dark(1);

        SetAffinityRequirement(Affinity.Blue, 3);
        SetAffinityRequirement(Affinity.Dark, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.Draw(1);
        GameActions.Bottom.DrawNextTurn(magicNumber);

        if (CheckAffinity(Affinity.Blue) && CheckAffinity(Affinity.Dark) && info.TryActivateLimited())
        {
            GameActions.Bottom.MakeCardInHand(new ChlammyZell_Scheme());
        }
    }
}