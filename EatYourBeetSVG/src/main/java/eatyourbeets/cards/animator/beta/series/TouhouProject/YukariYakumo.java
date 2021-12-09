package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.animator.InvertPower;
import eatyourbeets.powers.common.ImpairedPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class YukariYakumo extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YukariYakumo.class).SetSkill(2, CardRarity.RARE, EYBCardTarget.Self).SetSeriesFromClassPackage();
    public static final int DESECRATION_COST = 10;

    public YukariYakumo()
    {
        super(DATA);

        Initialize(0, 0, 4, 2);
        SetUpgrade(0, 0, 0, 1);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Dark(1, 0, 0);

        SetExhaust(true);
        SetAffinityRequirement(Affinity.Dark, 6);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ApplyFrail(p, p, magicNumber);
        GameActions.Bottom.ApplyVulnerable(p, p, magicNumber);
        GameActions.Bottom.ApplyWeak(p, p, magicNumber);
        GameActions.Bottom.StackPower(new ImpairedPower(player, magicNumber));
        GameActions.Bottom.StackPower(new InvertPower(player, secondaryValue));

        if (TrySpendAffinity(Affinity.Dark)) {
            //GameActions.Bottom.ApplyFrail(TargetHelper.Enemies(), secondaryValue);
            GameActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), secondaryValue);
            GameActions.Bottom.ApplyWeak(TargetHelper.Enemies(), secondaryValue);
        }
    }
}

