package eatyourbeets.blights.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.blights.AnimatorBlight;
import eatyourbeets.utilities.GameActionsHelper;

public class UltimateCrystal extends AnimatorBlight
{
    public static final String ID = CreateFullID(UltimateCrystal.class.getSimpleName());

    public UltimateCrystal()
    {
        super(ID, 2);

        this.counter = -1;
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        AbstractPlayer p = AbstractDungeon.player;
        GameActionsHelper.ApplyPower(p, p, new WeakPower(p, initialAmount, false), initialAmount);
        GameActionsHelper.ApplyPower(p, p, new FrailPower(p, initialAmount, false), initialAmount);
        this.flash();
    }
}