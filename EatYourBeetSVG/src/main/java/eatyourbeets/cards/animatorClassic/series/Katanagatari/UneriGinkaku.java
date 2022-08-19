package eatyourbeets.cards.animatorClassic.series.Katanagatari;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.AnimatedSlashEffect;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.animator.EarthenThornsPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class UneriGinkaku extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(UneriGinkaku.class).SetAttack(1, CardRarity.COMMON);

    public UneriGinkaku()
    {
        super(DATA);

        Initialize(13, 0, 3);
        SetUpgrade(4, 0, 1);
        SetScaling(0, 3, 0);

        SetEthereal(true);
        SetSeries(CardSeries.Katanagatari);
        SetMartialArtist();
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        boolean playable = super.cardPlayable(m);
        if (playable && !isInAutoplay)
        {
            for (AbstractCard card : AbstractDungeon.actionManager.cardsPlayedThisTurn)
            {
                if (card.type == CardType.ATTACK)
                {
                    return false;
                }
            }
        }

        return playable;
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.StackPower(new EarthenThornsPower(player, magicNumber));
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE)
        .SetDamageEffect(enemy ->
        {
            float wait = GameEffects.List.Add(new AnimatedSlashEffect(enemy.hb.cX, enemy.hb.cY - 30f * Settings.scale,
                    500f, 200f, 290f, 3f, Color.LIGHT_GRAY.cpy(), Color.RED.cpy())).duration;
            wait += GameEffects.Queue.Add(new AnimatedSlashEffect(enemy.hb.cX, enemy.hb.cY - 60f * Settings.scale,
                    500f, 200f, 290f, 5f, Color.DARK_GRAY.cpy(), Color.BLACK.cpy())).duration;
            SFX.Play(SFX.ATTACK_REAPER);
            return wait * 0.65f;
        });
    }
}