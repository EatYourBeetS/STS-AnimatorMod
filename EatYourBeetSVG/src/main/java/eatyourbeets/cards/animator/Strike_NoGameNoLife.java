package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;

import java.util.ArrayList;

public class Strike_NoGameNoLife extends Strike
{
    public static final String ID = CreateFullID(Strike_NoGameNoLife.class.getSimpleName());

    public Strike_NoGameNoLife()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(6,0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

        ArrayList<AbstractCard> cards = p.hand.group;

        for (int i = cards.size() - 1; i >= 0; i--)
        {
            AbstractCard card = cards.get(i);
            if (card != this && card.type != CardType.CURSE && card.type != CardType.STATUS && !card.retain)
            {
                card.retain = true;
                card.flash();
                return;
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