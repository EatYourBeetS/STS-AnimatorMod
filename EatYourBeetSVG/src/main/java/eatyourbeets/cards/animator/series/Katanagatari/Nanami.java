package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.effects.NanamiEffects.NanamiEffect;
import eatyourbeets.ui.cards.TargetEffectPreview;
import eatyourbeets.utilities.GameUtilities;

public class Nanami extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Nanami.class)
            .SetSkill(X_COST, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    private TargetEffectPreview effectPreview = new TargetEffectPreview(this::ChangeEffect);
    private NanamiEffect currentEffect = null;

    public Nanami()
    {
        super(DATA);

        Initialize(5, 4, 3);
        SetUpgrade(1, 1, 1);

        SetAffinity_Red(2);
        SetAffinity_Green(2);
        SetAffinity_Dark(2);

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
        return currentEffect != null ? currentEffect.GetDamageInfo(this) : null;
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return currentEffect != null ? currentEffect.GetBlockInfo(this) : null;
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
        NanamiEffect.GetEffect(m).EnqueueActions(this, p, m);
    }

    private void ChangeEffect(AbstractMonster enemy)
    {
        energyOnUse = -1;

        if (enemy != null)
        {
            energyOnUse = GameUtilities.GetXCostEnergy(this);
            currentEffect = NanamiEffect.GetEffect(enemy);
            cardText.OverrideDescription(currentEffect.GetDescription(this), true);
        }
        else
        {
            currentEffect = null;
            cardText.OverrideDescription(null, true);
        }
    }
}