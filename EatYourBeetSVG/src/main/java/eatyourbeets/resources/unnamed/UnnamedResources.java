package eatyourbeets.resources.unnamed;

import basemod.BaseMod;
import basemod.abstracts.CustomUnlockBundle;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.AbstractUnlock;
import eatyourbeets.cards.base.EYBCardMetadata;
import eatyourbeets.characters.UnnamedCharacter;
import eatyourbeets.relics.unnamed.InfinitePower;
import eatyourbeets.resources.AbstractResources;

import java.util.Map;

public class UnnamedResources extends AbstractResources
{
    public final static String ID = "unnamed";
    public final AbstractCard.CardColor CardColor = Enums.Cards.THE_UNNAMED;
    public final AbstractPlayer.PlayerClass PlayerClass = Enums.Characters.THE_UNNAMED;
    //public final UnnamedDungeonData Dungeon = AnimatorDungeonData.Register(CreateID("Data"));
    //public final UnnamedPlayerData Data = new AnimatorPlayerData();
    public final UnnamedStrings Strings = new UnnamedStrings();
    public final UnnamedImages Images = new UnnamedImages();
    //public final UnnamedConfig Config = new AnimatorConfig();
    public Map<String, EYBCardMetadata> CardData;

    public UnnamedResources()
    {
        super(ID);
    }

    @Override
    protected void PostInitialize()
    {

    }

    @Override
    protected void InitializeColor()
    {
        Color color = CardHelper.getColor(60, 77, 106);

        BaseMod.addColor(Enums.Cards.THE_UNNAMED, color, color, color, color, color, color, color,
                Images.ATTACK_PNG,  Images.SKILL_PNG ,    Images.POWER_PNG ,
                Images.ORB_1A_PNG,  Images.ATTACK_P_PNG , Images.SKILL_P_PNG ,
                Images.POWER_P_PNG, Images.ORB_1B_PNG,    Images.ORB_1C_PNG);
    }

    @Override
    protected void InitializeCharacter()
    {
        BaseMod.addCharacter(new UnnamedCharacter(), Images.CHAR_BUTTON_PNG, Images.CHAR_PORTRAIT_JPG, PlayerClass);

        BaseMod.addUnlockBundle(new CustomUnlockBundle(AbstractUnlock.UnlockType.MISC, "UNNECESSARY unlock bar", "Anime", "A new starting deck!"), PlayerClass, 0);
        BaseMod.addUnlockBundle(new CustomUnlockBundle(AbstractUnlock.UnlockType.MISC, "Nothing", "Anime", "A new starting deck!"), PlayerClass, 1);
        BaseMod.addUnlockBundle(new CustomUnlockBundle(AbstractUnlock.UnlockType.MISC, "Unicorns", "BaseMod", "A new starting deck!"), PlayerClass, 2);
        BaseMod.addUnlockBundle(new CustomUnlockBundle(AbstractUnlock.UnlockType.MISC, "A piece of paper", "BaseMod", "A new starting deck!"), PlayerClass, 3);
        BaseMod.addUnlockBundle(new CustomUnlockBundle(AbstractUnlock.UnlockType.MISC, "A new bug", "BaseMod", "A new starting deck!"), PlayerClass, 4);
        BaseMod.addUnlockBundle(new CustomUnlockBundle(AbstractUnlock.UnlockType.MISC, "The Void", "Breaking changes", "A new starting deck!"), PlayerClass, 5);
    }

    @Override
    protected void InitializeCards()
    {
        Strings.Initialize();
        Images.Initialize();
        LoadCustomCards();
    }

    @Override
    protected void InitializeStrings()
    {
        LoadCustomStrings(OrbStrings.class);
        LoadCustomStrings(CharacterStrings.class);
        LoadCustomStrings(CardStrings.class);
        LoadCustomStrings(RelicStrings.class);
        LoadCustomStrings(PowerStrings.class);
        LoadCustomStrings(UIStrings.class);
        LoadCustomStrings(EventStrings.class);
        LoadCustomStrings(PotionStrings.class);
        LoadCustomStrings(MonsterStrings.class);
        LoadCustomStrings(BlightStrings.class);
    }

    @Override
    protected void InitializeRelics()
    {
        BaseMod.addRelicToCustomPool(new InfinitePower(), Enums.Cards.THE_UNNAMED);
    }

    @Override
    protected void InitializeKeywords()
    {
        LoadKeywords();
    }
}