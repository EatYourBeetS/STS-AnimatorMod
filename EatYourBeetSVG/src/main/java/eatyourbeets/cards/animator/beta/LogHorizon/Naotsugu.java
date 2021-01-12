package eatyourbeets.cards.animator.beta.LogHorizon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Naotsugu extends AnimatorCard {
    public static final EYBCardData DATA = Register(Naotsugu.class).SetAttack(3, CardRarity.UNCOMMON, EYBAttackType.Normal);
    public static int basePlatedArmor;

    public Naotsugu() {
        super(DATA);

        Initialize(9, 0);
        SetUpgrade(3, 0);
        SetScaling(0,0,1);

        basePlatedArmor = magicNumber;

        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        GameActions.Bottom.GainBlock(damage);

        if (GameUtilities.IsInStance(ForceStance.STANCE_ID))
        {
            GameActions.Bottom.FetchFromPile(name, 1, p.drawPile)
            .SetOptions(true, false)
            .AddCallback(cards ->
            {
                if (cards.size() > 0)
                {
                    AbstractCard card = cards.get(0);
                    card.setCostForTurn(card.costForTurn + 1);
                    GameUtilities.Retain(card);
                    GameActions.Bottom.Add(new RefreshHandLayout());
                }
            });
        }
    }
}