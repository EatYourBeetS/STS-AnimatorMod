package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Rena extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Rena.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();

    public Rena()
    {
        super(DATA);

        Initialize(0, 2, 1, 1);
        SetUpgrade(0, 2);

        SetAffinity_Green(1, 1, 1);
        SetAffinity_Dark(1);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.GainAgility(1);
        GameActions.Bottom.GainBlur(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(p), 1);
    }
}