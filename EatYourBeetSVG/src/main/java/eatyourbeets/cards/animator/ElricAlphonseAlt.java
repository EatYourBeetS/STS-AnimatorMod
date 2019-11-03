package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import eatyourbeets.interfaces.metadata.Hidden;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class ElricAlphonseAlt extends AnimatorCard implements Hidden
{
    public static final String ID = Register(ElricAlphonseAlt.class.getSimpleName());

    public ElricAlphonseAlt()
    {
        super(ID, 1, CardType.SKILL, CardRarity.SPECIAL, CardTarget.SELF);

        Initialize(0,0, 2);

        SetExhaust(true);
        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.AddToBottom(new IncreaseMaxOrbAction(1));
        GameActionsHelper.ApplyPower(p, p, new PlatedArmorPower(p, this.magicNumber), this.magicNumber);

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
            upgradeMagicNumber(1);
        }
    }
}