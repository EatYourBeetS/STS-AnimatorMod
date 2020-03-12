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
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.screens.stats.CharStat;
import eatyourbeets.cards.unnamed.Defend;
import eatyourbeets.cards.unnamed.Strike;
import eatyourbeets.relics.unnamed.InfinitePower;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.unnamed.UnnamedResources;

import java.util.ArrayList;

public class UnnamedCharacter extends CustomPlayer
{
    public static final CharacterStrings characterStrings = UnnamedResources.GetCharacterStrings("Unnamed");
    public static final Color MAIN_COLOR = CardHelper.getColor(210, 147, 106);
    public static final String[] NAMES = characterStrings.NAMES;
    public static final String[] TEXT = characterStrings.TEXT;
    public static final String NAME = NAMES[0];

    public UnnamedCharacter()
    {
        super(NAME, GR.Unnamed.PlayerClass, GR.Unnamed.Images.ORB_TEXTURES, GR.Unnamed.Images.ORB_VFX_PNG, (String) null, null);

        initializeClass(null, GR.Unnamed.Images.SHOULDER2_PNG, GR.Unnamed.Images.SHOULDER1_PNG, GR.Unnamed.Images.CORPSE_PNG,
                getLoadout(), 0.0F, -20.0F, 240.0F, 260.0F, new EnergyManager(3));

        reloadAnimation();
    }

    @Override
    public String getPortraitImageName()
    {
        return null;
    }

    public void reloadAnimation()
    {
        this.loadAnimation(GR.Unnamed.Images.SKELETON_ATLAS, GR.Unnamed.Images.SKELETON_JSON, 1.0f);
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
        return new UnnamedCharacter();
    }

    @Override
    public String getSpireHeartText()
    {
        return com.megacrit.cardcrawl.events.beyond.SpireHeart.DESCRIPTIONS[10];
    }

    @Override
    public Color getSlashAttackColor()
    {
        return Color.VIOLET;
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
        return Vampires.DESCRIPTIONS[5];
    }

    @Override
    public Color getCardTrailColor()
    {
        return MAIN_COLOR.cpy();
    }

    @Override
    public int getAscensionMaxHPLoss()
    {
        return 3;
    }

    @Override
    public BitmapFont getEnergyNumFont()
    {
        return FontHelper.energyNumFontBlue;
    }

    @Override
    public void doCharSelectScreenSelectEffect()
    {
        CardCrawlGame.sound.playA("ORB_DARK_CHANNEL", MathUtils.random(-0.1F, 0.2F));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey()
    {
        return "ORB_DARK_CHANNEL";
    }

    @Override
    public ArrayList<String> getStartingDeck()
    {
        ArrayList<String> cards = new ArrayList<>();

        cards.add(Strike.DATA.ID);
        cards.add(Strike.DATA.ID);
        cards.add(Strike.DATA.ID);
        cards.add(Strike.DATA.ID);
        cards.add(Defend.DATA.ID);
        cards.add(Defend.DATA.ID);
        cards.add(Defend.DATA.ID);
        cards.add(Defend.DATA.ID);

        return cards;
    }

    @Override
    public ArrayList<String> getStartingRelics()
    {
        ArrayList<String> relics = new ArrayList<>();

        relics.add(InfinitePower.ID);

        return relics;
    }

    @Override
    public AbstractCard getStartCardForEvent()
    {
        return new Strike();
    }

    @Override
    public CharSelectInfo getLoadout()
    {
        return new CharSelectInfo(NAME, TEXT[0], 66, 66, 3, 99,
                5, this, getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public String getTitle(PlayerClass playerClass)
    {
        return NAME;
    }

    @Override
    public AbstractCard.CardColor getCardColor()
    {
        return GR.Unnamed.CardColor;
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