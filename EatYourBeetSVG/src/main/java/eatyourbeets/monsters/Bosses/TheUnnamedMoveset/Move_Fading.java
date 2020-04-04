package eatyourbeets.monsters.Bosses.TheUnnamedMoveset;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;
import eatyourbeets.actions.special.PlayTempBgmAction;
import eatyourbeets.blights.animator.Doomed;
import eatyourbeets.cards.animator.special.Respite;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.EYBAbstractMove;

public class Move_Fading extends EYBAbstractMove
{
    public int fadingTurns;

    public Move_Fading(int turns)
    {
        fadingTurns = turns;
    }

    @Override
    public boolean CanUse(Byte previousMove)
    {
        return super.CanUse(previousMove) && !AbstractDungeon.player.hasBlight(Doomed.ID);
    }

    public void Select()
    {
        owner.setMove(id, AbstractMonster.Intent.STRONG_DEBUFF);
    }

    public void QueueActions(AbstractCreature target)
    {
        GameActions.Bottom.SFX("MONSTER_COLLECTOR_DEBUFF");
        GameActions.Bottom.VFX(new CollectorCurseEffect(target.hb.cX, target.hb.cY), 2f);
        GameActions.Bottom.Callback(() -> AbstractDungeon.getCurrRoom().spawnBlightAndObtain(owner.hb.cX, owner.hb.cY, new Doomed(fadingTurns)));
        GameActions.Bottom.Add(new PlayTempBgmAction("MINDBLOOM", 1));

        AbstractDungeon.player.drawPile.addToTop(new Respite());
    }
}