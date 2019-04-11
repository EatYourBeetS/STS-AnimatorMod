package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.MoveSpecificCardAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class CrowleyEusford extends AnimatorCard
{
    public static final String ID = CreateFullID(CrowleyEusford.class.getSimpleName());

    public CrowleyEusford()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY);

        Initialize(11,0,7);

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        AbstractPlayer p = AbstractDungeon.player;

        GameActionsHelper.AddToBottom(new ModifyDamageAction(this.uuid, this.magicNumber));
        GameActionsHelper.AddToBottom(new MoveSpecificCardAction(this, p.drawPile, p.discardPile, true));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageRandomEnemy(p, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HEAVY);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
            upgradeMagicNumber(2);
        }
    }
}