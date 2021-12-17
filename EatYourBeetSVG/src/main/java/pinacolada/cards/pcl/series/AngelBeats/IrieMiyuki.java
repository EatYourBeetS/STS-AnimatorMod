package pinacolada.cards.pcl.series.AngelBeats;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;

public class IrieMiyuki extends PCLCard
{
    public static final PCLCardData DATA = Register(IrieMiyuki.class).SetSkill(1, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.Self).SetSeriesFromClassPackage();

    public IrieMiyuki()
    {
        super(DATA);

        Initialize(0, 3, 2, 5);
        SetUpgrade(0,0,0,0);
        SetAffinity_Blue(1);
        SetAffinity_Light(1, 0, 1);

        SetHealing(true);
        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this).SetText(GetXValue(), Settings.CREAM_COLOR);
    }

    @Override
    public int GetXValue() {
        return magicNumber + CombatStats.CardsExhaustedThisTurn().size() * magicNumber;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainWisdom(magicNumber);
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.GainTemporaryHP(GetXValue());

        if (info.IsSynergizing && PCLCombatStats.MatchingSystem.GetLastAffinitySynergy() == PCLAffinity.Blue && info.TryActivateLimited()) {
            PCLActions.Bottom.Heal(secondaryValue);
        }
    }
}