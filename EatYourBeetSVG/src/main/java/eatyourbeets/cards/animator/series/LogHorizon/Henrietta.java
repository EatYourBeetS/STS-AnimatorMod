package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardEffectChoice;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.misc.GenericEffects.GenericEffect_EnterStance;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Henrietta extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Henrietta.class).SetPower(2, CardRarity.UNCOMMON);

    public Henrietta()
    {
        super(DATA);

        Initialize(0, 0, 1, 1);
        SetEthereal(true);

        SetSynergy(Synergies.LogHorizon);
        SetAlignment(0, 0, 1, 1, 0);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (!GameUtilities.InStance(NeutralStance.STANCE_ID))
        {
            GameActions.Bottom.GainEnergy(1);
        }

        GameActions.Bottom.StackPower(new HenriettaPower(p, secondaryValue));
    }

    public static class HenriettaPower extends AnimatorPower
    {
        private static final CardEffectChoice choices = new CardEffectChoice();
        private static final Henrietta sourceCard = new Henrietta();

        public HenriettaPower(AbstractPlayer owner, int amount)
        {
            super(owner, Henrietta.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            for (int i = 0; i < amount; i++)
            {
                GameActions.Bottom.SpendEnergy(1, false)
                .AddCallback(() ->
                {
                    if (choices.TryInitialize(sourceCard))
                    {
                        choices.AddEffect(new GenericEffect_EnterStance(AgilityStance.STANCE_ID));
                        choices.AddEffect(new GenericEffect_EnterStance(IntellectStance.STANCE_ID));
                        choices.AddEffect(new GenericEffect_EnterStance(ForceStance.STANCE_ID));
                        choices.AddEffect(new GenericEffect_EnterStance(NeutralStance.STANCE_ID));
                    }

                    choices.Select(GameActions.Top, 1, null)
                    .CancellableFromPlayer(true);
                });
            }

            flash();
        }
    }
}