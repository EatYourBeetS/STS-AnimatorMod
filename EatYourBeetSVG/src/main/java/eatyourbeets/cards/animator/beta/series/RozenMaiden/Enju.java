package eatyourbeets.cards.animator.beta.series.RozenMaiden;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.cardManipulation.RandomCardUpgrade;
import eatyourbeets.cards.animator.status.Crystallize;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Enju extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Enju.class)
    		.SetPower(-1, CardRarity.RARE);
    static
    {
        DATA.AddPreview(new Enju_Barasuishou(-1), false);
        DATA.AddPreview(new Crystallize(), false);
    }

    public Enju()
    {
        super(DATA);

        Initialize(0, 0, 0, 0);
        SetUpgrade(0, 0, 0, 1);
        
        SetSynergy(Synergies.RozenMaiden);
    }

    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int stacks = GameUtilities.UseXCostEnergy(this);

        GameActions.Bottom.MakeCardInDrawPile(new Enju_Barasuishou(stacks+secondaryValue));

        GameActions.Bottom.StackPower(new EnjuPower(p, 1));
    }


    public static class EnjuPower extends AnimatorPower
    {
        public EnjuPower(AbstractCreature owner, int amount)
        {
            super(owner, Enju.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount);
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            for (int i = 0; i < amount; i ++)
                GameActions.Bottom.Add(new RandomCardUpgrade());

            flash();
        }

        @Override
        public void atStartOfTurn()
        {
            flash();
            for (int i = 0; i < amount; i ++)
                GameActions.Bottom.MakeCardInHand(new Crystallize());
        }
    }
}
