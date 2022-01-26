package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class GenericEffect_GainBlock extends GenericEffect
{
    public static final String ID = Register(GenericEffect_GainBlock.class);

    public GenericEffect_GainBlock(int amount)
    {
        super(ID, null, GR.Tooltips.Block, PCLCardTarget.Self, amount);
    }

    @Override
    public String GetText()
    {
        return GR.PCL.Strings.Actions.GainAmount(amount, tooltip, true);
    }

    @Override
    public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
    {
        PCLActions.Bottom.GainBlock(amount);
    }
}
