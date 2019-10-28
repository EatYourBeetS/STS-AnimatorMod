package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import eatyourbeets.interfaces.Hidden;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class ElricAlphonseAlt extends AnimatorCard implements Hidden
{
    public static final String ID = Register(ElricAlphonseAlt.class.getSimpleName());

    public ElricAlphonseAlt()
    {
        super(ID, 1, CardType.SKILL, CardRarity.SPECIAL, CardTarget.SELF);

        Initialize(0,2, 2);

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ApplyPower(p, p, new PlatedArmorPower(p, this.magicNumber), this.magicNumber);
        GameActionsHelper.GainBlock(p, this.block);

        if (HasActiveSynergy())
        {
            GameActionsHelper.ChannelOrb(new Lightning(), true);
        }
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