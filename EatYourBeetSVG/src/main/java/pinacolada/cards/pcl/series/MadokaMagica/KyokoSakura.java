package pinacolada.cards.pcl.series.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.special.KyokoSakura_Ophelia;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class KyokoSakura extends PCLCard
{
    public static final PCLCardData DATA = Register(KyokoSakura.class)
            .SetAttack(1, CardRarity.COMMON, PCLAttackType.Piercing, eatyourbeets.cards.base.EYBCardTarget.Normal)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(new KyokoSakura_Ophelia(), true);
            });

    public KyokoSakura()
    {
        super(DATA);

        Initialize(9, 0, 1, 2);
        SetUpgrade(3, 0, 1);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(0,0,1);
        SetSoul(4, 0, KyokoSakura_Ophelia::new);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_VERTICAL);
        PCLActions.Bottom.ApplyBurning(TargetHelper.Normal(m), secondaryValue);
        PCLActions.Bottom.Exchange(name, magicNumber);

        cooldown.ProgressCooldownAndTrigger(m);
    }
}
