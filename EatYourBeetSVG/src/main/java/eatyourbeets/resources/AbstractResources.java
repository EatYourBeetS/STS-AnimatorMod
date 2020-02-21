package eatyourbeets.resources;

import basemod.interfaces.*;
import com.megacrit.cardcrawl.core.Settings;

public abstract class AbstractResources extends GR
implements EditCharactersSubscriber, EditCardsSubscriber, EditKeywordsSubscriber,
           EditRelicsSubscriber, EditStringsSubscriber, PostInitializeSubscriber,
           AddAudioSubscriber
{
    protected final String prefix;
    protected String defaultLanguagePath;

    protected AbstractResources(String prefix)
    {
        this.prefix = prefix;
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

    public String GetFallbackLanguagePath()
    {
//        if (defaultLanguagePath == null)
//        {
//            File f = new File("c:/temp/" + prefix + "-localization/");
//            if (f.exists() && f.isDirectory())
//            {
//                defaultLanguagePath = f.getAbsolutePath();
//            }
//            else
//            {
//                defaultLanguagePath = GetLanguagePath(Settings.GameLanguage.ENG);
//            }
//        }
        return GetLanguagePath(Settings.GameLanguage.ENG);
    }

    public String GetLanguagePath(Settings.GameLanguage language)
    {
        return "localization/" + prefix + "/" + language.name().toLowerCase() + "/";
    }

    protected void LoadKeywords()
    {
        super.LoadKeywords(GetFallbackLanguagePath() + "KeywordStrings.json");

        if (Settings.language != Settings.GameLanguage.ENG)
        {
            super.LoadKeywords(GetLanguagePath(Settings.language) + "KeywordStrings.json");
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
        super.LoadCustomStrings(type, GetFallbackLanguagePath() + type.getSimpleName() + ".json");

//        if (Settings.language != Settings.GameLanguage.ENG)
//        {
//            super.LoadCustomStrings(type, GetLanguagePath(Settings.language) + type.getSimpleName() + ".json");
//        }
    }
}
