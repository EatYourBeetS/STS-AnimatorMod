package patches.soul;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.Soul;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import eatyourbeets.interfaces.subscribers.OnAddedToDeckSubscriber;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameEffects;

import java.util.ArrayList;

@SpirePatch(clz = Soul.class, method = "obtain", paramtypez = {AbstractCard.class})
public class Soul_Obtain
{
    public static void Postfix(Soul soul, AbstractCard card)
    {
        if (card instanceof OnAddedToDeckSubscriber)
        {
            ((OnAddedToDeckSubscriber)card).OnAddedToDeck();
        }

        if (card.tags.contains(GR.Enums.CardTags.UNIQUE))
        {
            AbstractCard first = null;

            ArrayList<AbstractCard> toRemove = new ArrayList<>();
            ArrayList<AbstractCard> cards = AbstractDungeon.player.masterDeck.group;
            for (AbstractCard c : cards)
            {
                if (c.cardID.equals(card.cardID))
                {
                    if (first == null)
                    {
                        first = c;
                    }
                    else
                    {
                        toRemove.add(c);
                        for (int i = 0; i <= c.timesUpgraded; i++)
                        {
                            first.upgrade();
                        }
                    }
                }
            }

            for (AbstractCard c : toRemove)
            {
                cards.remove(c);
            }

            if (first != null && toRemove.size() > 0 && GameEffects.TopLevelQueue.Count() < 5)
            {
                GameEffects.TopLevelQueue.Add(new UpgradeShineEffect((float) Settings.WIDTH / 4.0F, (float) Settings.HEIGHT / 2.0F));
                GameEffects.TopLevelQueue.ShowCardBriefly(first.makeStatEquivalentCopy(), (float) Settings.WIDTH / 4.0F, (float) Settings.HEIGHT / 2.0F);
            }
        }
    }
}