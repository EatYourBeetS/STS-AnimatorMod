package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Shichika_Kyotouryuu;
import eatyourbeets.cards.base.*;
import eatyourbeets.misc.GenericEffects.GenericEffect;
import eatyourbeets.powers.common.CounterAttackPower;
import eatyourbeets.stances.MightStance;
import eatyourbeets.stances.VelocityStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class Shichika extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shichika.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Shichika_Kyotouryuu(), true));

    private static final CardEffectChoice choices = new CardEffectChoice();

    public Shichika()
    {
        super(DATA);

        Initialize(0, 1, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(2, 0, 1);
        SetAffinity_Green(2);

        SetAffinityRequirement(Affinity.Red, 3);
        SetAffinityRequirement(Affinity.Green, 3);

        SetProtagonist(true);
        SetHarmonic(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        choices.Initialize(this, true);
        choices.AddEffect(new GenericEffect_Force(this));
        choices.AddEffect(new GenericEffect_Agility(this));
        choices.Select(1, m);

        if (costForTurn > 0 && info.CanActivateLimited && TrySpendAffinity(Affinity.Red, Affinity.Green) && info.TryActivateLimited()) {
            GameActions.Bottom.GainEnergy(costForTurn);
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
            return JUtils.Format(Shichika.DATA.Strings.EXTENDED_DESCRIPTION[0], kyotouryuu.name);
        }

        @Override
        public void Use(AnimatorCard card, AbstractPlayer p, AbstractMonster m)
        {
            GameActions.Bottom.ChangeStance(MightStance.STANCE_ID);
            GameActions.Bottom.MakeCardInHand(kyotouryuu);
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
            return JUtils.Format(Shichika.DATA.Strings.EXTENDED_DESCRIPTION[1], shichika.magicNumber);
        }

        @Override
        public void Use(AnimatorCard card, AbstractPlayer p, AbstractMonster m)
        {
            GameActions.Bottom.ChangeStance(VelocityStance.STANCE_ID);
            GameActions.Bottom.StackPower(new CounterAttackPower(p, shichika.magicNumber));
        }
    }
}