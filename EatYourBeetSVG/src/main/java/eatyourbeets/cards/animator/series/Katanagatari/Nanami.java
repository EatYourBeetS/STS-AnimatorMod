package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.misc.NanamiEffects.NanamiEffect;
import eatyourbeets.ui.cards.TargetEffectPreview;
import eatyourbeets.utilities.GameUtilities;

public class Nanami extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Nanami.class).SetSkill(-1, CardRarity.UNCOMMON);

    private TargetEffectPreview effectPreview = new TargetEffectPreview(this::ChangeEffect);
    private NanamiEffect currentEffect = null;

    public Nanami()
    {
        super(DATA);

        Initialize(5, 4, 3);
        SetUpgrade(1, 1, 1);

        SetAttackType(EYBAttackType.Normal);
        SetExhaust(true);
        SetSynergy(Synergies.Katanagatari);
        SetAlignment(2, 2, 0, 0, 2);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
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
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
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