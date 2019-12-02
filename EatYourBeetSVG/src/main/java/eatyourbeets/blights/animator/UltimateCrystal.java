package eatyourbeets.blights.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.common.RefreshHandLayoutAction;
import eatyourbeets.blights.AnimatorBlight;
import eatyourbeets.resources.Resources_Animator_Strings;
import eatyourbeets.utilities.GameActionsHelper;

import java.util.ArrayList;

public class UltimateCrystal extends AnimatorBlight
{
    public static final String ID = CreateFullID(UltimateCrystal.class.getSimpleName());

    public UltimateCrystal()
    {
        super(ID, 3);

        this.counter = -1;
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        String message = Resources_Animator_Strings.Actions.TEXT[6] + " (" + this.name + ")";
        GameActionsHelper.ChooseFromHand(initialAmount, false, this::OnCompletion, this, message);

        this.flash();
    }

    private void OnCompletion(Object state, ArrayList<AbstractCard> cards)
    {
        if (state == this && cards != null)
        {
            AbstractPlayer p = AbstractDungeon.player;
            for (AbstractCard c : cards)
            {
                GameActionsHelper.MoveCard(c, p.drawPile, p.hand, true);
            }

            GameActionsHelper.AddToBottom(new RefreshHandLayoutAction());
        }
    }
}