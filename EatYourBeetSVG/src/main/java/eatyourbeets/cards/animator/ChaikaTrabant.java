package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.misc.WeightedList;
import eatyourbeets.powers.BurningPower;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.subscribers.OnStartOfTurnPostDrawSubscriber;

public class ChaikaTrabant extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final String ID = CreateFullID(ChaikaTrabant.class.getSimpleName());

    private AbstractMonster target;

    public ChaikaTrabant()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.SELF_AND_ENEMY);

        Initialize(13,9, 2);

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.GainBlock(p, this.block);

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
            target = Utilities.GetRandomElement(PlayerStatistics.GetCurrentEnemies(true));
        }

        AbstractPlayer p = AbstractDungeon.player;
        AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(this.makeStatEquivalentCopy()));
        PlayerStatistics.onStartOfTurnPostDraw.Unsubscribe(this);

        this.applyPowers();

        GameActionsHelper.DamageTargetPiercing(p, target, this, AbstractGameAction.AttackEffect.FIRE).bypassBlock = false;

        WeightedList<AbstractPower> debuffs = GetRandomDebuffs(p, target);
        for (int i = 0; i < magicNumber; i++)
        {
            AbstractPower debuff = debuffs.Retrieve(AbstractDungeon.miscRng);
            GameActionsHelper.ApplyPower(p, target, debuff, debuff.amount);
        }
    }
}