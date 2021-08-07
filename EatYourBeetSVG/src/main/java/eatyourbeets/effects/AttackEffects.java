package eatyourbeets.effects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.effects.vfx.GenericRenderEffect;
import eatyourbeets.interfaces.delegates.FuncT2;
import eatyourbeets.interfaces.delegates.FuncT3;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.common.CommonImages;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;
import java.util.HashMap;

import static com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;

public class AttackEffects
{
   private static final HashMap<AttackEffect, AttackEffectData> map = new HashMap<>();
   private static final CommonImages.Effects IMAGES = GR.Common.Images.Effects;
   private static final ArrayList<AttackEffect> melee = new ArrayList<>();
   private static final ArrayList<AttackEffect> magic = new ArrayList<>();
   private static final ArrayList<AttackEffect> other = new ArrayList<>();

   public static final AttackEffect BLUNT_LIGHT = AttackEffect.BLUNT_LIGHT;
   public static final AttackEffect BLUNT_HEAVY = AttackEffect.BLUNT_HEAVY;
   public static final AttackEffect SLASH_DIAGONAL = AttackEffect.SLASH_DIAGONAL;
   public static final AttackEffect SLASH_HEAVY = AttackEffect.SLASH_HEAVY;
   public static final AttackEffect SLASH_HORIZONTAL = AttackEffect.SLASH_HORIZONTAL;
   public static final AttackEffect SLASH_VERTICAL = AttackEffect.SLASH_VERTICAL;
   public static final AttackEffect SMASH = AttackEffect.SMASH;
   public static final AttackEffect NONE = AttackEffect.NONE;
   public static final AttackEffect FIRE = AttackEffect.FIRE;
   public static final AttackEffect POISON = AttackEffect.POISON;
   public static final AttackEffect SHIELD = AttackEffect.SHIELD;
   public static final AttackEffect LIGHTNING = AttackEffect.LIGHTNING;
   // Custom:
   public static final AttackEffect SHIELD_FROST = GR.Enums.AttackEffect.SHIELD_FROST;
   public static final AttackEffect GUNSHOT = GR.Enums.AttackEffect.GUNSHOT;
   public static final AttackEffect DAGGER = GR.Enums.AttackEffect.DAGGER;
   public static final AttackEffect SPEAR = GR.Enums.AttackEffect.SPEAR;

   public static EYBEffect GetVFX(AttackEffect effect, AbstractCreature source, float t_cX, float t_cY)
   {
      if (effect != null && effect != NONE)
      {
         final AttackEffectData data = map.get(effect);
         if (data.createVFX2 != null)
         {
            return data.createVFX2.Invoke(source, t_cX, t_cY);
         }
         if (data.createVFX != null)
         {
            return data.createVFX.Invoke(t_cX, t_cY);
         }

         final TextureRegion region = data.GetTexture();
         if (region != null)
         {
            return new GenericRenderEffect(region, t_cX, t_cY)
            .SetRotation(effect == BLUNT_HEAVY ? MathUtils.random(0, 360) : MathUtils.random(-12f, 12f));
         }
      }

      return new GenericRenderEffect((TextureRegion) null, t_cX, t_cY);
   }

   public static void PlaySound(AttackEffect effect, float pitchMin, float pitchMax)
   {
      SFX.Play(GetSoundKey(effect), pitchMin, pitchMax);
   }

   public static String GetSoundKey(AttackEffect effect)
   {
      return effect == NONE ? null : map.get(effect).GetSound();
   }

   public static TextureRegion GetTextureRegion(AttackEffect effect)
   {
      return effect == NONE ? null : map.get(effect).GetTexture();
   }

   public static AttackEffect RandomMelee()
   {
      return JUtils.Random(melee);
   }

   public static AttackEffect RandomMagic()
   {
      return JUtils.Random(magic);
   }

