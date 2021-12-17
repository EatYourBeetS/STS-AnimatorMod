package pinacolada.resources;

import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import pinacolada.utilities.PCLJUtils;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public abstract class AbstractResources extends GR
implements EditCharactersSubscriber, EditCardsSubscriber, EditKeywordsSubscriber,
           EditRelicsSubscriber, EditStringsSubscriber, PostInitializeSubscriber,
           AddAudioSubscriber
{
    protected final FileHandle testFolder;
    protected final String prefix;
    protected String defaultLanguagePath;

    protected AbstractResources(String prefix)
    {
        this.prefix = prefix;
        this.testFolder = new FileHandle("c:/temp/" + prefix + "-localization/");
    }

    public String CreateID(String suffix)
    {
        return CreateID(prefix, suffix);
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
        return Gdx.files.internal("localization/" + prefix.toLowerCase() + "/eng/" + fileName);
    }

    public <T> T GetFallbackStrings(String fileName, Type typeOfT)
    {
        FileHandle file = GetFallbackFile(fileName);
        if (!file.exists())
        {
            PCLJUtils.LogWarning(this, "File not found: " + file.path());
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

            return Gdx.files.internal("localization/" + prefix.toLowerCase() + "/" + language.name().toLowerCase() + "/" + fileName);
        }
    }

    protected void LoadKeywords()
    {
        super.LoadKeywords(GetFallbackFile("KeywordStrings.json"));

        if (IsBetaTranslation() || IsTranslationSupported(Settings.language))
        {
            super.LoadKeywords(GetFile(Settings.language, "KeywordStrings.json"));
        }
    }

    protected void LoadCustomRelics()
    {
        super.LoadCustomRelics(prefix);
    }

    protected void LoadCustomCards()
    {
        super.LoadCustomCards(prefix);
    }

    protected void LoadCustomPowers()
    {
        super.LoadCustomPowers(prefix);
    }

    protected void LoadCustomStrings(Class<?> type)
    {
        super.LoadCustomStrings(type, GetFallbackFile(type.getSimpleName() + ".json"));

        if (IsBetaTranslation() || IsTranslationSupported(Settings.language))
        {
            super.LoadCustomStrings(type, GetFile(Settings.language, type.getSimpleName() + ".json"));
        }
    }
}
