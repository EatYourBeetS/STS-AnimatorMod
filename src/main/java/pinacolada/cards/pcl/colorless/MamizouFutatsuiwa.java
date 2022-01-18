package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.cards.pcl.special.MamizouFutatsuiwa_Daruma;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class MamizouFutatsuiwa extends PCLCard
{
    public static final PCLCardData DATA = Register(MamizouFutatsuiwa.class)
            .SetSkill(1, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.TouhouProject)
            .PostInitialize(data ->
            {
                data.AddPreview(new MamizouFutatsuiwa_Daruma(0, 0), true);
                data.AddPreview(new MamizouFutatsuiwa_Daruma(1, 0), true);
                data.AddPreview(new MamizouFutatsuiwa_Daruma(2, 0), true);
            });


    public MamizouFutatsuiwa()
    {
        super(DATA);

        Initialize(0, 0, 2, 3);
        SetUpgrade(0, 0, 3);

        SetAffinity_Star(1, 0, 0);

        SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return (magicNumber > 0) ? TempHPAttribute.Instance.SetCard(this, true) : null;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainTemporaryHP(magicNumber);
        PCLActions.Bottom.StackPower(new MamizouFutatsuiwaPower(p, secondaryValue));
        if (PCLGameUtilities.GetCurrentMatchCombo() >= secondaryValue) {
            PCLActions.Bottom.MakeCardInDiscardPile(this.makeStatEquivalentCopy());
        }
    }

    public static class MamizouFutatsuiwaPower extends PCLPower
    {
        public MamizouFutatsuiwaPower(AbstractCreature owner, int amount)
        {
            super(owner, MamizouFutatsuiwa.DATA);

            Initialize(amount);
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            PCLActions.Bottom.MakeCardInHand(MamizouFutatsuiwa_Daruma.GetRandomCard());
            ReducePower(1);
        }
    }
}
