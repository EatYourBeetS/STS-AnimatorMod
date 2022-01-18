package pinacolada.cards.pcl.series.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class HousakiMinori extends PCLCard
{
    public static final PCLCardData DATA = Register(HousakiMinori.class)
            .SetSkill(1, CardRarity.COMMON, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new HousakiTohya(), false));

    public HousakiMinori()
    {
        super(DATA);

        Initialize(0, 6, 1, 2);
        SetUpgrade(0, 1, 0);

        SetAffinity_Blue(1);
        SetAffinity_Light(1, 0, 1);

        SetCooldown(4, -1, this::OnCooldownCompleted);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        cooldown.ProgressCooldownAndTrigger(info.GetPreviousCardID().equals(HousakiTohya.DATA.ID) ? 3 : 1, m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        PCLActions.Bottom.GainArtifact(magicNumber);
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        final AbstractCard last = PCLGameUtilities.GetLastCardPlayed(true);
        return last != null && HousakiTohya.DATA.ID.equals(last.cardID);
    }
}