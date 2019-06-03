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
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.monsters.Bosses.TheUnnamed;

public class Move_Taunt extends AbstractMove
{
    private String[] dialog;

    @Override
    public void Init(byte id, AbstractMonster owner)
    {
        super.Init(id, owner);

        dialog = ((TheUnnamed)owner).data.strings.DIALOG;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.UNKNOWN);
    }

    public void Execute(AbstractPlayer target)
    {
        GameActionsHelper.AddToBottom(new TalkAction(owner, GetLine()));
    }

    private String GetLine()
    {
        RandomizedList<String> lines = new RandomizedList<>();
        lines.Add(dialog[4]);
        lines.Add(dialog[5]);
        lines.Add(dialog[6]);
        lines.Add(dialog[7]);
        lines.Add(dialog[8]);
        lines.Add(dialog[9]);
        lines.Add(dialog[10]);

        if (AbstractDungeon.player.masterDeck.findCardById(Gilgamesh.ID) != null)
        {
            lines.Add(dialog[11]);
        }

        if (AbstractDungeon.player.masterDeck.findCardById(Megumin.ID) != null)
        {
            lines.Add(dialog[12]);
        }

        if (AbstractDungeon.player.masterDeck.findCardById(Ainz.ID) != null)
        {
            lines.Add(dialog[13]);
        }

        return lines.Retrieve(AbstractDungeon.aiRng);
    }
}
