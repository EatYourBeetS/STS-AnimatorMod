package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.CalmStance;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Gluttony extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Gluttony.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Gluttony()
    {
        super(DATA);

        Initialize(0, 0, 20, 12);
        SetUpgrade(0,0,3,3);

        SetAffinity_Earth();
        SetAffinity_Nature();

        SetHaste(true);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetUnplayable(!GameUtilities.HasFullHand());
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (GameUtilities.HasFullHand())
        {
            GameActions.Bottom.ExhaustFromHand(name, 4, true)
            .ShowEffect(true, true);

            GameActions.Bottom.RaiseEarthLevel(magicNumber);
            GameActions.Bottom.RaiseNatureLevel(magicNumber);
            GameActions.Bottom.ChangeStance(CalmStance.STANCE_ID);
        }
    }
}