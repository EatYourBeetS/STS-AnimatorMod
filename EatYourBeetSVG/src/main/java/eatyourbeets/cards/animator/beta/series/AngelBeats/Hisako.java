package eatyourbeets.cards.animator.beta.series.AngelBeats;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.GirlDeMo;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Hisako extends AnimatorCard implements Hidden
{
    public static final EYBCardData DATA = Register(Hisako.class).SetAttack(2, CardRarity.UNCOMMON).SetSeriesFromClassPackage();

    public Hisako()
    {
        super(DATA);

        Initialize(8, 0, 3, 2);
        SetAffinity_Orange(2, 0, 2);
        SetAffinity_Light(1, 0, 0);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.RaiseEarthLevel(1, upgraded);
        GameActions.Bottom.Motivate(secondaryValue);

        if (CombatStats.ControlPile.Contains(this))
        {
            GameActions.Bottom.MakeCardInDrawPile(new GirlDeMo());
        }
    }
}