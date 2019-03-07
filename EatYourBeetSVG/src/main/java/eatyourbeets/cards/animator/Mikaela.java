package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.ExhaustFromPileAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Mikaela extends AnimatorCard
{
    public static final String ID = CreateFullID(Mikaela.class.getSimpleName());

    public Mikaela()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(8,0, 3);

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        //AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));

        GameActionsHelper.ApplyPower(p, m, new PoisonPower(m, p, this.magicNumber), this.magicNumber);

        if (p.discardPile.size() > 0)
        {
            AbstractDungeon.actionManager.addToBottom(new ExhaustFromPileAction(1, !upgraded, p.discardPile));
        }
    }

    @Override
    public void upgrade() 
    {
        TryUpgrade();
    }
}