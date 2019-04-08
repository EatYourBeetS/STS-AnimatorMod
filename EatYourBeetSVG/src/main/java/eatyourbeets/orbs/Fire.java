//package eatyourbeets.orbs;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.math.MathUtils;
//import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.core.CardCrawlGame;
//import com.megacrit.cardcrawl.core.Settings;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.helpers.FontHelper;
//import com.megacrit.cardcrawl.helpers.ImageMaster;
//import com.megacrit.cardcrawl.powers.AbstractPower;
//import com.megacrit.cardcrawl.vfx.combat.DarkOrbActivateEffect;
//import eatyourbeets.GameActionsHelper;
//import eatyourbeets.actions.EarthOrbEvokeAction;
//import eatyourbeets.powers.EarthenThornsPower;
//
//public class Fire extends AnimatorOrb
//{
//    public static final String ORB_ID = CreateFullID(Fire.class.getSimpleName());
//
//    public static Texture imgRight;
//    public static Texture imgLeft;
//    public static Texture imgMid;
//    private final boolean hFlip1;
//    private final boolean hFlip2;
//
//    private boolean evoked;
//    private int turns;
//
//    public Fire()
//    {
//        super(ORB_ID);
//
//        if (imgRight == null)
//        {
//            imgRight = ImageMaster.loadImage("images/orbs/" + ORB_ID + "Right.png");
//            imgLeft = ImageMaster.loadImage("images/orbs/" + ORB_ID + "Left.png");
//            imgMid = ImageMaster.loadImage("images/orbs/" + ORB_ID + "Mid.png");
//        }
//
//        this.hFlip1 = MathUtils.randomBoolean();
//        this.hFlip2 = MathUtils.randomBoolean();
//        this.turns = 3;
//        this.evoked = false;
//        this.baseEvokeAmount = 16;
//        this.evokeAmount = this.baseEvokeAmount;
//        this.basePassiveAmount = 2;
//        this.passiveAmount = this.basePassiveAmount;
//        this.updateDescription();
//        this.channelAnimTimer = 0.5F;
//    }
//
//    public void updateDescription()
//    {
//        String[] desc = orbStrings.DESCRIPTION;
//
//        this.applyFocus();
//        this.description = desc[0] + this.passiveAmount + desc[1] + this.evokeAmount + desc[2] + this.turns + desc[3];
//    }
//
//    public void onEvoke()
//    {
//        if (evokeAmount > 0)
//        {
//            GameActionsHelper.AddToBottom(new EarthOrbEvokeAction(evokeAmount));
//        }
//        evoked = true;
//    }
//
//    public void onEndOfTurn()
//    {
//        if (evoked)
//        {
//            return;
//        }
//
//        this.turns -= 1;
//        if (turns <= 0)
//        {
//            GameActionsHelper.AddToBottom(new EvokeSpecificOrbAction(this));
//            evoked = true;
//        }
//        else
//        {
//            AbstractPlayer p = AbstractDungeon.player;
//            GameActionsHelper.ApplyPower(p, p, new EarthenThornsPower(p, this.passiveAmount), this.passiveAmount);
//        }
//
//        this.updateDescription();
//    }
//
//    public void triggerEvokeAnimation()
//    {
//        CardCrawlGame.sound.play("ANIMATOR_ORB_EARTH_EVOKE", 0.1F);
//        AbstractDungeon.effectsQueue.add(new DarkOrbActivateEffect(this.cX, this.cY));
//    }
//
//    public void applyFocus()
//    {
//        AbstractPower power = AbstractDungeon.player.getPower("Focus");
//        if (power != null)
//        {
//            this.passiveAmount = Math.max(0, this.basePassiveAmount + power.amount);
//            this.evokeAmount = Math.max(0, this.baseEvokeAmount + (power.amount * 2));
//        }
//        else
//        {
//            this.evokeAmount = this.baseEvokeAmount;
//            this.passiveAmount = this.basePassiveAmount;
//        }
//    }
//
//    public void updateAnimation()
//    {
//        super.updateAnimation();
//        this.angle += Gdx.graphics.getDeltaTime() * 18f; //180.0F;
//    }
//
//    public void render(SpriteBatch sb)
//    {
//        sb.setColor(this.c);
//        sb.draw(imgLeft, this.cX - 48.0F + this.bobEffect.y / 4.0F, this.cY - 48.0F - this.bobEffect.y / 4.0F, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale, 0.0F, 0, 0, 96, 96, this.hFlip1, false);
//        sb.draw(imgMid, this.cX - 48.0F - this.bobEffect.y / 4.0F, this.cY - 48.0F + this.bobEffect.y / 2.0F, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale, 0.0F, 0, 0, 96, 96, this.hFlip2, false);
//        sb.draw(imgRight, this.cX - 48.0F + this.bobEffect.y / 4.0F, this.cY - 48.0F + this.bobEffect.y / 4.0F, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale, 0.0F, 0, 0, 96, 96, this.hFlip1, false);
//        this.renderText(sb);
//        this.hb.render(sb);
//    }
//
//    @Override
//    protected void renderText(SpriteBatch sb)
//    {
//        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.evokeAmount), this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET - 4.0F * Settings.scale, new Color(0.2F, 1.0F, 1.0F, this.c.a), this.fontScale);
//        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.passiveAmount), this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET + 20.0F * Settings.scale, new Color(0.8F, 0.7F, 0.2F, this.c.a), this.fontScale);
//        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.turns), this.cX - NUM_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET + 20.0F * Settings.scale, this.c, this.fontScale);
//    }
//
//    public void playChannelSFX()
//    {
//        CardCrawlGame.sound.play("ANIMATOR_ORB_EARTH_CHANNEL", 0.2f);
//    }
//}