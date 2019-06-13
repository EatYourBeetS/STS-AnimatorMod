package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

import java.util.ArrayList;

public class MaesHughes extends AnimatorCard
{
    public static final String ID = CreateFullID(MaesHughes.class.getSimpleName());

    public MaesHughes()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(12,0,2);

        this.damageType = this.damageTypeForTurn = DamageInfo.DamageType.THORNS;
        this.isMultiDamage = true;

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        applyPowers();

        if (this.multiDamage != null && this.multiDamage.length > 0)
        {
            GameActionsHelper.DamageAllEnemies(AbstractDungeon.player, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DrawCard(p, 1, this::OnCardDrawn, this);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(6);
            upgradeMagicNumber(1);
        }
    }

    private void OnCardDrawn(Object state, ArrayList<AbstractCard> cards)
    {
        if (state != this)
        {
            return;
        }

        if (cards != null && cards.size() > 0)
        {
            AbstractCard card = cards.get(0);
            if (card.costForTurn > 0)
            {
                card.setCostForTurn(card.costForTurn - this.magicNumber);
            }
        }
    }
}