package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class GenericEffect_NextTurnBlock extends GenericEffect
{
    public static final String ID = Register(GenericEffect_NextTurnBlock.class);

    public GenericEffect_NextTurnBlock(int amount)
    {
        super(ID, null, GR.Tooltips.Block, PCLCardTarget.Self, amount);
    }

    @Override
    public String GetText()
    {
        return GR.PCL.Strings.Actions.NextTurnBlock(amount, true);
    }

    @Override
    public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
    {
        PCLActions.Bottom.StackPower(new NextTurnBlockPower(p, amount));
    }
}
