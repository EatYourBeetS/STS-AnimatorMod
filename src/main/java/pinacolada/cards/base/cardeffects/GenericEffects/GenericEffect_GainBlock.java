package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class GenericEffect_GainBlock extends GenericEffect
{
    public GenericEffect_GainBlock(int amount)
    {
        this.amount = amount;
        this.tooltip = GR.Tooltips.Block;
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
