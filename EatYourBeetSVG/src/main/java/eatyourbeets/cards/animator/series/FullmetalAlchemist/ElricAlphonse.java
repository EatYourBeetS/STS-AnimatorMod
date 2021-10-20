package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ElricAlphonse_Alt;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ElricAlphonse extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ElricAlphonse.class)
            .SetSkill(0, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new ElricAlphonse_Alt(), true));

    public ElricAlphonse()
    {
        super(DATA);

        Initialize(0, 0, 6);
        SetUpgrade(0, 0, 3);

        SetAffinity_Earth();
    }

    @Override
    protected void OnUpgrade() {
        super.OnUpgrade();

        SetHaste(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.RaiseEarthLevel(magicNumber);

        if (GameUtilities.HasFullHand()) {
            GameActions.Bottom.MakeCardInDiscardPile(new ElricAlphonse_Alt()).SetUpgrade(upgraded, true);
            GameActions.Last.Exhaust(this);
        }
    }
}