package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.TargetHelper;

public class Nyanta extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Nyanta.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Piercing);

    public Nyanta()
    {
        super(DATA);

        Initialize(2, 0, 3);
        SetScaling(0, 1, 0);

        SetRetain(true);
        SetSynergy(Synergies.LogHorizon);
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
    protected void Refresh(AbstractMonster enemy)
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
        GameActions.Bottom.GainAgility(1, true);

        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
            .SetVFX(true, false);
            GameActions.Bottom.StackPower(TargetHelper.Normal(m), JUtils.GetRandomElement(GameUtilities.GetCommonDebuffs()), 1);
        }
    }
}