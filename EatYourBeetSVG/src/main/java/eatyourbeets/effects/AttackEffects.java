package eatyourbeets.effects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import eatyourbeets.effects.vfx.GenericAnimationEffect;
import eatyourbeets.effects.vfx.GenericRenderEffect;
import eatyourbeets.effects.vfx.ShieldEffect;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.common.CommonImages;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.TupleT3;

public class AttackEffects
{
   public static final AbstractGameAction.AttackEffect BLUNT_LIGHT = AbstractGameAction.AttackEffect.BLUNT_LIGHT;
   public static final AbstractGameAction.AttackEffect BLUNT_HEAVY = AbstractGameAction.AttackEffect.BLUNT_HEAVY;
   public static final AbstractGameAction.AttackEffect SLASH_DIAGONAL = AbstractGameAction.AttackEffect.SLASH_DIAGONAL;
   public static final AbstractGameAction.AttackEffect SLASH_HEAVY = AbstractGameAction.AttackEffect.SLASH_HEAVY;
   public static final AbstractGameAction.AttackEffect SLASH_HORIZONTAL = AbstractGameAction.AttackEffect.SLASH_HORIZONTAL;
   public static final AbstractGameAction.AttackEffect SLASH_VERTICAL = AbstractGameAction.AttackEffect.SLASH_VERTICAL;
   public static final AbstractGameAction.AttackEffect NONE = AbstractGameAction.AttackEffect.NONE;
   public static final AbstractGameAction.AttackEffect FIRE = AbstractGameAction.AttackEffect.FIRE;
   public static final AbstractGameAction.AttackEffect POISON = AbstractGameAction.AttackEffect.POISON;
   public static final AbstractGameAction.AttackEffect SHIELD = AbstractGameAction.AttackEffect.SHIELD;
   public static final AbstractGameAction.AttackEffect LIGHTNING = AbstractGameAction.AttackEffect.LIGHTNING;
   // Custom:
   public static final AbstractGameAction.AttackEffect SHIELD_FROST = GR.Enums.AttackEffect.SHIELD_FROST;
   public static final AbstractGameAction.AttackEffect GUNSHOT = GR.Enums.AttackEffect.GUNSHOT;
   public static final AbstractGameAction.AttackEffect SPEAR = GR.Enums.AttackEffect.SPEAR; // TODO:

   private static final CommonImages.Effects IMAGES = GR.Common.Images.Effects;
   private static final String[] BLOCK_SOUNDS = { SFX.BLOCK_GAIN_1, SFX.BLOCK_GAIN_2, SFX.BLOCK_GAIN_3 };
   private static final String[] BLOCK_FROST_SOUNDS = { SFX.ORB_FROST_DEFEND_1, SFX.ORB_FROST_DEFEND_2, SFX.ORB_FROST_DEFEND_3 };
   private static final String[] POISON_SOUNDS = { SFX.ATTACK_POISON, SFX.ATTACK_POISON2 };

   public static AbstractGameEffect GetVFX(AbstractGameAction.AttackEffect effect, float cX, float cY)
   {
      if (effect == AbstractGameAction.AttackEffect.SMASH)
      {
         throw new RuntimeException("AttackEffect.SMASH is deprecated");
      }
      if (effect == LIGHTNING)
      {
         return new LightningEffect(cX, cY);
      }
      if (effect == SHIELD || effect == SHIELD_FROST)
      {
         return new ShieldEffect(cX, cY); // TODO: Frost shield Texture
      }

      TupleT3<Texture, Integer, Integer> t = GetTexture(effect);
      if (t != null)
      {
         final int h = t.V1.getHeight() / t.V2;
         final int w = t.V1.getWidth() / t.V3;
         return new GenericAnimationEffect(t.V1, cX, cY, w, h, 0.03f, t.V2 * t.V3);
      }
      else
      {
         GenericRenderEffect vfx = new GenericRenderEffect(GetTextureRegion(effect), cX, cY);
         if (effect == BLUNT_HEAVY)
         {
            vfx.SetRotation(MathUtils.random(360f));
         }
         return vfx;
      }
   }

   //Texture, rows, columns
   public static TupleT3<Texture, Integer, Integer> GetTexture(AbstractGameAction.AttackEffect effect)
   {
      if (effect == GUNSHOT)
      {
         return new TupleT3<>(IMAGES.Shot1.Texture(), 4, 4);
      }
      if (effect == SPEAR)
      {
         throw new RuntimeException("Spear effect is not implemented yet.");
      }

      return null;
   }

   public static TextureRegion GetTextureRegion(AbstractGameAction.AttackEffect effect)
   {
      switch (effect)
      {
         case BLUNT_LIGHT: return ImageMaster.ATK_BLUNT_LIGHT;
         case BLUNT_HEAVY: return ImageMaster.ATK_BLUNT_HEAVY;
         case SLASH_DIAGONAL: return ImageMaster.ATK_SLASH_D;
         case SLASH_HEAVY: return ImageMaster.ATK_SLASH_HEAVY;
         case SLASH_HORIZONTAL: return ImageMaster.ATK_SLASH_H;
         case SLASH_VERTICAL: return ImageMaster.ATK_SLASH_V;
         case FIRE: return ImageMaster.ATK_FIRE;
         case POISON: return ImageMaster.ATK_POISON;
         case SHIELD: return ImageMaster.ATK_SHIELD;

         case LIGHTNING:
         case SMASH:
         case NONE:
         default:
            return null;
      }
   }

   public static void PlaySound(AbstractGameAction.AttackEffect effect, float pitchMin, float pitchMax)
   {
      SFX.Play(GetSoundKey(effect), pitchMin, pitchMax);
   }

   public static String GetSoundKey(AbstractGameAction.AttackEffect effect)
   {
      if (effect == GUNSHOT)
      {
         return SFX.ANIMATOR_GUNSHOT;
      }
      if (effect == SHIELD_FROST)
      {
         return SFX.GetRandom(BLOCK_FROST_SOUNDS);
      }

      switch (effect)
      {
         case BLUNT_LIGHT: return SFX.BLUNT_FAST;
         case BLUNT_HEAVY: return SFX.BLUNT_HEAVY;
         case SLASH_HEAVY: return SFX.ATTACK_HEAVY;
         case FIRE: return SFX.ATTACK_FIRE;
         case POISON: return JUtils.Random(POISON_SOUNDS);
         case SHIELD: return JUtils.Random(BLOCK_SOUNDS);
         case LIGHTNING: return SFX.ORB_LIGHTNING_EVOKE;

         case SLASH_DIAGONAL:
         case SLASH_HORIZONTAL:
         case SLASH_VERTICAL:
            return SFX.ATTACK_FAST;

         case SMASH:
         case NONE:
         default:
            return null;
      }
   }
}
