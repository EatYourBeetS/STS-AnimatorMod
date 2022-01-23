package pinacolada.cards.pcl.series.MadokaMagica;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.special.NagisaMomoe_Charlotte;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class NagisaMomoe extends PCLCard
{
    public static final PCLCardData DATA = Register(NagisaMomoe.class)
            .SetSkill(0, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .SetMultiformData(2, false)
            .PostInitialize(data ->
            {
                data.AddPreview(new NagisaMomoe_Charlotte(), true);
            });

    public NagisaMomoe()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Star(1);

        SetHealing(true);
        SetEthereal(true);
        SetExhaust(true);
        SetSoul(1, 0, NagisaMomoe_Charlotte::new);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (form == 1) {
            Initialize(0, 0, 1, 3);
            SetUpgrade(0, 0, 0, 0);
        }
        else {
            Initialize(0, 0, 2, 2);
            SetUpgrade(0, 0, 0, 0);
        }
        return super.SetForm(form, timesUpgraded);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainWisdom(magicNumber);
        PCLActions.Bottom.GainInvocation(magicNumber);

        cooldown.ProgressCooldownAndTrigger(m);
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c)
    {
        super.triggerOnOtherCardPlayed(c);

        if (player.hand.contains(this) && PCLGameUtilities.HasLightAffinity(c))
        {
            PCLActions.Bottom.GainTemporaryHP(secondaryValue);
            PCLActions.Bottom.Flash(this);
        }
    }
}