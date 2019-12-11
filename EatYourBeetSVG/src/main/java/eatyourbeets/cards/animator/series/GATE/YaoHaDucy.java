package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameUtilities;

public class YaoHaDucy extends AnimatorCard
{
    public static final String ID = Register(YaoHaDucy.class.getSimpleName(), EYBCardBadge.Discard);

    public YaoHaDucy()
    {
        super(ID, 0, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(3, 0, 2, 2);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (EffectHistory.TryActivateLimited(cardID))
        {
            AbstractPlayer p = AbstractDungeon.player;

            GameActions.Bottom.StackPower(new SupportDamagePower(p, secondaryValue));
            GameActions.Bottom.GainAgility(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);

        if (GameUtilities.IsAttacking(m.intent))
        {
            GameActions.Bottom.ReduceStrength(m, magicNumber, true);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
        }
    }
}