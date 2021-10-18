package eatyourbeets.cards.animator.series.Fate;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.AnimatedSlashEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Assassin extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Assassin.class)
            .SetAttack(0, CardRarity.UNCOMMON, EYBAttackType.Piercing)
            .SetSeriesFromClassPackage();

    public Assassin()
    {
        super(DATA);

        Initialize(4, 0, 3);
        SetUpgrade(1, 0);

        SetRetain(true);

        SetAffinity_Poison();
        SetAffinity_Dark();
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c)
    {
        if (c.type == CardType.ATTACK && player.hand.contains(this))
        {
            GameActions.Bottom.Exhaust(this);
        }
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        if (super.cardPlayable(m))
        {
            for (AbstractCard c : player.hand.group)
            {
                if (!GameUtilities.HasAffinity(c, Affinity.Light) && !(GameUtilities.HasAffinity(c, Affinity.Dark)))
                {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE)
            .SetDamageEffect(e -> DamageEffect(e, 0));
        GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE)
                .SetDamageEffect(e -> DamageEffect(e, 1));
        GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE)
                .SetDamageEffect(e -> DamageEffect(e, 2));
    }

    private float DamageEffect(AbstractCreature e, int index)
    {
        float x = e.hb.cX;
        float y = e.hb.cY - 60f * Settings.scale;
        float scale = 3;
        float dx;
        float dy;
        float angle;

        SFX.Play(SFX.ATTACK_SWORD, 0.85f + (0.2f * index));

        if (index == 0)
        {
            dx = 500;
            dy = 200;
            angle = 290;
        }
        else if (index == 1)
        {
            dx = -500;
            dy = 200;
            angle = -290;
        }
        else
        {
            dx = -500;
            dy = -200;
            angle = 120;
        }

        return GameEffects.List.Add(new AnimatedSlashEffect(x, y, dx, dy, angle, scale, Color.VIOLET, Color.TEAL)).duration * 0.4f;
    }
}