package eatyourbeets.cards.animator.series.Katanagatari;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.AnimatedSlashEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.MartialArtist;
import eatyourbeets.powers.animator.EarthenThornsPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class UneriGinkaku extends AnimatorCard implements MartialArtist
{
    public static final EYBCardData DATA = Register(UneriGinkaku.class).SetAttack(1, CardRarity.COMMON);

    public UneriGinkaku()
    {
        super(DATA);

        Initialize(13, 0, 3);
        SetUpgrade(4, 0, 1);
        SetScaling(0, 3, 0);

        //SetExhaust(true);
        SetEthereal(true);
        SetSynergy(Synergies.Katanagatari);
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
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE).SetDamageEffect(enemy ->
        {
            GameEffects.List.Add(new AnimatedSlashEffect(enemy.hb.cX, enemy.hb.cY - 30.0F * Settings.scale,
            500.0F, 200.0F, 290.0F, 3.0F, Color.LIGHT_GRAY.cpy(), Color.RED.cpy()));
            GameEffects.Queue.Add(new AnimatedSlashEffect(enemy.hb.cX, enemy.hb.cY - 60.0F * Settings.scale,
            500.0F, 200.0F, 290.0F, 5.0F, Color.DARK_GRAY.cpy(), Color.BLACK.cpy()));
            CardCrawlGame.sound.play("ANIMATOR_REAPER");
        });
    }
}