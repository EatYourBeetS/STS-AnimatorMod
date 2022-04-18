package eatyourbeets.cards_beta.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;

public class FujiwaraNoMokou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(FujiwaraNoMokou.class)
            .SetSkill(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public FujiwaraNoMokou()
    {
        super(DATA);

        Initialize(0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }
}