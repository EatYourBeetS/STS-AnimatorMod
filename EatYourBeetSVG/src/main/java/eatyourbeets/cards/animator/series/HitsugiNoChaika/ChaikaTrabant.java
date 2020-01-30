package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.utilities.*;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;

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
    public static final String ID = Register(ChaikaTrabant.class);

    private AbstractMonster target;

    public ChaikaTrabant()
    {
        super(ID, 2, CardRarity.RARE, CardType.ATTACK, CardTarget.SELF_AND_ENEMY);

        Initialize(21, 0, 6, 2);
        SetUpgrade(6, 0, 0, 0);

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


    private static WeightedList<AbstractPower> GetRandomDebuffs(AbstractPlayer p, AbstractMonster m)
    {
        WeightedList<AbstractPower> result = new WeightedList<>();
        result.Add(new WeakPower(m, 1, false), 4);
        result.Add(new VulnerablePower(m, 1, false), 4);
        result.Add(new PoisonPower(m, p, 3), 3);
        result.Add(new ConstrictedPower(m, p, 2), 3);
        result.Add(new BurningPower(p, m, 3), 2);
        result.Add(new StrengthPower(m, -1), 2);

        if (m.type != AbstractMonster.EnemyType.BOSS)
        {
            result.Add(new StunMonsterPower(m, 1), 1);
        }

        return result;
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        if (target == null || target.isDeadOrEscaped())
        {
            target = GameUtilities.GetRandomEnemy(true);
        }

        AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(this.makeStatEquivalentCopy()));
        PlayerStatistics.onStartOfTurnPostDraw.Unsubscribe(this);

        this.applyPowers();
        this.calculateCardDamage(target);

        GameActions.Bottom.DealDamage(this, target, AbstractGameAction.AttackEffect.FIRE)
                .SetPiercing(true, false);

        WeightedList<AbstractPower> debuffs = GetRandomDebuffs(player, target);
        for (int i = 0; i < secondaryValue; i++)
        {
            AbstractPower debuff = debuffs.Retrieve(AbstractDungeon.cardRandomRng);
            GameActions.Bottom.ApplyPower(player, target, debuff, debuff.amount);
        }
    }
}