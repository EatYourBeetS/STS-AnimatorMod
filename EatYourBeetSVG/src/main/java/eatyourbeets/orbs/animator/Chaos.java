package eatyourbeets.orbs.animator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.actions.basic.DrawCards;
import eatyourbeets.actions.handSelection.DiscardFromHand;
import eatyourbeets.actions.handSelection.ExhaustFromHand;
import eatyourbeets.actions.handSelection.SelectFromHand;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.vfx.OrbFlareEffect2;
import eatyourbeets.interfaces.subscribers.OnAfterCardPlayedSubscriber;
import eatyourbeets.orbs.AnimatorOrb;
import eatyourbeets.orbs.EYBOrb;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public class Chaos extends AnimatorOrb implements OnAfterCardPlayedSubscriber
{
    public static final String ORB_ID = CreateFullID(Chaos.class);

    public static Texture img1;
    public static Texture img2;
    public static Texture img3;

    private final boolean hFlip1;
    private AbstractOrb currentForm;

    public Chaos()
    {
        super(ORB_ID, Timing.StartOfTurnPostDraw);

        if (img1 == null)
        {
            img1 = ImageMaster.loadImage("images/orbs/animator/Chaos1.png");
            img2 = ImageMaster.loadImage("images/orbs/animator/Chaos2.png");
            img3 = ImageMaster.loadImage("images/orbs/animator/Chaos3.png");
        }

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

        CombatStats.onAfterCardPlayed.Subscribe(this);
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
                    orb = GameUtilities.GetRandomOrb();
                }
                while (orb.ID.equals(id));

                orb.cX = orb.tX = currentForm.tX;
                orb.cY = orb.tY = currentForm.tY;

                currentForm = orb;
                currentForm.setSlot(index, orbs.size());
                currentForm.playChannelSFX();
                if (currentForm instanceof EYBOrb)
                {
                    ((EYBOrb)currentForm).onChannel();
                }
                orbs.set(index, currentForm);
            }
        }
    }

    @Override
    public void onEndOfTurn()
    {
        super.onEndOfTurn();

        CombatStats.onAfterCardPlayed.Unsubscribe(this);
    }

    @Override
    public void updateDescription()
    {
        String[] desc = orbStrings.DESCRIPTION;
        this.applyFocus();
        this.description = desc[0];
    }

    @Override
    public void updateAnimation()
    {
        super.updateAnimation();

        this.angle += Gdx.graphics.getRawDeltaTime() * 60f; //180f;
    }

    @Override
    public void render(SpriteBatch sb)
    {
        sb.setColor(this.c);

        float by = bobEffect.y;
        sb.draw(img1, this.cX - 48f, this.cY - 48f, 48f, 48f, 96f, 96f, scale, scale, -2 * angle, 0, 0, 96, 96, hFlip1, false);
        sb.draw(img2, this.cX - 48f, this.cY - 48f, 48f, 48f, 96f, 96f, scale + (by * 0.03f), scale + (by * 0.03f), -angle, 0, 0, 96, 96, hFlip1, false);
        sb.draw(img3, this.cX - 48f, this.cY - 48f, 48f, 48f, 96f, 96f, scale + (by * 0.01f), scale + (by * 0.01f), 0.6f * angle, 0, 0, 96, 96, hFlip1, false);

        //this.renderText(sb);
        this.hb.render(sb);
    }

    @Override
    public void playChannelSFX()
    {
        SFX.Play(SFX.ORB_LIGHTNING_CHANNEL, 0.3f, 1.3f);
        SFX.Play(SFX.ORB_FROST_CHANNEL, 0.3f, 1.3f);
        SFX.Play(SFX.ORB_DARK_CHANNEL, 0.3f, 1.3f);
        SFX.Play(SFX.ORB_PLASMA_CHANNEL, 0.3f, 1.3f);
        SFX.Play(SFX.ATTACK_FIRE, 0.3f, 1.3f);
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

            playChannelSFX();
            GameActions.Last.Callback(this::ExecuteEffect);
        }
    }

    private void ExecuteEffect()
    {
        RandomizedList<AbstractGameAction> actions = new RandomizedList<>();
        actions.Add(new DiscardFromHand(name, 1, true).ShowEffect(true, true));
        actions.Add(new ExhaustFromHand(name, 1, true).ShowEffect(true, true));
        actions.Add(new DrawCards(1));
        actions.Add(new SelectFromHand(name, 1, true)
        .SetFilter(c -> !GameUtilities.IsCurseOrStatus(c))
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                GameActions.Top.PlayCard(cards.get(0), AbstractDungeon.player.hand, null);
            }
        }));
        GameActions.Bottom.WaitRealtime(0.3f);
        while (actions.Size() > 0)
        {
            GameActions.Bottom.Add(actions.Retrieve(GameUtilities.GetRNG(), true));
        }
    }

    @Override
    protected OrbFlareEffect2 GetOrbFlareEffect()
    {
        return super.GetOrbFlareEffect().SetColors(Color.PURPLE, Color.GREEN);
    }
}