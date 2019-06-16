package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.actions.common.PlayCardFromPileAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActionsHelper;

public class Illya extends AnimatorCard
{
    public static final String ID = CreateFullID(Illya.class.getSimpleName());

    public Illya()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(0,0);

        this.exhaust = true;

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ApplyPower(p, p, new VulnerablePower(p, 1, false), 1);

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
            GameActionsHelper.AddToTop(new PlayCardFromPileAction(bestCard, p.drawPile, false, false, m));
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            this.exhaust = false;
        }
    }
}