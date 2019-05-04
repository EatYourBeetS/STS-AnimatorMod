package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.AnimatorAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.Synergies;

import java.util.ArrayList;

public class ShikizakiKiki extends AnimatorCard_UltraRare
{
    public static final String ID = CreateFullID(ShikizakiKiki.class.getSimpleName());

    public ShikizakiKiki()
    {
        super(ID, 3, CardType.SKILL, CardTarget.SELF);

        Initialize(0, 0, 2);

        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DrawCard(p, this.magicNumber);
        GameActionsHelper.AddToBottom(new ShikizakiKikiAction());
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }

    private class ShikizakiKikiAction extends AnimatorAction
    {

        @Override
        public void update()
        {
            for (AbstractCard card : AbstractDungeon.player.hand.group)
            {
                if (card.type == CardType.ATTACK)
                {
                    int newDamage = Math.min(9999, card.baseDamage * 2) - card.baseDamage;
                    if (newDamage > 0)
                    {
                        GameActionsHelper.AddToBottom(new ModifyDamageAction(card.uuid, newDamage));
                    }

                    card.flash();
                }
            }

            this.isDone = true;
        }
    }
}