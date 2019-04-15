package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.DrawSpecificCardAction;
import eatyourbeets.cards.AnimatorCard_Cooldown;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.StolenGoldPower;

public class Chris extends AnimatorCard_Cooldown
{
    public static final String ID = CreateFullID(Chris.class.getSimpleName());

    public Chris()
    {
        super(ID, 0, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(4,0, 4);

        AddExtendedDescription();

        SetSynergy(Synergies.Konosuba);
    }

//    @Override
//    public void triggerOnExhaust()
//    {
//        super.triggerOnExhaust();
//
//        if (AbstractDungeon.player.gold >= 300)
//        {
//            AbstractDungeon.player.loseGold(50);
//            GameActionsHelper.AddToBottom(new MakeTempCardInHandAction(new Eris()));
//        }
//    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (m != null)
        {
            GameActionsHelper.ApplyPower(p, m, new StolenGoldPower(m, this.magicNumber), this.magicNumber);
        }

        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);

        if (ProgressCooldown())
        {
            OnCooldownCompleted(p, m);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
            upgradeMagicNumber(2);
        }
    }

    @Override
    protected int GetBaseCooldown()
    {
        return 1;
    }

    @Override
    protected void OnCooldownCompleted(AbstractPlayer p, AbstractMonster m)
    {
        for (AbstractCard c : p.drawPile.group)
        {
            if (c.costForTurn == 0 && c.type != CardType.CURSE && c.type != CardType.STATUS)
            {
                GameActionsHelper.AddToBottom(new DrawSpecificCardAction(c));
                return;
            }
        }
    }
}