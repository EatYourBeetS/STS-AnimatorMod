package eatyourbeets.cards.animator.series.TenseiSlime;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.SelectCreature;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.VFX;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Souei extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Souei.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage()
            .ObtainableAsReward((data, deck) -> data.GetTotalCopies(deck) <= 0 || deck.size() >= 24);
    private static final int POISON_AMOUNT = 3;
    private static final int ACTIVATIONS_STEP = 3;
    private static final int TURNS = 2;

    public Souei()
    {
        super(DATA);

        Initialize(0, 0, 3, 2);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Green(1, 1, 0);
        SetAffinity_Dark(1);

        SetEthereal(true);

        SetAffinityRequirement(Affinity.Green, 3);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        for (EnemyIntent intent : GameUtilities.GetIntents())
        {
            intent.AddPlayerIntangible();
        }
    }

    @Override
    public ColoredString GetSpecialVariableString()
    {
        return super.GetSpecialVariableString(TURNS);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainCorruption(1, true);
        GameActions.Bottom.GainAgility(1, true);
        GameActions.Bottom.DiscardFromHand(name, secondaryValue, false)
        .SetOptions(true, true, true)
        .AddCallback(cards ->
        {
            final int amount = cards.size();
            if (amount > 0)
            {
                GameActions.Bottom.StackPower(new SoueiPower(player, TURNS, amount));
            }
        });

        if (info.CanActivateLimited && TryUseAffinity(Affinity.Green) && info.TryActivateLimited())
        {
            GameActions.Bottom.GainIntangible(1);
        }
    }

    protected static class SoueiPower extends AnimatorPower
    {
        private int poisonAmount;

        public SoueiPower(AbstractCreature owner, int turns, int poison)
        {
            super(owner, Souei.DATA);

            ID += poison;
            poisonAmount = poison;

            Initialize(turns, PowerType.BUFF, true);
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, poisonAmount);
        }

        @Override
        public void onUseCard(AbstractCard card, UseCardAction action)
        {
            super.onUseCard(card, action);

            if (card.type == CardType.SKILL)
            {
                GameActions.Delayed.SelectCreature(SelectCreature.Targeting.Random, name)
                .AutoSelectSingleTarget(true)
                .SkipConfirmation(true)
                .AddCallback(m ->
                {
                    if (GameUtilities.IsValidTarget(m))
                    {
                        GameActions.Top.ApplyPoison(owner, m, poisonAmount);
                        GameActions.Top.VFX(VFX.ThrowDagger(m.hb, 0.1f).PlaySFX(false)
                        .SetColor(new Color(0.2f, 1.0f, 0.2f, 1f)), 0.33f, true);
                    }
                });
                flash();
            }
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            ReducePower(1);
        }

        @Override
        protected ColoredString GetSecondaryAmount(Color c)
        {
            return new ColoredString(poisonAmount, Colors.Green(c.a));
        }
    }
}