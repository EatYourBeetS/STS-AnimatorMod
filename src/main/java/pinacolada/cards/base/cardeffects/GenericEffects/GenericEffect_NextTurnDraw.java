package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class GenericEffect_NextTurnDraw extends GenericEffect
{
    public static final String ID = Register(GenericEffect_NextTurnDraw.class);

    public GenericEffect_NextTurnDraw(int amount)
    {
        super(ID, null, GR.Tooltips.NextTurnDraw, PCLCardTarget.None, amount);
    }

    @Override
    public String GetText()
    {
        return GR.PCL.Strings.Actions.NextTurnDraw(amount, true);
    }

    @Override
    public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
    {
        PCLActions.Bottom.StackPower(new DrawCardNextTurnPower(p, amount));
    }
}
