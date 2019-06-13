package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.Utilities;
import eatyourbeets.cards.AnimatorCard;

public class Strike_Kancolle extends Strike// implements OnRemoveFromDeckSubscriber
{
    public static final String ID = CreateFullID(Strike_Kancolle.class.getSimpleName());

    public Strike_Kancolle()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(6,0, 5);

        this.tags.add(CardTags.HEALING);

        this.baseSecondaryValue = this.secondaryValue = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        if (ProgressBoost())
        {
            for(int i = 0; i < this.magicNumber; ++i)
            {
                AbstractDungeon.effectList.add(new GainPennyEffect(p.hb.cX, p.hb.cY + (p.hb.height / 2)));
            }
            p.gainGold(this.magicNumber);
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