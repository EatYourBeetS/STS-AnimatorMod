package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.actions.common.ModifyBlockActionWhichActuallyWorks;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActionsHelper;

public class Chung extends AnimatorCard
{
    public static final String ID = Register(Chung.class.getSimpleName(), EYBCardBadge.Exhaust);

    public Chung()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ALL);

        Initialize(0, 12, 3);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActionsHelper.ChannelOrb(new Frost(), true);
        GameActionsHelper.ChannelOrb(new Frost(), true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (this.block > 0)
        {
            GameActionsHelper.GainBlock(p, this.block);
        }

        GameActionsHelper.AddToBottom(new ModifyBlockActionWhichActuallyWorks(this.uuid, -magicNumber));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBlock(1);
            upgradeMagicNumber(-1);
        }
    }
}