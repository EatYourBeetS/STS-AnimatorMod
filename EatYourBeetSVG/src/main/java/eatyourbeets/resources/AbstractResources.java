package eatyourbeets.resources;

import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public abstract class AbstractResources extends GR
implements EditCharactersSubscriber, EditCardsSubscriber, EditKeywordsSubscriber,
           EditRelicsSubscriber, EditStringsSubscriber, PostInitializeSubscriber,
           AddAudioSubscriber
{
    public final String Prefix;
    public final AbstractCard.CardColor CardColor;
    public final AbstractPlayer.PlayerClass PlayerClass;
    public final AbstractCardLibrary CardLibrary;

    protected final FileHandle testFolder;
    protected String defaultLanguagePath;
    protected boolean isLoaded;

    protected AbstractResources(String prefix, AbstractCard.CardColor cardColor, AbstractPlayer.PlayerClass playerClass, AbstractCardLibrary cardLibrary)
    {
        this.Prefix = prefix;
        this.CardColor = cardColor;
        this.PlayerClass = playerClass;
        this.CardLibrary = cardLibrary;
        this.testFolder = new FileHandle("c:/temp/" + prefix + "-localization/");
    }

    public String CreateID(String suffix)
    {
        return CreateID(Prefix, suffix);
    }

    public String ConvertID(String id, boolean fromAnimator)
    {
        return fromAnimator ?
                (Prefix + id.substring(GR.Animator.Prefix.length())) :
                (GR.Animator.Prefix + id.substring(Prefix.length()));
    }

    public int GetUnlockLevel()
    {
        return UnlockTracker.getUnlockLevel(PlayerClass);
    }

    public boolean IsSelected()
    {
        return GameUtilities.IsPlayerClass(PlayerClass);
    }

    @Override
    public final void receiveEditCards()
    {
        InitializeCards();
    }

    @Override
    public final void receiveEditCharacters()
    {

        InitializeCharacter();
    }

    @Override
    public final void receiveEditKeywords()
    {
        InitializeKeywords();
    }

    @Override
    public final void receiveEditRelics()
    {
        InitializeRelics();
    }

    @Override
    public final void receiveEditStrings()
    {
        InitializeStrings();
    }

    @Override
    public final void receiveAddAudio()
    {
        InitializeAudio();
    }

    @Override
    public final void receivePostInitialize()
    {
        InitializeEvents();
        InitializeMonsters();
        InitializePotions();
        InitializeRewards();
        InitializePowers();
        PostInitialize();

        isLoaded = true;
    }

    protected void InitializeInternal()  { }
    protected void InitializeColor()     { }
    protected void InitializeMonsters()  { }
    protected void InitializeEvents()    { }
    protected void InitializeRewards()   { }
    protected void InitializeAudio()     { }
    protected void InitializeCards()     { }
    protected void InitializePowers()    { }
    protected void InitializeStrings()   { }
    protected void InitializeTextures()  { }
    protected void InitializeRelics()    { }
    protected void InitializePotions()   { }
    protected void InitializeCharacter() { }
    protected void InitializeKeywords()  { }
    protected void PostInitialize()      { }

    public FileHandle GetFallbackFile(String fileName)
    {
        return Gdx.files.internal("localization/" + Prefix.toLowerCase() + "/eng/" + fileName);
    }

    public <T> T GetFallbackStrings(String fileName, Type typeOfT)
    {
        FileHandle file = GetFallbackFile(fileName);
        if (!file.exists())
        {
            JUtils.LogWarning(this, "File not found: " + file.path());
            return null;
        }

        String json = file.readString(String.valueOf(StandardCharsets.UTF_8));
        Gson gson = new Gson();
        return gson.fromJson(json, typeOfT);
    }

    public boolean IsBetaTranslation()
    {
        return testFolder.isDirectory();
    }

    public FileHandle GetFile(Settings.GameLanguage language, String fileName)
    {
        if (IsBetaTranslation() && new File(testFolder.path() + "/" + fileName).isFile())
        {
            return Gdx.files.internal(testFolder.path() + "/" + fileName);
        }
        else
        {
            if (!IsTranslationSupported(language))
            {
                language = Settings.GameLanguage.ENG;
            }

            return Gdx.files.internal("localization/" + Prefix.toLowerCase() + "/" + language.name().toLowerCase() + "/" + fileName);
        }
    }

    protected void LoadKeywords(AbstractCard.CardColor requiredColor)
    {
        super.LoadKeywords(GetFallbackFile("KeywordStrings.json"), requiredColor);

        if (IsBetaTranslation() || IsTranslationSupported(Settings.language))
        {
            super.LoadKeywords(GetFile(Settings.language, "KeywordStrings.json"), requiredColor);
        }
    }

    protected void LoadCustomRelics()
    {
        super.LoadCustomRelics(Prefix, CardColor);
    }

    protected void LoadCustomCards()
    {
        super.LoadCustomCards(Prefix);
    }

    protected void LoadCustomPowers()
    {
        super.LoadCustomPowers(Prefix);
    }

    protected void LoadCustomStrings(Class<?> type)
    {
        super.LoadCustomStrings(type, GetFallbackFile(type.getSimpleName() + ".json"));

        if (IsBetaTranslation() || IsTranslationSupported(Settings.language))
        {
            super.LoadCustomStrings(type, GetFile(Settings.language, type.getSimpleName() + ".json"));
        }
    }

    public String ProcessJson(String originalString, boolean useFallback)
    {
        final String path = "CardStringsShortcuts.json";
        final FileHandle file = useFallback ? GetFallbackFile(path) : GetFile(Settings.language, path);

        if (!file.exists())
        {
            return originalString;
        }

        final String shortcutsJson = file.readString(String.valueOf(StandardCharsets.UTF_8));
        final Map<String, String> items = new Gson().fromJson(shortcutsJson, new TypeToken<Map<String, String>>(){}.getType());
        final int size = items.size();
        final String[] shortcuts = items.keySet().toArray(new String[size]);
        final String[] replacement = items.values().toArray(new String[size]);

        return StringUtils.replaceEach(originalString, shortcuts, replacement);
    }
}
