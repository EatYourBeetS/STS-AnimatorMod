package eatyourbeets.monsters.Bosses.TheUnnamedMoveset;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.monsters.Bosses.TheUnnamed;
import eatyourbeets.powers.FadingPlayerPower;

public class Move_Fading extends AbstractMove
{
    public int fadingTurns;

    public Move_Fading(int turns)
    {
        fadingTurns = turns;
    }

    @Override
    public boolean CanUse(Byte previousMove)
    {
        return super.CanUse(previousMove) && !AbstractDungeon.player.hasPower(FadingPlayerPower.POWER_ID);
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.STRONG_DEBUFF);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        GameActionsHelper.AddToBottom(new SFXAction("MONSTER_COLLECTOR_DEBUFF"));
        GameActionsHelper.AddToBottom(new VFXAction(new CollectorCurseEffect(target.hb.cX, target.hb.cY), 2.0F));

        FadingPlayerPower fading = (FadingPlayerPower) target.getPower(FadingPlayerPower.POWER_ID);

        if (fading != null)
        {
            fading.amount = fadingTurns;
        }
        else
        {
            fading = new FadingPlayerPower(target, fadingTurns);
            target.powers.add(fading);
        }

        fading.flash();
        if (owner instanceof TheUnnamed)
        {
            ((TheUnnamed) owner).appliedFading = true;
        }
    }
}