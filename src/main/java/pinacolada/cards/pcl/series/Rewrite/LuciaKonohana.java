package pinacolada.cards.pcl.series.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.PotionBounceEffect;
import pinacolada.cards.base.PCLCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLPower;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class LuciaKonohana extends PCLCard
{
    public static final PCLCardData DATA = Register(LuciaKonohana.class).SetSkill(-1, CardRarity.UNCOMMON, PCLCardTarget.Normal).SetSeriesFromClassPackage();

    public LuciaKonohana()
    {
        super(DATA);

        Initialize(0, 2, 4, 2);
        SetUpgrade(0, 0, 1);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Green(1,0,1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        int stacks = PCLGameUtilities.UseXCostEnergy(this);

        for (int i=0; i<stacks; i++)
        {
            if (i == 0)
            {
                PCLActions.Bottom.VFX(new PotionBounceEffect(p.hb.cX, p.hb.cY, m.hb.cX, m.hb.cY), 0.3f);
            }
            else
            {
                PCLActions.Bottom.VFX(new PotionBounceEffect(m.hb.cX, m.hb.cY, m.hb.cX, m.hb.cY), 0.4f);
            }
            PCLActions.Bottom.ApplyPoison(p, m, magicNumber);
            PCLActions.Bottom.ApplyWeak(p, m, 1);
        }

        if (stacks >= secondaryValue)
        {
            if (CombatStats.TryActivateLimited(cardID))
            {
                PCLActions.Bottom.ApplyPower(p, m, new LuciaKonohanaPower(m, 1));
            }
        }
    }

    public static class LuciaKonohanaPower extends PCLPower
    {
        public LuciaKonohanaPower(AbstractCreature owner, int amount)
        {
            super(owner, LuciaKonohana.DATA);

            this.amount = amount;
            this.type = PowerType.DEBUFF;

            updateDescription();
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0);
        }

        @Override
        public void onDeath()
        {
            final AbstractCreature corpse = this.owner;
            int powAmount;

            for (AbstractPower debuff : corpse.powers)
            {
                for (PCLPowerHelper commonDebuffHelper : PCLGameUtilities.GetPCLCommonDebuffs()) {
                    if (commonDebuffHelper.ID.equals(debuff.ID)) {
                        powAmount = PCLGameUtilities.GetPowerAmount(corpse, debuff.ID);
                        PCLActions.Bottom.ApplyPower(TargetHelper.Enemies(), commonDebuffHelper, powAmount);
                    }
                }
            }
        }
    }
}