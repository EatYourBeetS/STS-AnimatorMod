package eatyourbeets.cards.animator.curse;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.MadokaMagica.IrohaTamaki;
import eatyourbeets.cards.animator.series.MadokaMagica.SayakaMiki;
import eatyourbeets.cards.base.AnimatorCard_Curse;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class IrohaTamaki_Giovanna extends AnimatorCard_Curse
{
    public static final EYBCardData DATA = Register(IrohaTamaki_Giovanna.class)
            .SetCurse(0, EYBCardTarget.None, true)
            .SetSeries(SayakaMiki.DATA.Series)
            .PostInitialize(data -> data.AddPreview(new IrohaTamaki(), false));

    public IrohaTamaki_Giovanna()
    {
        super(DATA, false);

        Initialize(0, 0, 10, 1);
        SetUpgrade(0, 0, 1, 0);
        SetAutoplay(true);
        SetPurge(true);

        SetAffinity_Blue(1);
        SetAffinity_Dark(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ReduceStrength(player,magicNumber,true);
        GameActions.Bottom.StackPower(TargetHelper.RandomEnemy(), PowerHelper.Shackles, magicNumber);
        GameActions.Bottom.FetchFromPile(name,1,player.exhaustPile).SetFilter(c -> IrohaTamaki.DATA.ID.equals(c.cardID));
    }
}
