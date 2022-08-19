package eatyourbeets.cards.animatorClassic.series.LogHorizon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.TargetHelper;

public class Nyanta extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Nyanta.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Piercing);

    public Nyanta()
    {
        super(DATA);

        Initialize(2, 0, 3);
        SetScaling(0, 1, 0);

        SetRetain(true);
        SetSeries(CardSeries.LogHorizon);
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
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainAgility(1, upgraded);

        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
            .SetVFX(true, false);
            GameActions.Bottom.StackPower(TargetHelper.Normal(m), JUtils.Random(GameUtilities.GetCommonDebuffs()), 1)
            .ShowEffect(false, true);
        }
    }
}