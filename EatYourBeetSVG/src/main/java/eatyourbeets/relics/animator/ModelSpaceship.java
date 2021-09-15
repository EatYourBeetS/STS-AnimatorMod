package eatyourbeets.relics.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

import java.util.ArrayList;

public class ModelSpaceship extends AnimatorRelic
{
    public static final String ID = CreateFullID(ModelSpaceship.class);
    public static final int GOLD_COST = 80;
    public static final int DAMAGE_AMOUNT = 3;

    public ModelSpaceship()
    {
        super(ID, RelicTier.BOSS, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, GOLD_COST, DAMAGE_AMOUNT);
    }

    @Override
    protected void ActivateBattleEffect()
    {
        super.ActivateBattleEffect();

        player.gameHandSize += 1;
        this.counter = GetAttackCount();
    }

    @Override
    protected void DeactivateBattleEffect()
    {
        super.DeactivateBattleEffect();

        player.gameHandSize -= 1;
        this.counter = -1;
    }

    @Override
    public void onPlayerEndTurn()
    {
        super.onPlayerEndTurn();

        counter = GetAttackCount();

        if (counter > 0)
        {
            GameEffects.Queue.BorderFlash(Color.ORANGE);

            for (int i = 0; i < counter; i++)
            {
                GameActions.Bottom.DealDamageToRandomEnemy(DAMAGE_AMOUNT, DamageInfo.DamageType.THORNS, AttackEffects.NONE)
                .SetOptions(true, false, false)
                .SetDamageEffect(enemy ->
                {
                    SFX.Play(SFX.ATTACK_MAGIC_BEAM_SHORT);
                    final Hitbox source = (player.relics.indexOf(this) / MAX_RELICS_PER_PAGE == relicPage) ? this.hb : player.hb;
                    GameEffects.List.Add(VFX.SmallLaser(source, enemy.hb, Color.ORANGE));
                    return 0f;
                });
            }
        }
    }

    @Override
    public void onRefreshHand()
    {
        super.onRefreshHand();

        this.counter = GetAttackCount();
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        player.loseGold(GOLD_COST);
    }

    @Override
    public boolean canSpawn()
    {
        return super.canSpawn() && player.gold >= GOLD_COST;
    }

    protected int GetAttackCount()
    {
        int count = 0;
        final ArrayList<AbstractCard> cards = player.hand.group;
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < cards.size(); i++)
        {
            if (cards.get(i).type == AbstractCard.CardType.ATTACK)
            {
                count += 1;
            }
        }

        return count;
    }
}