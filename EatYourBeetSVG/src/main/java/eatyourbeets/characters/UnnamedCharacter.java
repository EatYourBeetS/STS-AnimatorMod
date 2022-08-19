package eatyourbeets.characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import eatyourbeets.cards.unnamed.basic.Defend;
import eatyourbeets.cards.unnamed.basic.PowerStrike;
import eatyourbeets.cards.unnamed.basic.Strike;
import eatyourbeets.cards.unnamed.basic.SummonDoll;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.combatOnly.TalkEffect;
import eatyourbeets.monsters.Bosses.TheUnnamed;
import eatyourbeets.monsters.UnnamedReign.UltimateShape.UltimateShape;
import eatyourbeets.relics.unnamed.Incomplete;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameEffects;

import java.util.ArrayList;
import java.util.HashSet;

public class UnnamedCharacter extends EYBPlayerCharacter
{
    public static final CharacterStrings characterStrings = GR.GetCharacterStrings("Unnamed");
    public static final String[] NAMES = characterStrings.NAMES;
    public static final String[] TEXT = characterStrings.TEXT;
    public static final String ORIGINAL_NAME = NAMES[0];
    public static final String OVERRIDE_NAME = NAMES.length > 1 ? NAMES[1] : ORIGINAL_NAME; // Support for Beta/Alt

    public static final int BASE_HP = 50;
    public static final int BASE_GOLD = 99;

    private static HashSet<String> AlreadySpoken = new HashSet<>();

    public UnnamedCharacter()
    {
        super(ORIGINAL_NAME, GR.Unnamed.PlayerClass, GR.Unnamed.Images.ORB_TEXTURES, GR.Unnamed.Images.ORB_VFX_PNG);

        initializeClass(null, GR.Unnamed.Images.SHOULDER2_PNG, GR.Unnamed.Images.SHOULDER1_PNG, GR.Unnamed.Images.CORPSE_PNG,
        getLoadout(), 0f, -5f, 240f, 244f, new EnergyManager(3));

        reloadAnimation();
    }

    public void reloadAnimation()
    {
        super.reloadAnimation(GR.Unnamed.Images.SKELETON_ATLAS, GR.Unnamed.Images.SKELETON_JSON);
    }

    @Override
    public String getLocalizedCharacterName()
    {
        return ORIGINAL_NAME;
    }

    @Override
    public String getTitle(PlayerClass playerClass) // Top panel title
    {
        return OVERRIDE_NAME;
    }

    @Override
    public AbstractPlayer newInstance()
    {
        return new UnnamedCharacter();
    }

    @Override
    public Color getSlashAttackColor()
    {
        return Color.VIOLET;
    }

    @Override
    public Color getCardTrailColor()
    {
        return GR.Unnamed.RenderColor.cpy();
    }

    @Override
    public int getAscensionMaxHPLoss()
    {
        return BASE_HP / 10;
    }

    @Override
    public BitmapFont getEnergyNumFont()
    {
        return FontHelper.energyNumFontPurple;
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey()
    {
        return SFX.ORB_DARK_EVOKE;
    }

    @Override
    public String getPortraitImageName()
    {
        return null; // Updated in AnimatorCharacterSelectScreen
    }

    @Override
    public ArrayList<String> getStartingDeck()
    {
        final ArrayList<String> cards = new ArrayList<>();
        cards.add(Strike.DATA.ID);
        cards.add(Strike.DATA.ID);
        cards.add(Strike.DATA.ID);
        cards.add(Strike.DATA.ID);
        cards.add(Defend.DATA.ID);
        cards.add(Defend.DATA.ID);
        cards.add(Defend.DATA.ID);
        cards.add(Defend.DATA.ID);
        cards.add(SummonDoll.DATA.ID);
        cards.add(PowerStrike.DATA.ID);
        return cards;
    }

    @Override
    public ArrayList<String> getStartingRelics()
    {
        final ArrayList<String> relics = new ArrayList<>();
        relics.add(Incomplete.ID);
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
        return new CharSelectInfo(NAMES[0], TEXT[1], BASE_HP, BASE_HP, 0, BASE_GOLD, 5, this, getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public AbstractCard.CardColor getCardColor()
    {
        return GR.Unnamed.CardColor;
    }

    @Override
    public Color getCardRenderColor()
    {
        return GR.Unnamed.RenderColor.cpy();
    }

    @Override
    public void damage(DamageInfo info)
    {
        super.damage(info);

        if (isDead)
        {
            Talk(this, GR.Unnamed.Strings.Chat.Defeated);
        }
    }

    @Override
    public void applyStartOfCombatLogic()
    {
        super.applyStartOfCombatLogic();

        final MapRoomNode node = AbstractDungeon.getCurrMapNode();
        final AbstractRoom room = node != null ? node.getRoom() : null;
        if (room != null)
        {
            if (GR.Common.Dungeon.IsUnnamedReign() && node.y == 0)
            {
                Talk(this, GR.Unnamed.Strings.Chat.UnnamedReign);
            }
            if (room.monsters != null && room.monsters.monsters.size() > 0)
            {
                final AbstractMonster m = room.monsters.monsters.get(0);
                if (m.id.equals(UltimateShape.ID))
                {
                    Talk(this, GR.Unnamed.Strings.Chat.UltimateShape);
                }
                else if (m.id.equals(TheUnnamed.ID))
                {
                    GameEffects.TopLevelQueue.WaitRealtime(2f).AddCallback(() -> Talk(this, GR.Unnamed.Strings.Chat.ToOtherMe1));
                    GameEffects.TopLevelQueue.WaitRealtime(4f).AddCallback(m, (other, __) -> Talk(other, GR.Unnamed.Strings.Chat.OtherMeReply));
                    GameEffects.TopLevelQueue.WaitRealtime(6f).AddCallback(() -> Talk(this, GR.Unnamed.Strings.Chat.ToOtherMe2));
                }
            }
        }
    }

    private TalkEffect Talk(AbstractCreature source, String message)
    {
        if (AlreadySpoken.contains(message))
        {
            return null;
        }

        AlreadySpoken.add(message);
        return GameEffects.TopLevelQueue.Talk(source, message);
    }
}