   public static void Initialize()
   {
      Add(melee, BLUNT_LIGHT, ImageMaster.ATK_BLUNT_LIGHT)
              .SetSFX(SFX.BLUNT_FAST);

      Add(melee, BLUNT_HEAVY, ImageMaster.ATK_BLUNT_HEAVY)
              .SetSFX(SFX.BLUNT_HEAVY);

      Add(melee, SLASH_DIAGONAL, ImageMaster.ATK_SLASH_D)
              .SetSFX(SFX.ATTACK_FAST);

      Add(melee, SLASH_HEAVY, ImageMaster.ATK_SLASH_HEAVY)
              .SetSFX(SFX.ATTACK_HEAVY);

      Add(melee, SLASH_HORIZONTAL, ImageMaster.ATK_SLASH_H)
              .SetSFX(SFX.ATTACK_FAST);

      Add(melee, SLASH_VERTICAL, ImageMaster.ATK_SLASH_V)
              .SetSFX(SFX.ATTACK_FAST);

      Add(melee, SMASH)
              .SetVFX(VFX::Whack)
              .SetSFX(SFX.BLUNT_FAST);

      Add(magic, FIRE, ImageMaster.ATK_FIRE)
              .SetSFX(SFX.ATTACK_FIRE);

      Add(magic, POISON, ImageMaster.ATK_POISON)
              .SetSFX(SFX.ATTACK_POISON, SFX.ATTACK_POISON2);

      Add(other, SHIELD, ImageMaster.ATK_SHIELD)
              .SetVFX(VFX::Shield)
              .SetSFX(SFX.BLOCK_GAIN_1, SFX.BLOCK_GAIN_2, SFX.BLOCK_GAIN_3);

      Add(magic, LIGHTNING)
              .SetVFX(VFX::Lightning)
              .SetSFX(SFX.ORB_LIGHTNING_EVOKE);

      Add(other, SHIELD_FROST, ImageMaster.ATK_SHIELD) // TODO: dedicated texture
              .SetVFX(VFX::Shield)
              .SetSFX(SFX.ORB_FROST_DEFEND_1, SFX.ORB_FROST_DEFEND_2, SFX.ORB_FROST_DEFEND_3);

      Add(other, GUNSHOT)
              .SetVFX(VFX::Gunshot)
              .SetSFX(SFX.ANIMATOR_GUNSHOT);

      Add(melee, DAGGER, ImageMaster.ATK_SLASH_H)
              .SetSFX(SFX.ATTACK_DAGGER_1, SFX.ATTACK_DAGGER_2);

      Add(melee, SPEAR, ImageMaster.ATK_SLASH_V)
              .SetVFX2(VFX::Pierce)
              .SetSFX(SFX.ANIMATOR_SPEAR_1, SFX.ANIMATOR_SPEAR_2);
   }

   private static AttackEffectData Add(ArrayList<AttackEffect> category, AttackEffect effect)
   {
      return Add(category, effect, null);
   }

   private static AttackEffectData Add(ArrayList<AttackEffect> category, AttackEffect effect, TextureRegion texture)
   {
      AttackEffectData data = new AttackEffectData();
      data.texture = texture;
      category.add(effect);
      map.put(effect, data);
      return data;
   }

   private static class AttackEffectData
   {
      private String[] sounds;
      private FuncT2<EYBEffect, Float, Float> createVFX;
      private FuncT3<EYBEffect, AbstractCreature, Float, Float> createVFX2;
      private TextureRegion texture;

      private AttackEffectData SetSFX(String... sounds)
      {
         this.sounds = sounds;

         return this;
      }

      private AttackEffectData SetTexture(TextureRegion texture)
      {
         this.texture = texture;

         return this;
      }

      private AttackEffectData SetVFX(FuncT2<EYBEffect, Float, Float> createVFX)
      {
         this.createVFX = createVFX;

         return this;
      }

      private AttackEffectData SetVFX2(FuncT3<EYBEffect, AbstractCreature, Float, Float> createVFX2)
      {
         this.createVFX2 = createVFX2;

         return this;
      }

      private TextureRegion GetTexture()
      {
         return texture;
      }
      
      private String GetSound()
      {
         return sounds.length == 0 ? null : sounds.length == 1 ? sounds[0] : JUtils.Random(sounds);
      }
   }
}
