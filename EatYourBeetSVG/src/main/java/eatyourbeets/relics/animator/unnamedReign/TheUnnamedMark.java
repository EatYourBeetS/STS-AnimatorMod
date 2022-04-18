package eatyourbeets.relics.animator.unnamedReign;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.interfaces.listeners.OnEquipUnnamedReignRelicListener;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.*;

import java.util.ArrayList;

public class TheUnnamedMark extends AnimatorRelic implements OnEquipUnnamedReignRelicListener
{
    public static final String ID = AnimatorRelic.CreateFullID(TheUnnamedMark.class);
    public static final int MAX_MARKS = 6;
    public static final int ENEMY_BONUS = 2;
    public static final PowerHelper[] POWERS = new PowerHelper[MAX_MARKS];
    static
    {
        POWERS[0] = PowerHelper.Flight;
        POWERS[1] = PowerHelper.Strength;
        POWERS[2] = PowerHelper.Metallicize;
        POWERS[3] = PowerHelper.PlatedArmor;
        POWERS[4] = PowerHelper.Artifact;
        POWERS[5] = PowerHelper.Thorns;
    }

    private final ArrayList<AbstractMonster> newEnemies = new ArrayList<>();

    public TheUnnamedMark()
    {
        this(AbstractDungeon.player == null ? 0 : Math.min(MAX_MARKS, JUtils.Count(AbstractDungeon.player.relics, TheUnnamedMark.class::isInstance)));
    }

    public TheUnnamedMark(int index)
    {
        super(ID, RelicTier.SPECIAL, LandingSound.MAGICAL);

        SetIndex(index);
    }

    @Override
    public void OnEquipUnnamedReignRelic()
    {

    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, ENEMY_BONUS, GetPower().Tooltip.title);
    }

    @Override
    public void atBattleStartPreDraw()
    {
        super.atBattleStartPreDraw();

        int index = 0;
        for (AbstractRelic relic : AbstractDungeon.player.relics)
        {
            if (TheUnnamedMark.ID.equals(relic.relicId))
            {
                if (relic == this)
                {
                    SetIndex(index);
                    return;
                }

                index += 1;
            }
        }
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        newEnemies.addAll(GameUtilities.GetEnemies(true));
    }

    @Override
    public void onSpawnMonster(AbstractMonster monster)
    {
        super.onSpawnMonster(monster);

        newEnemies.add(monster);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        if (newEnemies.size() > 0)
        {
            for (AbstractMonster m : newEnemies)
            {
                if (m.hasPower(MinionPower.POWER_ID))
                {
                    GameActions.Bottom.StackPower(TargetHelper.Source(m), GetPower(), ENEMY_BONUS);
                }
            }

            newEnemies.clear();
            flash();
        }
    }

    public PowerHelper GetPower()
    {
        counter = Mathf.Clamp(counter, 1, POWERS.length);
        return POWERS[counter - 1];
    }

    public void SetIndex(int index)
    {
        setCounter(Mathf.Clamp(index, 0, POWERS.length) + 1);
        updateDescription(true);
    }
}