package eatyourbeets.monsters.Bosses.TheUnnamedMoveset;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.animator.Ainz;
import eatyourbeets.cards.animator.Gilgamesh;
import eatyourbeets.cards.animator.Megumin;
import eatyourbeets.misc.RandomizedList;
import eatyourbeets.monsters.Bosses.TheUnnamed;

public class Move_Talk extends Move
{
    public Move_Talk(int id, int ascensionLevel, TheUnnamed owner)
    {
        super((byte) id, ascensionLevel, owner);
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.UNKNOWN);
    }

    public void Execute(AbstractPlayer target)
    {
        GameActionsHelper.AddToBottom(new TalkAction(theUnnamed, GetLine()));
    }

    private String GetLine()
    {
        RandomizedList<String> lines = new RandomizedList<>();
        lines.Add("How dare you challenge me!");
        lines.Add("I don't even need to attack you...");
        lines.Add("You'd better run away while you can.");
        lines.Add("You call that a deck?");
        lines.Add("It will all end soon...");
        lines.Add("How did you even get this far?");
        lines.Add("You know... You should deal some damage to progress...");

        if (AbstractDungeon.player.masterDeck.findCardById(Gilgamesh.ID) != null)
        {
            lines.Add("Gilgamesh will not save you.");
        }

        if (AbstractDungeon.player.masterDeck.findCardById(Megumin.ID) != null)
        {
            lines.Add("Megumin is overrated.");
        }

        if (AbstractDungeon.player.masterDeck.findCardById(Ainz.ID) != null)
        {
            lines.Add("You are cheating, I see that Ainz is in your deck...");
        }

        return lines.Retrieve(AbstractDungeon.aiRng);
    }
}
