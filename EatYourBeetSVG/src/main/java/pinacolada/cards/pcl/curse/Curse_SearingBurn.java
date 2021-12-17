package pinacolada.cards.pcl.curse;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardTarget;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCard_Curse;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Curse_SearingBurn extends PCLCard_Curse
{
    public static final PCLCardData DATA = Register(Curse_SearingBurn.class).SetCurse(-2, EYBCardTarget.ALL, false);

    public Curse_SearingBurn()
    {
        super(DATA, false);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 2, 1);
        SetEthereal(true);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (!this.dontTriggerOnUseCard)
        {
            this.performAction();
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        this.performAction();
    }

    private void performAction() {
        int[] damageMatrix = DamageInfo.createDamageMatrix(magicNumber, true);
        if (auxiliaryData.form == 0) {
            PCLActions.Bottom.DealDamage(null, player, magicNumber, DamageInfo.DamageType.THORNS, AttackEffects.FIRE);
        }
        PCLActions.Bottom.DealDamageToAll(damageMatrix, DamageInfo.DamageType.THORNS, AttackEffects.FIRE);

        for (AbstractMonster enemy : PCLGameUtilities.GetEnemies(true))
        {
            PCLActions.Bottom.ApplyBurning(null, enemy, secondaryValue);
        }
        PCLActions.Bottom.ApplyBurning(null, player, secondaryValue);
    }
}