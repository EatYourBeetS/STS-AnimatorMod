package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;

public class Millim extends AnimatorCard
{
    public static final String ID = Register(Millim.class, EYBCardBadge.Synergy);

    public Millim()
    {
        super(ID, 2, CardRarity.COMMON, AttackType.Elemental);

        Initialize(6, 0, 2);
        SetUpgrade(1, 0, 1);

        SetUnique(true, true);
        SetSynergy(Synergies.TenSura);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActions.Bottom.ApplyBurning(p, m, magicNumber);
        GameActions.Bottom.ApplyPoison(p, m, magicNumber);

        if (HasSynergy())
        {
            GameActions.Bottom.StackPower(new DrawCardNextTurnPower(p, 2));
        }
    }
}