package eatyourbeets.powers;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.DrawSpecificCardAction;
import eatyourbeets.misc.RandomizedList;
import eatyourbeets.subscribers.OnBlockBrokenSubscriber;

public class EmiyaShirouPower extends AnimatorPower implements OnBlockBrokenSubscriber
{
    public static final String POWER_ID = CreateFullID(EmiyaShirouPower.class.getSimpleName());

    public EmiyaShirouPower(AbstractCreature owner)
    {
        super(owner, POWER_ID);
        this.amount = -1;

        updateDescription();
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        GameActionsHelper.AddToTop(new VFXAction(new BorderLongFlashEffect(Color.ORANGE)));
        PlayerStatistics.onBlockBroken.Subscribe(this);
    }

    @Override
    public void OnBlockBroken(AbstractCreature creature)
    {
        if (!creature.isPlayer)
        {
            AbstractPlayer p =  AbstractDungeon.player;
            CardGroup attacks = p.drawPile.getAttacks();
            if (attacks != null && attacks.size() > 0)
            {
                RandomizedList<AbstractCard> randomAttacks = new RandomizedList<>(attacks.group);

                AbstractCard card = randomAttacks.Retrieve(AbstractDungeon.miscRng);
                if (card != null)
                {
                    GameActionsHelper.AddToBottom(new DrawSpecificCardAction(card));
                }
            }

            GameActionsHelper.GainEnergy(1);
            flash();
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));

        super.atEndOfTurn(isPlayer);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        PlayerStatistics.onBlockBroken.Unsubscribe(this);
    }
}
