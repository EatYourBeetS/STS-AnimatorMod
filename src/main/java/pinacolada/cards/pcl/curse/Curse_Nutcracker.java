package pinacolada.cards.pcl.curse;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Curse_Nutcracker extends PCLCard_Curse
{
    public static final PCLCardData DATA = Register(Curse_Nutcracker.class)
            .SetCurse(-2, PCLCardTarget.None, false)
            .SetSeries(CardSeries.YoujoSenki);

    public Curse_Nutcracker()
    {
        super(DATA, true);

        Initialize(0, 0, 3);

        SetAffinity_Dark(1);
        SetUnplayable(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        PCLActions.Bottom.GainInspiration(1);
    }

    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (dontTriggerOnUseCard)
        {
            for (AbstractMonster m1 : PCLGameUtilities.GetEnemies(true))
            {
                PCLActions.Bottom.Add(new HealAction(m1, null, magicNumber));
            }
        }
    }
}