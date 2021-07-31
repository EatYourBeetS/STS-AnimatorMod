package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.TargetHelper;

public class SakuraMatou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SakuraMatou.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();

    public SakuraMatou()
    {
        super(DATA);

        Initialize(0, 0, 3, 2);

        SetAffinity_Dark(2);
        SetAffinity_Blue(1);

        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
        LoadImage("2");
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        int darkOrbs = JUtils.Count(player.orbs, Dark.class::isInstance);
        if (darkOrbs > 0)
        {
            GameUtilities.IncreaseMagicNumber(this, darkOrbs * secondaryValue, true);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.ApplyConstricted(TargetHelper.Enemies(p), magicNumber);
    }
}