package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.cardManipulation.PlayCardFromPile;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.common.SelfDamagePower;
import eatyourbeets.utilities.GameActions;

public class Illya extends AnimatorCard
{
    public static final String ID = Register(Illya.class.getSimpleName(), EYBCardBadge.Exhaust);

    public Illya()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(0,0, 6, 8);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.Callback(__ ->
        {
            AbstractPlayer p = AbstractDungeon.player;
            if (!DrawBerserker(p.drawPile))
            {
                if (!DrawBerserker(p.discardPile))
                {
                    if (!DrawBerserker(p.exhaustPile))
                    {
                        DrawBerserker(p.hand);
                    }
                }
            }

            GameActions.Bottom.GainEnergy(1);
        });
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        AbstractCard bestCard = null;
        int maxDamage = Integer.MIN_VALUE;
        for (AbstractCard c : p.drawPile.group)
        {
            if (c.type == CardType.ATTACK && c.cardPlayable(m))
            {
                c.calculateCardDamage(m);
                if (c.damage > maxDamage)
                {
                    maxDamage = c.damage;
                    bestCard = c;
                }
            }
        }

        if (bestCard != null)
        {
            GameActions.Bottom.StackPower(new SelfDamagePower(p, magicNumber));
            GameActions.Top.Add(new PlayCardFromPile(bestCard, p.drawPile, false, false, m));
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(-2);
        }
    }

    private boolean DrawBerserker(CardGroup group)
    {
        for (AbstractCard c : group.group)
        {
            if (Berserker.ID.equals(c.cardID))
            {
                if (group.type != CardGroup.CardGroupType.HAND)
                {
                    GameActions.Top.MoveCard(c, AbstractDungeon.player.hand, group, true);
                }

                return true;
            }
        }

        return false;
    }
}