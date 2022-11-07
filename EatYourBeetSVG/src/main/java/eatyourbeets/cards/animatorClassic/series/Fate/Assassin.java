package eatyourbeets.cards.animatorClassic.series.Fate;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.AnimatedSlashEffect;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Assassin extends AnimatorClassicCard
{
    private static int EFFECT = 0;

    public static final EYBCardData DATA = Register(Assassin.class).SetSeriesFromClassPackage().SetAttack(0, CardRarity.COMMON, EYBAttackType.Piercing);
    public static final int DEBUFFS_COUNT = 3;

    public Assassin()
    {
        super(DATA);

        Initialize(2, 0, DEBUFFS_COUNT);
        SetUpgrade(2, 0);
        SetScaling(0, 1, 0);

        SetRetain(true);
        SetMartialArtist();

    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c)
    {
        if (c.type == CardType.ATTACK && player.hand.contains(this))
        {
            GameActions.Bottom.MoveCard(this, player.hand, player.discardPile)
            .AddCallback(() -> GameActions.Bottom.Draw(1));
        }
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        if (super.cardPlayable(m))
        {
            if (m == null)
            {
                for (AbstractMonster enemy : GameUtilities.GetEnemies(true))
                {
                    if (GameUtilities.GetDebuffsCount(enemy.powers) >= DEBUFFS_COUNT)
                    {
                        return true;
                    }
                }
            }
            else
            {
                return GameUtilities.GetDebuffsCount(m) >= DEBUFFS_COUNT;
            }
        }

        return false;
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL)
        .SetDamageEffect(this::DamageEffect)
        .SetDuration(0.3f, false);

        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
        .SetDamageEffect(this::DamageEffect)
        .SetDuration(0.3f, false);

        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL)
        .SetDamageEffect(this::DamageEffect)
        .SetDuration(0.2f, false);
    }

    private float DamageEffect(AbstractCreature e)
    {
        float x = e.hb.cX;
        float y = e.hb.cY - 60f * Settings.scale;
        float scale = 3;
        float dx;
        float dy;
        float angle;

        if (EFFECT == 0)
        {
            dx = 500;
            dy = 200;
            angle = 290;

            EFFECT = 1;
        }
        else if (EFFECT == 1)
        {
            dx = -500;
            dy = 200;
            angle = -290;

            EFFECT = 2;
        }
        else
        {
            dx = -500;
            dy = -200;
            angle = -230;

            EFFECT = 0;
        }

        return GameEffects.List.Add(new AnimatedSlashEffect(x, y, dx, dy, angle, scale, Color.VIOLET.cpy(), Color.TEAL.cpy())).duration;
    }
}