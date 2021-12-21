package pinacolada.cards.pcl.series.Bleach;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.misc.GenericEffects.GenericEffect_ApplyToAll;
import pinacolada.misc.GenericEffects.GenericEffect_StackPower;
import pinacolada.powers.PowerHelper;
import pinacolada.stances.MightStance;
import pinacolada.utilities.PCLActions;

public class IsshinKurosaki extends PCLCard
{
    public static final PCLCardData DATA = Register(IsshinKurosaki.class).SetAttack(2, CardRarity.UNCOMMON).SetSeriesFromClassPackage();
    private static final CardEffectChoice choices = new CardEffectChoice();

    public IsshinKurosaki()
    {
        super(DATA);

        Initialize(4, 7, 2, 4);
        SetUpgrade(0, 3, 0);
        SetAffinity_Red(2, 0, 2);

        SetAffinityRequirement(PCLAffinity.Red, 6);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        PCLActions.Bottom.GainBlock(block);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        makeChoice(m);

        if (TrySpendAffinity(PCLAffinity.Red) || MightStance.IsActive())
        {
            makeChoice(m);
        }
    }

    private void makeChoice(AbstractMonster m) {
        if (choices.TryInitialize(this))
        {
            choices.AddEffect(new GenericEffect_ApplyToAll(TargetHelper.Enemies(), PowerHelper.Burning, magicNumber));
            choices.AddEffect(new GenericEffect_StackPower(PowerHelper.CounterAttack, secondaryValue));
        }
        choices.Select(1, m);
    }
}