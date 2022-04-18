package eatyourbeets.cards.unnamed.uncommon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.interfaces.subscribers.OnCardReshuffledSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Deflect extends UnnamedCard implements OnCardReshuffledSubscriber
{
    public static final EYBCardData DATA = Register(Deflect.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public Deflect()
    {
        super(DATA);

        Initialize(0, 4, 2, 4);
        SetUpgrade(0, 2, 0, 2);

        SetRetain(true);
    }

    @Override
    public void onRetained()
    {
        super.onRetained();

        GameActions.Bottom.Flash(this);
        GameActions.Bottom.GainBlock(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
    }

    @Override
    public void OnCardReshuffled(AbstractCard card, CardGroup sourcePile)
    {
        if (card == this && sourcePile == player.hand)
        {
            GameEffects.Queue.ShowCopy(this, Settings.WIDTH * 0.35f, Settings.HEIGHT * 0.5f);
            GameActions.Bottom.GainBlock(secondaryValue);
        }
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        CombatStats.onCardReshuffled.Subscribe(this);
    }
}