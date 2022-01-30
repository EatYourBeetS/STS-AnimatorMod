package pinacolada.cards.pcl.curse;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.PCLCard_Curse;
import pinacolada.utilities.PCLActions;

public class Curse_Regret extends PCLCard_Curse
{
    public static final PCLCardData DATA = Register(Curse_Regret.class)
            .SetCurse(-2, PCLCardTarget.None, false);

    public Curse_Regret()
    {
        super(DATA, true);

        Initialize(0, 0, 1, 2);

        SetUnplayable(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();
        PCLActions.Bottom.GainTemporaryHP(secondaryValue);
    }

    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (dontTriggerOnUseCard)
        {
            PCLActions.Bottom.LoseHP(magicNumber * player.hand.size(), AbstractGameAction.AttackEffect.NONE);
        }
    }
}