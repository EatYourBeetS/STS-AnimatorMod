package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.stances.*;
import eatyourbeets.utilities.GameActions;

public class Shimako extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shimako.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None).SetSeriesFromClassPackage();

    public Shimako()
    {
        super(DATA);

        Initialize(0, 4, 2);
        SetUpgrade(0, 3, 0);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Blue(1, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        AbstractOrb orb;
        String curStance = player.stance.ID;
        if (curStance.equals(MightStance.STANCE_ID) || curStance.equals(SuperchargeStance.STANCE_ID))
        {
            orb = new Fire();
        }
        else if (curStance.equals(EnduranceStance.STANCE_ID))
        {
            orb = new Earth();
        }
        else if (curStance.equals(WisdomStance.STANCE_ID) || curStance.equals(DesecrationStance.STANCE_ID))
        {
            orb = new Dark();
        }
        else
        {
            orb = new Lightning();
        }

        GameActions.Bottom.ChannelOrb(orb);

        if (curStance.equals(WisdomStance.STANCE_ID))
        {
            GameActions.Bottom.ChannelOrb(new Dark());
        }
    }
}