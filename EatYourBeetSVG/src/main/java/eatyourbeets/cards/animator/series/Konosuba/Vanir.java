package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.shrines.Transmogrifier;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Astrolabe;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.actions.animator.VanirAction;
import eatyourbeets.cards.base.AnimatorCard_Boost;
import eatyourbeets.cards.base.Synergies;

import java.util.ArrayList;

public class Vanir extends AnimatorCard_Boost
{
    public static final String ID = Register(Vanir.class.getSimpleName(), EYBCardBadge.Exhaust);

    public Vanir()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(6,0,6);
        
        AddExtendedDescription();

        SetSynergy(Synergies.Konosuba, true);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        if (GetCurrentBoost() > 0)
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
            GameActions.Bottom.SelectFromPile(name, 1, AbstractDungeon.player.drawPile)
            .SetOptions(false, true)
            .SetMessage(Transmogrifier.OPTIONS[2])
            .AddCallback(cards ->
            {
               if (cards.size() > 0)
               {
                   ArrayList<AbstractCard> temp = AbstractDungeon.player.drawPile.group;

                   AbstractCard card = cards.get(0);
                   for (int i = 0; i < temp.size(); i++)
                   {
                       if (temp.get(i) == card)
                       {
                           AbstractCard vanir = makeStatEquivalentCopy();
                           if (upgraded)
                           {
                               vanir.upgrade();
                           }

                           temp.remove(i);
                           temp.add(i, vanir);

                           return;
                       }
                   }
               }
            });
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SMASH);

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