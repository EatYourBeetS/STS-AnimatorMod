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

        Initialize(0, 0, 1, 3);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Blue(1, 1, 0);
        SetAffinity_Orange(1);
        SetAffinity_Dark(1);

        SetAffinityRequirement(Affinity.Blue, 2);
        SetAffinityRequirement(Affinity.Dark, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Draw(2);
        GameActions.Bottom.DrawNextTurn(magicNumber);

        if (CheckAffinity(Affinity.Blue) && CheckAffinity(Affinity.Dark) && info.TryActivateLimited())
        {
            GameActions.Bottom.MakeCardInHand(new ChlammyZell_Scheme());
        }
    }
}