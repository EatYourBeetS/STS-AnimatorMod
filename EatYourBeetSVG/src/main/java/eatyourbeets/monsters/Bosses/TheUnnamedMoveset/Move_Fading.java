package eatyourbeets.monsters.Bosses.TheUnnamedMoveset;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.Bosses.TheUnnamed;
import eatyourbeets.powers.FadingPlayerPower;

public class Move_Fading extends Move
{
    private int FADING_TURNS;

    public Move_Fading(int id, int ascensionLevel, TheUnnamed owner)
    {
        super((byte) id, ascensionLevel, owner);

        FADING_TURNS = 4;
    }

    @Override
    public boolean CanUse(Byte previousMove)
    {
        return false;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.STRONG_DEBUFF);
    }

    public void Execute(AbstractPlayer target)
    {
        GameActionsHelper.AddToBottom(new SFXAction("MONSTER_COLLECTOR_DEBUFF"));
        GameActionsHelper.AddToBottom(new VFXAction(new CollectorCurseEffect(target.hb.cX, target.hb.cY), 2.0F));

        FadingPlayerPower fading = (FadingPlayerPower) target.getPower(FadingPlayerPower.POWER_ID);

        if (fading != null)
        {
            fading.amount = FADING_TURNS;
        }
        else
        {
            fading = new FadingPlayerPower(target, FADING_TURNS);
            target.powers.add(fading);
        }

        fading.flash();
        theUnnamed.appliedFading = true;
    }
}