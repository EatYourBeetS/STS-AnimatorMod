package eatyourbeets.relics.animator.beta;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DemonFormPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class PhilosopherStonePlus extends AnimatorRelic
{
    public static final String ID = CreateFullID(PhilosopherStonePlus.class);

    public PhilosopherStonePlus()
    {
        super(ID, RelicTier.BOSS, LandingSound.CLINK);
    }

    public void atBattleStart() {

        for (AbstractMonster monster : GameUtilities.GetEnemies(true)) {
            Buff(monster);
        }

        AbstractDungeon.onModifyPower();
    }

    public void onSpawnMonster(AbstractMonster monster) {
        Buff(monster);
        AbstractDungeon.onModifyPower();
    }

    public void onEquip() {
        AbstractDungeon.player.energy.energyMaster += 2;
    }

    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster -= 2;
    }

    protected void Buff(AbstractMonster monster)
    {
        GameActions.Bottom.Callback(new RelicAboveCreatureAction(monster, this), monster, (m, __) ->
        {
            GameActions.Bottom.StackPower(new StrengthPower(m, 1));
            GameActions.Bottom.StackPower(new DemonFormPower(m, 1));
        });
    }
}