package pinacolada.cards.pcl.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import eatyourbeets.utilities.ListSelection;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.monsters.PCLEnemyIntent;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

import java.util.Comparator;

public class Genos extends PCLCard
{
    public static final PCLCardData DATA = Register(Genos.class)
            .SetAttack(1, CardRarity.COMMON, PCLAttackType.Ranged)
            .SetSeriesFromClassPackage();

    public Genos()
    {
        super(DATA);

        Initialize(14, 0, 2, 2);
        SetUpgrade(4, 0, 0, 0);

        SetAffinity_Red(1);
        SetAffinity_Light(1, 0, 1);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (!player.hasPower(ArtifactPower.POWER_ID))
        {
            for (PCLEnemyIntent intent : PCLGameUtilities.GetPCLIntents())
            {
                intent.AddPlayerBurning();
            }
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        PCLActions.Bottom.ReduceDebuffs(player, secondaryValue)
        .SetSelection(ListSelection.First(0), 1)
        .Sort(Comparator.comparingInt(a -> -a.amount));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.FIRE);
        PCLActions.Bottom.ApplyBurning(p, p, magicNumber);
        PCLActions.Bottom.ApplyBurning(p, m, magicNumber);
    }
}