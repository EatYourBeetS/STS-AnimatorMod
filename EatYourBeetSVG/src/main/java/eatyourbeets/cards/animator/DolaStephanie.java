package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.handSelection.SelectFromHand;
import eatyourbeets.actions.pileSelection.FetchFromPile;
import eatyourbeets.resources.Resources_Animator_Strings;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions._legacy.animator.StephanieAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.utilities.JavaUtilities;

import javax.swing.text.Utilities;
import java.util.ArrayList;

public class DolaStephanie extends AnimatorCard
{
    public static final String ID = Register(DolaStephanie.class.getSimpleName());

    public DolaStephanie()
    {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0);

        SetExhaust(true);
        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper2.SelectFromHand(name, 1, false)
        .SetMessage(Resources_Animator_Strings.Actions.TEXT[0])
        .SetOptions(false, false, false)
        .AddCallback(cards ->
        {
            AnimatorCard card = JavaUtilities.SafeCast(cards.get(0), AnimatorCard.class);
            if (card == null)
            {
                return;
            }

            GameActionsHelper2.AddToTop(new FetchFromPile(name, 1, AbstractDungeon.player.drawPile)
                    .SetOptions(false, false)
                    .SetFilter(card::HasSynergy));
        });
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            SetExhaust(false);
        }
    }
}