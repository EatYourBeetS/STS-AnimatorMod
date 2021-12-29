package pinacolada.cards.pcl.series.MadokaMagica;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.special.IrohaTamaki_Giovanna;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class IrohaTamaki extends PCLCard
{
    public static final PCLCardData DATA = Register(IrohaTamaki.class)
            .SetAttack(1, CardRarity.COMMON, PCLAttackType.Electric)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new IrohaTamaki_Giovanna(), false));

    public IrohaTamaki()
    {
        super(DATA);

        Initialize(3, 5, 2, 2);
        SetUpgrade(0, 2, 1 ,1);

        SetAffinity_Blue(1);
        SetAffinity_Light(1, 0 ,1);

        SetAffinityRequirement(PCLAffinity.Light, 3);

        SetSoul(4, 0, IrohaTamaki_Giovanna::new);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_VERTICAL);

        if (info.IsSynergizing)
        {
            PCLActions.Bottom.Scry(magicNumber).AddCallback(cards -> {
                for (AbstractCard c : cards) {
                    if (PCLGameUtilities.IsHindrance(c)) {
                        PCLActions.Bottom.PlayCopy(c, m);
                        PCLActions.Last.Purge(c).ShowEffect(true);
                    }
                }
            });
        }
    }
}