package eatyourbeets.powers.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.common.DrawSpecificCardAction;
import eatyourbeets.utilities.RandomizedList;
import eatyourbeets.interfaces.OnBlockBrokenSubscriber;

public class EmiyaShirouPower extends AnimatorPower implements OnBlockBrokenSubscriber
{
    public static final String POWER_ID = CreateFullID(EmiyaShirouPower.class.getSimpleName());

    public EmiyaShirouPower(AbstractCreature owner)
    {
        super(owner, POWER_ID);

        this.amount = 1;

        updateDescription();
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        GameActionsHelper.AddToTop(new VFXAction(new BorderFlashEffect(Color.ORANGE)));
        PlayerStatistics.onBlockBroken.Subscribe(this);
    }

    @Override
    public void OnBlockBroken(AbstractCreature creature)
    {
        if (!creature.isPlayer)
        {
            AbstractPlayer p = AbstractDungeon.player;
            CardGroup attacks = p.drawPile.getAttacks();
            if (attacks != null && attacks.size() > 0)
            {
                RandomizedList<AbstractCard> randomAttacks = new RandomizedList<>(attacks.group);

                for (int i = 0; i < amount; i++)
                {
                    AbstractCard card = randomAttacks.Retrieve(AbstractDungeon.cardRandomRng);
                    if (card != null)
                    {
                        GameActionsHelper.AddToBottom(new DrawSpecificCardAction(card));
                    }
                }
            }

            GameActionsHelper.GainEnergy(amount);

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