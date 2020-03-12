package eatyourbeets.cards.animator.beta.DateALive;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.cardManipulation.RandomCardUpgrade;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class YamaiSisters extends AnimatorCard {
    public static final EYBCardData DATA = Register(YamaiSisters.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal);

    public YamaiSisters() {
        super(DATA);

        Initialize(2, 0);
        SetUpgrade(1, 0);

        SetSynergy(Synergies.DateALive);

        this.isMultiUpgrade = true;
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);

        for (AbstractCard c : AbstractDungeon.player.hand.group)
        {
            if (c.cardID.equals(YamaiSisters.DATA.ID) && c.canUpgrade())
            {
                c.upgrade();
                c.flash();
            }
        }
    }

    @Override
    public boolean canUpgrade()
    {
        return timesUpgraded < 8;
    }

    @Override
    protected boolean TryUpgrade(boolean updateDescription)
    {
        if (canUpgrade() && timesUpgraded == 7)
        {
            SetScaling(1, 1, 1);
        }

        return super.TryUpgrade(updateDescription);
    }
}