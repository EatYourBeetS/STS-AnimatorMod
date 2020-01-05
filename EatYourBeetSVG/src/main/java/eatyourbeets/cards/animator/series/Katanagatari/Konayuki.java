package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Konayuki extends AnimatorCard
{
    public static final String ID = Register(Konayuki.class, EYBCardBadge.Special);

    public Konayuki()
    {
        super(ID, 2, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0, 6, 2, 1);
        SetUpgrade(0, 0, 0, 1);

        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainForce(magicNumber)
        .AddCallback(force ->
        {
            if (force.amount >= 10 && EffectHistory.TryActivateLimited(cardID))
            {
                GameEffects.Queue.ShowCardBriefly(this.makeStatEquivalentCopy());
                GameActions.Bottom.DealDamageToRandomEnemy(40, damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                .SetPiercing(true, false);
            }
        });

        for (AbstractCard card : GameUtilities.GetAllInBattleInstances(this))
        {
            card.baseMagicNumber = card.magicNumber += secondaryValue;
        }
    }
}