package eatyourbeets.cards.animator.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import patches.AbstractEnums;

public class TakanashiRikka extends AnimatorCard
{
    public static final String ID = Register(TakanashiRikka.class.getSimpleName());

    public TakanashiRikka()
    {
        super(ID, 2, CardType.SKILL, CardColor.COLORLESS, CardRarity.RARE, CardTarget.SELF);

        Initialize(0, 0, 0);

        this.exhaust = true;

        SetSynergy(Synergies.Chuunibyou);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (AbstractCard c : p.hand.getAttacks().group)
        {
            AbstractCard copy = c.makeStatEquivalentCopy();

            if (copy.cost > 0)
            {
                copy.cost = 0;
                copy.costForTurn = 0;
                copy.isCostModified = true;
            }

            copy.baseDamage = magicNumber;
            copy.tags.add(AbstractEnums.CardTags.PURGE);

            GameActions.Top.MakeCardInHand(copy);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(2);
        }
    }
}