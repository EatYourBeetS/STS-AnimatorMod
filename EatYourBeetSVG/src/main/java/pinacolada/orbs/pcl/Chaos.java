package pinacolada.orbs.pcl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.interfaces.subscribers.OnAfterCardPlayedSubscriber;
import eatyourbeets.interfaces.subscribers.OnEndOfTurnSubscriber;
import eatyourbeets.ui.TextureCache;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.effects.SFX;
import pinacolada.orbs.PCLOrb;
import pinacolada.powers.PCLCombatStats;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;

public class Chaos extends PCLOrb implements OnEndOfTurnSubscriber, OnAfterCardPlayedSubscriber
{
    public static final String ORB_ID = CreateFullID(Chaos.class);

    public static TextureCache img1 = IMAGES.Chaos1;
    public static TextureCache img2 = IMAGES.Chaos2;
    public static TextureCache img3 = IMAGES.Chaos3;

    private final boolean hFlip1;
    private AbstractOrb currentForm;

    public Chaos()
    {
        super(ORB_ID, Timing.StartOfTurnPostDraw);

        this.hFlip1 = MathUtils.randomBoolean();
        this.baseEvokeAmount = this.evokeAmount = 1;
        this.basePassiveAmount = this.passiveAmount = 1;
        this.currentForm = this;

        this.updateDescription();
        this.channelAnimTimer = 0.5f;
    }

    @Override
    public void onChannel()
    {
        super.onChannel();

        PCLCombatStats.onEndOfTurn.Subscribe(this);
        PCLCombatStats.onAfterCardPlayed.Subscribe(this);
    }

    @Override
    public void OnAfterCardPlayed(AbstractCard card)
    {
        if (card.type == AbstractCard.CardType.ATTACK)
        {
            ArrayList<AbstractOrb> orbs = AbstractDungeon.player.orbs;
            int index = orbs.indexOf(currentForm);
            if (index >= 0)
            {
                AbstractOrb orb;
                String id = currentForm.ID;
                do
                {
                    orb = PCLGameUtilities.GetRandomOrb();
                }
                while (orb.ID.equals(id));

                orb.cX = orb.tX = currentForm.tX;
                orb.cY = orb.tY = currentForm.tY;

                currentForm = orb;
                currentForm.setSlot(index, orbs.size());
                currentForm.playChannelSFX();
                if (currentForm instanceof PCLOrb)
                {
                    ((PCLOrb)currentForm).onChannel();
                }
                orbs.set(index, currentForm);
                AbstractDungeon.onModifyPower();
            }
        }
    }

    @Override
    public void OnEndOfTurn(boolean isPlayer)
    {
        PCLCombatStats.onEndOfTurn.Unsubscribe(this);
        PCLCombatStats.onAfterCardPlayed.Unsubscribe(this);
    }


    @Override
    public void updateAnimation()
    {
        super.updateAnimation();

        this.angle += GR.UI.Delta(60f);
    }

    @Override
    public void render(SpriteBatch sb)
    {
        sb.setColor(this.c);

        float by = bobEffect.y;
        sb.draw(img1.Texture(), this.cX - 48f, this.cY - 48f, 48f, 48f, 96f, 96f, scale, scale, -2 * angle, 0, 0, 96, 96, hFlip1, false);
        sb.draw(img2.Texture(), this.cX - 48f, this.cY - 48f, 48f, 48f, 96f, 96f, scale + (by * 0.03f), scale + (by * 0.03f), -angle, 0, 0, 96, 96, hFlip1, false);
        sb.draw(img3.Texture(), this.cX - 48f, this.cY - 48f, 48f, 48f, 96f, 96f, scale + (by * 0.01f), scale + (by * 0.01f), 0.6f * angle, 0, 0, 96, 96, hFlip1, false);

        //this.renderText(sb);
        this.hb.render(sb);
    }

    @Override
    public void playChannelSFX()
    {
        playChannelSFX(0.93f);
    }

    public void playChannelSFX(float volume)
    {
        SFX.Play(SFX.ORB_LIGHTNING_CHANNEL, 0.3f, 1.3f, volume);
        SFX.Play(SFX.ORB_FROST_CHANNEL, 0.3f, 1.3f, volume);
        SFX.Play(SFX.ORB_DARK_CHANNEL, 0.3f, 1.3f, volume);
        SFX.Play(SFX.ORB_PLASMA_CHANNEL, 0.3f, 1.3f, volume);
        SFX.Play(SFX.ATTACK_FIRE, 0.3f, 1.3f, volume);
    }

    @Override
    public void Evoke()
    {
        if (currentForm == this)
        {
            super.Evoke();

            ExecuteEffect();
        }
    }

    @Override
    public void Passive()
    {
        if (currentForm == this)
        {
            super.Passive();

            playChannelSFX(0.85f);
            PCLActions.Last.Callback(this::ExecuteEffect);
        }
    }

    private void ExecuteEffect()
    {
        final RandomizedList<AbstractCard> toDecrease = new RandomizedList<>();
        final RandomizedList<AbstractCard> toIncrease = new RandomizedList<>();
        for (AbstractCard c : AbstractDungeon.player.hand.group)
        {
            if (c.costForTurn >= 0)
            {
                toIncrease.Add(c);

                if (c.costForTurn > 0)
                {
                    toDecrease.Add(c);
                }
            }
        }

        if (toDecrease.Size() > 0)
        {
            final AbstractCard c = toDecrease.Retrieve(PCLGameUtilities.GetRNG(), true);
            PCLGameUtilities.ModifyCostForTurn(c, -1, true);
            PCLGameUtilities.Flash(c, true);
            toIncrease.Remove(c);
        }

        if (toIncrease.Size() > 0)
        {
            final AbstractCard c = toIncrease.Retrieve(PCLGameUtilities.GetRNG(), true);
            PCLGameUtilities.ModifyCostForTurn(c, +1, true);
            PCLGameUtilities.Flash(c, Colors.Red(1), true);;
        }
    }

    @Override
    protected Color GetColor1()
    {
        return Color.PURPLE;
    }

    @Override
    protected Color GetColor2()
    {
        return Color.GREEN;
    }
}