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

        Initialize(0, 2, 6);
        SetUpgrade(0, 2, 0);

        SetAffinity_Red(1);
        SetAffinity_Blue(1);
        SetAffinity_Green(1);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetAttackTarget(GameUtilities.HasOrb(Earth.ORB_ID) ? EYBCardTarget.Normal : EYBCardTarget.None);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.TriggerOrbPassive(1)
        .SetFilter(c -> Earth.ORB_ID.equals(c.ID))
        .AddCallback(m, (c, orbs) ->
        {
            if (orbs.size() > 0)
            {
                GameActions.Bottom.DealDamageAtEndOfTurn(player, c, magicNumber, AttackEffects.SMASH);
            }
        });

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.Draw(1);
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return super.CheckSpecialConditionSemiLimited(tryUse, __ -> GameUtilities.IsSealed(this));
    }
}