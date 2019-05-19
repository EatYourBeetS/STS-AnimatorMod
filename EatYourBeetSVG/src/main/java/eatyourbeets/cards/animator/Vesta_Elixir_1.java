package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.GameActionsHelper;

public class Vesta_Elixir_1 extends Vesta_Elixir
{
    public static final String ID = CreateFullID(Vesta_Elixir_1.class.getSimpleName());

    private static final int TEMPORARY_HP = 8;

    public Vesta_Elixir_1()
    {
        super(ID);

        Initialize(0, 0, 2, 1);

        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber);
        GameActionsHelper.ApplyPower(p, p, new DexterityPower(p, this.secondaryValue), this.secondaryValue);
        GameActionsHelper.GainTemporaryHP(p, p, TEMPORARY_HP);
    }
}