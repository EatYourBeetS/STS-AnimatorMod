package pinacolada.cards.pcl.series.Katanagatari;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.monsters.PCLEnemyIntent;
import pinacolada.powers.common.CounterAttackPower;
import pinacolada.powers.temporary.TemporaryEnvenomPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class Nanami extends PCLCard
{
    public static final PCLCardData DATA = Register(Nanami.class)
            .SetSkill(2, CardRarity.UNCOMMON, PCLCardTarget.AoE)
            .SetSeriesFromClassPackage();

    public Nanami()
    {
        super(DATA);

        Initialize(0, 7, 2, 4);
        SetUpgrade(0, 2, 1);

        SetAffinity_Red(1);
        SetAffinity_Green(1, 0, 2);
        SetAffinity_Dark(1);

        SetExhaust(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (PCLJUtils.Find(PCLGameUtilities.GetPCLIntents(), PCLEnemyIntent::IsAttacking) != null && CombatStats.TryActivateSemiLimited(cardID)) {
            PCLActions.Bottom.Cycle(name, 1).AddCallback(cards -> {
               for (AbstractCard c : cards) {
                   if (PCLGameUtilities.HasGreenAffinity(c)) {
                       PCLActions.Bottom.StackPower(new TemporaryEnvenomPower(player, 1));
                   }
               }
            });
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);

        for (PCLEnemyIntent intent : PCLGameUtilities.GetPCLIntents()) {
            if (intent.IsAttacking()) {
                PCLActions.Bottom.ApplyWeak(TargetHelper.Normal(intent.enemy), magicNumber);
            }
            else {
                PCLActions.Bottom.ApplyVulnerable(TargetHelper.Normal(intent.enemy), magicNumber);
            }
        }

        if (CheckPrimaryCondition(true)) {
            PCLActions.Bottom.GainTemporaryThorns(secondaryValue);
        }
        else {
            PCLActions.Bottom.StackPower(new CounterAttackPower(p, secondaryValue));
        }
    }

    @Override
    public boolean CheckPrimaryCondition(boolean tryUse)
    {
        return PCLJUtils.Count(PCLGameUtilities.GetPCLIntents(), PCLEnemyIntent::IsAttacking) >= PCLGameUtilities.GetPCLIntents().size() / 2;
    }
}