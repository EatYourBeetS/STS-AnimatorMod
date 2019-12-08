package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions._legacy.common.DrawSpecificCardAction;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;

public class Strike_OnePunchMan extends Strike
{
    public static final String ID = Register(Strike_OnePunchMan.class.getSimpleName());

    public Strike_OnePunchMan()
    {
        super(ID, 1, CardTarget.SELF_AND_ENEMY);

        Initialize(6,0, 1);

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper2.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

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