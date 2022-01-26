package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.powers.special.ElementalMasteryPower;
import pinacolada.utilities.PCLActions;

public class Traveler_Wish extends PCLCard
{
    public static final PCLCardData DATA = Register(Traveler_Wish.class).SetSkill(0, CardRarity.SPECIAL, PCLCardTarget.None).SetColor(CardColor.COLORLESS);

    public Traveler_Wish()
    {
        super(DATA);

        Initialize(0, 0, 15, 5);
        SetUpgrade(0, 0, 10, 0);
        SetAffinity_Star(1);
        SetPermanentHaste(true);
        SetRetainOnce(true);
        SetPurge(true);
    }

    @Override
    public void OnUpgrade()
    {
        super.OnUpgrade();

        SetLoyal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainInvocation(1, upgraded);
        PCLActions.Bottom.StackPower(new ElementalMasteryPower(p, magicNumber));
    }
}