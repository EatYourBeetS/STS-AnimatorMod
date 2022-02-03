package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.replacement.AntiArtifactSlowPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class AnarchyStocking extends PCLCard
{
    public static final PCLCardData DATA = Register(AnarchyStocking.class).SetSkill(1, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetColor(CardColor.COLORLESS).SetSeries(CardSeries.PantyStocking);

    public AnarchyStocking()
    {
        super(DATA);

        Initialize(0, 0, 6);
        SetUpgrade(0, 0, 0);

        SetAffinity_Light(1);
        SetAffinity_Dark(1);
        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    public void OnUpgrade() {
        SetHaste(true);
    }

    @Override
    public void triggerOnPurge() {
        int constricted = PCLGameUtilities.GetPowerAmount(ConstrictedPower.POWER_ID);
        if (constricted > 0) {
            PCLActions.Bottom.RemovePower(player, player, ConstrictedPower.POWER_ID);
            PCLActions.Bottom.ApplyConstricted(TargetHelper.Enemies(), constricted);
        }
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(new AntiArtifactSlowPower(m, 1));
        PCLActions.Bottom.ApplyPoison(TargetHelper.Player(), magicNumber);
        PCLActions.Bottom.ApplyConstricted(TargetHelper.Player(), magicNumber);
    }
}