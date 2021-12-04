package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.ui.cards.TargetEffectPreview;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Mitsurugi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Mitsurugi.class)
            .SetAttack(0, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    private final TargetEffectPreview targetEffectPreview = new TargetEffectPreview(this::OnTargetChanged);
    private boolean showDamage = true;

    public Mitsurugi()
    {
        super(DATA);

        Initialize(8, 0, 2, 4);
        SetUpgrade(3, 0, 0, 0);

        SetAffinity_Red(2, 0, 1);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        if (showDamage)
        {
            return super.GetDamageInfo();
        }

        return null;
    }

    @Override
    public void update()
    {
        super.update();

        targetEffectPreview.Update();
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.GainBlock(secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.AddAffinity(Affinity.Red, magicNumber);

        if (GameUtilities.IsAttacking(m.intent))
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);
        }
    }

    private void OnTargetChanged(AbstractMonster monster)
    {
        showDamage = (monster == null || !GameUtilities.IsAttacking(monster.intent));
    }
}