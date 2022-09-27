package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Priestess extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Priestess.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();

    public Priestess()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 2);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);

        SetAffinityRequirement(Affinity.Light, 1);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        for (EnemyIntent intent : GameUtilities.GetIntents())
        {
            intent.AddWeak();
        }
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.ApplyWeak(TargetHelper.Enemies(), 1);

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.ExhaustFromPile(name, 1, p.drawPile, p.hand, p.discardPile)
            .ShowEffect(true, true)
            .SetOptions(true, true)
            .SetFilter(GameUtilities::IsHindrance);
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return (HasHindrances(player.drawPile) || HasHindrances(player.hand) || HasHindrances(player.discardPile)) && super.CheckSpecialCondition(tryUse);
    }

    protected boolean HasHindrances(CardGroup group)
    {
        for (AbstractCard c : group.group)
        {
            if (GameUtilities.IsHindrance(c))
            {
                return true;
            }
        }

        return false;
    }
}