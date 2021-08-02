package eatyourbeets.relics.animator.unnamedReign;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class TheWolleyCore extends UnnamedReignRelic
{
    public static final String ID = AnimatorRelic.CreateFullID(TheWolleyCore.class);
    public static final int DAMAGE_AMOUNT = 2;
    public static final int BLOCK_AMOUNT = 1;
    public static final int CARD_DRAW = 2;

    public TheWolleyCore()
    {
        super(ID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, CARD_DRAW, DAMAGE_AMOUNT, BLOCK_AMOUNT);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        GameActions.Bottom.Draw(CARD_DRAW);
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m)
    {
        super.onPlayCard(c, m);

        GameActions.Bottom.GainBlock(player, BLOCK_AMOUNT)
        .SetVFX(true, true);

        int[] damage = DamageInfo.createDamageMatrix(DAMAGE_AMOUNT, true);
        GameActions.Bottom.DealDamageToAll(damage, DamageInfo.DamageType.THORNS, AttackEffects.NONE)
        .SetVFX(true, true)
        .SetDamageEffect((enemy, mute) ->
        {
            if (!mute)
            {
                CardCrawlGame.sound.play("ATTACK_HEAVY");
                GameEffects.List.Add(new CleaveEffect());
            }
        });
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