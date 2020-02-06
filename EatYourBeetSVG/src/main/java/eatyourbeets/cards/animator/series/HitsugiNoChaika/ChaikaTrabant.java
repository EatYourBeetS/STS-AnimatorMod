package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

// TODO:
//"EXTENDED_DESCRIPTION":
//[
//"The Ripper", "Next turn, deal NL !D! Piercing NL damage.",
//"The Burner", "Deal !D! damage. NL Apply !M! Burning.",
//"The Intruder", "Deal !D! damage. NL Apply !M! Poison NL to ALL enemies.",
//"The Blinder", "Deal !D! damage. NL Apply !M! Weak.",
//"The Slugger ", "Next turn, deal !D! NL damage and apply NL !M! Vulnerable."
//],

public class ChaikaTrabant extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final EYBCardData DATA = Register(ChaikaTrabant.class).SetAttack(2, CardRarity.RARE, EYBAttackType.Elemental);

    private AbstractMonster target;

    public ChaikaTrabant()
    {
        super(DATA);

        Initialize(21, 0, 6, 2);
        SetUpgrade(7, 0, 0, 0);
        SetScaling(2, 0, 0);

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.DealDamageToAll(DamageInfo.createDamageMatrix(this.magicNumber, false),
        damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE).SetPiercing(true, false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        m.useFastShakeAnimation(0.5F);

        ChaikaTrabant other = (ChaikaTrabant) makeStatEquivalentCopy();
        other.target = m;
        PlayerStatistics.onStartOfTurnPostDraw.Subscribe(other);
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        if (target == null || GameUtilities.IsDeadOrEscaped(target))
        {
            target = GameUtilities.GetRandomEnemy(true);
        }

        GameEffects.Queue.ShowCardBriefly(makeStatEquivalentCopy());
        PlayerStatistics.onStartOfTurnPostDraw.Unsubscribe(this);

        this.applyPowers();
        this.calculateCardDamage(target);

        GameActions.Bottom.DealDamage(this, target, AbstractGameAction.AttackEffect.FIRE);
        GameActions.Bottom.ApplyWeak(player, target, 1);
        GameActions.Bottom.ApplyVulnerable(player, target, 1);
    }
}