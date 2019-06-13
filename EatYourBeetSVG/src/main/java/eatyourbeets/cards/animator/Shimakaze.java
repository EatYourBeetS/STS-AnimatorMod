package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Shimakaze extends AnimatorCard
{
    public static final String ID = CreateFullID(Shimakaze.class.getSimpleName());

    public Shimakaze()
    {
        super(ID, 1, CardType.ATTACK, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(3,3, 3);

        SetSynergy(Synergies.Kancolle);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        GameActionsHelper.GainBlock(p, this.block);
        GameActionsHelper.DrawCard(p, this.magicNumber);
        GameActionsHelper.AddToBottom(new MakeTempCardInDrawPileAction(new Dazed(), 1, true, true));
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
            upgradeDamage(1);
            upgradeBlock(1);
        }
    }

//    private void OnCardDrawn(Object context, ArrayList<AbstractCard> cards)
//    {
//        AbstractMonster m = Utilities.SafeCast(context, AbstractMonster.class);
//
//        if (m != null && cards.size() > 0)
//        {
//            AbstractPlayer p = AbstractDungeon.player;
//            for (AbstractCard c : cards)
//            {
//                if (c.type == CardType.ATTACK)
//                {
//                    GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
//                }
//            }
//        }
//    }
}