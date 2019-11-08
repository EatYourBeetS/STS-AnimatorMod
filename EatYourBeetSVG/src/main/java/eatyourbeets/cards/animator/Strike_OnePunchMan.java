package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.common.DrawSpecificCardAction;
import eatyourbeets.utilities.GameActionsHelper;

public class Strike_OnePunchMan extends Strike
{
    public static final String ID = Register(Strike_OnePunchMan.class.getSimpleName());

    public Strike_OnePunchMan()
    {
        super(ID, 1, CardTarget.SELF_AND_ENEMY);

        Initialize(6,0, 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

        for (AbstractCard c : p.drawPile.getAttacks().group)
        {
            if (c.tags.contains(CardTags.STRIKE))
            {
                GameActionsHelper.AddToBottom(new DrawSpecificCardAction(c));
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