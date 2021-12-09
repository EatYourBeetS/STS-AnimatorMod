package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Thoma extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Thoma.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.Self).SetSeriesFromClassPackage(true);

    public Thoma()
    {
        super(DATA);

        Initialize(0, 4, 3, 1);
        SetUpgrade(0, 2, 0, 1);
        SetAffinity_Red(1, 0 ,0);
        SetAffinity_Orange(1, 0, 2);
    }

    @Override
    protected float GetInitialBlock()
    {
        return super.GetInitialBlock() + magicNumber * (GameUtilities.GetPowerAmount(player, BurningPower.POWER_ID) + GameUtilities.GetPowerAmount(player, VulnerablePower.POWER_ID));
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.ApplyBurning(TargetHelper.Enemies(), secondaryValue);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
    }
}