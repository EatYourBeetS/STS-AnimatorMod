package pinacolada.cards.pcl.series.Elsword;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.actions.orbs.RemoveOrb;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Chung extends PCLCard
{
    public static final PCLCardData DATA = Register(Chung.class)
            .SetAttack(1, CardRarity.COMMON, PCLAttackType.Ice)
            .SetSeriesFromClassPackage();

    public Chung()
    {
        super(DATA);

        Initialize(7, 0, 4, 2);
        SetUpgrade(2, 0, 2, 0);

        SetAffinity_Orange(1);
        SetAffinity_Silver(1);
        SetAffinity_Blue(0,0,2);
    }


    @Override
    protected float ModifyDamage(AbstractMonster enemy, float damage)
    {
        return super.ModifyDamage(enemy, damage + magicNumber * PCLGameUtilities.GetOrbCount(Frost.ORB_ID));
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        PCLActions.Bottom.ChannelOrb(new Frost());
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.ICE);

        boolean shouldEvoke = upgraded && auxiliaryData.form == 1;
        for (AbstractOrb orb : player.orbs) {
            if (orb != null && Frost.ORB_ID.equals(orb.ID)) {
                if (shouldEvoke) {
                    PCLActions.Bottom.EvokeOrb(1, orb);
                }
                else {
                    PCLActions.Bottom.Add(new RemoveOrb(orb));
                }
                PCLActions.Bottom.ApplyFreezing(TargetHelper.Normal(m), secondaryValue);
            }
        }
    }
}