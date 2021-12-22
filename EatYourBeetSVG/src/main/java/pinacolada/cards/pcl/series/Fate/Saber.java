package pinacolada.cards.pcl.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.special.Saber_Excalibur;
import pinacolada.effects.AttackEffects;
import pinacolada.monsters.PCLEnemyIntent;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;

public class Saber extends PCLCard
{
    public static final PCLCardData DATA = Register(Saber.class)
            .SetAttack(1, CardRarity.RARE, PCLAttackType.Normal, eatyourbeets.cards.base.EYBCardTarget.Normal)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Saber_Excalibur(), false));

    public Saber()
    {
        super(DATA);

        Initialize(9, 1, 3, 2);
        SetUpgrade(2, 1, 0);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Light(1, 0, 1);

        SetSoul(8, 0, Saber_Excalibur::new);
        SetLoyal(true);
    }

    @Override
    protected float GetInitialBlock()
    {
        ArrayList<PCLEnemyIntent> intents = PCLGameUtilities.GetPCLIntents();
        if (intents.size() == 0) {
            return super.GetInitialBlock();
        }
        for (PCLEnemyIntent intent : PCLGameUtilities.GetPCLIntents())
        {
            if (!intent.IsAttacking())
            {
                return super.GetInitialBlock();
            }
        }
        return super.GetInitialBlock();
    }

    @Override
    protected void OnUpgrade()
    {
        SetInnate(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainRandomAffinityPower(1,false, PCLAffinity.Light, PCLAffinity.Red);
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_DIAGONAL);
        if (block > 0) {
            PCLActions.Bottom.GainBlock(block);
        }

        cooldown.ProgressCooldownAndTrigger(info.IsSynergizing ? 1 + secondaryValue : 1, m);
    }
}