package eatyourbeets.cards.animator.beta.series.RozenMaiden;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.status.Status_Slimed;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.utilities.GameActions;

public class Suiseiseki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Suiseiseki.class)
    		.SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Status_Slimed(), false).AddPreview(new Souseiseki(), false));

    public Suiseiseki()
    {
        super(DATA);

        Initialize(0, 6, 4, 3);
        SetUpgrade(0, 2, 1);

        SetAffinity_Green(1, 0, 1);
        SetAffinity_Orange(1, 0, 0);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainTemporaryHP(magicNumber);

        GameActions.Bottom.MakeCard(new Status_Slimed(), player.drawPile);

        if (info.IsSynergizing && info.GetPreviousCardID().equals(Souseiseki.DATA.ID))
        {
            GameActions.Top.PlayCopy(this, m);
        }
    }
}
