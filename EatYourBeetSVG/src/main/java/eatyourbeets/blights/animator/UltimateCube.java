package eatyourbeets.blights.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.blights.AnimatorBlight;
import eatyourbeets.powers.common.SelfDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;

public class UltimateCube extends AnimatorBlight
{
    public static final String ID = CreateFullID(UltimateCube.class.getSimpleName());

    public UltimateCube()
    {
        super(ID, 12);

        this.counter = -1;
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        GameActions.Bottom.StackPower(new SelfDamagePower(AbstractDungeon.player, initialAmount));

        this.flash();
    }
}