package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.ModifyMagicNumberAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.subscribers.OnBattleStartSubscriber;
import eatyourbeets.subscribers.OnAttackSubscriber;

public class ChaikaBohdan extends AnimatorCard implements OnBattleStartSubscriber, OnAttackSubscriber
{
    public static final String ID = CreateFullID(ChaikaBohdan.class.getSimpleName());

    private int bonusDamage = 0;
    //private boolean returnToHand = false;

    public ChaikaBohdan()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(6,0,2);

        if (PlayerStatistics.InBattle() && !CardCrawlGame.isPopupOpen)
        {
            OnBattleStart();
        }

        AddExtendedDescription();
        SetSynergy(Synergies.Chaika);
    }

//    @Override
//    public void onMoveToDiscard()
//    {
//        super.onMoveToDiscard();
//
//        if (returnToHand)
//        {
//            AbstractPlayer p = AbstractDungeon.player;
//            GameActionsHelper.AddToBottom(new MoveSpecificCardAction(this, p.hand, p.discardPile));
//            this.retain = true;
//            returnToHand = false;
//        }
//    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
//        DamageAction damageAction = new DamageAction(m, new DamageInfo(p, this.damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
//        GameActionsHelper.AddToBottom(new OnDamageAction(m, damageAction, this::OnDamage, m.currentBlock, true));

        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);

        if (HasActiveSynergy())
        {
            GameActionsHelper.AddToBottom(new ModifyMagicNumberAction(this.uuid, 1));
        }

        AddDamageBonus(-bonusDamage);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
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
                AddDamageBonus(this.magicNumber);
            }

            this.flash();
        }
    }

    private void AddDamageBonus(int amount)
    {
        bonusDamage += amount;
        baseDamage += amount;
    }

//    private void OnDamage(Object state, AbstractMonster monster)
//    {
//        Integer initialBlock = Utilities.SafeCast(state, Integer.class);
//        if (initialBlock != null && monster != null)
//        {
//            if (initialBlock > 0 && monster.currentBlock <= 0)
//            {
//                AbstractPlayer p = AbstractDungeon.player;
//                GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, 1), 1);
//                //returnToHand = true;
//            }
//        }
//    }
}