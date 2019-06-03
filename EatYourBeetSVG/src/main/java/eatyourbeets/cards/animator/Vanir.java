package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.TransformIntoSpecificCardAction;
import eatyourbeets.cards.AnimatorCard_Boost;
import eatyourbeets.cards.Synergies;

public class Vanir extends AnimatorCard_Boost
{
    public static final String ID = CreateFullID(Vanir.class.getSimpleName());

    public Vanir()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(7,0,4);

        AddExtendedDescription();

        SetSynergy(Synergies.Konosuba, true);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        if (secondaryValue > 0)
        {
            return super.calculateModifiedCardDamage(player, mo, tmp + magicNumber);
        }
        else
        {
            return super.calculateModifiedCardDamage(player, mo, tmp);
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        CardGroup drawPile = AbstractDungeon.player.drawPile;
        if (drawPile.size() > 0)
        {
            GameActionsHelper.AddToBottom(new TransformIntoSpecificCardAction(this, drawPile, drawPile, 1));
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SMASH);
        ProgressBoost();
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
            upgradeMagicNumber(2);
        }
    }

    @Override
    protected int GetBaseBoost()
    {
        return 1;
    }
}