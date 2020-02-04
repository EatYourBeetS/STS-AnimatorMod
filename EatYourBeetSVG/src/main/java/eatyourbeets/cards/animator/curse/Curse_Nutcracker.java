package eatyourbeets.cards.animator.curse;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_Curse;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Curse_Nutcracker extends AnimatorCard_Curse
{
    public static final String ID = Register_Old(Curse_Nutcracker.class);

    public Curse_Nutcracker()
    {
        super(ID, -2, CardRarity.COMMON, CardTarget.NONE);

        Initialize(0, 0, 3);

        SetSynergy(Synergies.YoujoSenki);
    }

    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (this.dontTriggerOnUseCard)
        {
            for (AbstractMonster m1 : GameUtilities.GetCurrentEnemies(true))
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