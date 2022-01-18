package pinacolada.cards.pcl.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.stances.MightStance;
import pinacolada.stances.PCLStance;
import pinacolada.stances.VelocityStance;
import pinacolada.stances.WisdomStance;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class SajinKomamura extends PCLCard
{
    public static final PCLCardData DATA = Register(SajinKomamura.class).SetSkill(2, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.None).SetSeriesFromClassPackage();

    public SajinKomamura()
    {
        super(DATA);

        Initialize(0, 7, 2,1);
        SetUpgrade(0, 4, 0);
        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.Red, 4);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        AbstractStance stance = player.stance;
        if (stance != null && !stance.ID.equals(NeutralStance.STANCE_ID) && stance instanceof PCLStance) {
            PCLActions.Bottom.AddAffinity(((PCLStance) stance).affinity, magicNumber);
        }
        else if (PCLGameUtilities.InStance(NeutralStance.STANCE_ID)) {
            EnterRandomStanceNotCurrent();
        }

        if (TrySpendAffinity(PCLAffinity.Red) && TrySpendAffinity(PCLAffinity.Orange) && CombatStats.TryActivateSemiLimited(cardID))
        {
            PCLActions.Bottom.GainPlatedArmor(secondaryValue);
        }
    }

    private void EnterRandomStanceNotCurrent()
    {
        RandomizedList<String> stances = new RandomizedList<>();

        if (!player.stance.ID.equals(MightStance.STANCE_ID))
        {
            stances.Add(MightStance.STANCE_ID);
        }

        if (!player.stance.ID.equals(VelocityStance.STANCE_ID))
        {
            stances.Add(VelocityStance.STANCE_ID);
        }

        if (!player.stance.ID.equals(WisdomStance.STANCE_ID))
        {
            stances.Add(WisdomStance.STANCE_ID);
        }

        if (!player.stance.ID.equals(NeutralStance.STANCE_ID))
        {
            stances.Add(NeutralStance.STANCE_ID);
        }

        PCLActions.Bottom.ChangeStance(stances.Retrieve(rng));
    }
}