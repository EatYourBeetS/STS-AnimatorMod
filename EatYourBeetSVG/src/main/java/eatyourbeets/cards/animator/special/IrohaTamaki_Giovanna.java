package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.MadokaMagica.IrohaTamaki;
import eatyourbeets.cards.animator.series.MadokaMagica.SayakaMiki;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class IrohaTamaki_Giovanna extends AnimatorCard
{
    public static final EYBCardData DATA = Register(IrohaTamaki_Giovanna.class)
            .SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetSeries(SayakaMiki.DATA.Series)
            .PostInitialize(data -> data.AddPreview(new IrohaTamaki(), false));

    public IrohaTamaki_Giovanna()
    {
        super(DATA);

        Initialize(0, 2, 10, 1);
        SetUpgrade(0, 0, 1, 0);
        SetPurge(true);

        SetAffinity_Blue(1);
        SetAffinity_Dark(1);
    }

    public void OnUpgrade() {
        SetRetainOnce(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (IsStarter()) {
            GameActions.Bottom.StackPower(TargetHelper.AllCharacters(), PowerHelper.Shackles, magicNumber);
        }
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.FetchFromPile(name,1,player.exhaustPile).SetFilter(c -> IrohaTamaki.DATA.ID.equals(c.cardID));
    }
}
