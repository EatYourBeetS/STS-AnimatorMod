package eatyourbeets.cards.animator.beta.series.RozenMaiden;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.cardManipulation.RandomCardUpgrade;
import eatyourbeets.cards.animator.beta.special.Enju_Barasuishou;
import eatyourbeets.cards.animator.status.Crystallize;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Enju extends AnimatorCard //TODO
{
    public static final EYBCardData DATA = Register(Enju.class).SetPower(-1, CardRarity.RARE).SetSeriesFromClassPackage()
            .PostInitialize(data -> {
                data.AddPreview(new Enju_Barasuishou(-1), false);
                data.AddPreview(new Crystallize(), false);
            });

    public Enju()
    {
        super(DATA);

        Initialize(0, 0, 0, 0);
        SetUpgrade(0, 0, 0, 1);
        SetAffinity_Water(1, 0, 0);
        SetAffinity_Dark(1, 0, 0);
    }

    
    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
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
