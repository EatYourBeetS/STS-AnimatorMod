package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Gillette extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Gillette.class).SetAttack(1, CardRarity.COMMON);

    public Gillette()
    {
        super(DATA);

        Initialize(7, 0, 1);
        SetUpgrade(3, 0, 0);

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.ApplyWeak(TargetHelper.Enemies(), magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActions.Bottom.StackPower(new EnergizedPower(p, 1));
    }
}