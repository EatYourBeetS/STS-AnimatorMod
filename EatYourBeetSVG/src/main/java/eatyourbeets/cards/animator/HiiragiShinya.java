package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.misc.WeightedList;
import eatyourbeets.powers.BurningPower;

public class HiiragiShinya extends AnimatorCard
{
    public static final String ID = CreateFullID(HiiragiShinya.class.getSimpleName());

    public HiiragiShinya()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(9,0, 3);

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.AddToBottom(new SFXAction("ATTACK_FIRE"));
        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE);

        int attacks = 0;
        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn)
        {
           if (c.type == CardType.ATTACK)
           {
               attacks += 1;
           }
        }

        if (attacks <= 1)
        {
            AbstractPower debuff = GetRandomDebuffs(p, m, upgraded).Retrieve(AbstractDungeon.miscRng);
            if (debuff != null && debuff.amount > 0)
            {
                GameActionsHelper.ApplyPower(p, m, debuff, debuff.amount);
            }
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
        }
    }

    private static WeightedList<AbstractPower> GetRandomDebuffs(AbstractPlayer p, AbstractMonster m, boolean upgraded)
    {
        WeightedList<AbstractPower> result = new WeightedList<>();
        result.Add(new WeakPower(m, upgraded ? 2 : 1, false), 5);
        result.Add(new VulnerablePower(m, upgraded ? 2 : 1, false), 5);
        result.Add(new PoisonPower(m, p, upgraded ? 6 : 4), 4);
        result.Add(new BurningPower(m, p, upgraded ? 3 : 2), 3);
        result.Add(new StunMonsterPower(m, 1), 1);
        result.Add(new LoseStrengthPower(m, upgraded ? 2 : 1), 1);

        return result;
    }
}