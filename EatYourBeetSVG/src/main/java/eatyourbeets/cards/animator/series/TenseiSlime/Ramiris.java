package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.*;

public class Ramiris extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Ramiris.class)
            .SetSkill(0, CardRarity.RARE, EYBCardTarget.ALL)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();
    public static final int TEMP_HP = 2;
    public static final int SHACKLES = 10;

    public Ramiris()
    {
        super(DATA);

        Initialize(0, 0, 1, 4);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Light(2);
        SetAffinity_Blue(2);
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
            for (EnemyIntent intent : GameUtilities.GetIntents())
            {
                intent.AddWeak();
                intent.AddStrength(-secondaryValue);
            }
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainTemporaryHP(TEMP_HP);

        if (IsStarter())
        {
            GameActions.Bottom.StackPower(TargetHelper.Enemies(), PowerHelper.Weak, magicNumber);
            GameActions.Bottom.StackPower(TargetHelper.Enemies(), PowerHelper.Shackles, secondaryValue);
            GameActions.Bottom.StackPower(TargetHelper.Player(), PowerHelper.Shackles, SHACKLES);
        }
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle)
        {
            GameEffects.List.ShowCopy(this);
            GameActions.Bottom.GainTemporaryHP(TEMP_HP);
        }
    }
}