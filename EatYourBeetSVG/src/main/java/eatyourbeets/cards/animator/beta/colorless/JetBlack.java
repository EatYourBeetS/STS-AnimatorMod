package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import static com.megacrit.cardcrawl.cards.AbstractCard.CardTags.STARTER_DEFEND;

public class JetBlack extends AnimatorCard
{
    public static final EYBCardData DATA = Register(JetBlack.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Normal, EYBCardTarget.ALL).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.CowboyBebop);

    public JetBlack()
    {
        super(DATA);

        Initialize(7, 1, 1);
        SetUpgrade(3, 1, 0);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Orange(2, 0, 1);

        SetAffinityRequirement(Affinity.General, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int amount = magicNumber;
        if (CheckAffinity(Affinity.General) && CombatStats.TryActivateLimited(cardID)) {
            amount += 1;
        }
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.BLUNT_HEAVY);
        GameActions.Bottom.StackPower(new JetBlackPower(p, amount));
    }

    public static class JetBlackPower extends AnimatorPower
    {
        public JetBlackPower(AbstractPlayer owner, int amount)
        {
            super(owner, JetBlack.DATA);

            Initialize(amount);
        }

        public void atStartOfTurn()
        {
            super.atStartOfTurn();
            ReducePower(1);
        }

        @Override
        public void onUseCard(AbstractCard card, UseCardAction action)
        {
            super.onUseCard(card, action);
            if (card.hasTag(STARTER_DEFEND) && GameUtilities.CanPlayTwice(card)) {
                GameActions.Top.PlayCopy(card, (AbstractMonster)((action.target == null) ? null : action.target));
            }
        }
    }
}