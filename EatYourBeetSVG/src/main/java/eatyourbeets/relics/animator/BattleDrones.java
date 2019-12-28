package eatyourbeets.relics.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.cards.base.AnimatorCard;

public class BattleDrones extends AnimatorRelic
{
    public static final String ID = CreateFullID(BattleDrones.class.getSimpleName());

    private static final int DAMAGE_AMOUNT = 3;
    private static final int BLOCK_AMOUNT = 1;

    public BattleDrones()
    {
        super(ID, RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return JavaUtilities.Format(DESCRIPTIONS[0], DAMAGE_AMOUNT, BLOCK_AMOUNT);
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m)
    {
        super.onPlayCard(c, m);

        AnimatorCard card = JavaUtilities.SafeCast(c, AnimatorCard.class);
        if (card != null && card.HasSynergy())
        {
            AbstractPlayer p = AbstractDungeon.player;
            GameActions.Bottom.Add(new GainBlockAction(p, p, BLOCK_AMOUNT, true));
            GameActions.Bottom.DealDamageToRandomEnemy(DAMAGE_AMOUNT, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE)
            .SetDamageEffect(enemy ->
            {
                CardCrawlGame.sound.play("ATTACK_MAGIC_BEAM_SHORT");
                GameEffects.List.Add(new SmallLaserEffect(enemy.hb.cX, enemy.hb.cY, p.hb.cX, p.hb.cY));
                GameEffects.List.Add(new BorderFlashEffect(Color.SKY));
            });

            this.flash();
        }
    }
}