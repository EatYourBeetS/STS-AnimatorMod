package pinacolada.cards.pcl.series.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.monsters.PCLEnemyIntent;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class KotoriKanbe extends PCLCard
{
    public static final PCLCardData DATA = Register(KotoriKanbe.class).SetSkill(1, CardRarity.RARE).SetSeriesFromClassPackage();
    public static final int HP_HEAL_THRESHOLD = 30;

    public KotoriKanbe()
    {
        super(DATA);

        Initialize(0, 1, 3, 4);

        SetEthereal(true);
        SetExhaust(true);
        SetAffinity_Light(1, 0, 2);
        SetAffinity_Blue(1, 0, 0);
    }

    @Override
    public String GetRawDescription()
    {
        return GetRawDescription(HP_HEAL_THRESHOLD);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null)
        {
            int heal = m.maxHealth - m.currentHealth;
            int stacks = Math.floorDiv(heal, magicNumber);
            if (stacks > 0)
            {
                final PCLEnemyIntent intent = PCLGameUtilities.GetPCLIntent(m).AddWeak();
                if (heal >= HP_HEAL_THRESHOLD && CombatStats.CanActivateLimited(cardID))
                {
                    intent.AddStrength(-secondaryValue);
                }
            }
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);

        int heal = m.maxHealth - m.currentHealth;
        int stacks = Math.floorDiv(heal, magicNumber);

        PCLActions.Bottom.Heal(p, m, heal);

        if (stacks > 0)
        {
            PCLActions.Bottom.ApplyWeak(p, m, stacks);
            PCLActions.Bottom.ApplyVulnerable(p, m, stacks);

            if (heal >= HP_HEAL_THRESHOLD && info.TryActivateLimited())
            {
                PCLActions.Bottom.ReduceStrength(m, secondaryValue, false);
            }
        }
    }
}