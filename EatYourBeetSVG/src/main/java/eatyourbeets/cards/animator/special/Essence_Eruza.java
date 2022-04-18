package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class Essence_Eruza extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Essence_Eruza.class)
            .SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetMaxCopies(1);

    public Essence_Eruza()
    {
        super(DATA);

        Initialize(0, 3);

        SetAffinity_Star(1);

        SetExhaust(true);
    }

    @Override
    public boolean canUpgrade()
    {
        return false;
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Delayed.Callback(() ->
        {
            if (player.hand.contains(this))
            {
                GameActions.Top.AutoPlay(this, player.hand, null);
            }
        });
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainEnergy(1);
        GameActions.Bottom.GainBlock(block);
    }
}