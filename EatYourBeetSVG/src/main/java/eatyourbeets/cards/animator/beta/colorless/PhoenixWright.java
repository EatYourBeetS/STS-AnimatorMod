package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.misc.CounterIntentEffects.CounterIntentEffect;
import eatyourbeets.ui.cards.TargetEffectPreview;
import eatyourbeets.utilities.GameUtilities;

public class PhoenixWright extends AnimatorCard
{
    public static final EYBCardData DATA = Register(PhoenixWright.class)
            .SetSkill(-1, CardRarity.UNCOMMON)
            .SetColor(CardColor.COLORLESS).SetSeries(CardSeries.PhoenixWright);

    private TargetEffectPreview effectPreview = new TargetEffectPreview(this::ChangeEffect);
    private CounterIntentEffect currentEffect = null;

    public PhoenixWright()
    {
        super(DATA);

        Initialize(5, 4, 3);
        SetUpgrade(1, 1, 1);

        SetAffinity_Orange(2);

        SetAttackType(EYBAttackType.Normal);
        SetExhaust(true);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (currentEffect != null)
        {
            currentEffect.OnDrag(m);
        }
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        if (currentEffect != null)
        {
            return currentEffect.GetDamageInfo(this);
        }

        return null;
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        if (currentEffect != null)
        {
            return currentEffect.GetBlockInfo(this);
        }

        return null;
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return (currentEffect != null) ? currentEffect.GetSpecialInfo(this) : null;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo)
    {
        super.calculateCardDamage(mo);

        targetDrawScale = 1f;
        target_x = Settings.WIDTH * 0.4f;
        target_y = Settings.HEIGHT * 0.4f;
        effectPreview.SetCurrentTarget(mo);
    }

    @Override
    public void update()
    {
        super.update();

        effectPreview.Update();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        energyOnUse = GameUtilities.UseXCostEnergy(this);
        CounterIntentEffect.GetEffect(m).EnqueueActions(this, p, m);
    }

    private void ChangeEffect(AbstractMonster enemy)
    {
        energyOnUse = -1;

        if (enemy != null)
        {
            energyOnUse = GameUtilities.GetXCostEnergy(this);
            currentEffect = CounterIntentEffect.GetEffect(enemy);
            cardText.OverrideDescription(currentEffect.GetDescription(this), true);
        }
        else
        {
            currentEffect = null;
            cardText.OverrideDescription(null, true);
        }
    }
}