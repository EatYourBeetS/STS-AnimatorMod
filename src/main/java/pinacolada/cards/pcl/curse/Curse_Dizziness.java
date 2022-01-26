package pinacolada.cards.pcl.curse;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.status.Status_Dazed;
import pinacolada.utilities.PCLActions;

public class Curse_Dizziness extends PCLCard_Curse
{
    public static final PCLCardData DATA = Register(Curse_Dizziness.class)
            .SetCurse(-2, PCLCardTarget.None, true)
            .SetRarity(CardRarity.SPECIAL)
            .SetSeries(CardSeries.TouhouProject)
            .PostInitialize(data -> data.AddPreview(new Status_Dazed(), false));

    public Curse_Dizziness()
    {
        super(DATA, false);

        SetAffinity_Dark(1);
        SetUnplayable(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        PCLActions.Bottom.MakeCardInDrawPile(new Status_Dazed());
        PCLActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }
}