package eatyourbeets.blights.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.blights.AnimatorBlight;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.common.SelfDamagePower;
import eatyourbeets.utilities.GameActionsHelper;

public class UltimateCube extends AnimatorBlight
{
    public static final String ID = CreateFullID(UltimateCube.class.getSimpleName());

    public UltimateCube()
    {
        super(ID, 10);

        this.counter = -1;
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        AbstractPlayer p = AbstractDungeon.player;
        GameActionsHelper.ApplyPower(p, p, new SelfDamagePower(p, initialAmount), initialAmount);
        this.flash();
    }
}