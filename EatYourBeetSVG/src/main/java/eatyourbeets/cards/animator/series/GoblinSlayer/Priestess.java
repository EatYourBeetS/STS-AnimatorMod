package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Priestess extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Priestess.class).SetSkill(1, CardRarity.COMMON);

    public Priestess()
    {
        super(DATA);

        Initialize(0, 0, 4, 1);

        SetSynergy(Synergies.GoblinSlayer);
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
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        if (upgraded)
        {
            target = CardTarget.ALL;
        }
        else
        {
            target = CardTarget.SELF_AND_ENEMY;
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (upgraded)
        {
            GameActions.Bottom.ApplyWeak(TargetHelper.Enemies(), secondaryValue);
        }
        else if (m != null)
        {
            GameActions.Bottom.ApplyWeak(p, m, secondaryValue);
        }

        GameActions.Bottom.GainTemporaryHP(magicNumber);

        if (isSynergizing)
        {
            GameActions.Bottom.ExhaustFromPile(name, 1, p.drawPile, p.hand, p.discardPile)
            .ShowEffect(true, true)
            .SetOptions(true, true)
            .SetFilter(GameUtilities::IsCurseOrStatus);
        }
    }
}