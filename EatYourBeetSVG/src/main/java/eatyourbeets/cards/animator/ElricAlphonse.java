package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class ElricAlphonse extends AnimatorCard
{
    public static final String ID = Register(ElricAlphonse.class.getSimpleName(), EYBCardBadge.Exhaust);

    public ElricAlphonse()
    {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,0, 2);

        SetEthereal(true);
        SetSynergy(Synergies.FullmetalAlchemist);

        if (InitializingPreview())
        {
            cardData.InitializePreview(new ElricAlphonseAlt(), true);
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActionsHelper.MakeCardInDiscardPile(new ElricAlphonseAlt(), 1, upgraded);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (PlayerStatistics.GetFocus(p) <= magicNumber)
        {
            GameActionsHelper.GainIntellect(1);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }
}