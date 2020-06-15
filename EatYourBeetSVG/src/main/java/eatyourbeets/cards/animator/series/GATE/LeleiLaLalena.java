package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class LeleiLaLalena extends AnimatorCard
{
    public static final EYBCardData DATA = Register(LeleiLaLalena.class).SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None);

    public LeleiLaLalena()
    {
        super(DATA);

        Initialize(0, 0, 1);

        SetEvokeOrbCount(1);
        SetSynergy(Synergies.Gate);
        SetSpellcaster();
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        if (m != null && HasSynergy())
        {
            GameUtilities.GetIntent(m).AddWeak();
        }
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        if (HasSynergy())
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
        if (HasSynergy())
        {
            if (m == null)
            {
                m = GameUtilities.GetRandomEnemy(true);
            }

            GameActions.Bottom.ApplyWeak(p, m, 1);
        }

        GameActions.Bottom.DiscardFromHand(name, 1, !upgraded)
        .ShowEffect(!upgraded, !upgraded)
        .SetOptions(false, false, false)
        .AddCallback(() ->
        {
            for (int i = 0; i < magicNumber; i++)
            {
                GameActions.Bottom.ChannelOrb(new Frost(), true);
            }
        });
    }
}