package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.common.OnTargetBlockLostAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Cocytus extends AnimatorCard
{
    public static final String ID = CreateFullID(Cocytus.class.getSimpleName());

    public Cocytus()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(7,0,1);

        AddExtendedDescription();

        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        DamageAction damageAction = new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        GameActionsHelper.AddToBottom(new OnTargetBlockLostAction(m, damageAction, new GainBlockAction(p, p, 0)));

        if (HasActiveSynergy())
        {
            GameActionsHelper.Callback(new WaitAction(0.1f), this::ImproveAttacks, this);
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

    private void ImproveAttacks(Object state, AbstractGameAction action)
    {
        for (AbstractCard c : AbstractDungeon.player.hand.group)
        {
            if (c.type == CardType.ATTACK)
            {
                GameActionsHelper.AddToBottom(new ModifyDamageAction(c.uuid, this.magicNumber));

                if (c != this)
                {
                    c.flash();
                }
            }
        }
    }
}