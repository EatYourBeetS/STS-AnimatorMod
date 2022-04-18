package eatyourbeets.relics.animator.unnamedReign;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class TheWolleyCore extends UnnamedReignRelic
{
    public static final String ID = AnimatorRelic.CreateFullID(TheWolleyCore.class);
    public static final int DAMAGE_AMOUNT = 7;
    public static final int TEMP_HP_AMOUNT = 2;
    public static final int FREQUENCY = 3;
    public static final int CARD_DRAW = 2;

    public TheWolleyCore()
    {
        super(ID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, CARD_DRAW, FREQUENCY, TEMP_HP_AMOUNT, DAMAGE_AMOUNT);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        GameActions.Bottom.Draw(CARD_DRAW);
    }

    @Override
    protected void ActivateBattleEffect()
    {
        super.ActivateBattleEffect();

        SetCounter(0);
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        SetCounter(-1);
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m)
    {
        super.onPlayCard(c, m);

        if (SetCounter(counter + 1) >= FREQUENCY)
        {
            GameActions.Bottom.GainTemporaryHP(TEMP_HP_AMOUNT);
            int[] damage = DamageInfo.createDamageMatrix(DAMAGE_AMOUNT, true);
            GameActions.Bottom.DealDamageToAll(damage, DamageInfo.DamageType.THORNS, AttackEffects.NONE)
            .SetVFX(true, true)
            .SetDamageEffect((enemy, mute) ->
            {
                GameEffects.List.Add(VFX.SmallExplosion(enemy.hb, 0.2f).PlaySFX(!mute));
                GameEffects.List.Add(VFX.SmallExplosion(enemy.hb, 0.2f).PlaySFX(false));
            });

            SetCounter(0);
            flash();
        }
    }

    @Override
    protected void OnManualEquip()
    {

    }

    public String GetTimeMazeString()
    {
        return " NL #y" + name.replace(" ", " #y") + " protects, increasing the card play limit by #b" + GetTimeMazeLimitIncrease() + ".";
    }

    public int GetTimeMazeLimitIncrease()
    {
        return 2;
    }
}