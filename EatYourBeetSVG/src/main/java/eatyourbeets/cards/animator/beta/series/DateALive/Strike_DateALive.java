package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Strike_DateALive extends Strike
{
    public static final String ID = Register(Strike_DateALive.class).ID;

    public Strike_DateALive()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(7, 0, 2, 30);
        SetUpgrade(3, 0);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

        int totalCards = p.drawPile.size() + p.discardPile.size() + p.hand.size();
        if (totalCards >= secondaryValue && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.Draw(magicNumber);
        }
    }
}