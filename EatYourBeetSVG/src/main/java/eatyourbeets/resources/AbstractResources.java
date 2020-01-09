package eatyourbeets.resources;

import basemod.interfaces.*;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.resources.animator.AnimatorResources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public abstract class AbstractResources extends GR
implements EditCharactersSubscriber, EditCardsSubscriber, EditKeywordsSubscriber,
           EditRelicsSubscriber, EditStringsSubscriber, PostInitializeSubscriber,
           AddAudioSubscriber
{

    protected static final Logger logger = LogManager.getLogger(AnimatorResources.class.getName());

    protected String languagePath;
    protected String prefix;

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
    protected void InitializeRelics()    { }
    protected void InitializePotions()   { }
    protected void InitializeCharacter() { }
    protected void InitializeKeywords()  { }
    protected void PostInitialize()      { }

    protected String GetLanguagePath()
    {
        if (languagePath == null)
        {
            File f = new File("c:/temp/" + prefix + "-localization/");
            if (f.exists() && f.isDirectory())
            {
                languagePath = f.getAbsolutePath();
            }
            else
            {
                languagePath = GetLanguagePath(Settings.language);
            }
        }

        return languagePath;
    }

    protected String GetLanguagePath(Settings.GameLanguage language)
    {
        return "localization/" + prefix + "/" + language.name().toLowerCase() + "/";
    }

    protected void LoadKeywords()
    {
        super.LoadKeywords(GetLanguagePath() + "KeywordStrings.json");
    }

    protected void LoadDynamicKeywords()
    {
        super.LoadDynamicKeywords(GetLanguagePath() + "DynamicKeywordStrings.json");
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
        super.LoadCustomStrings(type, GetLanguagePath() + type.getSimpleName() + ".json");
    }
}
