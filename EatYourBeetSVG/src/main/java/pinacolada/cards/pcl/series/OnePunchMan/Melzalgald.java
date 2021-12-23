package pinacolada.cards.pcl.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.HPAttribute;
import pinacolada.cards.pcl.special.Melzalgald_1;
import pinacolada.cards.pcl.special.Melzalgald_2;
import pinacolada.cards.pcl.special.Melzalgald_3;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Melzalgald extends PCLCard
{
    public static final PCLCardData DATA = Register(Melzalgald.class)
            .SetAttack(3, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(new Melzalgald_1(), true);
                data.AddPreview(new Melzalgald_2(), true);
                data.AddPreview(new Melzalgald_3(), true);
            });

    public Melzalgald()
    {
        super(DATA);

        Initialize(18, 0, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Star(1, 0, 2);

        SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return heal <= 0 ? null : HPAttribute.Instance.SetCard(this, false).SetText(new ColoredString(heal, Colors.Cream(1f)));
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        this.heal = PCLGameUtilities.GetHealthRecoverAmount(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.RecoverHP(magicNumber);
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HEAVY);

        PCLActions.Bottom.MakeCardInHand(new Melzalgald_1()).SetUpgrade(upgraded, false);
        PCLActions.Bottom.MakeCardInHand(new Melzalgald_2()).SetUpgrade(upgraded, false);
        PCLActions.Bottom.MakeCardInHand(new Melzalgald_3()).SetUpgrade(upgraded, false);
    }
}