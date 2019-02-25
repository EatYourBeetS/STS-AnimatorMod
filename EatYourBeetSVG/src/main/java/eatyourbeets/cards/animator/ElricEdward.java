package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.VariableDiscardAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

import java.util.ArrayList;

public class ElricEdward extends AnimatorCard
{
    public static final String ID = CreateFullID(ElricEdward.class.getSimpleName());

    public ElricEdward()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(5,0, 1);

        AddExtendedDescription();

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE);
        GameActionsHelper.AddToBottom(new VariableDiscardAction(p, this.magicNumber, this, this::OnCardDiscard));
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
        }
    }

    private void OnCardDiscard(Object state, ArrayList<AbstractCard> cards)
    {
        if (state != this || cards == null || cards.size() == 0)
        {
            return;
        }

        for (AbstractCard c : cards)
        {
            switch (c.type)
            {
                case ATTACK:
                    GameActionsHelper.ChannelOrb(new Lightning(), true);
                    break;

                case SKILL:
                    GameActionsHelper.ChannelOrb(new Frost(), true);
                    break;

                case POWER:
                    GameActionsHelper.ChannelOrb(new Dark(), true);
                    break;
            }
        }

        GameActionsHelper.DrawCard(AbstractDungeon.player, cards.size());
    }
}