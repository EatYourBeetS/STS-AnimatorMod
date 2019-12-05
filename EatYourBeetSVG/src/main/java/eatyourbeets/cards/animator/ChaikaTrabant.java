package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.WeightedList;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.interfaces.OnStartOfTurnPostDrawSubscriber;

public class ChaikaTrabant extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final String ID = Register(ChaikaTrabant.class.getSimpleName(), EYBCardBadge.Discard);

    private AbstractMonster target;

    public ChaikaTrabant()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.SELF_AND_ENEMY);

        Initialize(21,0, 6, 2);

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        AbstractPlayer p = AbstractDungeon.player;
        GameActionsHelper.DamageAllEnemies(p, DamageInfo.createDamageMatrix(this.magicNumber, false), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        //GameActionsHelper.GainBlock(p, this.block);
        m.useFastShakeAnimation(0.5F);

        ChaikaTrabant other = (ChaikaTrabant)makeStatEquivalentCopy();

        other.target = m;

        PlayerStatistics.onStartOfTurnPostDraw.Subscribe(other);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(5);
        }
    }

    private static WeightedList<AbstractPower> GetRandomDebuffs(AbstractPlayer p, AbstractMonster m)
    {
        WeightedList<AbstractPower> result = new WeightedList<>();
        result.Add(new WeakPower(m, 1, false), 4);
        result.Add(new VulnerablePower(m, 1, false), 4);
        result.Add(new PoisonPower(m, p, 3), 3);
        result.Add(new ConstrictedPower(m, p, 2), 3);
        result.Add(new BurningPower(m, p, 3), 2);
        result.Add(new StrengthPower(m, -1), 2);
        result.Add(new StunMonsterPower(m, 1), 1);

        return result;
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        if (target == null || target.isDeadOrEscaped())
        {
            target = JavaUtilities.GetRandomElement(GameUtilities.GetCurrentEnemies(true));
        }

        AbstractPlayer p = AbstractDungeon.player;
        AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(this.makeStatEquivalentCopy()));
        PlayerStatistics.onStartOfTurnPostDraw.Unsubscribe(this);

        this.applyPowers();
        this.calculateCardDamage(target);

        GameActionsHelper.DamageTargetPiercing(p, target, this, AbstractGameAction.AttackEffect.FIRE).bypassBlock = false;

        WeightedList<AbstractPower> debuffs = GetRandomDebuffs(p, target);
        for (int i = 0; i < secondaryValue; i++)
        {
            AbstractPower debuff = debuffs.Retrieve(AbstractDungeon.cardRandomRng);
            GameActionsHelper.ApplyPower(p, target, debuff, debuff.amount);
        }
    }
}