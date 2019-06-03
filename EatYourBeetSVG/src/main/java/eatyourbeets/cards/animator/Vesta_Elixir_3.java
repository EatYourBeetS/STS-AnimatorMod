package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import eatyourbeets.GameActionsHelper;

public class Vesta_Elixir_3 extends Vesta_Elixir
{
    public static final String ID = CreateFullID(Vesta_Elixir_3.class.getSimpleName());

    private static final int DISCARD_AMOUNT = 2;

    public Vesta_Elixir_3()
    {
        super(ID);

        Initialize(0, 0, 4, 4);

        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DrawCard(p, this.magicNumber);
        GameActionsHelper.Discard(DISCARD_AMOUNT, false);
        GameActionsHelper.ApplyPower(p, p, new MetallicizePower(p, this.secondaryValue));
    }
}