package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.Synergies;

public class Hero extends AnimatorCard_UltraRare
{
    public static final String ID = Register(Hero.class, EYBCardBadge.Special);

    public Hero()
    {
        super(ID, 1, CardType.ATTACK, CardTarget.ENEMY);

        Initialize(8, 0, 2);
        SetUpgrade(4, 0, 0);

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActions.Bottom.Draw(this.magicNumber);
    }
}