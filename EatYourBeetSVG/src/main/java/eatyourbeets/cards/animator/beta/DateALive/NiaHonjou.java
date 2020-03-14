package eatyourbeets.cards.animator.beta.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.utilities.GameActions;

public class NiaHonjou extends AnimatorCard implements Hidden
{
    public static final EYBCardData DATA = Register(NiaHonjou.class).SetSkill(2, CardRarity.COMMON, EYBCardTarget.None);

    public NiaHonjou()
    {
        super(DATA);

        Initialize(0, 0, 0);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (upgraded)
        {
            GameActions.Bottom.Motivate(2);
        }
    }
}