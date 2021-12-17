package pinacolada.cards.pcl.series.Konosuba;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.effects.AttackEffects;
import pinacolada.ui.cards.TargetEffectPreview;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Mitsurugi extends PCLCard
{
    public static final PCLCardData DATA = Register(Mitsurugi.class)
            .SetAttack(0, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    private final TargetEffectPreview targetEffectPreview = new TargetEffectPreview(this::OnTargetChanged);
    private boolean showDamage = true;

    public Mitsurugi()
    {
        super(DATA);

        Initialize(8, 0, 2, 4);
        SetUpgrade(3, 0, 0, 0);

        SetAffinity_Red(1, 0, 1);
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

        PCLActions.Bottom.GainBlock(secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.AddAffinity(PCLAffinity.Red, magicNumber);

        if (PCLGameUtilities.IsAttacking(m.intent))
        {
            PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HEAVY);
        }
    }

    private void OnTargetChanged(AbstractMonster monster)
    {
        showDamage = (monster == null || !PCLGameUtilities.IsAttacking(monster.intent));
    }
}