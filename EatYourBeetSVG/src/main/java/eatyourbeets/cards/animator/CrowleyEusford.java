package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.DamageRandomEnemy2Action;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class CrowleyEusford extends AnimatorCard
{
    public static final String ID = CreateFullID(CrowleyEusford.class.getSimpleName());

    public CrowleyEusford()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY);

        Initialize(14,0);

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        if (costForTurn > 0 && PlayerStatistics.getCardsExhaustedThisTurn() > 0)
        {
            this.setCostForTurn(0);
            this.flash();
        }
    }

//    @Override
//    public void triggerOnManualDiscard()
//    {
//        super.triggerOnManualDiscard();
//
//        AbstractPlayer p = AbstractDungeon.player;
//
//        GameActionsHelper.AddToBottom(new ModifyDamageAction(this.uuid, this.magicNumber));
//        GameActionsHelper.AddToBottom(new MoveSpecificCardAction(this, p.drawPile, p.discardPile, true));
//    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        DamageInfo info = new DamageInfo(p, this.baseDamage, this.damageTypeForTurn);
        DamageRandomEnemy2Action action = new DamageRandomEnemy2Action(info, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActionsHelper.AddToBottom(action);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(4);
        }
    }
}