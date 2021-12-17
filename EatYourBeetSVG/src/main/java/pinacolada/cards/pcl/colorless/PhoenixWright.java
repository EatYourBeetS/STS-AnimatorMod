package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.misc.CounterIntentEffects.CounterIntentEffect;
import pinacolada.ui.cards.TargetEffectPreview;
import pinacolada.utilities.PCLGameUtilities;

public class PhoenixWright extends PCLCard
{
    public static final PCLCardData DATA = Register(PhoenixWright.class)
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
        SetAffinity_Light(1);

        SetAttackType(PCLAttackType.Normal);
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
        energyOnUse = PCLGameUtilities.UseXCostEnergy(this);
        CounterIntentEffect.GetEffect(m).EnqueueActions(this, p, m);
    }

    private void ChangeEffect(AbstractMonster enemy)
    {
        energyOnUse = -1;

        if (enemy != null)
        {
            energyOnUse = PCLGameUtilities.GetXCostEnergy(this);
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