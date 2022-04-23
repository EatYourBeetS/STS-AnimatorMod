package eatyourbeets.cards.animator.series.MadokaMagica;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.*;

public class IrohaTamaki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(IrohaTamaki.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged, EYBCardTarget.Random)
            .SetSeriesFromClassPackage();

    public IrohaTamaki()
    {
        super(DATA);

        Initialize(2, 0, 4, 2);
        SetUpgrade(1, 0, 0, 0);

        SetAffinity_Green(1);
        SetAffinity_Light(1, 1, 0);

        SetAffinityRequirement(Affinity.Light, 2);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    protected void UpdateDamage(float amount)
    {
        super.UpdateDamage(amount);

        if (damage != baseDamage)
        {
            amount = Mathf.Lerp(damage, baseDamage, 0.5f);

            if (damage < (baseDamage + 1))
            {
                damage = Mathf.CeilToInt(amount);
            }
            else
            {
                damage = Mathf.FloorToInt(amount);
            }

            isDamageModified = (damage != baseDamage);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.SFX(SFX.ANIMATOR_ARROW);
            GameActions.Bottom.DealDamageToRandomEnemy(this, AttackEffects.NONE)
            .SetDamageEffect(c -> GameEffects.List.Add(VFX.ThrowDagger(c.hb, 0.3f).SetColor(Color.TAN)).duration * 0.1f)
            .SetDuration(0.05f, false);
        }

        if (TryUseAffinity(Affinity.Light))
        {
            GameActions.Bottom.SelectFromPile(name, 1, p.drawPile)
            .SetFilter(GameUtilities::IsHindrance)
            .SetOptions(CardSelection.Top, true)
            .AddCallback(cards ->
            {
                final int max = cards.size();
                for (int i = 0; i < max; i++)
                {
                    final AbstractCard c = cards.get(i);
                    if (!c.hasTag(HASTE))
                    {
                        GameUtilities.MakeEthereal(c);
                        GameUtilities.SetCardTag(c, HASTE, true);
                        GameEffects.List.ShowCopy(c, Settings.WIDTH * 0.25f + (i * AbstractCard.IMG_WIDTH * 0.6f), Settings.HEIGHT * 0.4f);
                    }
                }
            });
        }
    }
}