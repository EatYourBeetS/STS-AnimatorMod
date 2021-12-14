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
import eatyourbeets.utilities.TargetHelper;

public class Thoma extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Thoma.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.Self).SetSeriesFromClassPackage(true);

    public Thoma()
    {
        super(DATA);

        Initialize(0, 4, 6, 1);
        SetUpgrade(0, 3, 0, 0);
        SetAffinity_Red(1, 0 ,0);
        SetAffinity_Orange(1, 0, 2);
    }

    @Override
    protected float GetInitialBlock()
    {
        return  (player.hasPower(BurningPower.POWER_ID) || player.hasPower(VulnerablePower.POWER_ID) ? super.GetInitialBlock() + magicNumber : super.GetInitialBlock());
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