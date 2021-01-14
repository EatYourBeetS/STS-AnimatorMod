package eatyourbeets.cards.animator.beta.LogHorizon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Akatsuki extends AnimatorCard {
    public static final EYBCardData DATA = Register(Akatsuki.class).SetAttack(2, CardRarity.RARE, EYBAttackType.Piercing);

    public Akatsuki() {
        super(DATA);

        Initialize(10, 0, 2);
        SetUpgrade(2, 0, 1);
        SetScaling(0,1,0);

        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    protected void OnUpgrade() {
        SetEthereal(false);
    }

    @Override
    public boolean HasSynergy(AbstractCard other)
    {
        return (other.rarity.equals(CardRarity.UNCOMMON)) || super.HasSynergy(other);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            for (int i=0; i<magicNumber; i++)
            {
                GameActions.Top.MakeCardInHand(this)
                .SetUpgrade(false, true)
                .AddCallback(card ->
                {
                    if (card.cost > 0 || card.costForTurn > 0) {
                        card.cost = 0;
                        card.costForTurn = 0;
                        card.isCostModified = true;
                    }

                    card.freeToPlayOnce = true;
                    card.baseDamage = 0;
                    card.exhaust = true;
                    card.exhaustOnUseOnce = true;
                    card.applyPowers();
                });
            }
        }
    }
}