package eatyourbeets.characters;

import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.screens.stats.CharStat;
import eatyourbeets.AnimatorResources;
import eatyourbeets.AnimatorResources_Images;
import eatyourbeets.cards.animator.Strike;
import patches.AbstractEnums;

import java.util.ArrayList;

public class AnimatorCharacter extends CustomPlayer
{
    public static final CharacterStrings characterStrings = AnimatorResources.GetCharacterStrings();
    public static final Color MAIN_COLOR = CardHelper.getColor(210, 147, 106);
    public static final String[] NAMES = characterStrings.NAMES;
    public static final String[] TEXT = characterStrings.TEXT;
    public static final String NAME = NAMES[0];

    public AnimatorCharacter(String name, PlayerClass playerClass)
    {
        super(name, playerClass, AnimatorResources_Images.ORB_TEXTURES, AnimatorResources_Images.ORB_VFX_PNG, (String) null, null);

        initializeClass(null, AnimatorResources_Images.SHOULDER2_PNG, AnimatorResources_Images.SHOULDER1_PNG, AnimatorResources_Images.CORPSE_PNG,
                getLoadout(), 0.0F, -5.0F, 240.0F, 244.0F, new EnergyManager(3));

        reloadAnimation();
    }

    public void reloadAnimation()
    {
        this.loadAnimation(AnimatorResources_Images.SKELETON_ATLAS, AnimatorResources_Images.SKELETON_JSON, 1.0f);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        this.stateData.setMix("Hit", "Idle", 0.1F);
        e.setTimeScale(0.9F);
    }

    public void damage(DamageInfo info)
    {
        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output - this.currentBlock > 0)
        {
            AnimationState.TrackEntry e = this.state.setAnimation(0, "Hit", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
            e.setTimeScale(0.9F);
        }

        super.damage(info);
    }

    @Override
    public String getLocalizedCharacterName()
    {
        return NAMES[0];
    }

    @Override
    public AbstractPlayer newInstance()
    {
        return new AnimatorCharacter(this.name, AbstractEnums.Characters.THE_ANIMATOR);
    }

    @Override
    public String getSpireHeartText()
    {
        return com.megacrit.cardcrawl.events.beyond.SpireHeart.DESCRIPTIONS[10];
    }

    @Override
    public Color getSlashAttackColor()
    {
        return Color.SKY;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect()
    {
        return new AbstractGameAction.AttackEffect[]
                {
                        AbstractGameAction.AttackEffect.SLASH_HEAVY,
                        AbstractGameAction.AttackEffect.FIRE,
                        AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                        AbstractGameAction.AttackEffect.SLASH_HEAVY,
                        AbstractGameAction.AttackEffect.FIRE,
                        AbstractGameAction.AttackEffect.SLASH_DIAGONAL
                };
    }

    @Override
    public String getVampireText()
    {
        return com.megacrit.cardcrawl.events.city.Vampires.DESCRIPTIONS[5];
    }

    @Override
    public Color getCardTrailColor()
    {
        return MAIN_COLOR.cpy();
    }

    @Override
    public int getAscensionMaxHPLoss()
    {
        return 4;
    }

    @Override
    public BitmapFont getEnergyNumFont()
    {
        return FontHelper.energyNumFontBlue;
    }

    @Override
    public void doCharSelectScreenSelectEffect()
    {
        CardCrawlGame.sound.playA("TINGSHA", MathUtils.random(-0.1F, 0.2F));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey()
    {
        return "TINGSHA";
    }

    @Override
    public ArrayList<String> getStartingDeck()
    {
        return AnimatorCharacterSelect.GetSelectedLoadout(true).GetStartingDeck();
    }

    @Override
    public ArrayList<String> getStartingRelics()
    {
        return AnimatorCharacterSelect.GetSelectedLoadout(true).GetStartingRelics();
    }

    @Override
    public AbstractCard getStartCardForEvent()
    {
        return new Strike();
    }

    @Override
    public CharSelectInfo getLoadout()
    {
        return AnimatorCharacterSelect.GetSelectedLoadout(true).GetLoadout(NAMES[0], TEXT[0], this);
    }

    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass)
    {
        return NAME;
    }

    @Override
    public AbstractCard.CardColor getCardColor()
    {
        return AbstractEnums.Cards.THE_ANIMATOR;
    }

    @Override
    public Color getCardRenderColor()
    {
        return MAIN_COLOR.cpy();
    }

    @Override
    public CharStat getCharStat()
    {
        // yes
        return super.getCharStat();
    }
}