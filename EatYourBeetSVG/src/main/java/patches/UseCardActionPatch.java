package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.Soul;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.utilities.Field;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.Utilities;

import java.util.ArrayList;

public class UseCardActionPatch
{
    @SpirePatch(clz = UseCardAction.class, method = "update")
    public static class UseCardAction_Update
    {
        private static Field<AbstractCard> cardField = Utilities.GetPrivateField("targetCard", UseCardAction.class);

        @SpirePostfixPatch
        public static void Postfix(UseCardAction action)
        {
            if (action.isDone)
            {
                AbstractCard card = cardField.Get(action);
                if (card != null && card.tags.contains(AbstractEnums.CardTags.PURGE))
                {
                    GameActionsHelper.PurgeCard(card);
                }
            }
        }
    }
}

