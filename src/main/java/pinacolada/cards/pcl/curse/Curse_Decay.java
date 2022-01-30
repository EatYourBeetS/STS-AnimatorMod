package pinacolada.cards.pcl.curse;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.PCLCard_Curse;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class Curse_Decay extends PCLCard_Curse
{
    public static final PCLCardData DATA = Register(Curse_Decay.class)
            .SetCurse(-2, PCLCardTarget.None, false);

    public Curse_Decay()
    {
        super(DATA, true);

        Initialize(0, 0, 2, 1);

        SetUnplayable(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();
        PCLActions.Top.ReducePower(player, player, WeakPower.POWER_ID, secondaryValue);
    }

    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (dontTriggerOnUseCard)
        {
            PCLActions.Bottom.TakeDamage(magicNumber, AttackEffects.POISON);
        }
    }
}