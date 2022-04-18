package eatyourbeets.cards.unnamed.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.unnamed.LoseSummonSlot;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Decay extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Decay.class)
            .SetMaxCopies(2)
            .SetSkill(0, CardRarity.UNCOMMON);

    public Decay()
    {
        super(DATA);

        Initialize(0, 0, 6);
        SetUpgrade(0, 0, 2);

        SetExhaust(true);
        SetRecast(true);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null)
        {
            GameUtilities.GetIntent(m).AddWithering(magicNumber);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackWithering(p, m, magicNumber);
        GameActions.Bottom.StackAmplification(p, m, magicNumber);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle)
        {
            GameEffects.Queue.ShowCopy(this);
            GameActions.Bottom.Add(new LoseSummonSlot(1));
        }
    }
}