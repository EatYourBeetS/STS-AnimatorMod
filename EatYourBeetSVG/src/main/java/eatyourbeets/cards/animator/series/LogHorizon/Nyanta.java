package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Nyanta extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Nyanta.class)
            .SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Piercing)
            .SetSeriesFromClassPackage();

    public Nyanta()
    {
        super(DATA);

        Initialize(2, 0, 3);

        SetAffinity_Green(2, 0, 1);
        SetAffinity_Orange(1);

        SetRetain(true);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        if (player.stance != null && !NeutralStance.STANCE_ID.equals(player.stance.ID))
        {
            GameUtilities.IncreaseMagicNumber(this, 1, true);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainAgility(1, upgraded);
        GameActions.Bottom.GainWillpower(1, upgraded);

        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL)
            .SetVFX(true, false);
            GameActions.Bottom.StackPower(TargetHelper.Normal(m), GameUtilities.GetRandomElement(GameUtilities.GetCommonDebuffs()), 1)
            .ShowEffect(false, true);
        }
    }
}