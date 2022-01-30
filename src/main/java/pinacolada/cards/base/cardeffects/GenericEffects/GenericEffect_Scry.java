package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.cardeffects.GenericEffect;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class GenericEffect_Scry extends GenericEffect
{
    public static final String ID = Register(GenericEffect_Draw.class);

    public GenericEffect_Scry(int amount)
    {
        super(ID, null, PCLCardTarget.None, amount);
    }

    @Override
    public String GetText()
    {
        return GR.PCL.Strings.Actions.Scry(amount, true);
    }

    @Override
    public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
    {
        PCLActions.Bottom.Scry(amount);
    }
}
