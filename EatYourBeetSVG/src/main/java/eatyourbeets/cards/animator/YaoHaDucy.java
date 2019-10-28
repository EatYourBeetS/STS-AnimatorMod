package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

import java.util.HashSet;
import java.util.Iterator;

public class YaoHaDucy extends AnimatorCard
{
    public static final String ID = Register(YaoHaDucy.class.getSimpleName());

    public YaoHaDucy()
    {
        super(ID, 0, CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY);

        Initialize(2, 0, 1);

        this.isMultiDamage = true;

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageAllEnemies(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);

        for (AbstractCard c : getAllCopies())
        {
            c.baseDamage += this.magicNumber;
            c.applyPowers();
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
        }
    }

    public HashSet<AbstractCard> getAllCopies()
    {
        HashSet<AbstractCard> cards = new HashSet<>();
        AbstractCard c;

        c = AbstractDungeon.player.cardInUse;
        if (c != null && c.cardID.equals(cardID))
        {
            cards.add(c);
        }

        Iterator var2 = AbstractDungeon.player.drawPile.group.iterator();
        while (var2.hasNext())
        {
            c = (AbstractCard) var2.next();
            if (c.cardID.equals(cardID))
            {
                cards.add(c);
            }
        }

        var2 = AbstractDungeon.player.discardPile.group.iterator();
        while (var2.hasNext())
        {
            c = (AbstractCard) var2.next();
            if (c.cardID.equals(cardID))
            {
                cards.add(c);
            }
        }

        var2 = AbstractDungeon.player.exhaustPile.group.iterator();
        while (var2.hasNext())
        {
            c = (AbstractCard) var2.next();
            if (c.cardID.equals(cardID))
            {
                cards.add(c);
            }
        }

        var2 = AbstractDungeon.player.limbo.group.iterator();
        while (var2.hasNext())
        {
            c = (AbstractCard) var2.next();
            if (c.cardID.equals(cardID))
            {
                cards.add(c);
            }
        }

        var2 = AbstractDungeon.player.hand.group.iterator();
        while (var2.hasNext())
        {
            c = (AbstractCard) var2.next();
            if (c.cardID.equals(cardID))
            {
                cards.add(c);
            }
        }

        return cards;
    }
}