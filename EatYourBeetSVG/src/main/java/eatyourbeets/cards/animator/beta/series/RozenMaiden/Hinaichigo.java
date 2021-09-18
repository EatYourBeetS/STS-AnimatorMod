package eatyourbeets.cards.animator.beta.series.RozenMaiden;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.stances.BlessingStance;
import eatyourbeets.utilities.GameActions;

public class Hinaichigo extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Hinaichigo.class)
    		.SetSkill(2, CardRarity.COMMON).SetSeriesFromClassPackage();

    public Hinaichigo()
    {
        super(DATA);

        Initialize(0, 0, 2, 4);
        SetUpgrade(0, 0, 1, 1);

        SetAffinity_Light(1, 1, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ApplyVulnerable(p, m, magicNumber);
        GameActions.Bottom.ApplyWeak(p, m, magicNumber);

        if (player.stance.ID.equals(BlessingStance.STANCE_ID))
        {
            GameActions.Bottom.ApplyPoison(p, m, secondaryValue);
        }
        else
        {
            GameActions.Bottom.ChangeStance(BlessingStance.STANCE_ID);
        }
    }
}
