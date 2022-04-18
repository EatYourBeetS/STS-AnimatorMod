package eatyourbeets.resources.unnamed;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.*;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardMetadata;
import eatyourbeets.characters.UnnamedCharacter;
import eatyourbeets.resources.AbstractResources;
import eatyourbeets.utilities.JUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class UnnamedResources extends AbstractResources
{
    public final static String ID = "unnamed";

    public final String OfficialName = "Unnamed"; // Don't change this
    public final UnnamedDungeonData Dungeon = UnnamedDungeonData.Register(CreateID("Data"));
    public final UnnamedPlayerData Data = new UnnamedPlayerData();
    public final UnnamedStrings Strings = new UnnamedStrings();
    public final UnnamedImages Images = new UnnamedImages();
    public final Color RenderColor = CardHelper.getColor(122, 141, 156);
    public Map<String, EYBCardMetadata> CardData;

    public UnnamedResources()
    {
        super(ID, Enums.Cards.THE_UNNAMED, Enums.Characters.THE_UNNAMED);
    }

    @Override
    protected void InitializeStrings()
    {
        if (isLoaded)
        {
            return;
        }

        JUtils.LogInfo(this, "InitializeStrings();");

        LoadCustomStrings(OrbStrings.class);
        LoadCustomStrings(CharacterStrings.class);

        String json = GetFallbackFile("CardStrings.json").readString(StandardCharsets.UTF_8.name());
        LoadGroupedCardStrings(ProcessJson(json, true));

        if (testFolder.isDirectory() || IsTranslationSupported(Settings.language))
        {
            FileHandle file = GetFile(Settings.language, "CardStrings.json");
            if (file.exists())
            {
                String json2 = file.readString(StandardCharsets.UTF_8.name());
                LoadGroupedCardStrings(ProcessJson(json2, false));
            }
        }

        final String jsonString = new String(Gdx.files.internal("Unnamed-CardMetadata.json").readBytes());
        CardData = new Gson().fromJson(jsonString, new TypeToken<Map<String, EYBCardMetadata>>(){}.getType());

        LoadCustomStrings(RelicStrings.class);
        LoadCustomStrings(PowerStrings.class);
        LoadCustomStrings(UIStrings.class);
        LoadCustomStrings(EventStrings.class);
        LoadCustomStrings(PotionStrings.class);
        LoadCustomStrings(MonsterStrings.class);
        LoadCustomStrings(BlightStrings.class);
    }

    @Override
    protected void InitializeColor()
    {
        if (isLoaded)
        {
            return;
        }

        final Color color = RenderColor.cpy();
        BaseMod.addColor(CardColor, color, color, color, color, color, color, color,
        Images.ATTACK_PNG, Images.SKILL_PNG, Images.POWER_PNG,
        Images.ORB_A_PNG, Images.ATTACK_PNG, Images.SKILL_PNG,
        Images.POWER_PNG, Images.ORB_B_PNG, Images.ORB_C_PNG);
    }

    @Override
    protected void InitializeCharacter()
    {
        if (isLoaded)
        {
            return;
        }

        BaseMod.addCharacter(new UnnamedCharacter(), Images.CHAR_BUTTON_PNG, Images.CHAR_PORTRAIT_JPG, PlayerClass);
    }

    @Override
    protected void InitializeCards()
    {
        if (isLoaded)
        {
            return;
        }

        JUtils.LogInfo(this, "InitializeCards();");

        Strings.Initialize();
        LoadCustomCards();
        EYBCardData.PostInitialize();
    }

    @Override
    protected void InitializeRelics()
    {
        if (isLoaded)
        {
            return;
        }

        JUtils.LogInfo(this, "InitializeRelics();");

        LoadCustomRelics();
    }

    @Override
    protected void InitializeKeywords()
    {
        if (isLoaded)
        {
            return;
        }

        JUtils.LogInfo(this, "InitializeKeywords();");

        LoadKeywords(CardColor);
    }

    @Override
    protected void InitializePowers()
    {
        // LoadCustomPowers();
    }

    @Override
    protected void InitializePotions()
    {

    }

    @Override
    protected void InitializeRewards()
    {

    }

    @Override
    protected void PostInitialize()
    {
        Data.Initialize();
        isLoaded = true;
    }
}