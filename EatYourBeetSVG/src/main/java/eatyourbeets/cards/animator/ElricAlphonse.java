package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class ElricAlphonse extends AnimatorCard
{
    public static final String ID = CreateFullID(ElricAlphonse.class.getSimpleName());

    public ElricAlphonse()
    {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,2, 2);

        this.isEthereal = true;

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        ElricAlphonseAlt other = new ElricAlphonseAlt();
        if (this.upgraded)
        {
            other.upgrade();
        }

        GameActionsHelper.AddToBottom(new MakeTempCardInDrawPileAction(other, 1, true, true));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.GainBlock(p, this.block);
        PlayerStatistics.ApplyTemporaryFocus(p, p, magicNumber);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBlock(2);
        }
    }
}