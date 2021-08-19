package eatyourbeets.blights.animator;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.blights.AnimatorBlight;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.EYBPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class UltimateCubeBlight extends AnimatorBlight
{
    public static final String ID = CreateFullID(UltimateCubeBlight.class);
    public static final int DAMAGE_AMOUNT = 8;

    public int damageDealtThisTurn;

    public UltimateCubeBlight()
    {
        super(ID, DAMAGE_AMOUNT);

        setCounter(-1);
    }

    @Override
    public void update()
    {
        super.update();

        if (GR.UI.Elapsed50())
        {
            Tracker.ApplyToAllEnemies(this);
        }
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        setCounter(GameUtilities.InBattle(true) ? DAMAGE_AMOUNT : -1);
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        setCounter(-1);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        damageDealtThisTurn = 0;
        setCounter(DAMAGE_AMOUNT);
    }

    @Override
    public void setCounter(int counter)
    {
        super.setCounter(counter);

        if (this.pulse = (counter > 0))
        {
            this.flashTimer += 0.01f; // because updateFlash() checks for flashTimer != 0 ...
        }
    }

    @Override
    public void onPlayerEndTurn()
    {
        super.onPlayerEndTurn();

        if (counter > 0)
        {
            GameActions.Bottom.Callback(counter, (damage, __) ->
            {
                GameActions.Top.TakeDamage(damage, AttackEffects.RandomMagic()).SetSoundPitch(0.75f, 0.9f);
                GameActions.Top.WaitRealtime(0.25f);
                this.flash();
            });
        }

        setCounter(DAMAGE_AMOUNT);
    }

    public void OnDamageDealt(int damage)
    {
        setCounter(Math.max(0, DAMAGE_AMOUNT - (damageDealtThisTurn += damage)));
    }

    private static class Tracker extends EYBPower implements InvisiblePower
    {
        public static String POWER_ID = UltimateCubeBlight.ID + "TrackerPower";

        private UltimateCubeBlight blight;

        public static void ApplyToAllEnemies(UltimateCubeBlight blight)
        {
            for (AbstractMonster m : GameUtilities.GetEnemies(true))
            {
                if (!m.hasPower(POWER_ID))
                {
                    m.powers.add(new Tracker(blight, m));
                }
            }
        }

        public Tracker(UltimateCubeBlight blight, AbstractCreature owner)
        {
            super(owner, POWER_ID);

            this.blight = blight;
        }

        @Override
        public void wasHPLost(DamageInfo info, int damageAmount)
        {
            super.wasHPLost(info, damageAmount);

            if (damageAmount > 0 && info.type == DamageInfo.DamageType.NORMAL && GameUtilities.IsPlayer(info.owner))
            {
                blight.OnDamageDealt(damageAmount);
            }
        }
    }
}