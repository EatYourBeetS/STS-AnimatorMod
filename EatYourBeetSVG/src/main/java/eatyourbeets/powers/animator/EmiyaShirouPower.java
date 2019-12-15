package eatyourbeets.powers.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActions;
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

        GameActions.Top.VFX(new BorderFlashEffect(Color.ORANGE));

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
                        GameActions.Bottom.Draw(1).SetOptions(true)
                        .SetFilter(c -> c.type == AbstractCard.CardType.ATTACK, false);
                    }
                }
            }

            GameActions.Bottom.GainEnergy(amount);

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

        PlayerStatistics.onBlockBroken.Unsubscribe(this);
    }
}