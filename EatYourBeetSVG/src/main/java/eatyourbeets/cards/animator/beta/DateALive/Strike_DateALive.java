package eatyourbeets.cards.animator.beta.DateALive;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

public class Strike_DateALive extends Strike
{
    public static final String ID = Register(Strike_DateALive.class).ID;

    public Strike_DateALive()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(7, 0);
        SetUpgrade(3, 0);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

        int totalCards = player.drawPile.size() + player.discardPile.size() + player.hand.size();
        if (totalCards >= 30 && EffectHistory.TryActivateLimited(cardID))
        {
            GameActions.Bottom.Draw(2);
        }
    }
}