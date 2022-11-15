package eatyourbeets.cards.animatorClassic.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.animator.PinaCoLadaPower;
import eatyourbeets.utilities.GameActions;

public class PinaCoLada extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(PinaCoLada.class).SetSeriesFromClassPackage().SetPower(2, CardRarity.RARE).SetMaxCopies(2);

    public PinaCoLada()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 6, 0);

        
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new PinaCoLadaPower(p, 1));
    }
}