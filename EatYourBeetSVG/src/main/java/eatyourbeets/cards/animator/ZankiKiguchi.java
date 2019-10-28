package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class ZankiKiguchi extends AnimatorCard
{
    public static final String ID = Register(ZankiKiguchi.class.getSimpleName(), EYBCardBadge.Synergy);

    public ZankiKiguchi()
    {
        super(ID, 0, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(5,0,5);

        //AddExtendedDescription();

        SetSynergy(Synergies.Katanagatari);
    }

//    @Override
//    public void triggerOnManualDiscard()
//    {
//        super.triggerOnManualDiscard();
//
//        BuffCards();
//    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        return super.calculateModifiedCardDamage(player, mo, tmp + PlayerStatistics.GetDexterity(player));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        //GameActionsHelper.GainBlock(p, this.block);
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        //DamageAction damageAction = new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        //GameActionsHelper.AddToBottom(new OnDamageAction(m, damageAction, this::OnDamage, m.currentBlock, true));
        if (HasActiveSynergy() && p.currentBlock >= this.magicNumber)
        {
            GameActionsHelper.AddToBottom(new LoseBlockAction(p, p, this.magicNumber));
            GameActionsHelper.ApplyPower(p, p, new DexterityPower(p, 1), 1);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
            upgradeMagicNumber(1);
        }
    }

//    private void OnDamage(Object state, AbstractMonster monster)
//    {
//        Integer initialBlock = Utilities.SafeCast(state, Integer.class);
//        if (initialBlock != null && monster != null)
//        {
//            if (initialBlock > 0 && monster.currentBlock <= 0)
//            {
//                BuffCards();
//            }
//        }
//    }
//
//    private void BuffCards()
//    {
//        for (AbstractCard c : AbstractDungeon.player.drawPile.group)
//        {
//            if (c != this && c.baseDamage > 0)
//            {
//                GameActionsHelper.AddToBottom(new ModifyDamageAction(c.uuid, this.magicNumber));
//            }
//        }
//    }
}