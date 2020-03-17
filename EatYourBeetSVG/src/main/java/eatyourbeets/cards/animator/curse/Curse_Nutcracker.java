package eatyourbeets.cards.animator.curse;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_Curse;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Curse_Nutcracker extends AnimatorCard_Curse
{
    public static final EYBCardData DATA = Register(Curse_Nutcracker.class).SetCurse(-2, EYBCardTarget.None);

    public Curse_Nutcracker()
    {
        super(DATA);

        Initialize(0, 0, 3);

        SetSynergy(Synergies.YoujoSenki);
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (this.dontTriggerOnUseCard)
        {
            for (AbstractMonster m1 : GameUtilities.GetAllEnemies(true))
            {
                GameActions.Bottom.Add(new HealAction(m1, null, magicNumber));
            }
        }
    }

    public void triggerOnEndOfTurnForPlayingCard()
    {
        this.dontTriggerOnUseCard = true;
        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
    }
}