package eatyourbeets.cards.animator.beta.series.LogHorizon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class RundelhausCode extends AnimatorCard
{
    public static final EYBCardData DATA = Register(RundelhausCode.class).SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL);

    public RundelhausCode()
    {
        super(DATA);

        Initialize(5, 0, 2, 1);
        SetUpgrade(3, 0, 0);
        SetSpellcaster();

        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.LIGHTNING);

        if (IsStarter())
        {
            for (AbstractCard c : GameUtilities.GetOtherCardsInHand(this))
            {
                if (c.type.equals(CardType.ATTACK) && c.baseDamage >= 0)
                {
                    if (c instanceof EYBCard && EYBAttackType.Elemental.equals(((EYBCard)c).attackType))
                    {
                        GameUtilities.IncreaseDamage(c, magicNumber, false);
                        c.flash();
                    }
                }
            }
        }

        if (HasSynergy())
        {
            for (int i=0; i<secondaryValue; i++)
            {
                GameActions.Bottom.ChannelOrb(new Lightning(), true);
            }
        }
    }
}