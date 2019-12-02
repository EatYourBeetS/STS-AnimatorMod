package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.interfaces.metadata.Spellcaster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class LeleiLaLalena extends AnimatorCard implements Spellcaster
{
    public static final String ID = Register(LeleiLaLalena.class.getSimpleName(), EYBCardBadge.Synergy);

    public LeleiLaLalena()
    {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);

        Initialize(0,0, 1);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        Spellcaster.ApplyScaling(this, 6);

        if (HasActiveSynergy())
        {
            target = CardTarget.SELF_AND_ENEMY;
        }
        else
        {
            target = CardTarget.SELF;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (GetOtherCardsInHand().size() > 0)
        {
            GameActionsHelper.Discard(1, !upgraded);

            for (int i = 0; i < this.magicNumber; i++)
            {
                GameActionsHelper.ChannelOrb(new Frost(), true);
            }
        }

        if (HasActiveSynergy() && m != null)
        {
            GameActionsHelper.ApplyPower(p, m, new WeakPower(m, 1, false), 1);
        }
    }

    @Override
    public void upgrade() 
    {
        TryUpgrade();
    }
}