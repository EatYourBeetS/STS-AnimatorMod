package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;

public class Vesta_Elixir_2 extends Vesta_Elixir
{
    public static final String ID = CreateFullID(Vesta_Elixir_2.class.getSimpleName());

    private static final int ENERGY_GAIN = 3;

    public Vesta_Elixir_2()
    {
        super(ID);

        Initialize(0, 0, 2, 2);

        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.AddToBottom(new ExhaustAction(p, p, this.magicNumber, false));
        GameActionsHelper.GainEnergy(ENERGY_GAIN);
        GameActionsHelper.DrawCard(p, this.secondaryValue);
    }
}