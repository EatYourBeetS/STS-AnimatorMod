package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.animator.PinaCoLadaPower;
import eatyourbeets.utilities.GameActions;

public class PinaCoLada extends AnimatorCard
{
    public static final EYBCardData DATA = Register(PinaCoLada.class)
            .SetPower(1, CardRarity.RARE)
            .SetSeriesFromClassPackage();

    public PinaCoLada()
    {
        super(DATA);

        Initialize(0, 0);
        SetUpgrade(0, 0);
        SetCostUpgrade(-1);

        SetAffinity_Mind();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new PinaCoLadaPower(p, 1));
    }
}