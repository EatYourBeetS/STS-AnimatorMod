package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;

public class Mitsuba extends AnimatorCard
{
    public static final String ID = Register(Mitsuba.class, EYBCardBadge.Exhaust);

    public Mitsuba()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(9, 6, 2);
        SetUpgrade(3, 2, 0);

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.Draw(magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (m.currentHealth > p.currentHealth)
        {
            GameActions.Bottom.GainBlock(this.block);
        }

        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
    }
}