package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
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
    public static final EYBCardData DATA = Register(Henrietta.class).SetPower(3, CardRarity.RARE);

    public Henrietta()
    {
        super(DATA);

        Initialize(0, 2, 1, 1);
        SetEthereal(true);

        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);

        if (!GameUtilities.InStance(NeutralStance.STANCE_ID))
        {
            GameActions.Bottom.Motivate(magicNumber);
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

            int energy = EnergyPanel.getCurrentEnergy();
            if (energy > 0)
            {
                EnergyPanel.useEnergy(energy);
                flash();

                if (choices.TryInitialize(sourceCard))
                {
                    choices.AddEffect(new GenericEffect_EnterStance(AgilityStance.STANCE_ID));
                    choices.AddEffect(new GenericEffect_EnterStance(IntellectStance.STANCE_ID));
                    choices.AddEffect(new GenericEffect_EnterStance(ForceStance.STANCE_ID));
                    choices.AddEffect(new GenericEffect_EnterStance(NeutralStance.STANCE_ID));
                }

                choices.Select(GameActions.Top, 1, null)
                .CancellableFromPlayer(true);
            }
        }
    }
}