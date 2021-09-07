package eatyourbeets.cards.animator.beta.series.AngelBeats;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.GirlDeMo;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Yui extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Yui.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None).SetSeriesFromClassPackage();
    static
    {
        DATA.AddPreview(new GirlDeMo(), true);
    }

    public Yui()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetAffinity_Light(2, 0, 0);
        SetExhaust(true);
        AfterLifeMod.Add(this);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.Motivate(secondaryValue);

        if (CombatStats.ControlPile.Contains(this))
        {
            GameActions.Bottom.MakeCardInDrawPile(new GirlDeMo());
        }
    }
}