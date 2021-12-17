package pinacolada.powers.deprecated;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import eatyourbeets.interfaces.subscribers.OnBlockBrokenSubscriber;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class EmiyaShirouPower extends PCLPower implements OnBlockBrokenSubscriber
{
    public static final String POWER_ID = CreateFullID(EmiyaShirouPower.class);

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

        PCLActions.Top.VFX(new BorderFlashEffect(Color.ORANGE));

        PCLCombatStats.onBlockBroken.Subscribe(this);
    }

    @Override
    public void OnBlockBroken(AbstractCreature creature)
    {
        if (!creature.isPlayer)
        {
            CardGroup attacks = player.drawPile.getAttacks();
            if (attacks != null && attacks.size() > 0)
            {
                RandomizedList<AbstractCard> randomAttacks = new RandomizedList<>(attacks.group);

                for (int i = 0; i < amount; i++)
                {
                    AbstractCard card = randomAttacks.Retrieve(rng);
                    if (card != null)
                    {
                        PCLActions.Bottom.Draw(1).ShuffleIfEmpty(true)
                        .SetFilter(c -> c.type == AbstractCard.CardType.ATTACK, false);
                    }
                }
            }

            PCLActions.Bottom.GainEnergy(amount);

            flash();
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        RemovePower();

        super.atEndOfTurn(isPlayer);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        PCLCombatStats.onBlockBroken.Unsubscribe(this);
    }
}