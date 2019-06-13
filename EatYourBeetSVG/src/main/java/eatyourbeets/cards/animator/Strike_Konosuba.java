package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.Utilities;
import eatyourbeets.cards.AnimatorCard;

public class Strike_Konosuba extends Strike
{
    public static final String ID = CreateFullID(Strike_Konosuba.class.getSimpleName());

    public Strike_Konosuba()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(6,0, 2);

        this.tags.add(CardTags.HEALING);

        this.baseSecondaryValue = this.secondaryValue = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

        if (ProgressBoost())
        {
            GameActionsHelper.AddToBottom(new HealAction(p, p, this.magicNumber));
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

    protected boolean ProgressBoost()
    {
        if (this.secondaryValue > 0)
        {
            int newValue = this.secondaryValue - 1;

            for (AbstractCard c : GetAllInBattleInstances.get(this.uuid))
            {
                AnimatorCard card = Utilities.SafeCast(c, AnimatorCard.class);
                if (card != null)
                {
                    if (newValue == 0)
                    {
                        card.baseSecondaryValue = 1;
                        card.secondaryValue = 0;
                        card.isSecondaryValueModified = true;
                    }
                    else
                    {
                        card.baseSecondaryValue = card.secondaryValue = newValue;
                    }
                }
            }

            return true;
        }

        return false;
    }
}