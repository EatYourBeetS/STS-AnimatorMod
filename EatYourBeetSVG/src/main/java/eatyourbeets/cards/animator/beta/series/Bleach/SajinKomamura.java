package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.EYBStance;
import eatyourbeets.stances.MightStance;
import eatyourbeets.stances.VelocityStance;
import eatyourbeets.stances.WisdomStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class SajinKomamura extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SajinKomamura.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None).SetSeriesFromClassPackage();

    public SajinKomamura()
    {
        super(DATA);

        Initialize(0, 7, 2,1);
        SetUpgrade(0, 4, 0);
        SetAffinity_Red(2, 0, 1);
        SetAffinity_Green(2, 0, 1);

        SetAffinityRequirement(Affinity.Red, 4);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        AbstractStance stance = player.stance;
        if (stance != null && !stance.ID.equals(NeutralStance.STANCE_ID) && stance instanceof EYBStance) {
            GameActions.Bottom.AddAffinity(((EYBStance) stance).affinity, magicNumber);
        }
        else if (GameUtilities.InStance(NeutralStance.STANCE_ID)) {
            EnterRandomStanceNotCurrent();
        }

        if (TrySpendAffinity(Affinity.Red) && TrySpendAffinity(Affinity.Orange) && CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.GainPlatedArmor(secondaryValue);
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

        GameActions.Bottom.ChangeStance(stances.Retrieve(rng));
    }
}