package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.Utilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.EnchantedArmorPower;

public class RinTohsaka extends AnimatorCard
{
    public static final String ID = Register(RinTohsaka.class.getSimpleName(), EYBCardBadge.Synergy);

    public RinTohsaka()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,3, 5);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ChannelOrb(Utilities.GetRandomOrb(), true);
        GameActionsHelper.GainBlock(p, this.block);

        if (HasActiveSynergy())
        {
            GameActionsHelper.ApplyPower(p, p, new EnchantedArmorPower(p, this.magicNumber), this.magicNumber);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBlock(3);
        }
    }
}