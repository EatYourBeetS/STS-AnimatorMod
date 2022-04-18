package eatyourbeets.characters;

import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.daily.mods.BlueCards;
import com.megacrit.cardcrawl.daily.mods.GreenCards;
import com.megacrit.cardcrawl.daily.mods.PurpleCards;
import com.megacrit.cardcrawl.daily.mods.RedCards;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.stats.CharStat;
import patches.RelicLibraryPatches;

import java.util.ArrayList;

public abstract class EYBPlayerCharacter extends CustomPlayer
{
    public static final int MAX_TEMP_HP = 99;
    public static final int MAX_BLOCK = 199;

    public EYBPlayerCharacter(String originalName, PlayerClass playerClass, String[] orbTextures, String orbVfx)
    {
        super(originalName, playerClass, orbTextures, orbVfx, (String) null, null);
    }

    public void reloadAnimation(String skeletonAtlas, String skeletonJson)
    {
        this.loadAnimation(skeletonAtlas, skeletonJson, 1f);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        this.stateData.setMix("Hit", "Idle", 0.1f);
        e.setTimeScale(0.9f);
    }

    @Override
    public void damage(DamageInfo info)
    {
        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output - this.currentBlock > 0)
        {
            AnimationState.TrackEntry e = this.state.setAnimation(0, "Hit", false);
            this.state.addAnimation(0, "Idle", true, 0f);
            e.setTimeScale(0.9f);
        }

        super.damage(info);
    }

    @Override
    public ArrayList<String> getRelicNames()
    {
        final ArrayList<String> list = new ArrayList<>();
        for (AbstractRelic r : relics)
        {
            RelicLibraryPatches.AddRelic(list, r);
        }

        return list;
    }

    @Override
    public ArrayList<AbstractCard> getCardPool(ArrayList<AbstractCard> arrayList)
    {
        arrayList = super.getCardPool(arrayList);

        if (ModHelper.isModEnabled(RedCards.ID))
        {
            CardLibrary.addRedCards(arrayList);
        }
        if (ModHelper.isModEnabled(GreenCards.ID))
        {
            CardLibrary.addGreenCards(arrayList);
        }
        if (ModHelper.isModEnabled(BlueCards.ID))
        {
            CardLibrary.addBlueCards(arrayList);
        }
        if (ModHelper.isModEnabled(PurpleCards.ID))
        {
            CardLibrary.addPurpleCards(arrayList);
        }

        return arrayList;
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
    public BitmapFont getEnergyNumFont()
    {
        return FontHelper.energyNumFontBlue;
    }

    @Override
    public void doCharSelectScreenSelectEffect()
    {
        CardCrawlGame.sound.playA(getCustomModeCharacterButtonSoundKey(), MathUtils.random(-0.1f, 0.2f));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }

    @Override
    public String getPortraitImageName()
    {
        return null; // Updated in AnimatorCharacterSelectScreen
    }

    @Override
    public CharStat getCharStat()
    {
        // yes
        return super.getCharStat();
    }
}