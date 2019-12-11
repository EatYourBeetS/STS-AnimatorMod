package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.utilities.GameActions;

public class LeleiLaLalena extends AnimatorCard implements Spellcaster
{
    public static final String ID = Register(LeleiLaLalena.class.getSimpleName(), EYBCardBadge.Synergy);

    public LeleiLaLalena()
    {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);

        Initialize(0,0, 1);

        SetEvokeOrbCount(1);
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
        if (HasActiveSynergy() && m != null)
        {
            GameActions.Bottom.ApplyWeak(p, m, 1);
        }

        GameActions.Bottom.DiscardFromHand(name, 1, !upgraded)
        .SetOptions(false, false, false)
        .AddCallback(__ ->
        {
            for (int i = 0; i < magicNumber; i++)
            {
                GameActions.Bottom.ChannelOrb(new Frost(), true);
            }
        });
    }

    @Override
    public void upgrade() 
    {
        TryUpgrade();
    }
}