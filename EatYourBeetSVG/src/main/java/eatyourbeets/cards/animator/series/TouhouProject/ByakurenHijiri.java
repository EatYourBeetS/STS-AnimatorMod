package eatyourbeets.cards.animator.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.SpecialAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ByakurenHijiri extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ByakurenHijiri.class)
            .SetSkill(0, CardRarity.COMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public ByakurenHijiri()
    {
        super(DATA);

        Initialize(0, 0, 2, 12);

        SetAffinity_Blue(1);
        SetAffinity_Dark(1);

        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return (inBattle && secondaryValue > 0) ? SpecialAttribute.Instance.SetCard(this, GR.Tooltips.DelayedDamage, secondaryValue) : null;
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        this.secondaryValue = Math.max(0, baseSecondaryValue - GameUtilities.GetPowerAmount(Affinity.Dark));
        this.isSecondaryValueModified = false;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainForce(magicNumber, false);
        GameActions.Bottom.GainAgility(magicNumber, false);
        GameActions.Bottom.GainIntellect(magicNumber, false);

        final int damage = baseSecondaryValue - GameUtilities.GetPowerAmount(Affinity.Dark);
        if (damage > 0)
        {
            GameActions.Bottom.TakeDamageAtEndOfTurn(damage, AttackEffects.DARK);
        }
    }
}