package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.MelzalgaldAlt_1;
import eatyourbeets.cards.animator.special.MelzalgaldAlt_2;
import eatyourbeets.cards.animator.special.MelzalgaldAlt_3;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;

public class Melzalgald extends AnimatorCard
{
    public static final String ID = Register(Melzalgald.class);

    public Melzalgald()
    {
        super(ID, 3, CardRarity.UNCOMMON, CardType.ATTACK, CardTarget.SELF_AND_ENEMY);

        Initialize(21, 0);

        SetExhaust(true);
        SetSynergy(Synergies.OnePunchMan, true);

        if (InitializingPreview())
        {
            cardData.InitializePreview(new MelzalgaldAlt_1(), true);
        }
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