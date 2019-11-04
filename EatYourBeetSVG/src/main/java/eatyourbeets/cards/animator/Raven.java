package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.common.DrawSpecificCardAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.RandomizedList;
import eatyourbeets.utilities.Utilities;

public class Raven extends AnimatorCard
{
    public static final String ID = Register(Raven.class.getSimpleName(), EYBCardBadge.Special);

    public Raven()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(5, 0, 1);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);

        if (PlayerStatistics.IsAttacking(m.intent))
        {
            GameActionsHelper.ApplyPower(p, m, new WeakPower(m, 1, false), 1);
        }
        else
        {
            GameActionsHelper.ApplyPower(p, m, new VulnerablePower(m, 1, false), 1);
        }

        if (p.drawPile.size() > 0)
        {
            AbstractCard selected = null;
            int minDamage = Integer.MAX_VALUE;
            for (AbstractCard c : p.drawPile.getAttacks().group)
            {
                if (c.baseDamage < minDamage)
                {
                    minDamage = c.baseDamage;
                    selected = c;
                }
            }

            if (selected != null)
            {
                GameActionsHelper.AddToTop(new DrawSpecificCardAction(selected));
            }
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