package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.interfaces.OnAttackSubscriber;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.JavaUtilities;

public class ChaikaBohdan extends AnimatorCard implements OnAttackSubscriber
{
    public static final String ID = Register(ChaikaBohdan.class.getSimpleName(), EYBCardBadge.Special);

    private int bonusDamage = 0;

    public ChaikaBohdan()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(4,0,3, 2);

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        if (AbstractDungeon.player.hand.contains(this))
        {
            PlayerStatistics.onAttack.Subscribe(this);
        }
    }

    @Override
    public void OnAttack(DamageInfo info, int damageAmount, AbstractCreature target)
    {
        if (AbstractDungeon.player.hand.contains(this))
        {
            if (info.type == DamageInfo.DamageType.NORMAL && target != null && !target.isPlayer)
            {
                for (AbstractCard c2 : GetAllInBattleInstances())
                {
                    ChaikaBohdan chaika = JavaUtilities.SafeCast(c2, ChaikaBohdan.class);
                    if (chaika != null)
                    {
                        chaika.AddDamageBonus(this.secondaryValue);
                        chaika.flash();
                    }
                }
            }
        }
        else
        {
            PlayerStatistics.onAttack.Unsubscribe(this);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);

        int handSize = p.hand.size();
        if (p.hand.contains(this))
        {
            handSize -= 1;
        }

        if (handSize <= 0)
        {
            GameActionsHelper.DrawCard(p, magicNumber);
        }

        AddDamageBonus(-bonusDamage);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(4);
        }
    }

    private void AddDamageBonus(int amount)
    {
        bonusDamage += amount;
        baseDamage += amount;
    }
}