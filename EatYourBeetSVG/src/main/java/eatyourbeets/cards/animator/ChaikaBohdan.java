package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.subscribers.OnBattleStartSubscriber;
import eatyourbeets.subscribers.OnAttackSubscriber;

public class ChaikaBohdan extends AnimatorCard implements OnBattleStartSubscriber, OnAttackSubscriber
{
    public static final String ID = CreateFullID(ChaikaBohdan.class.getSimpleName());

    private static final int DEFAULT_DAMAGE = 5;

    public ChaikaBohdan()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(DEFAULT_DAMAGE,0,2);
        this.misc = 0;

        if (PlayerStatistics.InBattle())
        {
            OnBattleStart();
        }

        AddExtendedDescription();
        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));

//        if (upgraded)
//        {
//            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
//        }

        SetDamageBonus(0);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
            upgradeDamage(2);
        }
    }

    @Override
    public void OnBattleStart()
    {
        PlayerStatistics.onAttack.Subscribe(this);
    }

    @Override
    public void OnAttack(DamageInfo info, int damageAmount, AbstractCreature target)
    {
        AbstractPlayer player = AbstractDungeon.player;
        if (player.hand.contains(this) && target instanceof AbstractMonster && info.owner == player && info.type != DamageInfo.DamageType.THORNS)
        {
            for (AbstractCard c : GetAllInBattleInstances.get(this.uuid))
            {
                SetDamageBonus(misc + this.magicNumber);
            }
            this.flash();
        }
    }

    private void SetDamageBonus(int newDamageBonus)
    {
        this.misc = newDamageBonus;
        baseDamage = DEFAULT_DAMAGE + misc;
        applyPowers();
        initializeDescription();
    }
}