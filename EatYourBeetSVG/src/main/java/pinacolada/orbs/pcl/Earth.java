package pinacolada.orbs.pcl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.interfaces.subscribers.OnRawDamageReceivedSubscriber;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.actions.orbs.EarthOrbEvokeAction;
import pinacolada.actions.orbs.EarthOrbPassiveAction;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.Projectile;
import pinacolada.effects.SFX;
import pinacolada.orbs.PCLOrb;
import pinacolada.powers.PCLCombatStats;
import pinacolada.resources.GR;
import pinacolada.ui.TextureCache;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;

public class Earth extends PCLOrb implements OnStartOfTurnPostDrawSubscriber, OnRawDamageReceivedSubscriber
{
    public static final String ORB_ID = CreateFullID(Earth.class);
    public static final int BASE_PROJECTILES = 6;
    public static final int MAX_PROJECTILES = 24;
    public static final int PROJECTILES_PER_TURN = 2;
    public static final int PROJECTILE_DAMAGE = 2;
    public static final int THORNS_PER_CHANNEL = 2;
    public static final int MAX_CHANNEL_EFFECTS = 3;

    private static final TextureCache[] images = { IMAGES.Earth1, IMAGES.Earth2, IMAGES.Earth3, IMAGES.Earth4 };
    private static final RandomizedList<TextureCache> textures = new RandomizedList<>();
    private float vfxTimer;

    public final ArrayList<Projectile> projectiles = new ArrayList<>();
    public boolean evoked;
    public int projectilesCount;
    public int turns;

    public static Texture GetRandomTexture()
    {
        if (textures.Size() <= 1) // Adds some randomness but still ensures all textures are cycled through
        {
            textures.AddAll(images);
        }

        return textures.RetrieveUnseeded(true).Texture();
    }

    public Earth()
    {
        super(ORB_ID, Timing.EndOfTurn);

        this.evoked = false;
        this.evokeAmount = this.baseEvokeAmount = PROJECTILE_DAMAGE;
        this.passiveAmount = this.basePassiveAmount = PROJECTILES_PER_TURN;
        this.projectilesCount = BASE_PROJECTILES;
        this.channelAnimTimer = 0.5f;
        this.turns = 3;

        this.updateDescription();
    }

    public void AddProjectiles(int amount)
    {
        final int max = Math.min(MAX_PROJECTILES - projectiles.size(), amount);
        for (int i = 0; i < max; i++)
        {
            projectiles.add(new Projectile(GetRandomTexture(), IMAGE_SIZE * 0.5f, IMAGE_SIZE * 0.5f)
            .SetPosition(cX, cY)
            .SetColor(Colors.Random(0.9f, 1f, false))
            .SetScale(0.05f)
            .SetTargetScale(MathUtils.random(0.6f, 1f), null)
            .SetFlip(projectiles.size() % 2 == 0, null)
            .SetOffset(0f, 0f, MathUtils.random(0f, 360f), null)
            .SetSpeed(2f, 2f, MathUtils.random(18f, 24f), null));
        }
    }

    public void RemoveProjectiles(int amount)
    {
        int i = 0;
        while (i < amount && projectiles.size() > 0) {
            projectiles.remove(projectiles.size() - 1);
            i++;
        }
    }

    @Override
    public void onChannel()
    {
        turns = 3;
        evoked = false;
        projectiles.clear();
        AddProjectiles(projectilesCount);

        PCLCombatStats.onStartOfTurnPostDraw.Subscribe(this);
        PCLCombatStats.onRawDamageReceived.Subscribe(this);
    }

    public String GetUpdatedDescription()
    {
        return FormatDescription(0, passiveAmount, MAX_PROJECTILES, evokeAmount, turns);
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        if (!AbstractDungeon.player.orbs.contains(this))
        {
            PCLCombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
            return;
        }

        this.turns -= 1;

        if (turns <= 0)
        {
            PCLActions.Bottom.Add(new EvokeSpecificOrbAction(this));

            evoked = true;
        }
    }

