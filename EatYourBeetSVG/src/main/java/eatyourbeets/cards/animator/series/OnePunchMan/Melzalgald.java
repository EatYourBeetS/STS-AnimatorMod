package eatyourbeets.cards.animator.series.OnePunchMan;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.MelzalgaldAlt_1;
import eatyourbeets.cards.animator.special.MelzalgaldAlt_2;
import eatyourbeets.cards.animator.special.MelzalgaldAlt_3;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Melzalgald extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Melzalgald.class)
            .SetAttack(3, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();
    static
    {
        DATA.AddPreview(new MelzalgaldAlt_1(), true);
        DATA.AddPreview(new MelzalgaldAlt_2(), true);
        DATA.AddPreview(new MelzalgaldAlt_3(), true);
    }

    public Melzalgald()
    {
        super(DATA);

        Initialize(21, 0);

        SetAffinity_Star(1, 1, 2);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);

        GameActions.Bottom.MakeCardInHand(new MelzalgaldAlt_1()).SetUpgrade(upgraded, false).AddCallback(GameUtilities::Retain);
        GameActions.Bottom.MakeCardInHand(new MelzalgaldAlt_2()).SetUpgrade(upgraded, false).AddCallback(GameUtilities::Retain);
        GameActions.Bottom.MakeCardInHand(new MelzalgaldAlt_3()).SetUpgrade(upgraded, false).AddCallback(GameUtilities::Retain);
    }
}