package pinacolada.cards.pcl.curse;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import pinacolada.cards.base.PCLCardTarget;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
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

        Initialize(0, 0, 2, 3);

        SetUnplayable(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();
        if (CombatStats.CanActivateLimited(cardID)) {
            for (AbstractPower po : player.powers)
            {
                if (VulnerablePower.POWER_ID.equals(po.ID) && CombatStats.TryActivateLimited(cardID))
                {
                    PCLActions.Top.RemovePower(player, po);
                    break;
                }
            }
        }
    }

    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (dontTriggerOnUseCard)
        {
            PCLActions.Bottom.TakeDamage(magicNumber, AttackEffects.POISON);
        }
    }
}