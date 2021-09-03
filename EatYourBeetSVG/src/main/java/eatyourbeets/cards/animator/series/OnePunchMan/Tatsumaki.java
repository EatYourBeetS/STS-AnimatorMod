package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.orbs.animator.Air;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;

public class Tatsumaki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Tatsumaki.class)
            .SetSkill(2, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Tatsumaki()
    {
        super(DATA);

        Initialize(0, 0, 1);

        SetAffinity_Blue(2);
        SetAffinity_Light(1);

        SetEthereal(true);
        SetEvokeOrbCount(1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (IntellectStance.IsActive())
        {
            GameActions.Bottom.GainOrbSlots(1);
        }

        GameActions.Bottom.ChannelOrb(new Air());

        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            if (player.stance.ID.equals(NeutralStance.STANCE_ID))
            {
                GameActions.Bottom.ChangeStance(IntellectStance.STANCE_ID);
            }
            else
            {
                GameActions.Bottom.ChangeStance(NeutralStance.STANCE_ID);
            }
        }
    }
}