package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.OwariNoSeraph.Yuuichirou;
import eatyourbeets.cards.animator.status.Status_Void;
import eatyourbeets.cards.base.*;
import eatyourbeets.stances.CorruptionStance;
import eatyourbeets.utilities.GameActions;

public class Yuuichirou_Asuramaru extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Yuuichirou_Asuramaru.class)
            .SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetSeries(Yuuichirou.DATA.Series)
            .PostInitialize(data -> data.AddPreview(new Status_Void(true), true));

    public Yuuichirou_Asuramaru()
    {
        super(DATA);

        Initialize(0, 0, 4, 2);
        SetUpgrade(0, 0, 2, 0);

        SetAffinity_Green(2);
        SetAffinity_Dark(2);

        SetRetainOnce(true);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (Affinity a : Affinity.Basic())
        {
            GameActions.Bottom.GainAffinity(a, magicNumber, false);
        }

        GameActions.Bottom.ChangeStance(CorruptionStance.STANCE_ID);
        GameActions.Bottom.MakeCardInHand(new Status_Void(true));
        GameActions.Bottom.MakeCardInHand(new Status_Void(true));
    }
}