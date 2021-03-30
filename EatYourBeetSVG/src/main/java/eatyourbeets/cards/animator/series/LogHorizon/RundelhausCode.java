package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class RundelhausCode extends AnimatorCard
{
    public static final EYBCardData DATA = Register(RundelhausCode.class).SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL);

    public RundelhausCode()
    {
        super(DATA);

        Initialize(5, 0, 2, 1);
        SetUpgrade(3, 0, 0);

        SetSynergy(Synergies.LogHorizon);
        SetSpellcaster();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.LIGHTNING);

        if (IsStarter())
        {
            for (AbstractCard c : GameUtilities.GetOtherCardsInHand(this))
            {
                EYBCard card = JUtils.SafeCast(c, EYBCard.class);
                if (card != null && EYBAttackType.Elemental.equals(card.attackType))
                {
                    GameUtilities.IncreaseDamage(card, magicNumber, false);
                    GameUtilities.Flash(card, false);
                }
            }
        }

        if (isSynergizing)
        {
            GameActions.Bottom.ChannelOrbs(Lightning::new, secondaryValue);
        }
    }
}