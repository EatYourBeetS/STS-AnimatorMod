package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.interfaces.listeners.OnAddToDeckListener;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.replacement.MultiTurnEntanglePower;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Ramiris extends AnimatorCard implements OnAddToDeckListener
{
    public static final EYBCardData DATA = Register(Ramiris.class)
            .SetSkill(0, CardRarity.RARE, EYBCardTarget.ALL)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public Ramiris()
    {
        super(DATA);

        Initialize(0, 0, 2, 3);
        SetUpgrade(0, 0, 0, 2);

        SetAffinity_Light(2);
        SetAffinity_Blue(2);
        SetAffinity_Green(1);

        SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, false).SetText("1", Colors.Cream(1));
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
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
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainTemporaryHP(1);

        if (IsStarter())
        {
            GameActions.Bottom.StackPower(TargetHelper.Enemies(), PowerHelper.Weak, magicNumber);
            GameActions.Bottom.StackPower(TargetHelper.Enemies(), PowerHelper.Shackles, secondaryValue);
            GameActions.Bottom.StackPower(new MultiTurnEntanglePower(p, 2));
        }
    }

    @Override
    public boolean OnAddToDeck()
    {
        player.increaseMaxHp(2, true);

        return true;
    }
}