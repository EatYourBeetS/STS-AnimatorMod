package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.interfaces.OnAttackSubscriber;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class ChaikaBohdan extends AnimatorCard implements OnAttackSubscriber
{
    public static final String ID = Register(ChaikaBohdan.class.getSimpleName(), EYBCardBadge.Special);

    private int bonusDamage = 0;

    public ChaikaBohdan()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(5,0,3, 2);

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
                this.AddDamageBonus(this.secondaryValue);
                this.flash();
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
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);

        int handSize = p.hand.size();
        if (p.hand.contains(this))
        {
            handSize -= 1;
        }

        if (handSize <= 0)
        {
            GameActions.Bottom.Draw(magicNumber);
        }

        AddDamageBonus(-bonusDamage);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
        }
    }

    private void AddDamageBonus(int amount)
    {
        bonusDamage += amount;
        baseDamage += amount;
    }
}