    @Override
    public void onEndOfTurn()
    {
        if (!evoked && turns > 0)
        {
            Passive();
        }

        this.updateDescription();
    }

    @Override
    public void applyFocus()
    {
        this.passiveAmount = Math.max(0, this.basePassiveAmount + GetFocus());
        PCLCombatStats.OnOrbApplyFocus(this);
    }

    @Override
    public void updateAnimation()
    {
        super.updateAnimation();

        final float delta = GR.UI.Delta();
        for (Projectile texture : projectiles)
        {
            texture.SetPosition(cX, cY).SetTargetRotation(angle, null).Update(delta);
        }

        if (!evoked && (vfxTimer -= delta) <= 0)
        {
            final float w = hb.width * 0.5f;
            final float h = hb.height * 0.5f;
            for (Projectile texture : projectiles)
            {
                texture.SetTargetOffset((w * 0.5f) - MathUtils.random(w), (h * 0.5f) - MathUtils.random(h), null, null);
            }
            vfxTimer = 1.5f;
        }

        this.angle += delta * 180f;
    }

    @Override
    public void render(SpriteBatch sb)
    {
        for (Projectile projectile : projectiles)
        {
            projectile.Render(sb, Colors.Copy(projectile.color, c.a));
        }

        this.renderText(sb);
        this.hb.render(sb);
    }

    @Override
    protected void renderText(SpriteBatch sb)
    {
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, evokeAmount + "x" + projectilesCount, this.cX + NUM_X_OFFSET,
                this.cY + this.bobEffect.y / 2f + NUM_Y_OFFSET - 4f * Settings.scale, new Color(0.2f, 1f, 1f, this.c.a), this.fontScale);
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.passiveAmount), this.cX + NUM_X_OFFSET,
                this.cY + this.bobEffect.y / 2f + NUM_Y_OFFSET + 20f * Settings.scale, new Color(0.8f, 0.7f, 0.2f, this.c.a), this.fontScale);
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.turns), this.cX - NUM_X_OFFSET, this.cY + this.bobEffect.y / 2f + NUM_Y_OFFSET + 20f * Settings.scale, this.c, this.fontScale);
    }

    @Override
    public void playChannelSFX()
    {
        SFX.Play(SFX.PCL_ORB_EARTH_CHANNEL, 0.8f, 1.2f);
    }

    @Override
    public AbstractOrb makeCopy()
    {
        final Earth copy = new Earth();
        if (!evoked)
        {
            copy.turns = turns;
            copy.projectilesCount = projectilesCount;
        }

        return copy;
    }

    @Override
    public void Passive()
    {
        PCLActions.Bottom.Add(new EarthOrbPassiveAction(this, passiveAmount));

        super.Passive();
    }

    @Override
    public void Evoke()
    {
        if (evokeAmount > 0)
        {
            if (projectiles.isEmpty())
            {
                AddProjectiles(projectilesCount);
            }

            PCLActions.Bottom.Add(new EarthOrbEvokeAction(this, evokeAmount));

            super.Evoke();
        }

        turns = 0;
        evoked = true;
        PCLCombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        PCLCombatStats.onRawDamageReceived.Unsubscribe(this);
    }

    @Override
    protected Color GetColor1()
    {
        return new Color(0.3f, 0.25f, 0f, 1f);
    }

    @Override
    protected Color GetColor2()
    {
        return Color.DARK_GRAY;
    }

    @Override
    public int OnRawDamageReceived(AbstractCreature target, DamageInfo info, int damage) {
        if (info.type == DamageInfo.DamageType.NORMAL && PCLGameUtilities.IsPlayer(target) && PCLGameUtilities.IsMonster(info.owner))
        {
            PCLActions.Top.Add(new EarthOrbPassiveAction(this, -1));
            PCLActions.Top.DealDamage(null, info.owner, evokeAmount, DamageInfo.DamageType.THORNS, AttackEffects.SMASH).SetVFXColor(Color.TAN);
            PCLActions.Top.GainBlock(passiveAmount);
        }
        return damage;
    }
}