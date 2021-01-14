package eatyourbeets.cards.animator.beta.LogHorizon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.subscribers.OnEndOfTurnSubscriber;
import eatyourbeets.utilities.GameActions;

public class Soujiro_Kawara extends AnimatorCard implements OnEndOfTurnSubscriber
{
    public static final EYBCardData DATA = Register(Soujiro_Kawara.class).SetAttack(0, CardRarity.SPECIAL, EYBAttackType.Normal);
    static
    {
        DATA.AddPreview(new Soujiro(), false);
    }

    public Soujiro_Kawara()
    {
        super(DATA);

        Initialize(5, 0, 0);
        SetUpgrade(2, 0, 0);

        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
    }

    @Override
    public void OnEndOfTurn(boolean isPlayer)
    {
        if (!JoinSoujiro(player.drawPile))
        {
            if (!JoinSoujiro(player.discardPile))
            {
                if (!JoinSoujiro(player.exhaustPile))
                {
                    JoinSoujiro(player.hand);
                }
            }
        }
    }

    private boolean JoinSoujiro(CardGroup group)
    {
        for (AbstractCard c : group.group)
        {
            if (Soujiro.DATA.ID.equals(c.cardID))
            {
                this.flash();
                GameActions.Top.MoveCard(c, group)
                        .ShowEffect(true, true);

                return true;
            }
        }

        return false;
    }
}