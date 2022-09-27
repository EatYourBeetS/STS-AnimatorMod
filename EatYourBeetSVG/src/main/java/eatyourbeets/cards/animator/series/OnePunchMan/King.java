package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class King extends AnimatorCard
{
    public static final EYBCardData DATA = Register(King.class)
            .SetSkill(UNPLAYABLE_COST, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public King()
    {
        super(DATA);

        Initialize(0, 0, 2);

        SetAffinity_Red(1);
        SetUnplayable(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.Draw(magicNumber);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.ApplyVulnerable(TargetHelper.RandomEnemy(player, magicNumber), 1).IgnoreArtifact(true);
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }
}