package pinacolada.cards.pcl.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCard_UltraRare;
import pinacolada.powers.PCLPower;
import pinacolada.powers.replacement.PlayerFlightPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Veldora extends PCLCard_UltraRare
{
    public static final PCLCardData DATA = Register(Veldora.class)
            .SetPower(3, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.TenseiSlime);

    public Veldora()
    {
        super(DATA);

        Initialize(0, 0, 3, 2);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Red(1);
        SetAffinity_Green(1);
        SetAffinity_Blue(1);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        for (int i = 0; i < secondaryValue; i++) {
            PCLActions.Bottom.StackPower(TargetHelper.RandomEnemy(), PCLGameUtilities.GetRandomElement(PCLGameUtilities.GetPCLCommonDebuffs()), secondaryValue)
                    .ShowEffect(false, true);
        }

        PCLActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (!p.hasPower(PlayerFlightPower.POWER_ID))
        {
            PCLActions.Bottom.StackPower(new PlayerFlightPower(p, 2));
        }

        PCLActions.Bottom.StackPower(new VeldoraPower(p, 1));
    }

    public class VeldoraPower extends PCLPower
    {
        public VeldoraPower(AbstractCreature owner, int amount)
        {
            super(owner, Veldora.DATA);

            Initialize(amount);
        }

        public void onApplyPower(AbstractPower p, AbstractCreature target, AbstractCreature source)
        {
            super.onApplyPower(p, target, source);

            if (p.type == PowerType.DEBUFF && !p.ID.equals(GainStrengthPower.POWER_ID) && source == owner && !target.hasPower(ArtifactPower.POWER_ID))
            {
                PCLActions.Bottom.GainRandomAffinityPower(1, true);
                this.flash();
            }
        }
    }
}