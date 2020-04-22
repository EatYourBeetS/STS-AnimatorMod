package eatyourbeets.cards.animator.beta.LogHorizon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Akatsuki extends AnimatorCard {
    public static final EYBCardData DATA = Register(Akatsuki.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Piercing);

    public Akatsuki() {
        super(DATA);

        Initialize(10, 0, 2);
        SetUpgrade(2, 0, 1);
        SetScaling(0,2,0);

        SetEthereal(true);
        SetMartialArtist();

        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    protected void OnUpgrade() {
        SetEthereal(false);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float damage)
    {
        if (GameUtilities.IsInStance(AgilityStance.STANCE_ID))
        {
            damage += CombatStats.SynergiesThisTurn() * secondaryValue;
        }

        return super.ModifyDamage(enemy, damage);
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