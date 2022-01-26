package pinacolada.cards.pcl.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCardTarget;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.monsters.PCLEnemyIntent;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class Ramiris extends PCLCard
{
    public static final PCLCardData DATA = Register(Ramiris.class)
            .SetSkill(0, CardRarity.RARE, PCLCardTarget.AoE)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();
    public static final int TEMP_HP = 2;
    public static final int SHACKLES = 10;

    public Ramiris()
    {
        super(DATA);

        Initialize(0, 0, 1, 4);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Light(1);
        SetAffinity_Blue(1);
        SetAffinity_Green(1);

        SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, false).SetText(TEMP_HP, Colors.Cream(1));
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (IsStarter())
        {
            for (PCLEnemyIntent intent : PCLGameUtilities.GetPCLIntents())
            {
                intent.AddWeak();
                intent.AddStrength(-secondaryValue);
            }
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainTemporaryHP(TEMP_HP);

        if (IsStarter())
        {
            PCLActions.Bottom.StackPower(TargetHelper.Enemies(), PCLPowerHelper.Weak, magicNumber);
            PCLActions.Bottom.StackPower(TargetHelper.Enemies(), PCLPowerHelper.Shackles, secondaryValue);
            PCLActions.Bottom.StackPower(TargetHelper.Player(), PCLPowerHelper.Shackles, SHACKLES);
        }
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle)
        {
            PCLGameEffects.List.ShowCopy(this);
            PCLActions.Bottom.GainTemporaryHP(TEMP_HP);
        }
    }
}