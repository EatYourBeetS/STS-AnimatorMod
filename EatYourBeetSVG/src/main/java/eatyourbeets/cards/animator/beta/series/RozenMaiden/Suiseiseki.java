package eatyourbeets.cards.animator.beta.series.RozenMaiden;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.status.Status_Slimed;
import eatyourbeets.cards.base.*;
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

        Initialize(0, 7, 4, 3);
        SetUpgrade(0, 3, 0);

        SetAffinity_Green(1, 0, 0);
        SetAffinity_Light(1, 0, 1);

        SetAffinityRequirement(Affinity.Orange, 7);
    }

    @Override
    public boolean HasDirectSynergy(AbstractCard other)
    {
        return Souseiseki.DATA.ID.equals(other.cardID) || super.HasDirectSynergy(other);
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

        Status_Slimed slimed = new Status_Slimed();
        if (CheckAffinity(Affinity.Orange)) {
            slimed.retain = true;
        }
        GameActions.Bottom.MakeCard(slimed, player.drawPile);

        if (info.GetPreviousCardID().equals(Souseiseki.DATA.ID) && info.TryActivateLimited())
        {
            GameActions.Top.PlayCopy(this, m);
        }
    }
}
