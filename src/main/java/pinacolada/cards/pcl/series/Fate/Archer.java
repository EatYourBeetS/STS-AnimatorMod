package pinacolada.cards.pcl.series.Fate;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Archer extends PCLCard
{
    public static final PCLCardData DATA = Register(Archer.class)
            .SetPower(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();
    public static final int COST = 6;

    public Archer()
    {
        super(DATA);

        Initialize(0, 0, 3, 4);
        SetUpgrade(0, 1, 0);

        SetAffinity_Green(1, 0, 0);
    }

    public void OnUpgrade() {
        SetRetainOnce(true);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(COST);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.StackPower(new ArcherPower(p, magicNumber));
    }

    public static class ArcherPower extends PCLClickablePower
    {

        public ArcherPower(AbstractCreature owner, int amount)
        {
            super(owner, Archer.DATA, PowerTriggerConditionType.Affinity, COST, null, null, PCLAffinity.Green);

            Initialize(amount);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            if (!isPlayer)
            {
                return;
            }

            for (AbstractMonster m : PCLGameUtilities.GetEnemies(true))
            {
                final int debuffs = PCLGameUtilities.GetDebuffsCount(m.powers);
                for (int i = 0; i < debuffs; i++)
                {
                    PCLActions.Bottom.VFX(VFX.ThrowDagger(m.hb, 0.2f));
                    PCLActions.Bottom.DealDamage(owner, m, amount, DamageInfo.DamageType.THORNS, AttackEffects.NONE)
                            .SetVFX(true, true);
                }
            }

            this.flash();
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            PCLActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), 1);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, amount, COST);
        }

    }
}