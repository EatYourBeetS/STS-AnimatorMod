package eatyourbeets.cards.animator.series.OnePunchMan;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Melzalgald_1;
import eatyourbeets.cards.animator.special.Melzalgald_2;
import eatyourbeets.cards.animator.special.Melzalgald_3;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Melzalgald extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Melzalgald.class)
            .SetAttack(3, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(new Melzalgald_1(), true);
                data.AddPreview(new Melzalgald_2(), true);
                data.AddPreview(new Melzalgald_3(), true);
            });

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

        GameActions.Bottom.MakeCardInHand(new Melzalgald_1()).SetUpgrade(upgraded, false).AddCallback(GameUtilities::Retain);
        GameActions.Bottom.MakeCardInHand(new Melzalgald_2()).SetUpgrade(upgraded, false).AddCallback(GameUtilities::Retain);
        GameActions.Bottom.MakeCardInHand(new Melzalgald_3()).SetUpgrade(upgraded, false).AddCallback(GameUtilities::Retain);
    }
}