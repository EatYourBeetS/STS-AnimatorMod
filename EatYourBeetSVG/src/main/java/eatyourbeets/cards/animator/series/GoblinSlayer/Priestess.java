package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Priestess extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Priestess.class)
            .SetSkill(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Priestess()
    {
        super(DATA);

        Initialize(0, 0, 4, 1);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);

        SetAffinityRequirement(Affinity.Light, 3);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        if (upgraded)
        {
            for (EnemyIntent intent : GameUtilities.GetIntents())
            {
                intent.AddWeak();
            }
        }
        else if (m != null)
        {
            GameUtilities.GetIntent(m).AddWeak();
        }
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    protected void OnUpgrade()
    {
        target = CardTarget.ALL;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.RetainPower(Affinity.Light);
        GameActions.Bottom.ApplyWeak(upgraded ? TargetHelper.Enemies() : TargetHelper.Normal(m), secondaryValue);

        if (isSynergizing || CheckAffinity(Affinity.Light))
        {
            GameActions.Bottom.ExhaustFromPile(name, 1, p.drawPile, p.hand, p.discardPile)
            .ShowEffect(true, true)
            .SetOptions(true, true)
            .SetFilter(GameUtilities::IsHindrance);
        }
    }
}