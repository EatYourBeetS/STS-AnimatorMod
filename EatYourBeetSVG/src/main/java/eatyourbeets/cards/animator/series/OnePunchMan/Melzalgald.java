package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.MelzalgaldAlt_1;
import eatyourbeets.cards.animator.special.MelzalgaldAlt_2;
import eatyourbeets.cards.animator.special.MelzalgaldAlt_3;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Melzalgald extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Melzalgald.class).SetAttack(3, CardRarity.UNCOMMON);
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
        SetScaling(2, 2, 2);

        SetExhaust(true);
        SetSynergy(Synergies.OnePunchMan, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);

        GameActions.Bottom.MakeCardInHand(new MelzalgaldAlt_1()).SetOptions(upgraded, false);
        GameActions.Bottom.MakeCardInHand(new MelzalgaldAlt_2()).SetOptions(upgraded, false);
        GameActions.Bottom.MakeCardInHand(new MelzalgaldAlt_3()).SetOptions(upgraded, false);
    }
}