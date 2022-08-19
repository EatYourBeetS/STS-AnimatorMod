package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Kuribayashi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kuribayashi.class)
            .SetAttack(2, CardRarity.COMMON, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage();

    public Kuribayashi()
    {
        super(DATA);

        Initialize(8, 0, 2, 16);
        SetUpgrade(2, 0, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1, 0, 1);

        SetAffinityRequirement(Affinity.Green, 1);
        SetAffinityRequirement(Affinity.Red, 3);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.GUNSHOT).SetSoundPitch(0.6f, 0.8f);
        GameActions.Bottom.ApplyVulnerable(p, m, magicNumber);

        if (TryUseAffinity(Affinity.Red))
        {
            GameActions.Bottom.DealDamageAtEndOfTurn(p, m, secondaryValue, AttackEffects.GUNSHOT);
        }

        if (TryUseAffinity(Affinity.Green))
        {
            GameActions.Bottom.StackPower(new KuribayashiPower(p, 1));
        }
    }

    public static class KuribayashiPower extends AnimatorPower
    {
        public KuribayashiPower(AbstractCreature owner, int amount)
        {
            super(owner, Kuribayashi.DATA);

            Initialize(amount);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            GameActions.Bottom.SelectFromHand(name, amount, false)
            .SetOptions(true, true, true)
            .SetFilter(c -> c.type == CardType.ATTACK && GameUtilities.CanRetain(c))
            .SetMessage(RetainCardsAction.TEXT[0])
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    GameUtilities.Retain(c);
                }
            });
            RemovePower();
            flash();
        }
    }
}