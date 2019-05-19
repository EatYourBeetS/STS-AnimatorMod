package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BlurPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import eatyourbeets.GameActionsHelper;

public class Vesta_Elixir_0 extends Vesta_Elixir
{
    public static final String ID = CreateFullID(Vesta_Elixir_0.class.getSimpleName());

    public Vesta_Elixir_0()
    {
        super(ID);

        Initialize(0, 0, 1, 2);

        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ApplyPower(p, p, new FocusPower(p, this.magicNumber), this.magicNumber);
        GameActionsHelper.AddToBottom(new IncreaseMaxOrbAction(2));
        GameActionsHelper.ApplyPower(p, p, new BlurPower(p, this.secondaryValue), this.secondaryValue);
    }
}