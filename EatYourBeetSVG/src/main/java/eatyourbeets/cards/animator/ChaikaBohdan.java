package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.Utilities;

public class ChaikaBohdan extends AnimatorCard
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
    public void triggerOnOtherCardPlayed(AbstractCard c)
    {
        super.triggerOnOtherCardPlayed(c);

        if (c.type == CardType.ATTACK && AbstractDungeon.player.hand.contains(this))
        {
            for (AbstractCard c2 : GetAllInBattleInstances())
            {
                ChaikaBohdan chaika = Utilities.SafeCast(c2, ChaikaBohdan.class);
                if (chaika != null)
                {
                    chaika.AddDamageBonus(this.secondaryValue);
                }
            }

            this.flash();
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