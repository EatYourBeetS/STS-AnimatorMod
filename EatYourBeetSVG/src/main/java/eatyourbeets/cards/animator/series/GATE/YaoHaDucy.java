package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class YaoHaDucy extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YaoHaDucy.class).SetAttack(0, CardRarity.COMMON);

    public YaoHaDucy()
    {
        super(DATA);

        Initialize(2, 0, 2, 1);
        SetUpgrade(3, 0, 0, 0);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);

        if (GameUtilities.IsAttacking(m.intent))
        {
            GameActions.Bottom.ReduceStrength(m, magicNumber, true);
        }

        if (HasSynergy() && GameUtilities.GetPowerAmount(p, SupportDamagePower.POWER_ID) > 0)
        {
            GameActions.Bottom.StackPower(new SupportDamagePower(p, secondaryValue));
        }
    }
}