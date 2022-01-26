package pinacolada.cards.pcl.series.Katanagatari;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.base.cardeffects.GenericEffects.GenericEffect;
import pinacolada.cards.pcl.special.Shichika_Kyotouryuu;
import pinacolada.powers.common.CounterAttackPower;
import pinacolada.stances.pcl.MightStance;
import pinacolada.stances.pcl.VelocityStance;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class Shichika extends PCLCard
{
    public static final PCLCardData DATA = Register(Shichika.class)
            .SetSkill(1, CardRarity.UNCOMMON, PCLCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Shichika_Kyotouryuu(), true));

    private static final CardEffectChoice choices = new CardEffectChoice();

    public Shichika()
    {
        super(DATA);

        Initialize(0, 2, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(1, 0, 2);
        SetAffinity_Green(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.Red, 3);
        SetAffinityRequirement(PCLAffinity.Green, 3);

        SetProtagonist(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        choices.Initialize(this, true);
        choices.AddEffect(new GenericEffect_Force(this));
        choices.AddEffect(new GenericEffect_Agility(this));
        choices.Select(1, m);

        if (costForTurn > 0 && info.CanActivateLimited && TrySpendAffinity(PCLAffinity.Red, PCLAffinity.Green) && info.TryActivateLimited()) {
            PCLActions.Bottom.GainEnergy(costForTurn);
        }
    }

    protected static class GenericEffect_Force extends GenericEffect
    {
        private final AbstractCard kyotouryuu;

        public GenericEffect_Force(Shichika shichika)
        {
            this.kyotouryuu = Shichika_Kyotouryuu.DATA.CreateNewInstance(shichika.upgraded);
        }

        @Override
        public String GetText()
        {
            return PCLJUtils.Format(Shichika.DATA.Strings.EXTENDED_DESCRIPTION[0], kyotouryuu.name);
        }

        @Override
        public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
        {
            PCLActions.Bottom.ChangeStance(MightStance.STANCE_ID);
            PCLActions.Bottom.MakeCardInHand(kyotouryuu);
        }
    }

    protected static class GenericEffect_Agility extends GenericEffect
    {
        private final AbstractCard shichika;

        public GenericEffect_Agility(Shichika shichika)
        {
            this.shichika = shichika;
        }

        @Override
        public String GetText()
        {
            return PCLJUtils.Format(Shichika.DATA.Strings.EXTENDED_DESCRIPTION[1], shichika.magicNumber);
        }

        @Override
        public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
        {
            PCLActions.Bottom.ChangeStance(VelocityStance.STANCE_ID);
            PCLActions.Bottom.StackPower(new CounterAttackPower(p, shichika.magicNumber));
        }
    }
}