package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

import java.util.ArrayList;

public class MaesHughes extends AnimatorCard
{
    public static final String ID = CreateFullID(MaesHughes.class.getSimpleName());

    public MaesHughes()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(12,0,2);

        this.isMultiDamage = true;

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActionsHelper.DamageAllEnemies(AbstractDungeon.player, this.multiDamage, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE);
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