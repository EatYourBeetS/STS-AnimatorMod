package eatyourbeets.cards.animator.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class TenshiHinanawi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(TenshiHinanawi.class)
            .SetSkill(0, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public TenshiHinanawi()
    {
        super(DATA);

        Initialize(0, 2, 7);
        SetUpgrade(0, 2, 0);

        SetAffinity_Red(1);
        SetAffinity_Blue(1);
        SetAffinity_Green(1);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetAttackTarget(CheckSpecialCondition(false) ? EYBCardTarget.Normal : EYBCardTarget.None);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.DealDamageAtEndOfTurn(p, m, magicNumber, AttackEffects.SMASH);
        }
    }

    @Override
    public void triggerOnAffinitySeal(boolean manual)
    {
        super.triggerOnAffinitySeal(manual);
        GameActions.Bottom.Draw(1);
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        final boolean result = GameUtilities.HasOrb(Earth.ORB_ID);
        if (result && tryUse)
        {
            GameActions.Bottom.TriggerOrbPassive(1)
            .SetFilter(c -> Earth.ORB_ID.equals(c.ID));
        }

        return result;
    }
}